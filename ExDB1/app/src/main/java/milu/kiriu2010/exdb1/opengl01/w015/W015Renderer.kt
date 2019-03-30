package milu.kiriu2010.exdb1.opengl01.w015

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.Matrix
import android.os.SystemClock

// https://wgld.org/d/webgl/w015.html
// https://android.googlesource.com/platform/development/+/master/samples/OpenGL/HelloOpenGLES20/src/com/example/android/opengl/MyGLRenderer.java
// https://android.keicode.com/basics/opengl-drawing-basic-shapes.php
// https://developer.android.com/training/graphics/opengl/draw
class W015Renderer: GLSurfaceView.Renderer {
    // 描画モデル
    private lateinit var drawObj: W015Model

    // プログラムハンドル
    private var programHandle: Int = 0

    // モデル変換行列
    private val matM = FloatArray(16)
    // モデル変換行列の逆行列
    private val matI = FloatArray(16)
    // ビュー変換行列
    private val matV = FloatArray(16)
    // プロジェクション変換行列
    private val matP = FloatArray(16)
    // モデル・ビュー・プロジェクション行列
    private val matMVP = FloatArray(16)
    // テンポラリ行列
    private val matT = FloatArray(16)
    // 点光源の位置
    private val vecLight = floatArrayOf(15f,10f,15f)
    // 環境光の色
    private val vecAmbientColor = floatArrayOf(0.1f,0.1f,0.1f,1f)
    // カメラの座標
    private val vecEye = floatArrayOf(0f,0f,1.5f)
    // カメラの上方向を表すベクトル
    private val vecEyeUp = floatArrayOf(0f,1f,0f)
    // 原点のベクトル
    private val vecCenter = floatArrayOf(0f,0f,-5f)

    // 回転角度
    private var angle1 = 0

    //private var mAngle: Float = 0f

    override fun onDrawFrame(gl: GL10) {

        // canvasを初期化
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        angle1 =(angle1+5)%360
        val t1 = angle1.toFloat()

        /*
        // Create a rotation for the triangle
        // Use the following code to generate constant rotation.
        // Leave this code out when using TouchEvents.
        val time = SystemClock.uptimeMillis() % 10000L;
        mAngle = (360f/10000f) * time.toFloat();
        */

        Matrix.setIdentityM(matM,0)
        Matrix.setRotateM(matM, 0, t1, 0f, 0f, 1.0f)
        Matrix.multiplyMM(matMVP,0,matV,0,matM,0)
        Matrix.multiplyMM(matMVP,0,matP,0,matMVP,0)

        // Draw triangle
        drawObj.draw(programHandle,matMVP)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat()/height.toFloat()

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(matP, 0, -ratio, ratio, -1f, 1f, 1f, 10f)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        // canvasを初期化する色を設定する
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // canvasを初期化する際の深度を設定する
        GLES20.glClearDepthf(1f)

        // シェーダプログラム登録
        programHandle = W015Shader().loadShader()

        // モデル生成
        drawObj = W015Model()

        // カメラの位置
        // ビュー座標変換行列
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
    }
}
