package milu.kiriu2010.excon2.b0x.cameraparam

import android.Manifest
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureResult
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.TotalCaptureResult
import android.hardware.camera2.CameraCaptureSession
import android.graphics.SurfaceTexture
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCharacteristics
import android.view.TextureView
import android.hardware.camera2.CameraManager
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.ImageReader
import android.os.Handler
import androidx.core.content.ContextCompat
import android.util.Log
import android.view.Surface
import java.util.*


class Camera2StateMachine {
    private lateinit var context: Context
    private var mCameraManager: CameraManager? = null

    private var mCameraDevice: CameraDevice? = null
    private var mCaptureSession: CameraCaptureSession? = null
    private var mImageReader: ImageReader? = null
    private var mPreviewRequestBuilder: CaptureRequest.Builder? = null

    private var mTextureView: AutoFitTextureView? = null
    private val mHandler: Handler? = null // default current thread.
    private var mState: State? = null
    private var mTakePictureListener: ImageReader.OnImageAvailableListener? = null

    // ===================================================================================
    // State Definition
    private val mInitSurfaceState = object : State("InitSurface") {

        private val mSurfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(texture: SurfaceTexture, width: Int, height: Int) {
                if (mState != null) mState!!.onSurfaceTextureAvailable(width, height)
            }

            override fun onSurfaceTextureDestroyed(texture: SurfaceTexture): Boolean {
                return true
            }

            override fun onSurfaceTextureSizeChanged(texture: SurfaceTexture, width: Int, height: Int) {}
            override fun onSurfaceTextureUpdated(texture: SurfaceTexture) {}
        }

        @Throws(CameraAccessException::class)
        override fun enter() {
            if (mTextureView!!.isAvailable()) {
                nextState(mOpenCameraState)
            } else {
                mTextureView!!.setSurfaceTextureListener(mSurfaceTextureListener)
            }
        }

        override fun onSurfaceTextureAvailable(width: Int, height: Int) {
            nextState(mOpenCameraState)
        }

        override fun onCameraOpened(cameraDevice: CameraDevice) {}

        override fun onSessionConfigured(cameraCaptureSession: CameraCaptureSession) {}

        override fun onCaptureResult(result: CaptureResult, isCompleted: Boolean) {}

        override fun finish() {}
    }
    // -----------------------------------------------------------------------------------
    private val mOpenCameraState = object : State("OpenCamera") {

        private val mStateCallback = object : CameraDevice.StateCallback() {
            override fun onOpened(cameraDevice: CameraDevice) {
                if (mState != null) mState!!.onCameraOpened(cameraDevice)
            }

            override fun onDisconnected(cameraDevice: CameraDevice) {
                nextState(mAbortState)
            }

            override fun onError(cameraDevice: CameraDevice, error: Int) {
                Log.e(TAG, "CameraDevice:onError:$error")
                nextState(mAbortState)
            }
        }

        // @SuppressLint("MissingPermission")
        @Throws(CameraAccessException::class)
        override fun enter() {
            val cameraId = Camera2Util.getCameraId(mCameraManager!!, CameraCharacteristics.LENS_FACING_BACK)
            val characteristics = mCameraManager!!.getCameraCharacteristics(cameraId!!)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)

            mImageReader = Camera2Util.getMaxSizeImageReader(map!!, ImageFormat.JPEG)
            val previewSize = Camera2Util.getBestPreviewSize(map!!, mImageReader!!)
            mTextureView!!.setPreviewSize(previewSize.getHeight(), previewSize.getWidth())

            // suppresslint(missingpermission)
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                mCameraManager!!.openCamera(cameraId!!, mStateCallback, mHandler)
            }
            Log.d(TAG, "openCamera:$cameraId")
        }

        override fun onCameraOpened(cameraDevice: CameraDevice) {
            mCameraDevice = cameraDevice
            nextState(mCreateSessionState)
        }

        override fun onSurfaceTextureAvailable(width: Int, height: Int) {}
        override fun onSessionConfigured(cameraCaptureSession: CameraCaptureSession) {}
        override fun onCaptureResult(result: CaptureResult, isCompleted: Boolean) {}
        override fun finish() {}
    }
    // -----------------------------------------------------------------------------------
    private val mCreateSessionState = object : State("CreateSession") {

        private val mSessionCallback = object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                if (mState != null) mState!!.onSessionConfigured(cameraCaptureSession)
            }

            override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
                nextState(mAbortState)
            }
        }

        @Throws(CameraAccessException::class)
        override fun enter() {
            mPreviewRequestBuilder = mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            val texture = mTextureView!!.getSurfaceTexture()
            texture.setDefaultBufferSize(mTextureView!!.getPreviewWidth(), mTextureView!!.getPreviewHeight())
            val surface = Surface(texture)
            mPreviewRequestBuilder!!.addTarget(surface)
            val outputs = Arrays.asList(surface, mImageReader!!.getSurface())
            mCameraDevice!!.createCaptureSession(outputs, mSessionCallback, mHandler)
        }

        override fun onSessionConfigured(cameraCaptureSession: CameraCaptureSession) {
            mCaptureSession = cameraCaptureSession
            nextState(mPreviewState)
        }

        override fun onSurfaceTextureAvailable(width: Int, height: Int) {}
        override fun onCaptureResult(result: CaptureResult, isCompleted: Boolean) {}
        override fun onCameraOpened(cameraDevice: CameraDevice) {}
        override fun finish() {}
    }
    // -----------------------------------------------------------------------------------
    private val mPreviewState = object : State("Preview") {

        @Throws(CameraAccessException::class)
        override fun enter() {
            mPreviewRequestBuilder!!.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
            mPreviewRequestBuilder!!.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
            mCaptureSession!!.setRepeatingRequest(mPreviewRequestBuilder!!.build(), mCaptureCallback, mHandler)
        }

        override fun onSurfaceTextureAvailable(width: Int, height: Int) {}
        override fun onSessionConfigured(cameraCaptureSession: CameraCaptureSession) {}
        override fun onCaptureResult(result: CaptureResult, isCompleted: Boolean) {}
        override fun onCameraOpened(cameraDevice: CameraDevice) {}
        override fun finish() {}
    }
    private val mCaptureCallback = object : CameraCaptureSession.CaptureCallback() {
        override fun onCaptureProgressed(session: CameraCaptureSession, request: CaptureRequest, partialResult: CaptureResult) {
            onCaptureResult(partialResult, false)
        }

        override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
            onCaptureResult(result, true)
        }

        private fun onCaptureResult(result: CaptureResult, isCompleted: Boolean) {
            try {
                if (mState != null) mState!!.onCaptureResult(result, isCompleted)
            } catch (e: CameraAccessException) {
                Log.e(TAG, "handle():", e)
                nextState(mAbortState)
            }

        }
    }
    // -----------------------------------------------------------------------------------
    private val mAutoFocusState = object : State("AutoFocus") {
        @Throws(CameraAccessException::class)
        override fun enter() {
            mPreviewRequestBuilder!!.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START)
            mCaptureSession!!.setRepeatingRequest(mPreviewRequestBuilder!!.build(), mCaptureCallback, mHandler)
        }

        @Throws(CameraAccessException::class)
        override fun onCaptureResult(result: CaptureResult, isCompleted: Boolean) {
            val afState = result.get(CaptureResult.CONTROL_AF_STATE)
            val isAfReady = (afState == null
                    || afState == CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED
                    || afState == CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED)
            if (isAfReady) {
                nextState(mAutoExposureState)
            }
        }
        override fun onSurfaceTextureAvailable(width: Int, height: Int) {}
        override fun onSessionConfigured(cameraCaptureSession: CameraCaptureSession) {}
        override fun onCameraOpened(cameraDevice: CameraDevice) {}
        override fun finish() {}
    }
    // -----------------------------------------------------------------------------------
    private val mAutoExposureState = object : State("AutoExposure") {
        @Throws(CameraAccessException::class)
        override fun enter() {
            mPreviewRequestBuilder!!.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                    CameraMetadata.CONTROL_AE_PRECAPTURE_TRIGGER_START)
            mCaptureSession!!.setRepeatingRequest(mPreviewRequestBuilder!!.build(), mCaptureCallback, mHandler)
        }

        @Throws(CameraAccessException::class)
        override fun onCaptureResult(result: CaptureResult, isCompleted: Boolean) {
            val aeState = result.get(CaptureResult.CONTROL_AE_STATE)
            val isAeReady = (aeState == null
                    || aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED
                    || aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED)
            if (isAeReady) {
                nextState(mTakePictureState)
            }
        }
        override fun onSurfaceTextureAvailable(width: Int, height: Int) {}
        override fun onSessionConfigured(cameraCaptureSession: CameraCaptureSession) {}
        override fun onCameraOpened(cameraDevice: CameraDevice) {}
        override fun finish() {}
    }
    // -----------------------------------------------------------------------------------
    private val mTakePictureState = object : State("TakePicture") {
        @Throws(CameraAccessException::class)
        override fun enter() {
            val captureBuilder = mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            captureBuilder.addTarget(mImageReader!!.getSurface())
            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
            captureBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, 90) // portraito
            mImageReader!!.setOnImageAvailableListener(mTakePictureListener, mHandler)

            mCaptureSession!!.stopRepeating()
            mCaptureSession!!.capture(captureBuilder.build(), mCaptureCallback, mHandler)
        }

        @Throws(CameraAccessException::class)
        override fun onCaptureResult(result: CaptureResult, isCompleted: Boolean) {
            if (isCompleted) {
                nextState(mPreviewState)
            }
        }

        @Throws(CameraAccessException::class)
        override fun finish() {
            mPreviewRequestBuilder!!.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL)
            mPreviewRequestBuilder!!.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
            mCaptureSession!!.capture(mPreviewRequestBuilder!!.build(), mCaptureCallback, mHandler)
            mTakePictureListener = null
        }
        override fun onSurfaceTextureAvailable(width: Int, height: Int) {}
        override fun onSessionConfigured(cameraCaptureSession: CameraCaptureSession) {}
        override fun onCameraOpened(cameraDevice: CameraDevice) {}
    }
    // -----------------------------------------------------------------------------------
    private val mAbortState = object : State("Abort") {

        @Throws(CameraAccessException::class)
        override fun enter() {
            shutdown()
            nextState(null)
        }
        override fun onSurfaceTextureAvailable(width: Int, height: Int) {}
        override fun onSessionConfigured(cameraCaptureSession: CameraCaptureSession) {}
        override fun onCaptureResult(result: CaptureResult, isCompleted: Boolean) {}
        override fun onCameraOpened(cameraDevice: CameraDevice) {}
        override fun finish() {}
    }

    fun open(activity: Activity, textureView: AutoFitTextureView) {
        context = activity
        if (mState != null) throw IllegalStateException("Alrady started state=" + mState!!)
        mTextureView = textureView
        mCameraManager = activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        nextState(mInitSurfaceState)
    }

    fun takePicture(listener: ImageReader.OnImageAvailableListener): Boolean {
        if (mState !== mPreviewState) return false
        mTakePictureListener = listener
        nextState(mAutoFocusState)
        return true
    }

    fun close() {
        nextState(mAbortState)
    }

    // ----------------------------------------------------------------------------------------
    // The following private
    private fun shutdown() {
        if (null != mCaptureSession) {
            mCaptureSession!!.close()
            mCaptureSession = null
        }
        if (null != mCameraDevice) {
            mCameraDevice!!.close()
            mCameraDevice = null
        }
        if (null != mImageReader) {
            mImageReader!!.close()
            mImageReader = null
        }
    }

    private fun nextState(nextState: State?) {
        Log.d(TAG, "state: $mState->$nextState")
        try {
            if (mState != null) mState!!.finish()
            mState = nextState
            if (mState != null) mState!!.enter()
        } catch (e: CameraAccessException) {
            Log.e(TAG, "next($nextState)", e)
            shutdown()
        }

    }

    private abstract inner class State(private val mName: String) {
        //@formatter:off
        override fun toString(): String {
            return mName
        }

        @Throws(CameraAccessException::class)
        abstract fun enter()

        abstract fun onSurfaceTextureAvailable(width: Int, height: Int)
        abstract fun onCameraOpened(cameraDevice: CameraDevice)
        abstract fun onSessionConfigured(cameraCaptureSession: CameraCaptureSession)
        @Throws(CameraAccessException::class)
        abstract fun onCaptureResult(result: CaptureResult, isCompleted: Boolean)

        @Throws(CameraAccessException::class)
        abstract fun finish()
        //@formatter:on
    }

    companion object {
        private val TAG = Camera2StateMachine::class.java.simpleName
    }
}