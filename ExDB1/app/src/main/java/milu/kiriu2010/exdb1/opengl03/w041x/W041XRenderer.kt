package milu.kiriu2010.exdb1.opengl03.w041x

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.model.Board01Model
import milu.kiriu2010.gui.model.Sphere01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import java.lang.RuntimeException
import java.nio.ByteBuffer
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// フレームバッファを使ってブラーフィルター
class W041XRenderer(ctx: Context): MgRenderer(ctx) {

    // 描画オブジェクト(球体)
    private lateinit var drawObjSphere: Sphere01Model
    // 描画オブジェクト(板)(ブラー)
    private lateinit var drawObjBoard: Board01Model

    // シェーダ(フレームバッファ用)
    private lateinit var shaderA: W041XAShader
    // シェーダ(ブラーフィルタ用)
    private lateinit var shaderB: W041XBShader


    /*
    // プログラムハンドル(フレームバッファ用)
    private var programHandleA: Int = 0
    // プログラムハンドル(ブラーフィルター用)
    private var programHandleB: Int = 0
    */

    // 画面縦横比
    var ratio: Float = 0f

    /*
    // 点光源の位置
    private val vecLight1 = floatArrayOf(-1f,2f,1f)
    private val vecLight2 = floatArrayOf(-1f,0f,0f)
    // カメラの座標
    private val vecEye1 = floatArrayOf(0f,0f,5f)
    private val vecEye2 = floatArrayOf(0f,0f,0.5f)
    */
    private val vecEye2 = floatArrayOf(0f,0f,0.5f)


    // ビットマップ配列
    val bmpArray = arrayListOf<Bitmap>()

    // テクスチャ配列
    val textures = IntArray(2)

    // フレームバッファ
    val bufFrame = IntBuffer.allocate(1)

    // 深度バッファ用レンダ―バッファ
    val bufDepthRender = IntBuffer.allocate(1)

    // フレームバッファ用のテクスチャ
    val frameTexture = IntBuffer.allocate(1)

    // ブラー
    var isBlur = false


    override fun onDrawFrame(gl: GL10?) {
        // 回転角度
        angle[0] =(angle[0]+2)%360
        angle[1] =(angle[1]+1)%360
        val t1 = angle[0].toFloat()
        val t2 = angle[1].toFloat()

        // フレームバッファをバインド
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[0])
        // フレームバッファを初期化
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // プログラムハンドル(フレームバッファ用)を有効化
        //GLES20.glUseProgram(shaderA.programHandle)

        // カメラの位置
        // ビュー座標変換行列
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        // ビュー×プロジェクション
        Matrix.perspectiveM(matP,0,45f,ratio,0.1f,100f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // 背景用球体をフレームバッファにレンダリング
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textures[1])
        Matrix.setIdentityM(matM,0)
        Matrix.scaleM(matM,0,50f,50f,50f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        Matrix.invertM(matI,0,matM,0)
        shaderA.draw(drawObjSphere,matM,matMVP,matI,vecLight,0,1)

        // 地球本体をフレームバッファにレンダリング
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textures[0])
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,t1,0f,1f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        Matrix.invertM(matI,0,matM,0)
        shaderA.draw(drawObjSphere,matM,matMVP,matI,vecLight,1,0)

        // -----------------------------------------------------------------------------------------

        // フレームバッファのバインドを解除
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0)

        // canvasを初期化
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // プログラムハンドル(ブラー用)を有効化
        //GLES20.glUseProgram(shaderB.programHandle)

        // フレームバッファに描きこんだ内容をテクスチャとして適用
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[0])

        // カメラの位置
        // ビュー座標変換行列
        Matrix.setLookAtM(matV, 0,
                vecEye2[0], vecEye2[1], vecEye2[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        // 正射影
        Matrix.orthoM(matP,0,-1f,1f,-1f,1f,0.1f,1f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // 板ポリゴンをレンダリング
        Matrix.setIdentityM(matM,0)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        val u_useBlur = if (isBlur) 1 else 0
        shaderB.draw(drawObjBoard,matMVP,u_useBlur,0,renderW.toFloat())
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        ratio = width.toFloat()/height.toFloat()

        renderW = width
        renderH = height

        Log.d(javaClass.simpleName,"renderW[${renderW}]renderH[${renderH}]")

        // テクスチャをバインド
        activateTextureResize(0,textures,bmpArray[0],renderW)
        activateTextureResize(1,textures,bmpArray[1],renderW)

        createFrameBuffer(renderW,renderH)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // canvasを初期化する色を設定する
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // canvasを初期化する際の深度を設定する
        GLES20.glClearDepthf(1f)

        // 深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)


        // テクスチャ作成し、idをtexturesに保存
        GLES20.glGenTextures(2,textures,0)
        MyGLFunc.checkGlError("glGenTextures")

        // シェーダプログラム登録(フレームバッファ用)
        shaderA = W041XAShader()
        shaderA.loadShader()
        // シェーダプログラム登録(ブラーフィルター用)
        shaderB = W041XBShader()
        shaderB.loadShader()


        // モデル生成(球体)
        drawObjSphere = Sphere01Model()
        drawObjSphere.createPath(mapOf(
                "row" to 32f,
                "column" to 32f,
                "radius" to 1f,
                "colorR" to 1f,
                "colorG" to 1f,
                "colorB" to 1f,
                "colorA" to 1f
        ))

        // モデル生成(ブラー)
        drawObjBoard = Board01Model()
        drawObjBoard.createPath()

        // 光源位置
        vecLight[0] = -1f
        vecLight[1] = 2f
        vecLight[2] = 1f
        // 視点位置
        vecEye[0] = 0f
        vecEye[1] = 0f
        vecEye[2] = 5f

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
        // ビュー・プロジェクション行列
        Matrix.setIdentityM(matVP,0)
    }

    fun activateTexture(id: Int, textures: IntArray, bmp: Bitmap) {
        // テクスチャをバインドする
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[id])
        MyGLFunc.checkGlError("glBindTexture")

        // ビットマップ⇒バッファへ変換
        val buffer = ByteBuffer.allocate(bmp.byteCount)
        bmp.copyPixelsToBuffer(buffer)
        buffer.rewind()

        // テクスチャへイメージを適用
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D,0,GLES20.GL_RGBA,bmp.width,bmp.height,0,
                GLES20.GL_RGBA,GLES20.GL_UNSIGNED_BYTE,buffer)

        /*
        // GLES20.glTexImage2Dを使わないやり方
        // ビットマップをテクスチャに設定
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0)
        MyGLFunc.checkGlError("texImage2D")
        */

        // ミップマップを生成
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)

        // テクスチャパラメータを設定
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT)

        // テクスチャのバインドを無効化
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)

        if ( bmp.isRecycled == false ) {
            bmp.recycle()
        }

        if (textures[id] == 0) {
            throw RuntimeException("Error loading texture[${id}]")
        }
    }

    fun activateTextureResize(id: Int, textures: IntArray, bmp: Bitmap, len: Int) {

        // テクスチャをバインドする
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[id])
        MyGLFunc.checkGlError("glBindTexture")

        val resizedBmp = Bitmap.createScaledBitmap(bmp,len,len,false)

        /*
        val mat = android.graphics.Matrix()
        mat.setScale(len.toFloat()/bmp.width.toFloat(), len.toFloat()/bmp.height.toFloat())
        val resizedBmp = Bitmap.createBitmap(bmp,0,0,bmp.width,bmp.height,mat,true)
        */

        // ビットマップ⇒バッファへ変換
        val buffer = ByteBuffer.allocate(resizedBmp.byteCount)
        resizedBmp.copyPixelsToBuffer(buffer)
        buffer.rewind()

        // テクスチャへイメージを適用
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D,0,GLES20.GL_RGBA,resizedBmp.width,resizedBmp.height,0,
                GLES20.GL_RGBA,GLES20.GL_UNSIGNED_BYTE,buffer)

        /*
        // GLES20.glTexImage2Dを使わないやり方
        // ビットマップをテクスチャに設定
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0)
        MyGLFunc.checkGlError("texImage2D")
        */

        // ミップマップを生成
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)

        // テクスチャパラメータを設定
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT)

        // テクスチャのバインドを無効化
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)

        if ( bmp.isRecycled == false ) {
            bmp.recycle()
        }

        if (textures[id] == 0) {
            throw RuntimeException("Error loading texture[${id}]")
        }
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
