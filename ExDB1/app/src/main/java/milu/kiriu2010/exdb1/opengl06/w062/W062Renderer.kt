package milu.kiriu2010.exdb1.opengl06.w062

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
import milu.kiriu2010.math.MyMathUtil
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// ステンシル鏡面反射
class W062Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画オブジェクト(トーラス)
    private lateinit var drawObjTorus: Torus01Model
    // 描画オブジェクト(球体)
    private lateinit var drawObjSphere: Sphere01Model
    // 描画オブジェクト(板ポリゴン)
    private lateinit var drawObjBoard: Board01Model

    // シェーダ(メイン)
    private lateinit var mainShader: W062ShaderMain
    // シェーダ(ステンシル鏡面反射)
    private lateinit var mirrorShader: W062ShaderMirror

    // 画面縦横比
    var ratio: Float = 1f

    // フレームバッファ
    val bufFrame = IntBuffer.allocate(1)

    // 深度バッファ用レンダ―バッファ
    val bufDepthRender = IntBuffer.allocate(1)

    // フレームバッファ用のテクスチャ
    val frameTexture = IntBuffer.allocate(1)

    // 映り込み係数
    //   0.0 - 1.0
    var u_alpha = 0.5f

    // プロジェクションxビュー(正射影用の座標変換行列)
    private val matVP4O = FloatArray(16)

    var upDown = 0f

    override fun onDrawFrame(gl: GL10?) {
        angle[0] =(angle[0]+1)%360
        val t0 = angle[0].toFloat()
        upDown = MyMathUtil.sinf(angle[0].toFloat()) * 0.25f

        // ビュー×プロジェクション座標変換行列
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,5f,5f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,-1f))
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,ratio,0.1f,10f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // 正射影用の座標変換行列
        Matrix.setLookAtM(matV,0,
                0f,0f,0.5f,
                0f,0f,0f,
                0f,1f,0f)
        Matrix.orthoM(matP,0,-1f,1f,-1f,1f,0.1f,1f)
        Matrix.multiplyMM(matVP4O,0,matP,0,matV,0)

        // --------------------------------------------------------------------------------
        // 【0:映りこむ世界、すなわち上下が反転している鏡の向こう側の世界をレンダリング】
        // --------------------------------------------------------------------------------

        // フレームバッファのバインド
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[0])

        // フレームバッファを初期化
        GLES20.glClearColor(0.3f, 0.9f, 0.9f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // ステンシルテストの無効化
        GLES20.glDisable(GLES20.GL_STENCIL_TEST)

        // カリング面の反転
        // 鏡面世界を映しこむため反転させている
        GLES20.glCullFace(GLES20.GL_FRONT)

        // レンダリング(トーラス)
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,t0,0f,1f,0f)
        Matrix.translateM(matM,0,0f,0.75f+upDown,0f)
        Matrix.rotateM(matM,0,90f,1f,0f,0f)
        Matrix.invertM(matI,0,matM,0)
        mainShader.draw(drawObjTorus,matM,matVP,matI,
                vecLight,vecEye, floatArrayOf(0f,0f,0f,0f), 1)

        // レンダリング(球体)
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,-t0,0f,1f,0f)
        Matrix.translateM(matM,0,0f,0.75f,1f)
        Matrix.invertM(matI,0,matM,0)
        mainShader.draw(drawObjSphere,matM,matVP,matI,
                vecLight,vecEye, floatArrayOf(0f,0f,0f,0f), 1)

        // --------------------------------------------------------------------------
        // 【1:通常のワールド空間にモデルを描画】
        // --------------------------------------------------------------------------

        // フレームバッファのバインドを解除
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0)

        // canvasを初期化
        GLES20.glClearColor(0f, 0.7f, 0.7f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClearStencil(0)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_STENCIL_BUFFER_BIT)

        // ステンシル設定
        // ステンシルテストに全て合格するように設定する
        // maskは~0(0の補数)なので"0.inv()"と思ったが"0xff"がいいっぽい
        // https://juejin.im/post/5acf2e85518825558a070164
        GLES20.glEnable(GLES20.GL_STENCIL_TEST)
        GLES20.glStencilFunc(GLES20.GL_ALWAYS,0,0xff)
        GLES20.glStencilOp(GLES20.GL_KEEP,GLES20.GL_KEEP,GLES20.GL_KEEP)

        // カリング面を元に戻す
        GLES20.glCullFace(GLES20.GL_BACK)

        // レンダリング(トーラス)
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,t0,0f,1f,0f)
        Matrix.translateM(matM,0,0f,0.75f+upDown,0f)
        Matrix.rotateM(matM,0,90f,1f,0f,0f)
        Matrix.invertM(matI,0,matM,0)
        mainShader.draw(drawObjTorus,matM,matVP,matI,
                vecLight,vecEye, floatArrayOf(0f,0f,0f,0f), 0)

        // レンダリング(球体)
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,-t0,0f,1f,0f)
        Matrix.translateM(matM,0,0f,0.75f,1f)
        Matrix.invertM(matI,0,matM,0)
        mainShader.draw(drawObjSphere,matM,matVP,matI,
                vecLight,vecEye, floatArrayOf(0f,0f,0f,0f), 0)

        // ステンシル設定
        // ステンシル値をインクリメント
        GLES20.glEnable(GLES20.GL_STENCIL_TEST)
        GLES20.glStencilFunc(GLES20.GL_ALWAYS,1,0xff)
        GLES20.glStencilOp(GLES20.GL_KEEP,GLES20.GL_KEEP,GLES20.GL_REPLACE)

        // レンダリング(板ポリゴン)
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,270f,1f,0f,0f)
        Matrix.scaleM(matM,0,2f,2f,1f)
        Matrix.invertM(matI,0,matM,0)
        mainShader.draw(drawObjBoard,matM,matVP,matI,
                vecLight,vecEye, floatArrayOf(0f,0f,0f,0f), 0)

        // ステンシル設定
        GLES20.glEnable(GLES20.GL_STENCIL_TEST)
        GLES20.glStencilFunc(GLES20.GL_EQUAL,1,0xff)
        GLES20.glStencilOp(GLES20.GL_KEEP,GLES20.GL_KEEP,GLES20.GL_KEEP)

        // テクスチャの設定
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[0])

        // レンダリング(板ポリゴン)
        mirrorShader.draw(drawObjBoard,matVP4O,0,u_alpha)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        ratio = width.toFloat()/height.toFloat()

        renderW = width
        renderH = height

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

        // カリングと深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        GLES20.glEnable(GLES20.GL_CULL_FACE)
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFuncSeparate(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA, GLES20.GL_ONE, GLES20.GL_ONE)
        GLES20.glBlendEquationSeparate(GLES20.GL_FUNC_ADD,GLES20.GL_FUNC_ADD)

        // シェーダ(メイン)
        mainShader = W062ShaderMain()
        mainShader.loadShader()

        // シェーダ(ステンシル鏡面反射)
        mirrorShader = W062ShaderMirror()
        mirrorShader.loadShader()

        // モデル生成(トーラス)
        drawObjTorus = Torus01Model()
        drawObjTorus.createPath(mapOf(
                "row"     to 32f,
                "column"  to 32f,
                "iradius" to 0.1f,
                "oradius" to 0.4f
        ))

        // モデル生成(球体)
        drawObjSphere = Sphere01Model()
        drawObjSphere.createPath(mapOf(
                "row" to 32f,
                "column" to 32f,
                "radius" to 0.25f
        ))

        // モデル生成(板ポリゴン)
        drawObjBoard = Board01Model()
        drawObjBoard.createPath(mapOf(
                "pattern" to 62f,
                "colorR" to 0.5f,
                "colorG" to 0.5f,
                "colorB" to 0.5f,
                "colorA" to 1f
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