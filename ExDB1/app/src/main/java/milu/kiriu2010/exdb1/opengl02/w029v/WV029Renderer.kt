package milu.kiriu2010.exdb1.opengl02.w029v

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.Matrix
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.Board01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs
import milu.kiriu2010.gui.vbo.es20.ES20VBOIpct

// ----------------------------------------------------------------------------------
// アルファブレンディング
//   色を混ぜ合わせることを可能にする
// ----------------------------------------------------------------------------------
// 描画元(これから描画されようとする色)と描画先(既に描画されている色)を混ぜ合わせる
// ----------------------------------------------------------------------------------
// 描画色 = 描画元の色 * sourceFactor + 描画先の色 * destinationFactor
// ----------------------------------------------------------------------------------
// https://wgld.org/d/webgl/w029.html
// ----------------------------------------------------------------------------------
class WV029Renderer(ctx: Context): MgRenderer(ctx) {

    // モデル
    private lateinit var model: Board01Model

    // シェーダ
    private lateinit var shader: WV029Shader

    // VBO
    private lateinit var bo: ES20VBOAbs

    // ビットマップ配列
    val bmpArray = arrayListOf<Bitmap>()

    // テクスチャ配列
    val textures = IntArray(2)

    // ブレンドタイプ
    var blendType = 1

    // ブレンディングに使うアルファ成分の割合
    var vertexAplha = 0.5f

    // 背景に使う色
    val colorBack = floatArrayOf(0f,0.7f,0.7f,1f)

    init {
        // ビットマップをロード
        bmpArray.clear()
        val bmp0 = BitmapFactory.decodeResource(ctx.resources, R.drawable.texture_w029)
        bmpArray.add(bmp0)
    }

    override fun onDrawFrame(gl: GL10) {
        // 回転角度
        if ( isRunning ) angle[0] =(angle[0]+1)%360
        val t0 = angle[0].toFloat()

        // canvasを初期化
        GLES20.glClearColor( colorBack[0], colorBack[1], colorBack[2], colorBack[3])
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // ビュー×プロジェクション座標変換行列
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // -------------------------------------------------------------------
        // ブレンドタイプ
        // -------------------------------------------------------------------
        // ブレンドを指定しない場合、
        // GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ZERO)
        // を実施しているのと同じ
        // -------------------------------------------------------------------
        // テクスチャ⇒ブレンディングは行なわず、テクスチャそのものの色を表示
        // 板ポリゴン⇒ブレンディングを行う。
        // -------------------------------------------------------------------
        // https://dev.classmethod.jp/smartphone/android/android-rajawali-tutorials-06/
        // https://blog.chocolapod.net/momokan/entry/23
        // http://memo.devjam.net/clip/538
        // http://d.hatena.ne.jp/mswar/20090319/1237474563
        // https://masuqat.net/programming/csharp/OpenTK01-09.php
        // -------------------------------------------------------------------
        when (blendType) {
            // --------------------------------------------------------------
            // 通常
            // --------------------------------------------------------------
            0 -> GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ZERO)
            // --------------------------------------------------------------
            // 半透明合成
            //   透過処理/よく使われるブレンディング
            // --------------------------------------------------------------
            // アルファ成分大⇒描画元(これから描画される色)が濃く表示される。
            //   板ポリゴン⇒モデルそのものの色が表示される
            //   テクスチャ⇒板ポリゴンに隠れる
            // アルファ成分小⇒描画先(既に描画されている色)が濃く表示される
            //   板ポリゴン⇒消える
            // --------------------------------------------------------------
            // 透過率が下がれば下がるほど、元の色が失われていく
            // --------------------------------------------------------------
            1 -> GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)
            // --------------------------------------------------------------
            // 加算合成
            //   炎の表示などに光が集まるような場合に使うブレンディング
            // --------------------------------------------------------------
            // アルファ成分大⇒描画元(これから描画される色)が濃く表示される。
            //   板ポリゴン⇒全体的に白っぽくなる
            //   テクスチャ⇒見える
            // アルファ成分小⇒描画先(既に描画されている色)が濃く表示される
            //   板ポリゴン⇒消える
            // --------------------------------------------------------------
            // 色を重ねた部分が白に近づいていく
            // --------------------------------------------------------------
            2 -> GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE)
            // --------------------------------------------------------------
            // 反転合成
            //   フィルムカメラのネガのようになるブレンディング
            // --------------------------------------------------------------
            3 -> GLES20.glBlendFunc(GLES20.GL_ONE_MINUS_DST_COLOR,GLES20.GL_ZERO)
            // --------------------------------------------------------------
            // 加算＋アルファ
            //   PhotoShop的スクリーン
            // --------------------------------------------------------------
            4 -> GLES20.glBlendFunc(GLES20.GL_SRC_COLOR,GLES20.GL_ONE)
            // --------------------------------------------------------------
            // 乗算合成
            //   カラーセロハンを使ったようなブレンディング
            //   暗い色を重ねるほど暗い色になる
            //   重ねる色に応じて、２つの色は、それぞれ個別に暗くなっていく
            // --------------------------------------------------------------
            5 -> GLES20.glBlendFunc(GLES20.GL_ZERO,GLES20.GL_SRC_COLOR)
        }

        // ----------------------------------------------------------------------
        // 後ろにテクスチャを描画(ブレンドなし)
        // ----------------------------------------------------------------------

        // モデル座標変換行列の生成
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,0.25f,0.25f,-0.25f)
        Matrix.rotateM(matM,0,t0,0f,1f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)

        // テクスチャ0をバインド
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textures[0])

        // ブレンディングを無効にする
        GLES20.glDisable(GLES20.GL_BLEND)

        // テクスチャを描画
        // アルファ成分の変更はしないので、
        // u_vertexAlphaに1を指定している
        shader.draw(model,bo,matMVP,1f,0,1)

        // ----------------------------------------------------------------------
        // 手前に板ポリゴンを描画(ブレンドあり)
        // ----------------------------------------------------------------------

        // モデル座標変換行列の生成(手前に表示)
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-0.25f,-0.25f,0.25f)
        Matrix.rotateM(matM,0,t0,0f,0f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)

        // テクスチャのバインドを解除
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)

        // ブレンディングを有効にする
        GLES20.glEnable(GLES20.GL_BLEND)

        // 板ポリゴンを描画
        // アルファ成分の値によって色が変わる
        shader.draw(model,bo,matMVP,vertexAplha,0,0)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat()/height.toFloat()

        Matrix.perspectiveM(matP,0,60f,ratio,0.1f,100f)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        // 深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)

        // シェーダ
        shader = WV029Shader()
        shader.loadShader()

        // モデル生成
        model = Board01Model()
        model.createPath(mapOf("pattern" to 29f))

        // VBO生成
        bo = ES20VBOIpct()
        bo.makeVIBO(model)

        // テクスチャ作成し、idをtexturesに保存
        GLES20.glGenTextures(1,textures,0)
        MyGLES20Func.checkGlError("glGenTextures")

        // テクスチャ0をバインド
        MyGLES20Func.createTexture(0,textures,bmpArray[0])

        // カメラの位置
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
        bo.deleteVIBO()
        shader.deleteShader()

        GLES20.glDeleteTextures(textures.size,textures,0)
    }
}
