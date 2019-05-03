package milu.kiriu2010.exdb1.opengl06.w058

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.color.MgColor
import milu.kiriu2010.gui.model.Board01Model
import milu.kiriu2010.gui.model.Cube01Model
import milu.kiriu2010.gui.model.Torus01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// --------------------------------------------
// グレアフィルタ
// --------------------------------------------
// まぶしい光や反射光などがあふれて見える現象
// 別名ライトブルーム
// --------------------------------------------
class W058Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画オブジェクト(トーラス)
    private lateinit var drawObjTorus: Torus01Model
    // 描画オブジェクト(板ポリゴン)
    private lateinit var drawObjBoard: Board01Model
    // 描画オブジェクト(立方体)
    private lateinit var drawObjCube: Cube01Model

    // シェーダ(シーン)
    private lateinit var screenShader: W058ShaderScreen
    // シェーダ(反射光)
    private lateinit var specularShader: W058ShaderSpecular
    // シェーダ(正射影で板ポリゴンをgaussianフィルタでレンダリング)
    private lateinit var gaussianShader: W058ShaderGaussian
    // シェーダ(正射影でレンダリング結果を合成する)
    private lateinit var finalShader: W058ShaderFinal

    // 画面縦横比
    var ratio: Float = 0f

    // フレームバッファ
    //   0: 反射光+ぼかしのレンダリング
    //   1: 通常のレンダリング
    val bufFrame = IntBuffer.allocate(2)

    // 深度バッファ用レンダ―バッファ
    val bufDepthRender = IntBuffer.allocate(2)

    // フレームバッファ用のテクスチャ
    val frameTexture = IntBuffer.allocate(2)

    // ガウス関数に与えるパラメータ
    var k_gaussian = 5f
    // u_gaussianフィルタの重み係数
    lateinit var u_weight: FloatArray

    // グレアフィルタをかけるかどうか
    var u_glare = 0

    // プロジェクションxビュー(正射影用の座標変換行列)
    private val matVP4O = FloatArray(16)

    override fun onDrawFrame(gl: GL10?) {
        angle[0] =(angle[0]+1)%360

        // 各トーラスの傾き
        val angleF = FloatArray(10)
        (0..9).forEach { i ->
            angleF[i] = ((angle[0]+40*i)%360).toFloat()
        }

        // ビュー×プロジェクション座標変換行列(パースあり)
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,20f,0f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,0f,-1f))
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,90f,ratio,0.1f,30f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // 正射影用の座標変換行列
        Matrix.setLookAtM(matV,0,
                0f,0f,0.5f,
                0f,0f,0f,
                0f,1f,0f)
        Matrix.orthoM(matP,0,-1f,1f,-1f,1f,0.1f,1f)
        Matrix.multiplyMM(matVP4O,0,matP,0,matV,0)

        // gaussianフィルタの重み係数を算出
        u_weight = MyGLFunc.gaussianWeigt(10,k_gaussian,1f)

        // -----------------------------------------------
        // 【0:スペキュラ成分のみをレンダリング】
        // -----------------------------------------------

        // フレームバッファのバインド(0:スペキュラ成分)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[0])

        // フレームバッファを初期化
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // -------------------------------------------------------
        // スペキュラ成分のみをレンダリング(トーラス)(10個)
        // -------------------------------------------------------
        (0..9).forEach { i ->
            Matrix.setIdentityM(matM,0)
            Matrix.rotateM(matM,0,i.toFloat()*360f/9f,0f,1f,0f)
            Matrix.translateM(matM,0,0f,0f,10f)
            Matrix.rotateM(matM,0,angleF[i],1f,1f,0f)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            Matrix.invertM(matI,0,matM,0)
            specularShader.draw(drawObjTorus,matMVP,matI,vecLight,vecEye)
        }

        // ---------------------------------------------------------------------------------
        // 【1:スペキュラ成分のみでレンダリングしたものに横方向のgaussianフィルタをかける】
        // ---------------------------------------------------------------------------------
        // フレームバッファのバインド(1:gaussianフィルタ)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[1])

        // フレームバッファを初期化
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // テクスチャのバインド(0:スペキュラ成分のみ)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[0])

        // 板ポリゴンのレンダリング(横方向ブラー)
        renderBoard(1,1)

        // ----------------------------------------------------------
        // 【2:"スペキュラ成分＋横方向ブラー"に縦方向ブラーをかける】
        // ----------------------------------------------------------

        // フレームバッファのバインド(0:スペキュラ成分のみ)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[0])

        // テクスチャのバインド(1:スペキュラ成分+横方向ブラー)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[1])

        // 板ポリゴンのレンダリング(縦方向のブラー)
        renderBoard(1,0)

        // --------------------------------------------------------------------------
        // 【3:通常のレンダリング】
        // --------------------------------------------------------------------------

        // フレームバッファのバインド()
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[1])

        // フレームバッファを初期化
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // -------------------------------------------------------
        // 通常のレンダリング(トーラス)(10個)
        // -------------------------------------------------------
        (0..9).forEach { i ->
            val amb = MgColor.hsva(i*40,1f,1f,1f)
            Matrix.setIdentityM(matM,0)
            Matrix.rotateM(matM,0,i.toFloat()*360f/9f,0f,1f,0f)
            Matrix.translateM(matM,0,0f,0f,10f)
            Matrix.rotateM(matM,0,angleF[i],1f,1f,0f)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            Matrix.invertM(matI,0,matM,0)
            screenShader.draw(drawObjTorus,matMVP,matI,vecLight,vecEye,amb.toFloatArray())
        }

        // -----------------------------------------------
        // 【4:合成結果をレンダリング】
        // -----------------------------------------------

        // フレームバッファのバインドを解除
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0)

        // テクスチャ0のバインド
        // フルカラートーラスのレンダリング結果
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[1])

        // テクスチャ1のバインド
        // スペキュラ成分のブラー
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[0])

        // canvasを初期化
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 板ポリゴンのレンダリング
        finalShader.draw(drawObjBoard,matVP4O,0,1,u_glare)
    }

    // 板ポリゴンを描画
    //   g:0 => gaussianフィルタ使わない
    //     1 => gaussianフィルタ使う
    //   h:0 => 縦方向
    //     1 => 横方向
    private fun renderBoard(g: Int, h:Int ) {
        // canvasを初期化
        GLES20.glClearColor(0f, 0f, 0f, 1.0f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 板ポリゴンの描画
        gaussianShader.draw(drawObjBoard,matVP4O,0,g,u_weight,h,renderW.toFloat())
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        ratio = width.toFloat()/height.toFloat()

        renderW = width
        renderH = height

        // フレームバッファ生成
        GLES20.glGenFramebuffers(2,bufFrame)
        // レンダ―バッファ生成
        GLES20.glGenRenderbuffers(2,bufDepthRender)
        // フレームバッファを格納するテクスチャ生成
        GLES20.glGenTextures(2,frameTexture)
        (0..1).forEach {
            createFrameBuffer(renderW,renderH,it)
        }
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // canvasを初期化する色を設定する
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // canvasを初期化する際の深度を設定する
        GLES20.glClearDepthf(1f)

        // カリングと深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        GLES20.glEnable(GLES20.GL_CULL_FACE)

        // シェーダ(シーン)
        screenShader = W058ShaderScreen()
        screenShader.loadShader()

        // シェーダ(反射光)
        specularShader = W058ShaderSpecular()
        specularShader.loadShader()

        // シェーダ(正射影で板ポリゴンをgaussianフィルタでレンダリング)
        gaussianShader = W058ShaderGaussian()
        gaussianShader.loadShader()

        // シェーダ(正射影でレンダリング結果を合成する)
        finalShader = W058ShaderFinal()
        finalShader.loadShader()

        // モデル生成(トーラス)
        drawObjTorus = Torus01Model()
        drawObjTorus.createPath(mapOf(
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
        drawObjBoard = Board01Model()
        drawObjBoard.createPath(mapOf(
                "pattern" to 53f
        ))

        // 描画オブジェクト(立方体)
        drawObjCube = Cube01Model()
        drawObjCube.createPath(mapOf(
                "pattern" to 2f,
                "scale"   to 2f,
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