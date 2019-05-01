package milu.kiriu2010.exdb1.opengl05.w052

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import android.view.MotionEvent
import milu.kiriu2010.exdb1.opengl05.w051.W051ShaderDepth
import milu.kiriu2010.exdb1.opengl05.w051.W051ShaderScreen
import milu.kiriu2010.gui.basic.MyQuaternion
import milu.kiriu2010.gui.model.Board01Model
import milu.kiriu2010.gui.model.Torus01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.floor
import kotlin.math.sqrt

// 高解像度シャドウマッピング
class W052Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画オブジェクト(トーラス)
    private lateinit var drawObjTorus: Torus01Model
    // 描画オブジェクト(板ポリゴン)
    private lateinit var drawObjBoard: Board01Model

    // シェーダ(深度値格納用)
    private lateinit var shaderDepth: W051ShaderDepth
    // シェーダ(スクリーンレンダリング用)
    private lateinit var shaderScreen: W051ShaderScreen

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
    //   k:30-60
    var k = 45f

    // 深度値を使うかどうか
    var u_depthBuffer = 1
    override fun onDrawFrame(gl: GL10?) {
        // 回転角度
        angle[0] =(angle[0]+1)%360
        val t1 = angle[0].toFloat()

        // ビュー×プロジェクション座標変換行列
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,70f,0f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,0f,-1f))
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,ratio,0.1f,150f)
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

        // ライトの距離を係数で調整
        //   k=30-60
        vecLight[0] = 0f * k
        vecLight[1] = 1f * k
        vecLight[2] = 0f * k

        // ライトから見たビュー座標変換行列
        Matrix.setLookAtM(matV4L, 0,
                vecLight[0], vecLight[1], vecLight[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecLightUp[0], vecLightUp[1], vecLightUp[2])
        // ライトから見たプロジェクション座標変換行列
        Matrix.perspectiveM(matP4L,0,90f,ratio,0.1f,150f)

        // テクスチャ座標変換行列
        Matrix.multiplyMM(matVP4L,0,matTex,0,matP4L,0)
        Matrix.multiplyMM(matTex,0,matVP4L,0,matV4L,0)

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
        GLES20.glClearColor(0.0f, 1f, 1f, 1.0f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // ビューポートをフレームバッファのサイズに調整する
        GLES20.glViewport(0,0,2048,2048)

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
            shaderDepth.draw(drawObjTorus,matMVP4L,u_depthBuffer)
        }

        // 板ポリゴンの描画(底面)
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,0f,-10f,0f)
        Matrix.scaleM(matM,0,30f,0f,30f)
        Matrix.multiplyMM(matMVP4L,0,matVP4L,0,matM,0)
        shaderDepth.draw(drawObjBoard,matMVP4L,0)

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

        // ビューポートを元のcanvasのサイズに戻る
        GLES20.glViewport(0,0,renderW,renderH)

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
            shaderScreen.draw(drawObjTorus,matM,matMVP,matI,matTex,matMVP4L,vecLight,0,u_depthBuffer)
        }

        // 板ポリゴンの描画(底面)
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,0f,-10f,0f)
        Matrix.scaleM(matM,0,30f,0f,30f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        Matrix.invertM(matI,0,matM,0)
        Matrix.multiplyMM(matMVP4L,0,matVP4L,0,matM,0)
        shaderScreen.draw(drawObjBoard,matM,matMVP,matI,matTex,matMVP4L,vecLight,0,0)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        ratio = width.toFloat()/height.toFloat()

        renderW = width
        renderH = height

        //createFrameBuffer(renderW,renderH)
        createFrameBuffer(2048,2048)
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

        // シェーダ(深度値格納用)
        shaderDepth = W051ShaderDepth()
        shaderDepth.loadShader()
        // シェーダ(スクリーンレンダリング用)
        shaderScreen = W051ShaderScreen()
        shaderScreen.loadShader()

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
        /*
        drawObjBoard.createPath(mapOf(
                "pattern" to 51f,
                "colorR"  to 0.5f,
                "colorG"  to 0.5f,
                "colorB"  to 0.5f,
                "colorA"  to 1f
        ))
        */
        drawObjBoard.createPath(mapOf(
                "pattern" to 51f,
                "colorR"  to 1f, // 0.5f
                "colorG"  to 1f, // 0.5f
                "colorB"  to 1f, // 0.5f
                "colorA"  to 1f
        ))

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

        // モデル変換行列(テクスチャ用)
        Matrix.setIdentityM(matTex,0)
        // ライトの座標変換行列
        Matrix.setIdentityM(matMVP4L,0)
        // ライトから見たビュー座標変換行列
        Matrix.setIdentityM(matV4L,0)
        // ライトから見たプロジェクション座標変換行列
        Matrix.setIdentityM(matP4L,0)
        // ライトから見たビュー×プロジェクション座標変換行列
        Matrix.setIdentityM(matVP4L,0)
    }


    // フレームバッファをオブジェクトとして生成する
    private fun createFrameBuffer(width: Int, height: Int) {
        // フレームバッファ生成
        GLES20.glGenFramebuffers(1,bufFrame)
        // フレームバッファのバインド
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[0])

        // 深度バッファ用レンダ―バッファ生成
        GLES20.glGenRenderbuffers(1,bufDepthRender)
        // 深度バッファ用レンダ―バッファのバインド
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER,bufDepthRender[0])

        // レンダ―バッファを深度バッファとして設定
        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, width, height)
        // フレームバッファにレンダ―バッファを関連付ける
        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER,bufDepthRender[0])

        // フレームバッファ用テクスチャ生成
        GLES20.glGenTextures(1,frameTexture)
        // フレームバッファ用のテクスチャをバインド
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[0])

        // フレームバッファ用のテクスチャにカラー用のメモリ領域を確保
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D,0,GLES20.GL_RGBA,width,height,0,GLES20.GL_RGBA,GLES20.GL_UNSIGNED_BYTE,null)

        // テクスチャパラメータ
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE)

        // フレームバッファにテクスチャを関連付ける
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,GLES20.GL_TEXTURE_2D,frameTexture[0],0)

        // 各種オブジェクトのバインドを解除
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0)
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER,0)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0)

        val status = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER)
        Log.d(javaClass.simpleName,"status[${status}]COMPLETE[${GLES20.GL_FRAMEBUFFER_COMPLETE}]")
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
    }
}