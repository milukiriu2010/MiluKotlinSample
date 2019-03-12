package milu.kiriu2010.exdb1.opengl.begin01

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.Matrix
import android.os.SystemClock

// https://android.googlesource.com/platform/development/+/master/samples/OpenGL/HelloOpenGLES20/src/com/example/android/opengl/MyGLRenderer.java
// https://android.keicode.com/basics/opengl-drawing-basic-shapes.php
// https://developer.android.com/training/graphics/opengl/draw
class MyTriangle01Renderer: GLSurfaceView.Renderer {
    private lateinit var mTriangle: MyTriangle01
    private lateinit var mSquare: MySquare01

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private val mMVPMatrix = FloatArray(16)
    private val mProjectionMatrix = FloatArray(16)
    private val mViewMatrix = FloatArray(16)
    private val mRotationMatrix = FloatArray(16)

    private var mAngle: Float = 0f

    override fun onDrawFrame(gl: GL10) {
        val scratch = FloatArray(16)

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0)

        // Draw square
        mSquare.draw(mMVPMatrix)

        // Create a rotation for the triangle
        // Use the following code to generate constant rotation.
        // Leave this code out when using TouchEvents.
        val time = SystemClock.uptimeMillis() % 4000L;
        mAngle = 0.090f * time.toFloat();
        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0f, 0f, 1.0f)

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0)

        // Draw triangle
        mTriangle.draw(scratch)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat()/height.toFloat()

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // initialize a triangle
        mTriangle = MyTriangle01()
        // initialize a square
        mSquare = MySquare01()
    }


}