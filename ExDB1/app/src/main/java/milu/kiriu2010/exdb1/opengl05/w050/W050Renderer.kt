package milu.kiriu2010.exdb1.opengl05.w050

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.color.MgColor
import milu.kiriu2010.gui.model.Cube01Model
import milu.kiriu2010.gui.model.Torus01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import java.nio.ByteBuffer
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// 光学迷彩
// http://opengles2learning.blogspot.com/2011/06/texturing-cube-different-textures-on.html
class W050Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画オブジェクト(立方体)
    private lateinit var drawObjCube: Cube01Model
    // 描画オブジェクト(トーラス)
    private lateinit var drawObjTorus: Torus01Model

    // シェーダ(光学迷彩)
    private lateinit var shaderStealth: W050ShaderStealth
    // シェーダ(反射光)
    private lateinit var shaderSpecular: W050ShaderSpecular
    // シェーダ(キューブ環境マッピング)
    private lateinit var shaderCubeMap: W050ShaderCubeMap


    // 画面縦横比
    var ratio: Float = 0f

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

    // ---------------------------------------------
    // ライトを視点とみなした場合の変換行列として使う
    // ---------------------------------------------
    // テクスチャ座標変換行列
    private val matTex = FloatArray(16)
    // テンポラリ行列(ライト視点)
    private val matT4L = FloatArray(16)

    // 光学迷彩にかける補正係数
    //   -1.0 ～ 1.0
    var k = 1f

    override fun onDrawFrame(gl: GL10?) {
        // 回転角度
        angle[0] =(angle[0]+1)%360
        val t1 = angle[0].toFloat()

        // フレームバッファをバインド
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[0])

        // フレームバッファを初期化
        GLES20.glClearColor(0.0f, 0.7f, 0.7f, 1.0f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // ビュー×プロジェクション座標変換行列
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,20f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,90f,ratio,0.1f,200f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // キューブマップテクスチャで背景用キューブをレンダリング
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP,textures[0])
        Matrix.setIdentityM(matM,0)
        Matrix.scaleM(matM,0,100f,100f,100f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderCubeMap.draw(drawObjCube,matM,matMVP,floatArrayOf(0f,0f,0f),0,0)

        // スペキュラライティングシェーダでトーラスモデルをレンダリング
        (0..8).forEach {  i ->
            val t = i.toFloat() * 360f/9f
            var amb = MgColor.hsva(i*40,1f,1f,1f).toFloatArray()
            Matrix.setIdentityM(matM,0)
            Matrix.rotateM(matM,0,t,0f,1f,0f)
            Matrix.translateM(matM,0,0f,0f,30f)
            Matrix.rotateM(matM,0,t1,1f,1f,0f)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            Matrix.invertM(matI,0,matM,0)
            shaderSpecular.draw(drawObjTorus,matMVP,matI,vecLight,vecEye,amb)
        }

        // フレームバッファのバインドを解除
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0)

        // canvasを初期化
        GLES20.glClearColor(0.0f, 0.7f, 0.7f, 1.0f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // キューブマップテクスチャで背景用キューブをレンダリング
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP,textures[0])
        Matrix.setIdentityM(matM,0)
        Matrix.scaleM(matM,0,100f,100f,100f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderCubeMap.draw(drawObjCube,matM,matMVP, floatArrayOf(0f,0f,0f),0,0)

        // スペキュラライティングシェーダでトーラスモデルをレンダリング
        (0..8).forEach {  i ->
            val t = i.toFloat() * 360f/9f
            var amb = MgColor.hsva(i*40,1f,1f,1f).toFloatArray()
            Matrix.setIdentityM(matM,0)
            Matrix.rotateM(matM,0,t,0f,1f,0f)
            Matrix.translateM(matM,0,0f,0f,30f)
            Matrix.rotateM(matM,0,t1,1f,1f,0f)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            Matrix.invertM(matI,0,matM,0)
            shaderSpecular.draw(drawObjTorus,matMVP,matI,vecLight,vecEye,amb)
        }

        // -------------------------------------------------------
        // テクスチャ変換用行列
        // -------------------------------------------------------
        matTex[0] = 0.5f
        matTex[1] = 0f
        matTex[2] = 0f
        matTex[3] = 0f
        matTex[4] = 0f
        // 画像から読み込んだ場合は、-0.5fだが、
        // フレームバッファに描いた風景は、初めから上下が判定しているので0.5f
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

        // 行列を掛け合わせる
        Matrix.multiplyMM(matT4L,0,matTex,0,matP,0)
        Matrix.multiplyMM(matTex,0,matT4L,0,matV,0)

        // フレームバッファテクスチャをバインド
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[0])

        // 光学迷彩でトーラスモデルをレンダリング
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,t1,1f,0f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderStealth.draw(drawObjTorus,matM,matTex,matMVP, k, 0)
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

        // シェーダ(光学迷彩)
        shaderStealth = W050ShaderStealth()
        shaderStealth.loadShader()
        // シェーダ(反射光)
        shaderSpecular = W050ShaderSpecular()
        shaderSpecular.loadShader()
        // シェーダ(キューブ環境マッピング)
        shaderCubeMap = W050ShaderCubeMap()
        shaderCubeMap.loadShader()

        // キューブマップを生成
        generateCubeMap()

        // モデル生成(立方体)
        drawObjCube = Cube01Model()
        drawObjCube.createPath(mapOf(
                "pattern" to 2f,
                "scale"   to 2f,
                "colorR"  to 1f,
                "colorG"  to 1f,
                "colorB"  to 1f,
                "colorA"  to 1f
        ))

        // モデル生成(トーラス)
        drawObjTorus = Torus01Model()
        drawObjTorus.createPath(mapOf(
                "row"     to 32f,
                "column"  to 32f,
                "iradius" to 2.5f,
                "oradius" to 5f,
                "colorR"  to 1f,
                "colorG"  to 1f,
                "colorB"  to 1f,
                "colorA"  to 1f
        ))

        // 光源位置(反射光によるライティングで利用)
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
        // ビュー・プロジェクション行列
        Matrix.setIdentityM(matVP,0)
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

    private fun generateCubeMap() {
        // テクスチャ作成し、idをtexturesに保存
        GLES20.glGenTextures(1,textures,0)
        MyGLFunc.checkGlError("glGenTextures")

        // テクスチャをキューブマップにバインド
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP,textures[0])

        // テクスチャへimageを適用
        (0..5).forEach { id ->
            val bitmap = bmpArray[id]
            val bw = bitmap.width
            val bh = bitmap.height
            val buffer = ByteBuffer.allocateDirect(bw*bh*4)
            bitmap.copyPixelsToBuffer(buffer)
            buffer.position(0)

            GLES20.glTexImage2D(targetArray[id],0,GLES20.GL_RGBA,
                    bw,bh,0,GLES20.GL_RGBA,
                    GLES20.GL_UNSIGNED_BYTE,buffer)
            if ( bitmap.isRecycled == false ) {
                bitmap.recycle()
            }
        }

        // ミニマップを生成
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_CUBE_MAP)

        // テクスチャのパラメータを設定
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)

        // テクスチャのバインド無効化
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP,0)
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
    }

}