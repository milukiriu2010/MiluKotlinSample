package milu.kiriu2010.exdb1.opengl06.w065

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.color.MgColor
import milu.kiriu2010.gui.model.Board01Model
import milu.kiriu2010.gui.model.Sphere01Model
import milu.kiriu2010.gui.model.Torus01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// 後光表面化散乱
class W065Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画オブジェクト(トーラス)
    private lateinit var drawObjTorus: Torus01Model
    // 描画オブジェクト(球体)
    private lateinit var drawObjSphere: Sphere01Model
    // 描画オブジェクト(板ポリゴン)
    private lateinit var drawObjBoard: Board01Model

    // シェーダ(メイン)
    private lateinit var mainShader: W065ShaderMain
    // シェーダ(深度値の差分レンダリング)
    private lateinit var diffShader: W065ShaderDiff
    // シェーダ(裏面深度値レンダリング)
    private lateinit var depthShader: W065ShaderDepth
    // シェーダ(ライトの位置を点でレンダリング)
    private lateinit var pointShader: W065ShaderPoint
    // シェーダ(ガウシアンブラー)
    private lateinit var gaussianShader: W065ShaderGaussian
    // シェーダ(正射影レンダリング)
    private lateinit var orthShader: W065ShaderOrth

    // 画面縦横比
    var ratio: Float = 0f

    // ビットマップ配列
    val bmpArray = arrayListOf<Bitmap>()

    // テクスチャ配列
    val textures = IntArray(1)

    // フレームバッファ
    //   0:裏面の深度値を描画
    //   1:深度の差分を描画
    //   2:ブラーで使用する中間バッファ
    //   3:ブラーをかけた深度値を描画
    val bufFrame = IntBuffer.allocate(4)

    // 深度バッファ用レンダ―バッファ
    val bufDepthRender = IntBuffer.allocate(4)

    // フレームバッファ用のテクスチャ
    val frameTexture = IntBuffer.allocate(4)

    // u_gaussianフィルタの重み係数
    val u_weight = MyGLFunc.gaussianWeigt(5,0.25f)

    // フォーカスする深度値
    var u_depthOffset = 0f

    // 選択値
    var u_result = 0

    // 視点ベクトルの逆行列
    private var vecI4Eye = FloatArray(3)

    // テクスチャ変換用行列
    private val matTex = FloatArray(16)

    // プロジェクションxビュー(正射影用の座標変換行列)
    private val matVP4O = FloatArray(16)

    init {
        // テクスチャ変換用行列を初期化
        matTex[0]  = 0.5f
        matTex[1]  =   0f
        matTex[2]  =   0f
        matTex[3]  =   0f
        matTex[4]  =   0f
        matTex[5]  = 0.5f
        matTex[6]  =   0f
        matTex[7]  =   0f
        matTex[8]  =   0f
        matTex[9]  =   0f
        matTex[10] =   1f
        matTex[11] =   0f
        matTex[12] = 0.5f
        matTex[13] = 0.5f
        matTex[14] =   0f
        matTex[15] =   1f
    }

    override fun onDrawFrame(gl: GL10?) {
        angle[0] =(angle[0]+1)%360

        // 各トーラスの傾き
        val angleF = FloatArray(10)
        (0..9).forEach { i ->
            angleF[i] = ((angle[0]+40*i)%360).toFloat()
        }

        // ビュー×プロジェクション座標変換行列
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,7f))
        vecI4Eye = qtnNow.toVecIII(floatArrayOf(0f,0f,-7f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))

        // 最終シーンで使う透視射影変換行列を生成
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,ratio,0.1f,15f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        /*
        // 正射影用の座標変換行列
        Matrix.setLookAtM(matV,0,
                0f,0f,0.5f,
                0f,0f,0f,
                0f,1f,0f)
        Matrix.orthoM(matP,0,-1f,1f,-1f,1f,0.1f,1f)
        Matrix.multiplyMM(matVP4O,0,matP,0,matV,0)





























        // -----------------------------------------------
        // 【0:ぼやけていないシーンのレンダリング】
        // -----------------------------------------------

        // フレームバッファのバインド(1:Scene)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[1])

        // フレームバッファを初期化
        GLES20.glClearColor(0f, 0f, 1f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // テクスチャのバインド
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textures[0])

        // ぼやけていないシーンをレンダリング(立方体)
        GLES20.glCullFace(GLES20.GL_FRONT)
        Matrix.setIdentityM(matM,0)
        Matrix.scaleM(matM,0,3.5f,2.5f,10f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        Matrix.invertM(matI,0,matM,0)
        mainShader.draw(drawObjSphere,matMVP,matI,vecLight,
                floatArrayOf(0f,0f,10f), floatArrayOf(1f,1f,1f,1f), 0)

        // -------------------------------------------------------
        // ぼやけていないシーンをレンダリング(トーラス)(10個)
        // -------------------------------------------------------
        GLES20.glCullFace(GLES20.GL_BACK)
        (0..9).forEach { i ->
            val amb = MgColor.hsva(i*40,1f,1f,1f)
            Matrix.setIdentityM(matM,0)
            Matrix.translateM(matM,0,0.2f*i.toFloat(),0f,8.8f-2.2f*i.toFloat())
            Matrix.rotateM(matM,0,angleF[i],1f,1f,0f)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            Matrix.invertM(matI,0,matM,0)
            mainShader.draw(drawObjTorus,matMVP,matI,vecLight,vecEye,amb.toFloatArray(),0)
        }

        // --------------------------------------------------------------------------
        // 【1:ぼやけていないシーンから小さくぼやけたシーン生成し一時バッファに格納】
        // --------------------------------------------------------------------------
        // フレームバッファのバインド(4:テンポラリ)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[4])

        // フレームバッファを初期化
        GLES20.glClearColor(0f, 0f, 1f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // テクスチャのバインド(Scene)
        // ボケていないシーンをテクスチャとしてバインド
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[1])

        // 板ポリゴンのレンダリング
        gaussianShader.draw(drawObjBoard,matVP4O,0,1,u_weight,1,renderW.toFloat())

        // -----------------------------------------------
        // 【2:小さくぼやけたシーンをバッファに描く】
        // -----------------------------------------------

        // フレームバッファのバインド(2:Blur1)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[2])

        // フレームバッファを初期化
        GLES20.glClearColor(0f, 0f, 1f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // テクスチャのバインド(4:テンポラリ)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[4])

        // 板ポリゴンのレンダリング
        gaussianShader.draw(drawObjBoard,matVP4O,0,1,u_weight,0,renderW.toFloat())

        // --------------------------------------------------------------------------
        // 【3:ぼやけていないシーンから大きくぼやけたシーン生成し一時バッファに格納】
        // --------------------------------------------------------------------------

        // フレームバッファのバインド(テンポラリ)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[4])

        // フレームバッファを初期化
        GLES20.glClearColor(0f, 0f, 1f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // テクスチャのバインド(1:Scene)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[1])

        // 板ポリゴンのレンダリング
        gaussianShader.draw(drawObjBoard,matVP4O,0,1,u_weight2,1,renderW.toFloat())

        // -----------------------------------------------
        // 【4:小さくぼやけたシーンをバッファに描く】
        // -----------------------------------------------

        // フレームバッファのバインド(3:Blur2)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[3])

        // フレームバッファを初期化
        GLES20.glClearColor(0f, 0f, 1f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // テクスチャのバインド(4:テンポラリ)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[4])

        // 板ポリゴンのレンダリング
        gaussianShader.draw(drawObjBoard,matVP4O,0,1,u_weight2,0,renderW.toFloat())

        // -----------------------------------------------
        // 【5:深度値マップをレンダリング】
        // -----------------------------------------------

        // フレームバッファのバインド(0:Depth)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[0])

        // フレームバッファを初期化
        GLES20.glClearColor(1f, 1f, 1f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 深度をレンダリング(立方体)
        GLES20.glCullFace(GLES20.GL_FRONT)
        Matrix.setIdentityM(matM,0)
        Matrix.scaleM(matM,0,3.5f,2.5f,10f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        depthShader.draw(drawObjSphere,matMVP,u_depthOffset)

        // -------------------------------------------------------
        // 深度をレンダリング(トーラス)(10個)
        // -------------------------------------------------------
        GLES20.glCullFace(GLES20.GL_BACK)
        (0..9).forEach { i ->
            val amb = MgColor.hsva(i*40,1f,1f,1f)
            Matrix.setIdentityM(matM,0)
            Matrix.translateM(matM,0,0.2f*i.toFloat(),0f,8.8f-2.2f*i.toFloat())
            Matrix.rotateM(matM,0,angleF[i],1f,1f,0f)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            depthShader.draw(drawObjTorus,matMVP,u_depthOffset)
        }

        // -----------------------------------------------
        // 【6:合成結果をレンダリング】
        // -----------------------------------------------

        // フレームバッファのバインドを解除
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0)

        // テクスチャのバインド
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[0])
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[1])
        GLES20.glActiveTexture(GLES20.GL_TEXTURE2)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[2])
        GLES20.glActiveTexture(GLES20.GL_TEXTURE3)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[3])

        // canvasを初期化
        GLES20.glClearColor(0f, 0f, 1f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 板ポリゴンのレンダリング
        orthShader.draw(drawObjBoard,matVP4O,0,1,2,3,u_result)

        */
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        ratio = width.toFloat()/height.toFloat()

        renderW = width
        renderH = height

        // フレームバッファ生成
        GLES20.glGenFramebuffers(4,bufFrame)
        // レンダ―バッファ生成
        GLES20.glGenRenderbuffers(4,bufDepthRender)
        // フレームバッファを格納するテクスチャ生成
        GLES20.glGenTextures(4,frameTexture)
        (0..3).forEach {
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

        // 正射影用の座標変換行列
        Matrix.setLookAtM(matV, 0,
                0f, 0f, 0.5f,
                0f, 0f, 0f,
                0f, 1f, 0f)
        Matrix.orthoM(matP,0,-1f,1f,-1f,1f,0.1f,1f)
        Matrix.multiplyMM(matVP4O,0,matP,0,matV,0)

        // シェーダ(メイン)
        mainShader = W065ShaderMain()
        mainShader.loadShader()

        // シェーダ(深度値の差分レンダリング)
        diffShader = W065ShaderDiff()
        diffShader.loadShader()

        // シェーダ(裏面深度値レンダリング)
        depthShader = W065ShaderDepth()
        depthShader.loadShader()

        // シェーダ(ライトの位置を点でレンダリング)
        pointShader = W065ShaderPoint()
        pointShader.loadShader()

        // シェーダ(ガウシアンブラー)
        gaussianShader = W065ShaderGaussian()
        gaussianShader.loadShader()

        // シェーダ(正射影レンダリング)
        orthShader = W065ShaderOrth()
        orthShader.loadShader()

        // モデル生成(トーラス)
        drawObjTorus = Torus01Model()
        drawObjTorus.createPath(mapOf(
                "row"     to 32f,
                "column"  to 32f,
                "iradius" to 0.25f,
                "oradius" to 0.5f,
                "colorR"  to 0.1f,
                "colorG"  to 0.1f,
                "colorB"  to 0.1f,
                "colorA"  to 1f
        ))

        // 描画オブジェクト(球体)
        drawObjSphere = Sphere01Model()
        drawObjSphere.createPath(mapOf(
                "radius"  to 0.5f,
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

        // 環境色
        vecAmbientColor[0] = 0.05f
        vecAmbientColor[1] = 0.05f
        vecAmbientColor[2] = 0.05f
        vecAmbientColor[3] = 0f

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