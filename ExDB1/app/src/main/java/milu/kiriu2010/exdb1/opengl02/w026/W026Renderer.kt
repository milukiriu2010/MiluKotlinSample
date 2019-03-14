package milu.kiriu2010.exdb1.opengl01.w019

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.Matrix
import android.os.SystemClock
import milu.kiriu2010.exdb1.opengl01.w025.MySphere06
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// ---------------------------------------------------
// テクスチャ
// ---------------------------------------------------
// https://wgld.org/d/webgl/w026.html
// ---------------------------------------------------
class W026Renderer: GLSurfaceView.Renderer {
    private lateinit var drawObj: W026Texture

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private val mMVPMatrix = FloatArray(16)
    private val tmpMatrix = FloatArray(16)
    private val mInvMatrix = FloatArray(16)
    private val mProjectionMatrix = FloatArray(16)
    private val mViewMatrix = FloatArray(16)
    private val mModelMatrix = FloatArray(16)

    override fun onDrawFrame(gl: GL10) {

        // canvasを初期化
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // ビュー×プロジェクション座標変換行列
        Matrix.multiplyMM(tmpMatrix,0,mProjectionMatrix,0,mViewMatrix,0)

        // プリミティブをアニメーション
        // 経過秒から回転角度を求める(10秒/周)
        val time = SystemClock.uptimeMillis() % 10000L
        val angleInDegrees = 360.0f / 10000.0f * time.toInt()
        val rad = angleInDegrees*PI.toFloat()/180f

        val tx = cos(rad) * 3.5f
        val ty = sin(rad) * 3.5f
        val tz = sin(rad) * 3.5f

        // モデル座標返還行列の生成
        Matrix.setIdentityM(mModelMatrix,0)
        Matrix.translateM(mModelMatrix,0,tx,-ty,-tz)
        Matrix.rotateM(mModelMatrix,0,angleInDegrees,0f,1f,1f)
        Matrix.multiplyMM(mMVPMatrix,0,tmpMatrix,0,mModelMatrix,0)
        Matrix.invertM(mInvMatrix,0,mModelMatrix,0)

        // １つ目のモデル描画
        drawObj.draw(mMVPMatrix, floatArrayOf())

    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat()/height.toFloat()

        Matrix.perspectiveM(mProjectionMatrix,0,45f,ratio,0.1f,100f)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        // canvasを初期化する色を設定する
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // canvasを初期化する際の深度を設定する
        GLES20.glClearDepthf(1f)

        // 深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)

        // 有効にするテクスチャユニットを指定
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)

        // カメラの位置
        Matrix.setLookAtM(mViewMatrix, 0,
                0f, 2f, 5f,
                0f, 0f, 0f,
                0f, 1.0f, 0.0f)

        drawObj = W026Texture()
    }

}
