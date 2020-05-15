package milu.kiriu2010.exdb1.opengl03.w030

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.Matrix
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.d2.Board00Model
import milu.kiriu2010.gui.renderer.MgRenderer

// ---------------------------------------------------
// ブレンドファクター:VBOなし
// OpenGL ES 2.0
// ---------------------------------------------------
// https://wgld.org/d/webgl/w030.html
// ---------------------------------------------------
class W030zRenderer(ctx: Context): MgRenderer(ctx) {

    // モデル
    private lateinit var model: Board00Model

    // シェーダ
    private lateinit var shader: W030zShader

    // ビットマップ配列
    val bmpArray = arrayListOf<Bitmap>()

    // テクスチャ配列
    val textures = IntArray(2)

    // コンテキストの色
    val contextColor = floatArrayOf(0f,0.7f,0.7f,1f)
    // ブレンドの色
    val blendColor = floatArrayOf(0f,0f,0f,1f)

    // ブレンド有効
    var blend = booleanArrayOf(false,true)
    // アルファ成分
    var vertexAplha = floatArrayOf(1f,0.5f)
    // 方程式(カラー)
    //  0: テクスチャ用
    //  1: ポリゴン用
    var equationColor = intArrayOf(GLES20.GL_FUNC_ADD,GLES20.GL_FUNC_ADD)
    // 方程式(アルファ)
    var equationAlpha = intArrayOf(GLES20.GL_FUNC_ADD,GLES20.GL_FUNC_ADD)
    // ブレンドファクター(カラー元)
    var blendFctSCBF = intArrayOf(GLES20.GL_ONE,GLES20.GL_SRC_ALPHA)
    // ブレンドファクター(カラー先)
    var blendFctDCBF = intArrayOf(GLES20.GL_ZERO,GLES20.GL_ONE_MINUS_SRC_ALPHA)
    // ブレンドファクター(アルファ元)
    var blendFctSABF = intArrayOf(GLES20.GL_ONE,GLES20.GL_ONE)
    // ブレンドファクター(アルファ先)
    var blendFctDABF = intArrayOf(GLES20.GL_ZERO,GLES20.GL_ONE)

    init {
        // ビットマップをロード
        bmpArray.clear()
        val bmp0 = BitmapFactory.decodeResource(ctx.resources, R.drawable.texture_w029)
        bmpArray.add(bmp0)
    }

    override fun onDrawFrame(gl: GL10) {
        // 回転角度
        angle[0] =(angle[0]+1)%360
        val t0 = angle[0].toFloat()

        // canvasを初期化
        GLES20.glClearColor(contextColor[0],contextColor[1],contextColor[2],contextColor[3])
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // ブレンドカラーを設定
        GLES20.glBlendColor(blendColor[0],blendColor[1],blendColor[2],blendColor[3])

        // ビュー×プロジェクション座標変換行列
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // ----------------------------------------------------------------------
        // 後ろにテクスチャを描画
        // ----------------------------------------------------------------------

        // モデル座標変換行列の生成
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,0.25f,0.25f,-0.25f)
        Matrix.rotateM(matM,0,t0,0f,1f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)

        // テクスチャ0をバインド
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textures[0])

        // テクスチャのブレンディングを有効/無効にする
        when (blend[0]) {
            true -> GLES20.glEnable(GLES20.GL_BLEND)
            false -> GLES20.glDisable(GLES20.GL_BLEND)
        }
        GLES20.glBlendEquationSeparate(equationColor[0],equationAlpha[0])
        GLES20.glBlendFuncSeparate(blendFctSCBF[0],blendFctDCBF[0],blendFctSABF[0],blendFctDABF[0])

        // テクスチャ描画
        shader.draw(model,matMVP,vertexAplha[0],0,1)

        // ----------------------------------------------------------------------
        // 手前に板ポリゴンを描画
        // ----------------------------------------------------------------------

        // テクスチャのバインドを解除
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0)

        // モデル座標変換行列の生成
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-0.25f,-0.25f,0.25f)
        Matrix.rotateM(matM,0,t0,0f,0f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)

        // ポリゴンのブレンディングを有効/無効にする
        when (blend[1]) {
            true -> GLES20.glEnable(GLES20.GL_BLEND)
            false -> GLES20.glDisable(GLES20.GL_BLEND)
        }
        GLES20.glBlendEquationSeparate(equationColor[1],equationAlpha[1])
        GLES20.glBlendFuncSeparate(blendFctSCBF[1],blendFctDCBF[1],blendFctSABF[1],blendFctDABF[1])

        // ポリゴンを描画
        shader.draw(model,matMVP,vertexAplha[1],0,0)
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
        shader = W030zShader()
        shader.loadShader()

        // モデル生成
        model = Board00Model()
        model.createPath(mapOf("pattern" to 29f))

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
    }
}
