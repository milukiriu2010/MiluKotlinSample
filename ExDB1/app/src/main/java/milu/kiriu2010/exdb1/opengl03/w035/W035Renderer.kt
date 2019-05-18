package milu.kiriu2010.exdb1.opengl03.w035

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.view.MotionEvent
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.basic.MyQuaternion
import milu.kiriu2010.gui.model.Board01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.Texture01Shader
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.sqrt

// ------------------------------------------------------------------------------
// ビルボード
// ------------------------------------------------------------------------------
// カメラの視線ベクトルに対し常に垂直な姿勢を持つようにモデルをレンダリングする
// ------------------------------------------------------------------------------
// https://wgld.org/d/webgl/w035.html
// ------------------------------------------------------------------------------
class W035Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画モデル
    private lateinit var model: Board01Model

    // シェーダ
    private lateinit var shader: Texture01Shader

    // 画面縦横比
    var ratio: Float = 0f

    // ビットマップ配列
    val bmpArray = arrayListOf<Bitmap>()

    // テクスチャ配列
    val textures = IntArray(2)

    // ビルボード用のビュー座標変換行列
    val matV4B = FloatArray(16)
    // ビルボード用ビュー座標変換行列の逆行列
    val matIV4B = FloatArray(16)

    // ビルボード(有効/無効)
    var isBillBoard = false

    init {
        // ビットマップをロード
        bmpArray.clear()
        val bmp0 = BitmapFactory.decodeResource(ctx.resources, R.drawable.texture_w035_0)
        val bmp1 = BitmapFactory.decodeResource(ctx.resources, R.drawable.texture_w035_1)
        bmpArray.add(bmp0)
        bmpArray.add(bmp1)
    }

    override fun onDrawFrame(gl: GL10?) {
        // canvasを初期化
        GLES20.glClearColor(0f,0f,0f,1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // クォータニオンを座標変換行列に変換
        var matQ = qtnNow.toMatIV()

        // カメラの位置
        // ビュー座標変換行列
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        // ビルボード用のビュー座標変換行列
        // ビルボードからカメラを見るので逆になる
        Matrix.setLookAtM(matV4B, 0,
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEye[0], vecEye[1], vecEye[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        // ビュー座標変換行列にクォータニオンの回転を適用
        Matrix.multiplyMM(matV  ,0,matV  ,0,matQ,0)
        Matrix.multiplyMM(matV4B,0,matV4B,0,matQ,0)

        // ビルボード用ビュー行列の逆行列を取得
        // カメラの回転を相殺するために使う
        Matrix.invertM(matIV4B,0,matV4B,0)

        // ビュー×プロジェクション
        Matrix.perspectiveM(matP,0,45f,ratio,0.1f,100f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // -------------------------------------------------------
        // フロア描画
        // -------------------------------------------------------

        // フロア用テクスチャをバインド
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textures[1])

        // フロア用テクスチャをレンダリング
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,90f,1f,0f,0f)
        Matrix.scaleM(matM,0,3f,3f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shader.draw(model,matMVP,1)

        // -------------------------------------------------------
        // ビルボード描画
        // -------------------------------------------------------

        // ビルボード用テクスチャ(ボール)をバインド
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textures[0])

        // ビルボード用テクスチャ(ボール)のレンダリング
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,0f,1f,0f)
        if (isBillBoard) {
            Matrix.multiplyMM(matM,0,matM,0,matIV4B,0)
        }
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shader.draw(model,matMVP,0)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        ratio = width.toFloat()/height.toFloat()
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // 深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        GLES20.glEnable(GLES20.GL_BLEND)

        // アルファブレンドを設定
        //   ビルボード用テクスチャ(ボール)の周りは透明のため、
        //   アルファブレンド用のパラメータを設定すると、
        //   背景とビルボードが重なっている透明部分は、
        //   背景が表示される
        GLES20.glBlendFuncSeparate(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA,GLES20.GL_ONE,GLES20.GL_ONE)

        // シェーダ
        shader = Texture01Shader()
        shader.loadShader()

        // モデル生成
        model = Board01Model()
        model.createPath()

        // テクスチャ作成し、idをtexturesに保存
        GLES20.glGenTextures(2,textures,0)
        MyGLFunc.checkGlError("glGenTextures")

        // ビルボード用テクスチャ(ボール)をバインド
        MyGLFunc.createTexture(0,textures,bmpArray[0],-1,-1)
        // フロア用テクスチャをバインド
        MyGLFunc.createTexture(1,textures,bmpArray[1])

        // カメラの座標位置
        vecEye[0] =  0f
        vecEye[1] =  5f
        vecEye[2] = 10f

        // ----------------------------------
        // 単位行列化
        // ----------------------------------
        // モデル変換行列
        Matrix.setIdentityM(matM,0)
        // モデル変換行列の逆行列
        Matrix.setIdentityM(matI,0)
        // ビュー変換行列
        Matrix.setIdentityM(matV,0)
        // プロジェクション変換行列
        Matrix.setIdentityM(matP,0)
        // モデル・ビュー・プロジェクション行列
        Matrix.setIdentityM(matMVP,0)
        // テンポラリ行列
        Matrix.setIdentityM(matVP,0)
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
    }
}