package milu.kiriu2010.exdb1.opengl01.w019

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.Matrix
import android.os.SystemClock
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// https://wgld.org/d/webgl/w019.html
// https://android.googlesource.com/platform/development/+/master/samples/OpenGL/HelloOpenGLES20/src/com/example/android/opengl/MyGLRenderer.java
// https://android.keicode.com/basics/opengl-drawing-basic-shapes.php
// https://developer.android.com/training/graphics/opengl/draw
class MySquare02Renderer: GLSurfaceView.Renderer {
    private lateinit var mSquare: MySquare02

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private val mMVPMatrix = FloatArray(16)
    private val tmpMatrix = FloatArray(16)
    private val mProjectionMatrix = FloatArray(16)
    private val mViewMatrix = FloatArray(16)
    private val mModelMatrix = FloatArray(16)

    private var mAngle: Float = 0f

    var culling = false
    var frontFace = false
    var depthTest = false

    override fun onDrawFrame(gl: GL10) {

        // canvasを初期化
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        when (culling) {
            true -> GLES20.glEnable(GLES20.GL_CULL_FACE)
            false -> GLES20.glDisable(GLES20.GL_CULL_FACE)
        }

        when (frontFace) {
            true -> GLES20.glFrontFace(GLES20.GL_CW)
            false -> GLES20.glFrontFace(GLES20.GL_CCW)
        }

        when (depthTest) {
            true -> GLES20.glEnable(GLES20.GL_DEPTH_TEST)
            false -> GLES20.glDisable(GLES20.GL_DEPTH_TEST)
        }


        // ビュー×プロジェクション座標変換行列
        Matrix.multiplyMM(tmpMatrix,0,mProjectionMatrix,0,mViewMatrix,0)

        // プリミティブをアニメーション
        // 経過秒から回転角度を求める(10秒/周)
        val time = SystemClock.uptimeMillis() % 10000L
        val angleInDegrees = 360.0f / 10000.0f * time.toInt()

        val x = cos(angleInDegrees*PI.toFloat()/180f) * 1.5f
        val z = sin(angleInDegrees*PI.toFloat()/180f) * 1.5f

        // ---------------------------------------------------
        // １つ目のモデル
        // X軸による回転
        // ---------------------------------------------------

        // １つ目のモデルを移動する
        Matrix.setIdentityM(mModelMatrix,0)
        Matrix.translateM(mModelMatrix,0,x,0f,z)
        Matrix.rotateM(mModelMatrix,0,angleInDegrees,1f,0f,0f)

        // モデル×ビュー×プロジェクション(１つ目のモデル)
        Matrix.multiplyMM(mMVPMatrix,0,tmpMatrix,0,mModelMatrix,0)
        // １つ目のモデル描画
        mSquare.draw(mMVPMatrix)


        // ---------------------------------------------------
        // ２つ目のモデル
        // Y軸による回転
        // ---------------------------------------------------

        // ２つ目のモデルを移動する
        Matrix.setIdentityM(mModelMatrix,0)
        Matrix.translateM(mModelMatrix,0,-x,0f,-z)
        Matrix.rotateM(mModelMatrix,0,angleInDegrees,0f,1f,0f)

        // モデル×ビュー×プロジェクション(２つ目のモデル)
        Matrix.multiplyMM(mMVPMatrix,0,tmpMatrix,0,mModelMatrix,0)
        // ２つ目のモデル描画
        mSquare.draw(mMVPMatrix)

    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat()/height.toFloat()

        /*
        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1f, 1f, 1f, 10f)
        */
        Matrix.perspectiveM(mProjectionMatrix,0,90f,ratio,0.1f,100f)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        // canvasを初期化する色を設定する
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // canvasを初期化する際の深度を設定する
        GLES20.glClearDepthf(1f)


        // カメラの位置
        Matrix.setLookAtM(mViewMatrix, 0,
                0f, 0f, 5f,
                0f, 0f, 0f,
                0f, 1.0f, 0.0f)

        // initialize a triangle
        mSquare = MySquare02()
    }
}
