package milu.kiriu2010.exdb1.es30x01.a03

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES30
import android.opengl.Matrix
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.basic.MyGLES30Func
import milu.kiriu2010.gui.model.Board01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -----------------------------------------
// GLSL ES 3.0
// -----------------------------------------
// https://wgld.org/d/webgl2/w003.html
// -----------------------------------------
class A03Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画オブジェクト(板ポリゴン)
    private lateinit var modelBoard: Board01Model

    // シェーダA
    private lateinit var shaderA: ES30a03ShaderA
    // シェーダB
    private lateinit var shaderB: ES30a03ShaderB

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

    init {
        // ビットマップをロード
        bmpArray.clear()
        val bmp0 = BitmapFactory.decodeResource(ctx.resources, R.drawable.texture_lenna_a03)
        bmpArray.add(bmp0)
    }

    override fun onDrawFrame(gl: GL10?) {
        angle[0] =(angle[0]+1)%360
        val t0 = angle[0].toFloat()

        // フレームバッファのバインド(マスク用)
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER,bufFrame[0])

        // フレームバッファを初期化
        GLES30.glClearColor(0.3f,0.3f,0.3f,1f)
        GLES30.glClearDepthf(1f)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        // テクスチャの適用
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,textures[0])

        // 板ポリゴンをレンダリング
        val matN = FloatArray(16)
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,t0,0f,1f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        Matrix.invertM(matI,0,matM,0)
        Matrix.transposeM(matN,0,matI,0)
        shaderA.draw(modelBoard,matM,matMVP,matN,vecLight,vecEye,0)

        // フレームバッファのバインドを解除
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER,0)

        // canvasを初期化
        GLES30.glClearColor(0f, 0f, 0f, 1f)
        GLES30.glClearDepthf(1f)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        // フレームバッファをテクスチャとして適用
        GLES30.glActiveTexture(GLES30.GL_TEXTURE1)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,frameTexture[0])

        // 板ポリゴンをレンダリング
        shaderB.draw(modelBoard,1)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)

        ratio = width.toFloat()/height.toFloat()

        renderW = width
        renderH = height

        // テクスチャを作成
        GLES30.glGenTextures(1,textures,0)
        // テクスチャに使う画像をロード
        MyGLES30Func.createTexture(0,textures,bmpArray[0],renderW)

        // フレームバッファ生成
        GLES30.glGenFramebuffers(1,bufFrame)
        // レンダ―バッファ生成
        GLES30.glGenRenderbuffers(1,bufDepthRender)
        // フレームバッファを格納するテクスチャ生成
        GLES30.glGenTextures(1,frameTexture)
        MyGLES30Func.createFrameBuffer(renderW,renderH,0,bufFrame,bufDepthRender,frameTexture)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // カリングと深度テストを有効にする
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
        GLES30.glDepthFunc(GLES30.GL_LEQUAL)
        GLES30.glEnable(GLES30.GL_CULL_FACE)

        // シェーダA
        shaderA = ES30a03ShaderA()
        shaderA.loadShader()

        // シェーダB
        shaderB = ES30a03ShaderB()
        shaderB.loadShader()

        // モデル生成(板ポリゴン)
        modelBoard = Board01Model()
        modelBoard.createPath(mapOf(
                "pattern" to 53f
        ))

        // ライトの向き
        vecLight[0] = 5f
        vecLight[1] = 2f
        vecLight[2] = 5f

        // 視点座標
        vecEye[0] =  0f
        vecEye[1] = 10f
        vecEye[2] =  0f
        // 視点の上方向
        vecEyeUp[0] =  0f
        vecEyeUp[1] =  0f
        vecEyeUp[2] =  1f

        // ビュー×プロジェクション座標変換行列
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,90f,ratio,0.1f,20f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
    }
}