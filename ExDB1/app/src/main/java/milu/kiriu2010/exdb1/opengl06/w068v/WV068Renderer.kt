package milu.kiriu2010.exdb1.opengl06.w068v

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.Matrix
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.color.MgColor
import milu.kiriu2010.gui.model.d2.Board00Model
import milu.kiriu2010.gui.model.Torus01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs
import milu.kiriu2010.gui.vbo.es20.ES20VBOIpnc
import milu.kiriu2010.gui.vbo.es20.ES20VBOIpt
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -----------------------------------------
// emuglGLESv2_enc: device/generic/goldfish-opengl/system/GLESv2_enc/GL2Encoder.cpp:s_glVertexAttribPointer:599 GL error 0x501
//    Info: Invalid vertex attribute index. Wanted index: 4294967295. Max index: 16
// WV068ShaderZoomBlur:a_TextureCoord:Board00Model:1281
// -----------------------------------------
// ゴッドレイフィルタ:VBOあり
// OpenGL ES 2.0
// -----------------------------------------
// https://wgld.org/d/webgl/w068.html
// -----------------------------------------
class WV068Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画オブジェクト(トーラス)
    private lateinit var modelTorus: Torus01Model
    // 描画オブジェクト(板ポリゴン)
    private lateinit var modelBoard: Board00Model

    // VBO(トーラス)
    private lateinit var boTorus: ES20VBOAbs
    // VBO(板ポリゴン)
    private lateinit var boBoard: ES20VBOAbs

    // シェーダ(メイン)
    private lateinit var screenShader: WV068ShaderScreen
    // シェーダ(ズームブラー)
    private lateinit var zoomBlurShader: WV068ShaderZoomBlur
    // シェーダ(正射影)
    private lateinit var orthShader: WV068ShaderOrth

    // 画面縦横比
    var ratio: Float = 1f

    // ビットマップ配列
    val bmpArray = arrayListOf<Bitmap>()

    // テクスチャ配列
    val textures = IntArray(2)

    // フレームバッファ
    // 0:マスク
    // 1:ブラー
    val bufFrame = IntBuffer.allocate(2)

    // 深度バッファ用レンダ―バッファ
    val bufDepthRender = IntBuffer.allocate(2)

    // フレームバッファ用のテクスチャ
    val frameTexture = IntBuffer.allocate(2)

    // 色相用カウンタ
    var cntColor = 0

    // 描画対象のテクスチャ
    var textureType = 0

    var u_strength = 5f

    // 正射影用の座標変換行列(合成用)
    //   ビュー×プロジェクション(正射影)
    val matOVP = FloatArray(16)

    // マウス位置
    val mouseP = FloatArray(2)

    init {
        // ビットマップをロード
        bmpArray.clear()
        val bmp0 = BitmapFactory.decodeResource(ctx.resources, R.drawable.texture_w68)
        bmpArray.add(bmp0)
    }

    override fun onDrawFrame(gl: GL10?) {
        angle[0] =(angle[0]+1)%360
        val t0 = angle[0].toFloat()
        if ( (angle[0]%2) == 0 ) {
            cntColor++
        }

        // テクスチャの適用
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textures[0])

        // フレームバッファのバインド(マスク用)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[0])

        // フレームバッファを初期化
        GLES20.glClearColor(1f,1f,1f,1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 一度深度バッファへの描きこみを無効にする
        GLES20.glDepthMask(false)

        // 板ポリゴンをレンダリングしテクスチャを画面いっぱいに貼り付ける
        orthShader.draw(modelBoard,boBoard,matOVP,0)

        // 深度バッファへの描きこみを有効化する
        GLES20.glDepthMask(true)

        // -------------------------------------------------------
        // トーラス描画(9個)
        // -------------------------------------------------------
        (0..8).forEach { i ->
            val amb = MgColor.hsva(i*40,1f,1f,1f)
            Matrix.setIdentityM(matM,0)
            Matrix.rotateM(matM,0,i.toFloat()*360f/9f,0f,1f,0f)
            Matrix.translateM(matM,0,0f,0f,10f)
            Matrix.rotateM(matM,0,t0,1f,1f,0f)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            Matrix.invertM(matI,0,matM,0)
            screenShader.draw(modelTorus,boTorus,matMVP,matI,vecLight,vecEye,amb.toFloatArray(),0)
        }

        // フレームバッファのバインド(ブラー用)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[1])

        // フレームバッファを初期化
        GLES20.glClearColor(0f,0f,0f,1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // フレームバッファ(マスク用)をテクスチャとして適用
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[0])

        // ズームブラーをかける
        zoomBlurShader.draw(modelBoard,boBoard,matOVP,0,u_strength,renderW.toFloat(),mouseP)

        // フレームバッファのバインドを解除
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0)

        // canvasを初期化
        GLES20.glClearColor(0f, 0f, 0.7f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 背景用に読み込んだ画像をテクスチャとして適用
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textures[0])

        // 一度深度バッファへの描きこみを無効にする
        GLES20.glDepthMask(false)

        // 板ポリゴンをレンダリングしテクスチャを画面いっぱいに貼り付ける
        orthShader.draw(modelBoard,boBoard,matOVP,0)

        // 深度バッファへの描きこみを有効化する
        GLES20.glDepthMask(true)

        // -------------------------------------------------------
        // トーラス(9個)をライティング有効でレンダリング
        // -------------------------------------------------------
        (0..8).forEach { i ->
            val amb = MgColor.hsva(i*40,1f,1f,1f)
            Matrix.setIdentityM(matM,0)
            Matrix.rotateM(matM,0,i.toFloat()*360f/9f,0f,1f,0f)
            Matrix.translateM(matM,0,0f,0f,10f)
            Matrix.rotateM(matM,0,t0,1f,1f,0f)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            Matrix.invertM(matI,0,matM,0)
            screenShader.draw(modelTorus,boTorus,matMVP,matI,vecLight,vecEye,amb.toFloatArray(),0)
        }

        // フレームバッファ(ブラー用)をテクスチャとして適用
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[1])

        // 加算合成するためにブレンドを有効化する
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFuncSeparate(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE,GLES20.GL_ONE,GLES20.GL_ONE)

        // ブラーを合成
        orthShader.draw(modelBoard,boBoard,matOVP,0)

        // ブレンドの無効化
        GLES20.glDisable(GLES20.GL_BLEND)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        ratio = width.toFloat()/height.toFloat()

        renderW = width
        renderH = height

        // マウス位置
        mouseP[0] = renderW/2f
        mouseP[1] = renderH/2f

        // テクスチャを作成
        GLES20.glGenTextures(1,textures,0)
        // テクスチャに使う画像をロード
        MyGLES20Func.createTexture(0,textures,bmpArray[0],renderW)

        // フレームバッファ生成
        GLES20.glGenFramebuffers(2,bufFrame)
        // レンダ―バッファ生成
        GLES20.glGenRenderbuffers(2,bufDepthRender)
        // フレームバッファを格納するテクスチャ生成
        GLES20.glGenTextures(2,frameTexture)

        // 0:マスク
        MyGLES20Func.createFrameBuffer(renderW,renderH,0,bufFrame,bufDepthRender,frameTexture)
        // 1:ブラー
        MyGLES20Func.createFrameBuffer(renderW,renderH,1,bufFrame,bufDepthRender,frameTexture)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // カリングと深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        GLES20.glEnable(GLES20.GL_CULL_FACE)

        // シェーダ(メイン)
        screenShader = WV068ShaderScreen()
        screenShader.loadShader()

        // シェーダ(ズームブラー)
        zoomBlurShader = WV068ShaderZoomBlur()
        zoomBlurShader.loadShader()

        // シェーダ(正射影)
        orthShader = WV068ShaderOrth()
        orthShader.loadShader()

        // モデル生成(トーラス)
        modelTorus = Torus01Model()
        modelTorus.createPath(mapOf(
                "row"     to 32f,
                "column"  to 32f,
                "iradius" to 1f,
                "oradius" to 2f,
                "colorR"  to 1f,
                "colorG"  to 1f,
                "colorB"  to 1f,
                "colorA"  to 1f
        ))

        // モデル生成(板ポリゴン)
        modelBoard = Board00Model()
        modelBoard.createPath(mapOf(
                "pattern" to 53f
        ))

        // VBO(トーラス)
        boTorus = ES20VBOIpnc()
        boTorus.makeVIBO(modelTorus)

        // VBO(板ポリゴン)
        boBoard = ES20VBOIpt()
        boBoard.makeVIBO(modelBoard)

        // ライトの向き
        vecLight[0] = -0.577f
        vecLight[1] =  0.577f
        vecLight[2] =  0.577f

        // 視点座標
        vecEye[0] =  0f
        vecEye[1] = 20f
        vecEye[2] =  0f

        // 視点の上方向
        vecEyeUp[0] =  0f
        vecEyeUp[1] =  0f
        vecEyeUp[2] = -1f

        // ビュー×プロジェクション座標変換行列
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,90f,ratio,0.1f,100f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // 正射影用の座標変換行列(合成用)
        Matrix.setLookAtM(matV,0,
                0f,0f,0.5f,
                0f,0f,0f,
                0f,1f,0f)
        Matrix.orthoM(matP,0,-1f,1f,-1f,1f,0.1f,1f)
        Matrix.multiplyMM(matOVP,0,matP,0,matV,0)
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
        boTorus.deleteVIBO()
        boBoard.deleteVIBO()
        screenShader.deleteShader()
        zoomBlurShader.deleteShader()
        orthShader.deleteShader()

        GLES20.glDeleteTextures(textures.size,textures,0)
        GLES20.glDeleteTextures(2,frameTexture)
        GLES20.glDeleteRenderbuffers(2,bufDepthRender)
        GLES20.glDeleteFramebuffers(2,bufFrame)
    }
}