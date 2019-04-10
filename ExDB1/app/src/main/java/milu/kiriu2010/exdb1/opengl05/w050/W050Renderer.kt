package milu.kiriu2010.exdb1.opengl05.w050

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import android.view.MotionEvent
import milu.kiriu2010.exdb1.opengl.MyGLFunc
import milu.kiriu2010.gui.basic.MyColor
import milu.kiriu2010.gui.basic.MyQuaternion
import java.nio.ByteBuffer
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.sqrt

// 光学迷彩
// http://opengles2learning.blogspot.com/2011/06/texturing-cube-different-textures-on.html
class W050Renderer: GLSurfaceView.Renderer {
    // 描画オブジェクト(立方体)
    private lateinit var drawObjCube: W050ModelCube
    // 描画オブジェクト(トーラス)
    private lateinit var drawObjTorus: W050ModelTorus

    // プログラムハンドル(光学迷彩)
    private var programHandleStealth: Int = 0
    // プログラムハンドル(スペキュラライティング)
    private var programHandleSpecular: Int = 0
    // プログラムハンドル(キューブ環境マッピング)
    private var programHandleCubeMap: Int = 0


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
    // ライトの向き(反射光によるライティングで利用)
    private val vecLight1 = floatArrayOf(-0.577f,0.577f,0.577f)
    private val vecLight2 = floatArrayOf(-1f,0f,0f)
    // 環境光の色
    private val vecAmbientColor = floatArrayOf(0.1f,0.1f,0.1f,1f)
    // カメラの座標
    private var vecEye = floatArrayOf(0f,0f,20f)
    // カメラの上方向を表すベクトル
    private var vecEyeUp = floatArrayOf(0f,1f,0f)
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

    // キューブマップ用のターゲットを格納する配列
    val targetArray = arrayListOf<Int>(
            GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X,
            GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y,
            GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z,
            GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X,
            GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y,
            GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z
    )

    // 視点座標(トーラス用)
    val torusEye = FloatArray(3*6)
    // カメラの上方向を表すベクトル(トーラス用)
    val torusCamUp = FloatArray(3*6)
    // 位置(トーラス用)
    val torusPos = FloatArray(3*6)
    // 環境色(トーラス用)
    val torusAmb = FloatArray(4*6)


    // テクスチャ配列
    val textures = IntArray(2)

    // フレームバッファ
    val bufFrame = IntBuffer.allocate(1)

    // 深度バッファ用レンダ―バッファ
    val bufDepthRender = IntBuffer.allocate(1)

    // フレームバッファ用のテクスチャ
    val frameTexture = IntBuffer.allocate(1)

    // レンダリングする幅・高さ
    var renderW = 0
    var renderH = 0

    // モデル変換行列(テクスチャ用)
    private val matM4Tex = FloatArray(16)
    // ビュー変換行列(テクスチャ用)
    private val matV4Tex = FloatArray(16)
    // プロジェクション変換行列(テクスチャ用)
    private val matP4Tex = FloatArray(16)
    // モデル×ビュー×プロジェクション行列(テクスチャ用)
    private val matMVP4Tex = FloatArray(16)

    // 光学迷彩にかける補正係数
    var k = 10f

    override fun onDrawFrame(gl: GL10?) {
        // 回転角度
        angle1 =(angle1+2)%360
        angle2 =(angle1+180)%360
        val t1 = angle1.toFloat()
        val t2 = angle2.toFloat()

        // フレームバッファをバインド
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[0])

        // フレームバッファを初期化
        GLES20.glClearColor(0.0f, 0.7f, 0.7f, 1.0f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // ビュー×プロジェクション座標変換行列
        vecEye = MyQuaternion.toVecIII(floatArrayOf(0f,0f,20f),xQuaternion)
        vecEyeUp = MyQuaternion.toVecIII(floatArrayOf(0f,1f,0f),xQuaternion)
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,90f,ratio,0.1f,200f)
        Matrix.multiplyMM(matT,0,matP,0,matV,0)

        // キューブマップテクスチャで背景用キューブをレンダリング
        GLES20.glUseProgram(programHandleCubeMap)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP,textures[0])
        Matrix.setIdentityM(matM,0)
        Matrix.scaleM(matM,0,100f,100f,100f)
        Matrix.multiplyMM(matMVP,0,matT,0,matM,0)
        drawObjCube.draw(programHandleCubeMap,matM,matMVP,vecEye,0,0)

        // スペキュラライティングシェーダでトーラスモデルをレンダリング
        GLES20.glUseProgram(programHandleSpecular)
        (0..8).forEach {  i ->
            val t = i.toFloat() * 360f/9f
            var amb = MyColor.hsva(i*40,1f,1f,1f).toFloatArray()
            Matrix.setIdentityM(matM,0)
            Matrix.rotateM(matM,0,t,0f,1f,0f)
            Matrix.translateM(matM,0,0f,0f,30f)
            Matrix.rotateM(matM,0,t1,1f,1f,0f)
            Matrix.multiplyMM(matMVP,0,matT,0,matM,0)
            Matrix.invertM(matI,0,matM,0)
            drawObjTorus.draw(programHandleSpecular,matMVP,matI,vecLight1,vecEye,amb)

        }

        // フレームバッファのバインドを解除
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0)













        // canvasを初期化
        GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // ビュー×プロジェクション座標変換行列
        vecEye = MyQuaternion.toVecIII(floatArrayOf(0f,0f,20f),xQuaternion)
        vecEyeUp = MyQuaternion.toVecIII(floatArrayOf(0f,1f,0f),xQuaternion)
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,ratio,0.1f,200f)
        Matrix.multiplyMM(matT,0,matP,0,matV,0)

        // キューブ環境マッピングシェーダ
        GLES20.glUseProgram(programHandleSpecular)

        // 背景用キューブをレンダリング
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP,textures[0])
        Matrix.setIdentityM(matM,0)
        Matrix.scaleM(matM,0,100f,100f,100f)
        Matrix.multiplyMM(matMVP,0,matT,0,matM,0)
        drawObjCube.draw(programHandleSpecular,matM,matMVP,vecEye,0,0)

        // 動的キューブマップテクスチャを適用して球体をレンダリング
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP,frameTexture[0])
        Matrix.setIdentityM(matM,0)
        //Matrix.rotateM(matM,0,t1,0f,0f,1f)
        //Matrix.translateM(matM,0,5f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matT,0,matM,0)
        //drawObjSphere.draw(programHandleSpecular,matM,matMVP,vecEye,0,1)

        // スペキュラライティングシェーダ
        GLES20.glUseProgram(programHandleStealth)

        // トーラスをレンダリング
        targetArray.forEachIndexed { id, target ->
            Matrix.setIdentityM(matM,0)
            Matrix.translateM(matM,0,torusPos[0+id*3],torusPos[1+id*3],torusPos[2+id*3])
            Matrix.rotateM(matM,0,t1,torusEye[0+id*3],torusEye[1+id*3],torusEye[2+id*3])
            Matrix.multiplyMM(matMVP,0,matT,0,matM,0)
            Matrix.invertM(matI,0,matM,0)

            val amb = FloatArray(4)
            amb[0] = torusAmb[0+id*4]
            amb[1] = torusAmb[1+id*4]
            amb[2] = torusAmb[2+id*4]
            amb[3] = torusAmb[3+id*4]
            drawObjTorus.draw(programHandleStealth,matMVP,matI,vecLight1,vecEye,amb)
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        ratio = width.toFloat()/height.toFloat()

        renderW = width
        renderH = height

        createFrameBuffer(renderW,renderH)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // canvasを初期化する色を設定する
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // canvasを初期化する際の深度を設定する
        GLES20.glClearDepthf(1f)

        // カリングと深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)

        // シェーダプログラム登録(光学迷彩)
        programHandleStealth = W050ShaderStealth().loadShader()
        // シェーダプログラム登録(スペキュラライティング)
        programHandleSpecular = W050ShaderSpecular().loadShader()
        // シェーダプログラム登録(キューブ環境マッピング)
        programHandleSpecular = W050ShaderCubeMap().loadShader()

        // キューブマップを生成
        generateCubeMap()

        // モデル生成(立方体)
        drawObjCube = W050ModelCube()

        // モデル生成(トーラス)
        drawObjTorus = W050ModelTorus()


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
    }

    // フレームバッファへの６方向レンダリング
    private fun render2FrameBuffer(t1:Float) {
        targetArray.forEachIndexed { id, target ->
            // フレームバッファにテクスチャを関連付ける
            GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER,GLES20.GL_COLOR_ATTACHMENT0,target,frameTexture[0],0)

            // フレームバッファを初期化
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
            GLES20.glClearDepthf(1f)
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

            // 方角を判別して処理する
            when (target) {
                GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X -> {
                    torusEye[0] = 1f
                    torusEye[1] = 0f
                    torusEye[2] = 0f
                    torusCamUp[0] = 0f
                    torusCamUp[1] = -1f
                    torusCamUp[2] = 0f
                    torusPos[0] = 6f
                    torusPos[1] = 0f
                    torusPos[2] = 0f
                    torusAmb[0] = 1f
                    torusAmb[1] = 0.5f
                    torusAmb[2] = 0.5f
                    torusAmb[3] = 1f
                }
                GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y -> {
                    torusEye[3] = 0f
                    torusEye[4] = 1f
                    torusEye[5] = 0f
                    torusCamUp[3] = 0f
                    torusCamUp[4] = 0f
                    torusCamUp[5] = 1f
                    torusPos[3] = 0f
                    torusPos[4] = 6f
                    torusPos[5] = 0f
                    torusAmb[4] = 0.5f
                    torusAmb[5] = 1f
                    torusAmb[6] = 0.5f
                    torusAmb[7] = 1f
                }
                GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z -> {
                    torusEye[6] = 0f
                    torusEye[7] = 0f
                    torusEye[8] = 1f
                    torusCamUp[6] = 0f
                    torusCamUp[7] = -1f
                    torusCamUp[8] = 0f
                    torusPos[6] = 0f
                    torusPos[7] = 0f
                    torusPos[8] = 6f
                    torusAmb[8] = 0.5f
                    torusAmb[9] = 0.5f
                    torusAmb[10] = 1f
                    torusAmb[11] = 1f
                }
                GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X -> {
                    torusEye[9] = -1f
                    torusEye[10] = 0f
                    torusEye[11] = 0f
                    torusCamUp[9] = 0f
                    torusCamUp[10] = -1f
                    torusCamUp[11] = 0f
                    torusPos[9] = -6f
                    torusPos[10] = 0f
                    torusPos[11] = 0f
                    torusAmb[12] = 0.5f
                    torusAmb[13] = 0f
                    torusAmb[14] = 0f
                    torusAmb[15] = 1f
                }
                GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y -> {
                    torusEye[12] = 0f
                    torusEye[13] = -1f
                    torusEye[14] = 0f
                    torusCamUp[12] = 0f
                    torusCamUp[13] = 0f
                    torusCamUp[14] = -1f
                    torusPos[12] = 0f
                    torusPos[13] = -6f
                    torusPos[14] = 0f
                    torusAmb[16] = 0f
                    torusAmb[17] = 0.5f
                    torusAmb[18] = 0f
                    torusAmb[19] = 1f
                }
                GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z -> {
                    torusEye[15] = 0f
                    torusEye[16] = 0f
                    torusEye[17] = -1f
                    torusCamUp[15] = 0f
                    torusCamUp[16] = -1f
                    torusCamUp[17] = 0f
                    torusPos[15] = 0f
                    torusPos[16] = 0f
                    torusPos[17] = -6f
                    torusAmb[20] = 0f
                    torusAmb[21] = 0f
                    torusAmb[22] = 0.5f
                    torusAmb[23] = 1f
                }
            }

            // ビュー×プロジェクション座標変換行列
            Matrix.setLookAtM(matV,0,
                    torusEye[0+id*3], torusEye[1+id*3], torusEye[2+id*3],
                    vecCenter[0],vecCenter[1],vecCenter[2],
                    torusCamUp[0+id*3], torusCamUp[1+id*3], torusCamUp[2+id*3]
                    )
            Matrix.perspectiveM(matP,0,90f,1f,0.1f,200f)
            Matrix.multiplyMM(matT,0,matP,0,matV,0)

            // キューブマップテクスチャで背景用キューブをレンダリング
            GLES20.glUseProgram(programHandleSpecular)
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, textures[0])
            Matrix.setIdentityM(matM,0)
            Matrix.scaleM(matM,0,100f,100f,100f)
            Matrix.multiplyMM(matMVP,0,matT,0,matM,0)
            drawObjCube.draw(programHandleSpecular,matM,matMVP, floatArrayOf(0f,0f,0f),0,0)

            // 視線ベクトルの変換
            var torusInvEye = FloatArray(3)
            torusInvEye[0] = -torusEye[0+id*3]
            torusInvEye[1] = -torusEye[1+id*3]
            torusInvEye[2] = -torusEye[2+id*3]

            // 環境色
            var amb = FloatArray(4)
            amb[0] = torusAmb[0+id*4]
            amb[1] = torusAmb[1+id*4]
            amb[2] = torusAmb[2+id*4]
            amb[3] = torusAmb[3+id*4]

            // スペキュラライティングシェーダでトーラスをレンダリング
            GLES20.glUseProgram(programHandleStealth)
            Matrix.setIdentityM(matM,0)
            Matrix.translateM(matM,0,torusPos[0+id*3],torusPos[1+id*3],torusPos[2+id*3])
            Matrix.rotateM(matM,0,t1,torusEye[0+id*3],torusEye[1+id*3],torusEye[2+id*3])
            Matrix.multiplyMM(matMVP,0,matT,0,matM,0)
            Matrix.invertM(matI,0,matM,0)
            drawObjTorus.draw(programHandleStealth,matMVP,matI,vecLight1,torusInvEye,amb)
        }

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
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP,frameTexture[0])

        // フレームバッファ用のテクスチャにカラー用のメモリ領域を６面分確保
        //GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D,0,GLES20.GL_RGBA,width,height,0,GLES20.GL_RGBA,GLES20.GL_UNSIGNED_BYTE,null)
        bmpArray.forEachIndexed { id, bitmap ->
            GLES20.glTexImage2D(targetArray[id],0,
                    GLES20.GL_RGBA,width,height,0,
                    GLES20.GL_RGBA,GLES20.GL_UNSIGNED_BYTE,null)
            /*
            val bw = bitmap.width
            val bh = bitmap.height
            val buffer = ByteBuffer.allocateDirect(bw*bh*4)
            bitmap.copyPixelsToBuffer(buffer)
            buffer.position(0)
            GLES20.glTexImage2D(targetArray[id],0,GLES20.GL_RGBA,
                    bw,bh,0,GLES20.GL_RGBA,
                    GLES20.GL_UNSIGNED_BYTE,buffer)
            bitmap.recycle()
            */
        }

        // テクスチャパラメータ
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)

        // 追加
        val status = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER)
        Log.d(javaClass.simpleName,"status[${status}]COMPLETE[${GLES20.GL_FRAMEBUFFER_COMPLETE}]")

        // バインド解除
        /*
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP,0)
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER,0)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0)
        */
    }

    private fun generateCubeMap() {
        // テクスチャ作成し、idをtexturesに保存
        GLES20.glGenTextures(1,textures,0)
        MyGLFunc.checkGlError("glGenTextures")

        // テクスチャをキューブマップ
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP,textures[0])

        // テクスチャへimageを適用
        bmpArray.forEachIndexed { id, bitmap ->
            //GLES20.glTexImage2D(targetArray[id],0,
            //        GLES20.GL_RGBA, GLES20.GL_RGBA,GLES20.GL_UNSIGNED_BYTE,)
            //GLUtils.texImage2D(targetArray[id],0,GLES20.GL_RGBA,bitmap,GLES20.GL_UNSIGNED_BYTE,GLES20.GL_RGBA)

            //val buffer = ByteBuffer.allocate(bitmap.byteCount)
            val bw = bitmap.width
            val bh = bitmap.height
            val buffer = ByteBuffer.allocateDirect(bw*bh*4)
            bitmap.copyPixelsToBuffer(buffer)
            buffer.position(0)
            GLES20.glTexImage2D(targetArray[id],0,GLES20.GL_RGBA,
                    bw,bh,0,GLES20.GL_RGBA,
                    GLES20.GL_UNSIGNED_BYTE,buffer)
            bitmap.recycle()
        }

        // ミニマップを生成
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_CUBE_MAP)

        // 縮小時の補完設定
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        // 拡大時の補完設定
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)



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