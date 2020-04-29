package milu.kiriu2010.exdb1.opengl06.w059v

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.Matrix
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.color.MgColor
import milu.kiriu2010.gui.model.d2.Board00Model
import milu.kiriu2010.gui.model.Cube01Model
import milu.kiriu2010.gui.model.Torus01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs
import milu.kiriu2010.gui.vbo.es20.ES20VBOIp
import milu.kiriu2010.gui.vbo.es20.ES20VBOIpnct
import milu.kiriu2010.gui.vbo.es20.ES20VBOIpt
import milu.kiriu2010.math.MyMathUtil
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -----------------------------------------------------------------------
// emuglGLESv2_enc: a vertex attribute index out of boundary is detected. Skipping corresponding vertex attribute. buf=0xe7b8f050
// -----------------------------------------------------------------------
// 被写界深度
// -----------------------------------------------------------------------
//   ピントが合っていない部分がぼやけて写るようにすること
//   被写界深度ではピントを合わせたい深度を決め、
//   その深度に応じて、ぼけていないシーンとぼけたシーンとを合成する
// -----------------------------------------------------------------------
// https://wgld.org/d/webgl/w059.html
// -----------------------------------------------------------------------
class WV059Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画オブジェクト(トーラス)
    private lateinit var modelTorus: Torus01Model
    // 描画オブジェクト(板ポリゴン)
    private lateinit var modelBoard: Board00Model
    // 描画オブジェクト(立方体)
    private lateinit var modelCube: Cube01Model

    // VBO(トーラス＋メイン)
    private lateinit var boTorusMain: ES20VBOAbs
    // VBO(立方体＋メイン)
    private lateinit var boCubeMain: ES20VBOAbs
    // VBO(トーラス＋深度)
    private lateinit var boTorusDepth: ES20VBOAbs
    // VBO(立方体＋深度)
    private lateinit var boCubeDepth: ES20VBOAbs
    // VBO(板ポリゴン)
    private lateinit var boBoard: ES20VBOAbs

    // シェーダ(メイン)
    private lateinit var mainShader: WV059ShaderMain
    // シェーダ(深度をレンダリング)
    private lateinit var depthShader: WV059ShaderDepth
    // シェーダ(正射影で板ポリゴンをgaussianフィルタでレンダリング)
    private lateinit var gaussianShader: WV059ShaderGaussian
    // シェーダ(正射影でレンダリング結果を合成する)
    private lateinit var finalShader: WV059ShaderFinal

    // 画面縦横比
    var ratio: Float = 0f

    // ビットマップ配列
    val bmpArray = arrayListOf<Bitmap>()

    // テクスチャ配列
    val textures = IntArray(1)

    // フレームバッファ
    // 0:depth:深度マップレンダリング用のバッファ
    // 1:scene:ぼやけてないシーン
    // 2:blur1:小さくぼやけたシーン
    // 3:blur2:大きくぼやけたシーン
    // 4:ぼやけたシーンの一時バッファ
    val bufFrame = IntBuffer.allocate(5)

    // 深度バッファ用レンダ―バッファ
    val bufDepthRender = IntBuffer.allocate(5)

    // フレームバッファ用のテクスチャ
    // 1:scene:ぼやけてないシーン
    val frameTexture = IntBuffer.allocate(5)

    // u_gaussianフィルタの重み係数
    val u_weight1 = MyMathUtil.gaussianWeigt(10,15f)
    val u_weight2 = MyMathUtil.gaussianWeigt(10,45f)

    // フォーカスする深度値
    // -0.425～0.425
    var u_depthOffset = 0f

    // 選択値
    var u_result = 0

    // プロジェクションxビュー(正射影用の座標変換行列)
    private val matVP4O = FloatArray(16)

    init {
        // ビットマップをロード
        bmpArray.clear()
        val bmp0 = BitmapFactory.decodeResource(ctx.resources, R.drawable.texture_w59)
        bmpArray.add(bmp0)
    }

    override fun onDrawFrame(gl: GL10?) {
        angle[0] =(angle[0]+1)%360

        // 各トーラスの傾き
        val angleF = FloatArray(10)
        (0..9).forEach { i ->
            angleF[i] = ((angle[0]+40*i)%360).toFloat()
        }

        // ビュー×プロジェクション座標変換行列(パースあり)
        vecEye = qtnNow.toVecIII(floatArrayOf(2f,0f,10f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
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
        mainShader.draw(modelCube,boCubeMain,matMVP,matI,vecLight,
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
            mainShader.draw(modelTorus,boTorusMain,matMVP,matI,vecLight,vecEye,amb.toFloatArray(),0)
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
        gaussianShader.draw(modelBoard,boBoard,matVP4O,0,1,u_weight1,1,renderW.toFloat())

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
        gaussianShader.draw(modelBoard,boBoard,matVP4O,0,1,u_weight1,0,renderW.toFloat())

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
        gaussianShader.draw(modelBoard,boBoard,matVP4O,0,1,u_weight2,1,renderW.toFloat())

        // -----------------------------------------------
        // 【4:大きくぼやけたシーンをバッファに描く】
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
        gaussianShader.draw(modelBoard,boBoard,matVP4O,0,1,u_weight2,0,renderW.toFloat())

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
        depthShader.draw(modelCube,boCubeDepth,matMVP,u_depthOffset)

        // -------------------------------------------------------
        // 深度をレンダリング(トーラス)(10個)
        // -------------------------------------------------------
        GLES20.glCullFace(GLES20.GL_BACK)
        (0..9).forEach { i ->
            //val amb = MgColor.hsva(i*40,1f,1f,1f)
            Matrix.setIdentityM(matM,0)
            Matrix.translateM(matM,0,0.2f*i.toFloat(),0f,8.8f-2.2f*i.toFloat())
            Matrix.rotateM(matM,0,angleF[i],1f,1f,0f)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            depthShader.draw(modelTorus,boTorusDepth,matMVP,u_depthOffset)
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
        finalShader.draw(modelBoard,boBoard,matVP4O,0,1,2,3,u_result)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        ratio = width.toFloat()/height.toFloat()

        renderW = width
        renderH = height

        // テクスチャを作成
        GLES20.glGenTextures(1,textures,0)
        // テクスチャに使う画像をロード
        MyGLES20Func.createTexture(0,textures,bmpArray[0])

        // フレームバッファ生成
        GLES20.glGenFramebuffers(5,bufFrame)
        // レンダ―バッファ生成
        GLES20.glGenRenderbuffers(5,bufDepthRender)
        // フレームバッファを格納するテクスチャ生成
        GLES20.glGenTextures(5,frameTexture)
        (0..4).forEach {
            MyGLES20Func.createFrameBuffer(renderW,renderH,it,bufFrame,bufDepthRender,frameTexture)
        }
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // カリングと深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        GLES20.glEnable(GLES20.GL_CULL_FACE)

        // シェーダ(メイン)
        mainShader = WV059ShaderMain()
        mainShader.loadShader()

        // シェーダ(深度をレンダリング)
        depthShader = WV059ShaderDepth()
        depthShader.loadShader()

        // シェーダ(正射影で板ポリゴンをgaussianフィルタでレンダリング)
        gaussianShader = WV059ShaderGaussian()
        gaussianShader.loadShader()

        // シェーダ(正射影でレンダリング結果を合成する)
        finalShader = WV059ShaderFinal()
        finalShader.loadShader()

        // モデル生成(トーラス)
        modelTorus = Torus01Model()
        modelTorus.createPath(mapOf(
                "row"     to 32f,
                "column"  to 32f,
                "iradius" to 0.3f,
                "oradius" to 0.7f,
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

        // VBO(トーラス＋メイン)
        boTorusMain = ES20VBOIpnct()
        boTorusMain.makeVIBO(modelTorus)

        // VBO(立方体＋メイン)
        boCubeMain = ES20VBOIpnct()
        boCubeMain.makeVIBO(modelCube)

        // VBO(トーラス＋深度)
        boTorusDepth = ES20VBOIp()
        boTorusDepth.makeVIBO(modelTorus)

        // VBO(立方体＋深度)
        boCubeDepth = ES20VBOIp()
        boCubeDepth.makeVIBO(modelCube)

        // VBO(板ポリゴン)
        boBoard = ES20VBOIpt()
        boBoard.makeVIBO(modelBoard)

        // 光源位置
        vecLight[0] = -0.577f
        vecLight[1] =  0.577f
        vecLight[2] =  0.577f
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
        boTorusMain.deleteVIBO()
        boCubeMain.deleteVIBO()
        boTorusDepth.deleteVIBO()
        boCubeDepth.deleteVIBO()
        boBoard.deleteVIBO()

        mainShader.deleteShader()
        depthShader.deleteShader()
        gaussianShader.deleteShader()
        finalShader.deleteShader()

        GLES20.glDeleteTextures(textures.size,textures,0)
        GLES20.glDeleteTextures(1,frameTexture)
        GLES20.glDeleteRenderbuffers(1,bufDepthRender)
        GLES20.glDeleteFramebuffers(1,bufFrame)
    }
}