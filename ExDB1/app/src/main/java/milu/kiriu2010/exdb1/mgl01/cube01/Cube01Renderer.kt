package milu.kiriu2010.exdb1.mgl01.cube01

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.SystemClock
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Cube01Renderer: GLSurfaceView.Renderer {
    // 描画オブジェクト
    private lateinit var drawObj: Cube01Model

    // モデル変換行列
    private val matM = FloatArray(16)
    // ビュー変換行列
    private val matV = FloatArray(16)
    // プロジェクション変換行列
    private val matP = FloatArray(16)
    // モデル・ビュー・プロジェクション行列
    private val matMVP = FloatArray(16)
    // テンポラリ行列
    private val matT = FloatArray(16)


    override fun onDrawFrame(gl: GL10?) {
        // canvasを初期化
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // ビュー×プロジェクション
        Matrix.multiplyMM(matT,0,matP,0,matV,0)

        // 経過秒から回転角度を求める(10秒/周)
        val time = SystemClock.uptimeMillis()%10000L
        val angle = 360f/10000f * time.toInt()

        // モデルを単位行列にする
        Matrix.setIdentityM(matM,0)
        // モデルを"Y軸"を中心に回転する
        //Matrix.rotateM(matM,0,angle,0f,1f,0f)
        // モデルを"Y軸45度/Z軸45度"を中心に回転する
        //Matrix.rotateM(matM,0,angle,0f,1f,1f)
        // モデルを"X軸45度Y軸45度/Z軸45度"を中心に回転する
        Matrix.rotateM(matM,0,angle,1f,1f,1f)
        // モデル×ビュー×プロジェクション
        Matrix.multiplyMM(matMVP,0,matT,0,matM,0)

        // モデルを描画
        drawObj.draw(matMVP)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat()/height.toFloat()

        Matrix.perspectiveM(matP,0,60f,ratio,0.1f,100f)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // canvasを初期化する色を設定する
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // canvasを初期化する際の深度を設定する
        GLES20.glClearDepthf(1f)

        // カリングと深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        //GLES20.glEnable(GLES20.GL_CULL_FACE)

        // カメラの位置
        Matrix.setLookAtM(matV, 0,
                0f, 0f, 5f,
                0f, 0f, 0f,
                0f, 1.0f, 0.0f)

        drawObj = Cube01Model()
    }
}