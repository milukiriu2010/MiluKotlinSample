package milu.kiriu2010.exdb1.opengl05.w053v

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.color.MgColor
import milu.kiriu2010.gui.model.Board01Model
import milu.kiriu2010.gui.model.Torus01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs
import milu.kiriu2010.gui.vbo.es20.ES20VBOIpnc
import milu.kiriu2010.gui.vbo.es20.ES20VBOIpt
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -------------------------------------
// シャドウマッピング
// -------------------------------------
// https://wgld.org/d/webgl/w053.html
// -------------------------------------
class WV053Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画オブジェクト(トーラス)
    private lateinit var modelTorus: Torus01Model
    // 描画オブジェクト(板ポリゴン)
    private lateinit var modelBoard: Board01Model

    // VBO(トーラス)
    private lateinit var boTorus: ES20VBOAbs
    // VBO(板ポリゴン)
    private lateinit var boBoard: ES20VBOAbs

    // シェーダ(モデルのレンダリング)
    private lateinit var shaderScreen: WV053ShaderScreen
    // シェーダ(グレースケール変換)
    private lateinit var shaderGray: WV053ShaderGray

    // 画面縦横比
    var ratio: Float = 0f

    // フレームバッファ
    val bufFrame = IntBuffer.allocate(1)

    // 深度バッファ用レンダ―バッファ
    val bufDepthRender = IntBuffer.allocate(1)

    // フレームバッファ用のテクスチャ
    val frameTexture = IntBuffer.allocate(1)

    // グレースケールを使うかどうか
    var u_grayScale = 1

    // 色相用カウンタ
    var cntColor = 0

    override fun onDrawFrame(gl: GL10?) {
        angle[0] =(angle[0]+1)%360
        val t0 = angle[0].toFloat()
        if ( (angle[0]%2) == 0 ) {
            cntColor++
        }

        // フレームバッファのバインド
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[0])

        // フレームバッファを初期化
        var hsv = MgColor.hsva(cntColor%360,1f,1f,1f)
        GLES20.glClearColor(hsv[0], hsv[1], hsv[2], hsv[3])
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // ビュー×プロジェクション座標変換行列
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,20f,0f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,0f,-1f))
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,90f,ratio,0.1f,100f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // -------------------------------------------------------
        // トーラス描画(10個)
        // -------------------------------------------------------
        (0..9).forEach { i ->
            val amb = MgColor.hsva(i*40,1f,1f,1f)
            Matrix.setIdentityM(matM,0)
            Matrix.rotateM(matM,0,i.toFloat()*360f/9f,0f,1f,0f)
            Matrix.translateM(matM,0,0f,0f,10f)
            Matrix.rotateM(matM,0,t0,1f,1f,0f)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            Matrix.invertM(matI,0,matM,0)
            shaderScreen.draw(modelTorus,boTorus,matMVP,matI,vecLight,vecEye,amb.toFloatArray())
        }

        // フレームバッファのバインドを解除
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0)

        // canvasを初期化
        GLES20.glClearColor(0f, 0f, 0f, 1.0f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 正射影用の座標変換行列
        // -------------------------------------------------------
        // 正射影によりテクスチャを画面いっぱいにレンダリングする
        // -------------------------------------------------------
        Matrix.setLookAtM(matV,0,
                0f,0f,0.5f,
                0f,0f,0f,
                0f,1f,0f)
        Matrix.orthoM(matP,0,-1f,1f,-1f,1f,0.1f,1f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // フレームバッファをテクスチャとしてバインド
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[0])

        // フレームバッファの内容をグレー化し描画
        shaderGray.draw(modelBoard,boBoard,matVP, 0,u_grayScale)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        ratio = width.toFloat()/height.toFloat()

        renderW = width
        renderH = height

        // フレームバッファ生成
        GLES20.glGenFramebuffers(1,bufFrame)
        // 深度バッファ用レンダ―バッファ生成
        GLES20.glGenRenderbuffers(1,bufDepthRender)
        // フレームバッファ用テクスチャ生成
        GLES20.glGenTextures(1,frameTexture)
        MyGLES20Func.createFrameBuffer(renderW,renderH,0,bufFrame,bufDepthRender,frameTexture)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // カリングと深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        GLES20.glEnable(GLES20.GL_CULL_FACE)

        // シェーダ(モデルのレンダリング)
        shaderScreen = WV053ShaderScreen()
        shaderScreen.loadShader()

        // シェーダ(グレースケール変換)
        shaderGray =  WV053ShaderGray()
        shaderGray.loadShader()

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

        // VBO(トーラス)
        boTorus = ES20VBOIpnc()
        boTorus.makeVIBO(modelTorus)

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
        boTorus.deleteVIBO()
        boBoard.deleteVIBO()

        GLES20.glDeleteTextures(1,frameTexture)
        GLES20.glDeleteRenderbuffers(1,bufDepthRender)
        GLES20.glDeleteFramebuffers(1,bufFrame)
    }
}