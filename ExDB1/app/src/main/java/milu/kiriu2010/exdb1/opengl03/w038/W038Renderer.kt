package milu.kiriu2010.exdb1.opengl03.w038

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.d2.Board00Model
import milu.kiriu2010.gui.renderer.MgRenderer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// ------------------------------------------------
// ステンシルバッファ
// ------------------------------------------------
// 基準値を保存するためのバッファとして機能する
// ------------------------------------------------
// https://wgld.org/d/webgl/w038.html
// ------------------------------------------------
class W038Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画オブジェクト
    private lateinit var model: Board00Model

    // シェーダ
    private lateinit var shader: W038Shader

    // 画面縦横比
    var ratio: Float = 0f

    // ビットマップ配列
    val bmpArray = arrayListOf<Bitmap>()

    // テクスチャ配列
    val textures = IntArray(2)

    init {
        // ビットマップをロード
        bmpArray.clear()
        val bmp0 = BitmapFactory.decodeResource(ctx.resources, R.drawable.texture_w038)
        bmpArray.add(bmp0)

        // ステンシルバッファに設定できる最大値をコンソールに出力
        val stencilBufSize = IntArray(1)
        GLES20.glGetIntegerv(GLES20.GL_STENCIL_BITS,stencilBufSize,0)
        Log.d(javaClass.simpleName,"stencilBufSize:${stencilBufSize[0]}")
    }

    override fun onDrawFrame(gl: GL10?) {
        // canvasを初期化
        GLES20.glClearColor(0.0f, 0.7f, 0.7f, 1.0f)
        GLES20.glClearDepthf(1f)
        // ステンシルバッファの基準値を0にする
        GLES20.glClearStencil(0)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_STENCIL_BUFFER_BIT)

        // 回転角度
        angle[0] =(angle[0]+1)%360
        val t0 = angle[0].toFloat()

        // クォータニオンを行列に適用
        var matQ = qtnNow.toMatIV()

        // カメラの位置
        // ビュー座標変換行列
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,ratio,0.1f,100f)
        // ビュー座標変換行列にクォータニオンの回転を適用
        Matrix.multiplyMM(matV,0,matV,0,matQ,0)
        // ビュー×プロジェクション
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // テクスチャ0をバインド
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textures[0])

        // ステンシルテストを有効にする
        GLES20.glEnable(GLES20.GL_STENCIL_TEST)

        // モデル１をレンダリング
        // -------------------------------------------------------------
        // 常にステンシルテストをパスする
        // ステンシルテストをパスすると、
        // ステンシルバッファの対象ピクセルの基準値が１に書き換えられる
        // -------------------------------------------------------------
        // 0の補数(~0)を0.inv()でなく0xffにしてみる
        // -------------------------------------------------------------
        GLES20.glStencilFunc(GLES20.GL_ALWAYS,1, 0.inv() )
        GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_REPLACE, GLES20.GL_REPLACE)
        render(floatArrayOf(-0.25f,0.25f,-0.5f))

        // モデル２をレンダリング
        // -------------------------------------------------------------
        // 常にステンシルテストをパスする
        // ステンシルテストをパスすると、
        // ステンシルバッファの対象ピクセルの基準値がインクリメントされる
        // すなわち、２枚目のポリゴンがレンダリングされた時点で、
        // 双方のポリゴンが重なっている領域の基準値は２
        // 重なっていない部分の基準値は最大でも１にしかなりえない
        // -------------------------------------------------------------
        // 0の補数(~0)を0.inv()でなく0xffにしてみる
        // -------------------------------------------------------------
        GLES20.glStencilFunc(GLES20.GL_ALWAYS,0, 0.inv() )
        GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_INCR, GLES20.GL_INCR)
        render(floatArrayOf(0f,0f,0f))

        // モデル３をレンダリング
        // -------------------------------------------------------------
        // ステンシルバッファの基準値２と同じものしか
        // ステンシルテストをパスしない
        // １枚目と２枚目が重なり合っている領域が基準値２の領域なので、
        // ３枚目のポリゴンは２つのポリゴンが重なっている
        // 領域しかレンダリングされない
        // -------------------------------------------------------------
        // 0の補数(~0)を0.inv()でなく0xffにしてみる
        // -------------------------------------------------------------
        GLES20.glStencilFunc(GLES20.GL_EQUAL,2, 0.inv() )
        GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_KEEP)
        render(floatArrayOf(0.25f,-0.25f,0.5f))

        // ステンシルテストを無効にする
        GLES20.glDisable(GLES20.GL_STENCIL_TEST)
    }

    // モデルをレンダリング
    private fun render(tr: FloatArray) {
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,tr[0],tr[1],tr[2])
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        Matrix.invertM(matI,0,matM,0)
        shader.draw(model,matMVP,matI,vecLight,0)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        ratio = width.toFloat()/height.toFloat()
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // カリングと深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)

        // シェーダプ
        shader = W038Shader()
        shader.loadShader()

        // モデル生成
        model = Board00Model()
        model.createPath(mapOf("pattern" to 29f))

        // テクスチャ作成し、idをtexturesに保存
        GLES20.glGenTextures(1,textures,0)
        MyGLES20Func.checkGlError("glGenTextures")

        // テクスチャ0をバインド
        MyGLES20Func.createTexture(0,textures,bmpArray[0])

        // 光源位置
        vecLight[0] = 1f
        vecLight[1] = 1f
        vecLight[2] = 1f
        // 視点位置
        vecEye[0] = 0f
        vecEye[1] = 0f
        vecEye[2] = 5f

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