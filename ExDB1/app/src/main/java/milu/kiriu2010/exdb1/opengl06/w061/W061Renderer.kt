package milu.kiriu2010.exdb1.opengl06.w061

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.Matrix
import android.os.SystemClock
import android.util.Log
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.basic.MyNoiseX
import milu.kiriu2010.gui.color.MgColor
import milu.kiriu2010.gui.model.Box01Model
import milu.kiriu2010.gui.model.Cube01Model
import milu.kiriu2010.gui.model.Particle01Model
import milu.kiriu2010.gui.model.Torus01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// 被写界深度
//   パーティクルフォグ
class W061Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画オブジェクト(トーラス)
    private lateinit var drawObjTorus: Torus01Model
    // 描画オブジェクト(ボックスモデル)
    private lateinit var drawObjBox: Box01Model
    // 描画オブジェクト(パーティクル)
    private lateinit var drawObjParticle: Particle01Model

    // シェーダ(メイン)
    private lateinit var mainShader: W061ShaderMain
    // シェーダ(深度をレンダリング)
    private lateinit var depthShader: W061ShaderDepth
    // シェーダ(フォグ)
    private lateinit var fogShader: W061ShaderFog

    // 画面縦横比
    var ratio: Float = 0f

    // ビットマップ配列
    val bmpArray = arrayListOf<Bitmap>()

    // テクスチャ配列
    val textures = IntArray(1)

    // フレームバッファ
    val bufFrame = IntBuffer.allocate(1)

    // 深度バッファ用レンダ―バッファ
    val bufDepthRender = IntBuffer.allocate(1)

    // フレームバッファ用のテクスチャ
    // 1:scene:ぼやけてないシーン
    val frameTexture = IntBuffer.allocate(1)

    // ソフトパーティクルを適用するかどうか
    var u_softParticle = 1

    // 深度値
    var u_depthCoef = 0.05f



    // テクスチャ座標変換行列
    private val matTex = FloatArray(16)

    // プロジェクションxビュー(正射影用の座標変換行列)
    private val matVP4T = FloatArray(16)

    override fun onDrawFrame(gl: GL10?) {
        angle[0] =(angle[0]+1)%360
        val angleF = angle[0].toFloat()

        // ビュー×プロジェクション座標変換行列
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,5f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,ratio,0.1f,10f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // -------------------------------------------------------
        // テクスチャ変換用行列
        // -------------------------------------------------------
        matTex[0] = 0.5f
        matTex[1] = 0f
        matTex[2] = 0f
        matTex[3] = 0f
        matTex[4] = 0f
        matTex[5] = 0.5f
        matTex[6] = 0f
        matTex[7] = 0f
        matTex[8] = 0f
        matTex[9] = 0f
        matTex[10] = 1f
        matTex[11] = 0f
        matTex[12] = 0.5f
        matTex[13] = 0.5f
        matTex[14] = 0f
        matTex[15] = 1f

        Matrix.multiplyMM(matVP4T,0,matTex,0,matP,0)
        Matrix.multiplyMM(matTex,0,matVP4T,0,matV,0)

        // フレームバッファのバインド(1:Scene)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[0])

        // フレームバッファを初期化
        GLES20.glClearColor(0f, 0f, 1f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 深度のレンダリング用にブレンドを無効化
        GLES20.glDisable(GLES20.GL_BLEND)

        // -------------------------------------------------------
        // トーラスをレンダリング
        // -------------------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,angleF,0f,1f,0f)
        Matrix.translateM(matM,0,0f,0.5f,0f)
        Matrix.rotateM(matM,0,90f,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        depthShader.draw(drawObjTorus,matMVP,0f)

        // -------------------------------------------------------
        // ボックスをレンダリング
        // -------------------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.scaleM(matM,0,2f,2f,2f)
        Matrix.translateM(matM,0,0f,-0.25f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        depthShader.draw(drawObjBox,matMVP,0f)

        // -----------------------------------------------
        // 【1:メインシーンをレンダリング】
        // -----------------------------------------------

        // フレームバッファのバインドを解除
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0)

        // canvasを初期化
        GLES20.glClearColor(0f, 0.7f, 0.7f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // ブレンドを有効化
        GLES20.glEnable(GLES20.GL_BLEND)

        // -------------------------------------------------------
        // トーラスをレンダリング
        // -------------------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,angleF,0f,1f,0f)
        Matrix.translateM(matM,0,0f,0.5f,0f)
        Matrix.rotateM(matM,0,90f,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        Matrix.invertM(matI,0,matM,0)
        mainShader.draw(drawObjTorus,matM,matMVP,matI,vecLight,vecEye, floatArrayOf(0f,0f,0f,0f))

        // -------------------------------------------------------
        // ボックスをレンダリング
        // -------------------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.scaleM(matM,0,2f,2f,2f)
        Matrix.translateM(matM,0,0f,-0.25f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        Matrix.invertM(matI,0,matM,0)
        mainShader.draw(drawObjBox,matM,matMVP,matI,vecLight,vecEye, floatArrayOf(0f,0f,0f,0f))

        // -----------------------------------------------
        // 【2:フォグをレンダリング】
        // -----------------------------------------------

        // テクスチャのバインド
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[0])
        //GLES20.glActiveTexture(GLES20.GL_TEXTURE1)
        //GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[0])

        // -------------------------------------------------------
        // パーティクルをレンダリング
        // -------------------------------------------------------

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        ratio = width.toFloat()/height.toFloat()

        renderW = width
        renderH = height

        // ノイズを生成するビットマップに描く
        val noise = MyNoiseX(5,2,0.6f)
        noise.seed = (SystemClock.uptimeMillis()/1000).toInt()
        val size = 128
        val noiseColor = FloatArray(size*size)
        (0 until size).forEach { i ->
            (0 until size).forEach { j ->
                noiseColor[i*size+j] = noise.snoise(i.toFloat(),j.toFloat(),size.toFloat())
            }
        }
        val bmp = noise.createImage(size,noiseColor)

        // テクスチャを作成
        GLES20.glGenTextures(1,textures,0)
        // テクスチャに使う画像をロード
        MyGLFunc.createTexture(0,textures,bmp)

        // フレームバッファ生成
        GLES20.glGenFramebuffers(1,bufFrame)
        // レンダ―バッファ生成
        GLES20.glGenRenderbuffers(1,bufDepthRender)
        // フレームバッファを格納するテクスチャ生成
        GLES20.glGenTextures(1,frameTexture)
        createFrameBuffer(renderW,renderH,0)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // canvasを初期化する色を設定する
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // canvasを初期化する際の深度を設定する
        GLES20.glClearDepthf(1f)

        // カリング,深度テスト,ブレンドを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        GLES20.glEnable(GLES20.GL_CULL_FACE)
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFuncSeparate(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA, GLES20.GL_ONE, GLES20.GL_ONE)
        GLES20.glBlendEquationSeparate(GLES20.GL_FUNC_ADD,GLES20.GL_FUNC_ADD)

        // シェーダ(メイン)
        mainShader = W061ShaderMain()
        mainShader.loadShader()

        // シェーダ(深度をレンダリング)
        depthShader = W061ShaderDepth()
        depthShader.loadShader()

        // シェーダ(フォグ)
        fogShader = W061ShaderFog()
        fogShader.loadShader()

        // モデル生成(トーラス)
        drawObjTorus = Torus01Model()
        drawObjTorus.createPath(mapOf(
                "row"     to 32f,
                "column"  to 32f,
                "iradius" to 0.25f,
                "oradius" to 0.5f
        ))

        // モデル生成(ボックス)
        drawObjBox = Box01Model()
        drawObjBox.createPath(mapOf(
                "colorR"  to 0.3f,
                "colorG"  to 0.3f,
                "colorB"  to 0.3f,
                "colorA"  to 1f
        ))

        // 描画オブジェクト(パーティクル)
        drawObjParticle = Particle01Model()
        drawObjParticle.createPath(mapOf(
                "colorR"  to 1f,
                "colorG"  to 1f,
                "colorB"  to 1f,
                "colorA"  to 1f
        ))

        // 光源位置
        vecLight[0] = -0.577f
        vecLight[1] =  0.577f
        vecLight[2] =  0.577f

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

    // フレームバッファをオブジェクトとして生成する
    private fun createFrameBuffer(width: Int, height: Int, id: Int) {
        val maxRenderbufferSize = IntBuffer.allocate(1)
        GLES20.glGetIntegerv(GLES20.GL_MAX_RENDERBUFFER_SIZE,maxRenderbufferSize)

        Log.d(javaClass.simpleName,"w[${width}]h[${height}]bufSize[${maxRenderbufferSize[0]}]")

        // フレームバッファのバインド
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[id])

        // 深度バッファ用レンダ―バッファのバインド
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER,bufDepthRender[id])

        // レンダ―バッファを深度バッファとして設定
        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, width, height)

        // フレームバッファにレンダ―バッファを関連付ける
        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER,bufDepthRender[id])

        // フレームバッファ用のテクスチャをバインド
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[id])

        // フレームバッファ用のテクスチャにカラー用のメモリ領域を確保
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D,0,GLES20.GL_RGBA,width,height,0,GLES20.GL_RGBA,GLES20.GL_UNSIGNED_BYTE,null)

        // テクスチャパラメータ
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)

        // フレームバッファにテクスチャを関連付ける
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER,GLES20.GL_COLOR_ATTACHMENT0,GLES20.GL_TEXTURE_2D,frameTexture[id],0)

        // 追加
        val status = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER)
        Log.d(javaClass.simpleName,"status[${status}]COMPLETE[${GLES20.GL_FRAMEBUFFER_COMPLETE}]")

        // バインド解除
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0)
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER,0)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0)
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
    }
}