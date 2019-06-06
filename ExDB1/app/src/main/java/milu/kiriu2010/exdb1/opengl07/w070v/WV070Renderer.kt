package milu.kiriu2010.exdb1.opengl07.w070v

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log
import milu.kiriu2010.exdb1.opengl07.w069.W069ShaderDepth
import milu.kiriu2010.exdb1.opengl07.w069.W069ShaderScreen
import milu.kiriu2010.exdb1.opengl07.w069v.WV069ShaderDepth
import milu.kiriu2010.exdb1.opengl07.w069v.WV069ShaderScreen
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.Board01Model
import milu.kiriu2010.gui.model.Torus01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs
import milu.kiriu2010.gui.vbo.es20.ES20VBOIpnc
import milu.kiriu2010.gui.vbo.es20.ES20VBOIpt
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.floor

// -------------------------------------------
// a vertex attribute index out of boundary is detected. Skipping corresponding vertex attribute. buf=0xe7b8ec30
// emuglGLESv2_enc: Out of bounds vertex attribute info: clientArray? 1 attribute 2 vbo 13 allocedBufferSize 64 bufferDataSpecified? 1 wantedStart 0 wantedEnd 17424
// -------------------------------------------
// 正しい深度値を適用したシャドウマッピング
// -------------------------------------------
// w69に対して
// 浮動小数点数テクスチャに対応
// -------------------------------------------
// https://wgld.org/d/webgl/w070.html
// -------------------------------------------
class WV070Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画オブジェクト(トーラス)
    private lateinit var modelTorus: Torus01Model
    // 描画オブジェクト(板ポリゴン)
    private lateinit var modelBoard: Board01Model

    // VBO(トーラス＋深度)
    private lateinit var boTorusDepth: ES20VBOAbs
    // VBO(板ポリゴン＋深度)
    private lateinit var boBoardDepth: ES20VBOAbs
    // VBO(トーラス＋スクリーン)
    private lateinit var boTorusScreen: ES20VBOAbs
    // VBO(板ポリゴン＋スクリーン)
    private lateinit var boBoardScreen: ES20VBOAbs

    // シェーダ(深度値格納用)
    private lateinit var shaderDepth: WV069ShaderDepth
    // シェーダ(スクリーンレンダリング用)
    private lateinit var shaderScreen: WV069ShaderScreen

    // ライトビューの上方向
    val vecLightUp = floatArrayOf(0f,0f,-1f)

    // 画面縦横比
    var ratio: Float = 0f

    // ビットマップ配列
    val bmpArray = arrayListOf<Bitmap>()

    // テクスチャ配列
    val textures = IntArray(2)

    // テクスチャ座標変換行列
    private val matTex = FloatArray(16)
    // ビュー×プロジェクション×テクスチャ座標変換行列
    private val matVPT = FloatArray(16)
    // ライトから見たモデル×ビュー×プロジェクション座標変換行列
    private val matMVP4L = FloatArray(16)
    // ライトから見たビュー座標変換行列
    private val matV4L = FloatArray(16)
    // ライトから見たプロジェクション座標変換行列
    private val matP4L = FloatArray(16)
    // ライトから見たビュー×プロジェクション座標変換行列
    private val matVP4L = FloatArray(16)

    // フレームバッファ
    val bufFrame = IntBuffer.allocate(1)

    // 深度バッファ用レンダ―バッファ
    val bufDepthRender = IntBuffer.allocate(1)

    // フレームバッファ用のテクスチャ
    val frameTexture = IntBuffer.allocate(1)

    // ライトの位置補正用係数
    //   k:20-40
    var k = 30f

    // 深度値を使うかどうか
    var u_depthBuffer = 1

    init {
        // -------------------------------------------------------
        // テクスチャ変換用行列
        // -------------------------------------------------------
        // matTex[5]は
        // 画像から読み込んだ場合は、-0.5fだが、
        // フレームバッファに描いた風景は、初めから上下が判定しているので0.5f
        // -------------------------------------------------------
        matTex[0]  = 0.5f;  matTex[1]  =   0f;  matTex[2]  = 0f;  matTex[3]  = 0f;
        matTex[4]  =   0f;  matTex[5]  = 0.5f;  matTex[6]  = 0f;  matTex[7]  = 0f;
        matTex[8]  =   0f;  matTex[9]  =   0f;  matTex[10] = 1f;  matTex[11] = 0f;
        matTex[12] = 0.5f;  matTex[13] = 0.5f;  matTex[14] = 0f;  matTex[15] = 1f;
    }

    override fun onDrawFrame(gl: GL10?) {
        // 回転角度
        angle[0] =(angle[0]+1)%360

        // ビュー×プロジェクション座標変換行列
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,70f,0f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,0f,-1f))
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,ratio,0.1f,150f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // ライトの距離を係数で調整
        //   k=20-40
        vecLight[0] = 0f * k
        vecLight[1] = 1f * k
        vecLight[2] = 0f * k

        // ライトから見たビュー座標変換行列
        Matrix.setLookAtM(matV4L, 0,
                vecLight[0], vecLight[1], vecLight[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecLightUp[0], vecLightUp[1], vecLightUp[2])
        // ライトから見たプロジェクション座標変換行列
        Matrix.perspectiveM(matP4L,0,90f,ratio,15f,100f)

        // ライトから見た座標変換行列を掛け合わせ
        // ビュー×プロジェクション×テクスチャ座標変換行列を求める
        val matPT = FloatArray(16)
        Matrix.multiplyMM(matPT,0,matTex,0,matP4L,0)
        Matrix.multiplyMM(matVPT,0,matPT,0,matV4L,0)

        // ライトから見たビュー×プロジェクション座標変換行列
        Matrix.multiplyMM(matVP4L,0,matP4L,0,matV4L,0)

        // ------------------------------------------------------
        // フレームバッファにはライトから見た時の深度値のみを描く
        // デプスバッファの範囲は0.0～1.0
        //   カメラに最も近いところ⇒0.0
        //   　　　　最も遠いところ⇒1.0
        // ------------------------------------------------------
        // フレームバッファをバインド
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[0])

        // フレームバッファを初期化
        GLES20.glClearColor(1f, 1f, 1f, 1.0f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // -------------------------------------------------------
        // シャドウマップ
        // -------------------------------------------------------
        // トーラス描画(10個)
        // 上段・下段５個ずつ、五角形の配置
        // -------------------------------------------------------
        (0..9).forEach { i ->
            // 回転角度
            val angleT1 =(angle[0]+i*36)%360
            val angleT2 =((i%5)*72)%360
            val t1 = angleT1.toFloat()
            val t2 = angleT2.toFloat()
            val ifl = -floor(i.toFloat()/5f) +1f
            Matrix.setIdentityM(matM,0)
            Matrix.rotateM(matM,0,t2,0f,1f,0f)
            Matrix.translateM(matM,0,0f,ifl*10f+10f,(ifl-2f)*7f)
            Matrix.rotateM(matM,0,t1,1f,1f,0f)
            Matrix.multiplyMM(matMVP4L,0,matVP4L,0,matM,0)
            shaderDepth.draw(modelTorus,boTorusDepth,matMVP4L)
        }

        // 板ポリゴンの描画(底面)
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,0f,-10f,0f)
        Matrix.scaleM(matM,0,30f,0f,30f)
        Matrix.multiplyMM(matMVP4L,0,matVP4L,0,matM,0)
        shaderDepth.draw(modelBoard,boBoardDepth,matMVP4L)

        // -----------------------------------------------
        // スクリーンレンダリング
        // -----------------------------------------------

        // フレームバッファのバインドを解除
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0)

        // フレームバッファをテクスチャとしてバインド
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[0])

        // canvasを初期化
        GLES20.glClearColor(0.0f, 0.7f, 0.7f, 1.0f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // -------------------------------------------------------
        // トーラス描画(10個)
        // -------------------------------------------------------
        (0..9).forEach { i ->
            // 回転角度
            val angleT1 =(angle[0]+i*36)%360
            val angleT2 =((i%5)*72)%360
            val t1 = angleT1.toFloat()
            val t2 = angleT2.toFloat()
            val ifl = -floor(i.toFloat()/5f) +1f
            Matrix.setIdentityM(matM,0)
            Matrix.rotateM(matM,0,t2,0f,1f,0f)
            Matrix.translateM(matM,0,0f,ifl*10f+10f,(ifl-2f)*7f)
            Matrix.rotateM(matM,0,t1,1f,1f,0f)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            Matrix.invertM(matI,0,matM,0)
            Matrix.multiplyMM(matMVP4L,0,matVP4L,0,matM,0)
            shaderScreen.draw(modelTorus,boTorusScreen,matM,matMVP,matI,matVPT,matMVP4L,vecLight,0)
        }

        // 板ポリゴンの描画(底面)
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,0f,-10f,0f)
        Matrix.scaleM(matM,0,30f,0f,30f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        Matrix.invertM(matI,0,matM,0)
        Matrix.multiplyMM(matMVP4L,0,matVP4L,0,matM,0)
        shaderScreen.draw(modelBoard,boBoardScreen,matM,matMVP,matI,matVPT,matMVP4L,vecLight,0)
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
        MyGLES20Func.createFrameBuffer(renderW,renderH,0,bufFrame,bufDepthRender,frameTexture,GLES20.GL_FLOAT)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // OpenGL Extension
        Log.d(javaClass.simpleName,"OpenGL Extensions:${GLES20.glGetString(GLES20.GL_EXTENSIONS)}")

        // カリングと深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        GLES20.glEnable(GLES20.GL_CULL_FACE)

        // シェーダ(深度値格納用)
        shaderDepth = WV069ShaderDepth()
        shaderDepth.loadShader()
        // シェーダ(スクリーンレンダリング用)
        shaderScreen = WV069ShaderScreen()
        shaderScreen.loadShader()

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
                "pattern" to 51f,
                "colorR"  to 1f,
                "colorG"  to 1f,
                "colorB"  to 1f,
                "colorA"  to 1f
        ))

        // VBO(トーラス＋深度)
        boTorusDepth = ES20VBOIpt()
        boTorusDepth.makeVIBO(modelTorus)

        // VBO(板ポリゴン＋深度)
        boBoardDepth = ES20VBOIpt()
        boBoardDepth.makeVIBO(modelBoard)

        // VBO(トーラス＋スクリーン)
        boTorusScreen = ES20VBOIpnc()
        boTorusScreen.makeVIBO(modelTorus)

        // VBO(板ポリゴン＋スクリーン)
        boBoardScreen = ES20VBOIpnc()
        boBoardScreen.makeVIBO(modelBoard)

        // 光源位置
        vecLight[0] = 0f
        vecLight[1] = 1f
        vecLight[2] = 0f
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
        boTorusDepth.deleteVIBO()
        boBoardDepth.deleteVIBO()
        boTorusScreen.deleteVIBO()
        boBoardScreen.deleteVIBO()
        shaderDepth.deleteShader()
        shaderScreen.deleteShader()

        GLES20.glDeleteTextures(textures.size,textures,0)
        GLES20.glDeleteTextures(1,frameTexture)
        GLES20.glDeleteRenderbuffers(1,bufDepthRender)
        GLES20.glDeleteFramebuffers(1,bufFrame)
    }
}