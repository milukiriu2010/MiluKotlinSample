package milu.kiriu2010.exdb1.opengl06.w058

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import milu.kiriu2010.exdb1.opengl05.w053.W053ShaderScreen
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.color.MgColor
import milu.kiriu2010.gui.model.Board01Model
import milu.kiriu2010.gui.model.Cube01Model
import milu.kiriu2010.gui.model.Torus01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.math.MyMathUtil
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// --------------------------------------------
// グレアフィルタ
// --------------------------------------------
// まぶしい光や反射光などがあふれて見える現象
// 別名ライトブルーム
// --------------------------------------------
// https://wgld.org/d/webgl/w058.html
// --------------------------------------------
class W058Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画オブジェクト(トーラス)
    private lateinit var modelTorus: Torus01Model
    // 描画オブジェクト(板ポリゴン)
    private lateinit var modelBoard: Board01Model
    // 描画オブジェクト(立方体)
    private lateinit var modelCube: Cube01Model

    // シェーダ(シーン)
    private lateinit var screenShader: W053ShaderScreen
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
        u_weight = MyMathUtil.gaussianWeigt(10,k_gaussian,1f)

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
            specularShader.draw(modelTorus,matMVP,matI,vecLight,vecEye)
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
            screenShader.draw(modelTorus,matMVP,matI,vecLight,vecEye,amb.toFloatArray())
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
        finalShader.draw(modelBoard,matVP4O,0,1,u_glare)
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
        gaussianShader.draw(modelBoard,matVP4O,0,g,u_weight,h,renderW.toFloat())
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
        MyGLES20Func.createFrameBuffer(renderW,renderH,0,bufFrame,bufDepthRender,frameTexture)
        MyGLES20Func.createFrameBuffer(renderW,renderH,1,bufFrame,bufDepthRender,frameTexture)
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
        screenShader = W053ShaderScreen()
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
        modelBoard = Board01Model()
        modelBoard.createPath(mapOf(
                "pattern" to 53f
        ))

        // 描画オブジェクト(立方体)
        modelCube = Cube01Model()
        modelCube.createPath(mapOf(
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
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
    }
}