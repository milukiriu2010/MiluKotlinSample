package milu.kiriu2010.exdb1.opengl02.w026v

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

// ---------------------------------------------------
// テクスチャ
// ---------------------------------------------------
// ミップマップとは、
// あらかじめ複数の大きさのイメージデータを
// 内部的に用意しておく仕組み
// テクスチャイメージを縮小表示する際に力を発揮する
// ---------------------------------------------------
// https://wgld.org/d/webgl/w026.html
// ---------------------------------------------------
class WV026Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画オブジェクト
    private lateinit var model: Board01Model

    // シェーダ
    private lateinit var shader: WV026Shader

    // VBO
    private lateinit var bo: ES20VBOAbs

    // ビットマップ配列
    val bmpArray = arrayListOf<Bitmap>()

    // テクスチャ配列
    val textures = IntArray(1)

    init {
        // ビットマップをロード
        bmpArray.clear()
        val bmp0 = BitmapFactory.decodeResource(ctx.resources, R.drawable.texture_w026)
        bmpArray.add(bmp0)
    }

    override fun onDrawFrame(gl: GL10) {

        // canvasを初期化
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        angle[0] =(angle[0]+1)%360
        val t0 = angle[0].toFloat()

        // テクスチャをバインドする
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0])

        // ビュー×プロジェクション座標変換行列
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // モデル座標変換行列の生成
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,t0,0f,1f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)

        // モデル描画
        shader.draw(model,bo,matMVP,0)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat()/height.toFloat()

        Matrix.perspectiveM(matP,0,45f,ratio,0.1f,100f)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        // 深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)

        // シェーダ
        shader = WV026Shader()
        shader.loadShader()

        // モデル
        model = Board01Model()
        model.createPath(mapOf(
                "colorR" to 1f,
                "colorG" to 1f,
                "colorB" to 1f,
                "colorA" to 1f
        ))

        // VBO生成
        bo = ES20VBOIpct()
        bo.makeVIBO(model)

        // テクスチャ作成し、idをtexturesに保存
        GLES20.glGenTextures(1,textures,0)
        MyGLES20Func.createTexture(0,textures,bmpArray[0])
        MyGLES20Func.checkGlError("glGenTextures")

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