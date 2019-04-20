package milu.kiriu2010.exdb1.opengl01.w019

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.Matrix
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.exdb1.opengl02.w027.W027Shader

// ---------------------------------------------------
// テクスチャ
// ---------------------------------------------------
// https://wgld.org/d/webgl/w027.html
// ---------------------------------------------------
class W027Renderer: GLSurfaceView.Renderer {
    // 描画オブジェクト
    private lateinit var drawObj: W027Model

    // プログラムハンドル
    private var programHandle: Int = 0

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
    // カメラの座標
    private val vecEye = floatArrayOf(0f,2f,5f)
    // カメラの上方向を表すベクトル
    private val vecEyeUp = floatArrayOf(0f,1f,0f)
    // 原点のベクトル
    private val vecCenter = floatArrayOf(0f,0f,0f)

    // ビットマップ配列
    val bmpArray = arrayListOf<Bitmap>()

    // テクスチャ配列
    val textures = IntArray(2)

    // 回転角度
    private var angle1 = 0

    override fun onDrawFrame(gl: GL10) {

        // canvasを初期化
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        angle1 =(angle1+1)%360
        val t1 = angle1.toFloat()

        // テクスチャをバインドする
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0])
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[1])

        // ビュー×プロジェクション座標変換行列
        Matrix.multiplyMM(matT,0,matP,0,matV,0)

        // モデル座標変換行列の生成
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,t1,0f,1f,0f)
        Matrix.multiplyMM(matMVP,0,matT,0,matM,0)


        // モデル描画
        drawObj.draw(programHandle,matMVP, 0,1)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat()/height.toFloat()

        Matrix.perspectiveM(matP,0,45f,ratio,0.1f,100f)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        // canvasを初期化する色を設定する
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // canvasを初期化する際の深度を設定する
        GLES20.glClearDepthf(1f)

        // 深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)

        // シェーダプログラム登録
        programHandle = W027Shader().loadShader()

        // テクスチャ作成し、idをtexturesに保存
        GLES20.glGenTextures(2,textures,0)
        MyGLFunc.checkGlError("glGenTextures")

        // モデル生成
        drawObj = W027Model()

        // テクスチャ0をバインド
        drawObj.activateTexture(0,textures,bmpArray[0])
        // テクスチャ1をバインド
        drawObj.activateTexture(1,textures,bmpArray[1])

        // カメラの位置
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])

    }

}
