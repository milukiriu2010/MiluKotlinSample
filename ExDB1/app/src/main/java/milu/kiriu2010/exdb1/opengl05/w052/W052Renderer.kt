package milu.kiriu2010.exdb1.opengl05.w052

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import android.view.MotionEvent
import milu.kiriu2010.gui.basic.MyQuaternion
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.floor
import kotlin.math.sqrt

// シャドウマッピング
class W052Renderer: GLSurfaceView.Renderer {
    // 描画オブジェクト(トーラス:デプス)
    private lateinit var drawObjTorusDepth: W052ModelTorus
    // 描画オブジェクト(トーラス:スクリーン)
    private lateinit var drawObjTorusScreen: W052ModelTorus
    // 描画オブジェクト(板ポリゴン:デプス)
    private lateinit var drawObjBoardDepth: W052ModelBoard
    // 描画オブジェクト(板ポリゴン:スクリーン)
    private lateinit var drawObjBoardScreen: W052ModelBoard

    // プログラムハンドル(深度値格納用)
    private var programHandleDepth: Int = 0
    // プログラムハンドル(スクリーンレンダリング用)
    private var programHandleScreen: Int = 0

    // 画面縦横比
    var ratio: Float = 0f

    // モデル変換行列
    private val matM = FloatArray(16)
    // モデル変換行列の逆行列
    private val matI = FloatArray(16)
    // ビュー変換行列
    private val matV = FloatArray(16)
    // プロジェクション変換行列
    private val matP = FloatArray(16)
    // モデル・ビュー・プロジェクション行列
    private val matMVP = FloatArray(16)
    // テンポラリ行列
    private val matT = FloatArray(16)
    // ライトの位置
    private val vecLight1 = floatArrayOf(0f,1f,0f)
    private val vecLight2 = floatArrayOf(-1f,0f,0f)
    // ライトビューの上方向を表すベクトル
    private var vecLight1Up = floatArrayOf(0f,0f,-1f)
    // 環境光の色
    private val vecAmbientColor = floatArrayOf(0.1f,0.1f,0.1f,1f)
    // カメラの座標
    private var vecEye = floatArrayOf(-10f,10f,10f)
    // カメラの上方向を表すベクトル
    private var vecEyeUp = floatArrayOf(0.577f,0.577f,-0.577f)
    // 原点のベクトル
    private val vecCenter = floatArrayOf(0f,0f,0f)

    // 回転スイッチ
    var rotateSwitch = false

    // 回転角度
    private var angle1 = 0
    private var angle2 = 0

    // クォータニオン
    var xQuaternion = MyQuaternion().identity()

    // ビットマップ配列
    val bmpArray = arrayListOf<Bitmap>()

    // テクスチャ配列
    val textures = IntArray(2)

    // レンダリングする幅・高さ
    var renderW = 0
    var renderH = 0



    // モデル変換行列(テクスチャ用)
    private val matM4Tex = FloatArray(16)
    // ビュー変換行列(テクスチャ用)
    //private val matV4Tex = FloatArray(16)
    // プロジェクション変換行列(テクスチャ用)
    //private val matP4Tex = FloatArray(16)
    // モデル×ビュー×プロジェクション行列(テクスチャ用)
    //private val matMVP4Tex = FloatArray(16)

    // ライトの座標変換行列
    private val matLight = FloatArray(16)
    // ライトから見たビュー座標変換行列
    private val matDV = FloatArray(16)
    // ライトから見たプロジェクション座標変換行列
    private val matDP = FloatArray(16)
    // ライトから見たビュー×プロジェクション座標変換行列
    private val matDVP = FloatArray(16)

    // フレームバッファ
    val bufFrame = IntBuffer.allocate(1)

    // 深度バッファ用レンダ―バッファ
    val bufDepthRender = IntBuffer.allocate(1)

    // フレームバッファ用のテクスチャ
    val frameTexture = IntBuffer.allocate(1)

    // ライトの位置補正用係数
    var k = 45f

    // 深度値を使うかどうか
    var u_depthBuffer = 1

    override fun onDrawFrame(gl: GL10?) {
        angle1 =(angle1+2)%360

        // ビュー×プロジェクション座標変換行列
        vecEye = xQuaternion.toVecIII(floatArrayOf(0f,0f,70f))
        vecEyeUp = xQuaternion.toVecIII(floatArrayOf(0f,0f,-1f))
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,ratio,0.1f,150f)
        Matrix.multiplyMM(matT,0,matP,0,matV,0)

        // -------------------------------------------------------
        // テクスチャ変換用行列
        // -------------------------------------------------------
        matM4Tex[0] = 0.5f
        matM4Tex[1] = 0f
        matM4Tex[2] = 0f
        matM4Tex[3] = 0f
        matM4Tex[4] = 0f
        matM4Tex[5] = 0.5f
        matM4Tex[6] = 0f
        matM4Tex[7] = 0f
        matM4Tex[8] = 0f
        matM4Tex[9] = 0f
        matM4Tex[10] = 1f
        matM4Tex[11] = 0f
        matM4Tex[12] = 0.5f
        matM4Tex[13] = 0.5f
        matM4Tex[14] = 0f
        matM4Tex[15] = 1f

        // ライトの距離を係数で調整
        vecLight1[0] = 0f * k
        vecLight1[1] = 1f * k
        vecLight1[2] = 0f * k

        // ライトから見たビュー座標変換行列
        Matrix.setLookAtM(matDV, 0,
                vecLight1[0], vecLight1[1], vecLight1[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecLight1Up[0], vecLight1Up[1], vecLight1Up[2])
        // ライトから見たプロジェクション座標変換行列
        Matrix.perspectiveM(matDP,0,90f,ratio,0.1f,150f)

        // テクスチャ座標変換行列
        Matrix.multiplyMM(matDVP,0,matM4Tex,0,matDP,0)
        Matrix.multiplyMM(matM4Tex,0,matDVP,0,matDV,0)

        // ライトから見たビュー×プロジェクション座標変換行列
        Matrix.multiplyMM(matDVP,0,matDP,0,matDV,0)

        // プログラムオブジェクトの選択(シャドウマッピング用)
        GLES20.glUseProgram(programHandleScreen)

        // フレームバッファを初期化
        GLES20.glClearColor(0.0f, 1f, 1f, 1.0f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // ビューポートをフレームバッファのサイズに調整する
        GLES20.glViewport(0,0,2048,2048)

        // -------------------------------------------------------
        // トーラス描画(10個)
        // -------------------------------------------------------
        (0..9).forEach { i ->
            // 回転角度
            val angleT1 =(angle1+i*36)%360
            val angleT2 =((i%5)*72)%360
            val t1 = angleT1.toFloat()
            val t2 = angleT2.toFloat()
            val ifl = -floor(i.toFloat()/5f) +1f
            Matrix.setIdentityM(matM,0)
            Matrix.rotateM(matM,0,t2,0f,1f,0f)
            Matrix.translateM(matM,0,0f,ifl*10f+10f,(ifl-2f)*7f)
            Matrix.rotateM(matM,0,t1,1f,1f,0f)
            Matrix.multiplyMM(matLight,0,matDVP,0,matM,0)
            drawObjTorusDepth.drawShaderDepth(programHandleDepth,matLight,u_depthBuffer)
        }

        // 板ポリゴンの描画(底面)
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,0f,-10f,0f)
        Matrix.scaleM(matM,0,30f,0f,30f)
        Matrix.multiplyMM(matLight,0,matDVP,0,matM,0)
        //drawObjBoardDepth.drawShaderDepth(programHandleDepth,matLight,u_depthBuffer)
        drawObjBoardDepth.drawShaderDepth(programHandleDepth,matLight,0,"frameBuffer")

        // プログラムオブジェクトの選択(スクリーンレンダリング用)
        GLES20.glUseProgram(programHandleScreen)

        // フレームバッファのバインドを解除
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0)

        // フレームバッファをテクスチャとしてバインド
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[0])

        // canvasを初期化
        GLES20.glClearColor(0.0f, 1f, 1f, 1.0f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // ビューポートを元のcanvasのサイズに戻る
        GLES20.glViewport(0,0,renderW,renderH)

        // -------------------------------------------------------
        // トーラス描画(10個)
        // -------------------------------------------------------
        (0..9).forEach { i ->
            // 回転角度
            val angleT1 =(angle1+i*36)%360
            val angleT2 =((i%5)*72)%360
            val t1 = angleT1.toFloat()
            val t2 = angleT2.toFloat()
            val ifl = -floor(i.toFloat()/5f) +1f
            Matrix.setIdentityM(matM,0)
            Matrix.rotateM(matM,0,t2,0f,1f,0f)
            Matrix.translateM(matM,0,0f,ifl*10f+10f,(ifl-2f)*7f)
            Matrix.rotateM(matM,0,t1,1f,1f,0f)
            Matrix.multiplyMM(matMVP,0,matT,0,matM,0)
            Matrix.invertM(matI,0,matM,0)
            Matrix.multiplyMM(matLight,0,matDVP,0,matM,0)
            drawObjTorusScreen.draw(programHandleScreen,matM,matMVP,matI,matM4Tex,matLight,vecLight1,0,u_depthBuffer)
        }


        // 板ポリゴンの描画(底面)
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,0f,-10f,0f)
        Matrix.scaleM(matM,0,30f,0f,30f)
        Matrix.multiplyMM(matMVP,0,matT,0,matM,0)
        Matrix.invertM(matI,0,matM,0)
        Matrix.multiplyMM(matLight,0,matDVP,0,matM,0)
        drawObjBoardScreen.draw(programHandleScreen,matM,matMVP,matI,
                matM4Tex,matLight,vecLight1,0,0)



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

        // シェーダプログラム登録(深度値格納用)
        programHandleDepth = W052ShaderDepth().loadShader()
        // シェーダプログラム登録(スクリーンレンダリング用)
        programHandleScreen = W052ShaderScreen().loadShader()

        // モデル生成(トーラス:デプス)
        drawObjTorusDepth = W052ModelTorus()

        // モデル生成(トーラス:スクリーン)
        drawObjTorusScreen = W052ModelTorus()

        // モデル生成(板ポリゴン：デプス)
        drawObjBoardDepth = W052ModelBoard()

        // モデル生成(板ポリゴン：スクリーン)
        drawObjBoardScreen = W052ModelBoard()

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
        Matrix.setIdentityM(matT,0)

        // モデル変換行列(テクスチャ用)
        Matrix.setIdentityM(matM4Tex,0)
        // ライトの座標変換行列
        Matrix.setIdentityM(matLight,0)
        // ライトから見たビュー座標変換行列
        Matrix.setIdentityM(matDV,0)
        // ライトから見たプロジェクション座標変換行列
        Matrix.setIdentityM(matDP,0)
        // ライトから見たビュー×プロジェクション座標変換行列
        Matrix.setIdentityM(matDVP,0)
    }


    // フレームバッファをオブジェクトとして生成する
    private fun createFrameBuffer(width: Int, height: Int) {

        val maxRenderbufferSize = IntBuffer.allocate(1)
        GLES20.glGetIntegerv(GLES20.GL_MAX_RENDERBUFFER_SIZE,maxRenderbufferSize)

        Log.d(javaClass.simpleName,"w[${width}]h[${height}]bufSize[${maxRenderbufferSize[0]}]")

        // フレームバッファ生成
        GLES20.glGenFramebuffers(1,bufFrame)
        // レンダ―バッファ生成
        GLES20.glGenRenderbuffers(1,bufDepthRender)
        // テクスチャ生成
        GLES20.glGenTextures(1,frameTexture)

        // フレームバッファのバインド
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[0])

        // 深度バッファ用レンダ―バッファのバインド
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER,bufDepthRender[0])

        // レンダ―バッファを深度バッファとして設定
        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, width, height)

        // フレームバッファにレンダ―バッファを関連付ける
        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER,bufDepthRender[0])


        // フレームバッファ用のテクスチャをキューブマップテクスチャとしてバインド
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[0])

        // フレームバッファ用のテクスチャにカラー用のメモリ領域を確保
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D,0,GLES20.GL_RGBA,width,height,0,GLES20.GL_RGBA,GLES20.GL_UNSIGNED_BYTE,null)
        /*
        // フレームバッファ用のテクスチャにカラー用のメモリ領域を６面分確保
        //GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D,0,GLES20.GL_RGBA,width,height,0,GLES20.GL_RGBA,GLES20.GL_UNSIGNED_BYTE,null)
        bmpArray.forEachIndexed { id, bitmap ->
            GLES20.glTexImage2D(targetArray[id],0,
                    GLES20.GL_RGBA,width,height,0,
                    GLES20.GL_RGBA,GLES20.GL_UNSIGNED_BYTE,null)
        }
        */

        // テクスチャパラメータ
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)

        // フレームバッファにテクスチャを関連付ける
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER,GLES20.GL_COLOR_ATTACHMENT0,GLES20.GL_TEXTURE_2D,frameTexture[0],0)

        // 追加
        val status = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER)
        Log.d(javaClass.simpleName,"status[${status}]COMPLETE[${GLES20.GL_FRAMEBUFFER_COMPLETE}]")

        // バインド解除
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0)
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER,0)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0)
    }

    fun receiveTouch(ev: MotionEvent, w: Int, h: Int ) {
        var wh = 1f/ sqrt((w*w+h*h).toFloat())
        // canvasの中心点からみたタッチ点の相対位置
        var x = ev.x - w.toFloat()*0.5f
        var y = ev.y - h.toFloat()*0.5f
        var sq = sqrt(x*x+y*y)
        //var r = sq*2f*PI.toFloat()*wh
        // 回転角
        var r = sq*wh*360f
        if (sq != 1f) {
            sq = 1f/sq
            x *= sq
            y *= sq
        }
        xQuaternion = MyQuaternion.rotate(r, floatArrayOf(y,x,0f))
    }
}