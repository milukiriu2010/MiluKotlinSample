package milu.kiriu2010.exdb1.opengl06.w065

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.basic.MyQuaternion
import milu.kiriu2010.gui.model.Board01Model
import milu.kiriu2010.gui.model.Point01Model
import milu.kiriu2010.gui.model.Sphere01Model
import milu.kiriu2010.gui.model.Torus01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.math.MyMathUtil
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// ----------------------------------------
// 後光表面化散乱
// ----------------------------------------
// https://wgld.org/d/webgl/w065.html
// ----------------------------------------
class W065Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画オブジェクト(トーラス)
    private lateinit var modelTorus: Torus01Model
    // 描画オブジェクト(球体)
    private lateinit var modelSphere: Sphere01Model
    // 描画オブジェクト(板ポリゴン)
    private lateinit var modelBoard: Board01Model

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
    val u_weight = MyGLFunc.gaussianWeigt(5,10f,1f)

    // 視点ベクトルの逆行列
    private var vecI4Eye = FloatArray(3)

    // テクスチャ変換用行列
    private val matTex = FloatArray(16)
    // プロジェクション(テクスチャ変換用行列)
    //   tvpMatrix
    private val matP4Tex = FloatArray(16)
    // プロジェクション×ビュー(テクスチャ変換用行列)
    //   tmvpMatrix
    private val matVP4Tex = FloatArray(16)
    // プロジェクション×ビュー(テクスチャ変換用行列)(X軸反転版)
    //   itmvpMatrix
    private val matVP4TexX = FloatArray(16)

    // プロジェクションxビュー(正射影用の座標変換行列)
    //   ortMatrix
    private val matO = FloatArray(16)
    // プロジェクション(正射影用の座標変換行列)
    //   ort_pMatrix
    private val matP4O = FloatArray(16)
    // プロジェクションxビュー(正射影用の座標変換行列)
    //   ort_tmpMatrix
    private val matVP4O = FloatArray(16)

    // ビュー(裏面の深度値を描く際に使用する正射影座標変換行列)
    //   inv_vMatrix
    private val matV4I = FloatArray(16)
    // プロジェクション×ビュー(裏面の深度値を描く際に使用する正射影座標変換行列)
    //   inv_ort_tmpMatrix
    private val matVP4I = FloatArray(16)

    // モデル座標変換行列(トーラス)
    private val matM4Torus = FloatArray(16)
    // モデル座標変換行列(球体)
    private val matM4Sphere = FloatArray(16)


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

        Matrix.setLookAtM(matV,0,
                0f,0f,0.5f,
                0f,0f,0f,
                0f,1f,0f)
        Matrix.orthoM(matP,0,-1f,1f,-1f,1f,0.1f,1f)
        Matrix.multiplyMM(matO,0,matP,0,matV,0)
    }

    override fun onDrawFrame(gl: GL10?) {
        angle[0] =(angle[0]+1)%360
        val angleF0 = angle[0].toFloat()

        // ビュー×プロジェクション座標変換行列
        vecEye   = qtnNow.toVecIII(floatArrayOf(0f,0f, 7f))
        vecI4Eye = qtnNow.toVecIII(floatArrayOf(0f,0f,-7f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f, 0f))

        // 最終シーンで使う透視射影変換行列を生成(tmpMatrix)
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,ratio,0.1f,15f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // バックバッファに描きこむ際に使用する正射影座標変換行列を生成(orth_tmpMatrix)
        Matrix.orthoM(matP4O,0,-3f,3f,-3f,3f,0.1f,15f)
        Matrix.multiplyMM(matVP4O,0,matP4O,0,matV,0)

        // 裏面の深度値を描きこむ際に使用する正射影変換行列を生成(inv_ort_tmpMatrix)
        Matrix.setLookAtM(matV4I,0,
                vecI4Eye[0],vecI4Eye[1],vecI4Eye[2],
                vecCenter[0],vecCenter[1],vecCenter[2],
                vecEyeUp[0],vecEyeUp[1],vecEyeUp[2])
        Matrix.multiplyMM(matVP4I,0,matP4O,0,matV4I,0)

        // テクスチャ座標変換用の行列を掛け合わせる
        matTex[0] = 0.5f
        Matrix.multiplyMM( matP4Tex,0,  matTex,0,matP4O,0)
        Matrix.multiplyMM(matVP4Tex,0,matP4Tex,0,  matV,0)

        // テクスチャ座標変換用の行列を掛け合わせる(X軸反転版)
        matTex[0] = -0.5f
        Matrix.multiplyMM(  matP4Tex,0,  matTex,0,matP4O,0)
        Matrix.multiplyMM(matVP4TexX,0,matP4Tex,0,  matV,0)

        // ライトの位置
        vecLight[0] = -1.75f
        vecLight[1] =  1.75f
        vecLight[2] =  1.75f


        // ライトを回転させる際の軸ベクトル
        var vecLightAxis = floatArrayOf(1f,1f,0f)

        // ライト回転軸ベクトルの正規化
        vecLightAxis = MyMathUtil.normalize(vecLightAxis)

        // ライトの位置を回転させるためのクォータニオン
        // クォータニオンを回転
        val qtnLightPos = MyQuaternion.rotate(angleF0,vecLightAxis)

        // 回転後のライトの位置
        // ライトの位置をクォータニオンで変換
        val vecLight2 = qtnLightPos.toVecIII(vecLight)

        // -----------------------------------------------
        // 各種レンダリング開始
        // -----------------------------------------------

        // --------------------------------------------------------------
        // 【0】
        //  裏面からみた深度値を１つ目のフレームバッファにレンダリング
        // --------------------------------------------------------------

        // フレームバッファをバインド
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[0])

        // フレームバッファを初期化
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // トーラスのレンダリング(裏面の深度)
        Matrix.setIdentityM(matM4Torus,0)
        Matrix.rotateM(matM4Torus,0,90f,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP4I,0,matM4Torus,0)
        depthShader.draw(modelTorus,matM4Torus,matMVP,vecI4Eye)

        // 球体のレンダリング(裏面の深度)
        Matrix.setIdentityM(matM4Sphere,0)
        Matrix.rotateM(matM4Sphere,0,angleF0,0f,0f,1f)
        Matrix.translateM(matM4Sphere,0,0f,1.5f,0f)
        Matrix.multiplyMM(matMVP,0,matVP4I,0,matM4Sphere,0)
        depthShader.draw(modelSphere,matM4Sphere,matMVP,vecI4Eye)

        // --------------------------------------------------------------
        // 【1】
        //  表側と裏側の深度値の差分を２つ目のフレームバッファにレンダリング
        // --------------------------------------------------------------

        // フレームバッファをバインド
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[1])

        // 裏面深度をレンダリングしたフレームバッファをテクスチャとしてバインド
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[0])

        // フレームバッファを初期化
        GLES20.glClearColor(1f, 1f, 1f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // トーラスのレンダリング(深度の差分をレンダリング)
        Matrix.setIdentityM(matM4Torus,0)
        Matrix.rotateM(matM4Torus,0,90f,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP4O,0,matM4Torus,0)
        diffShader.draw(modelTorus,matM4Torus,matMVP,matVP4TexX,vecEye,0)

        // 球体のレンダリング(深度の差分をレンダリング)
        Matrix.setIdentityM(matM4Sphere,0)
        Matrix.multiplyMM(matMVP,0,matVP4O,0,matM4Sphere,0)
        diffShader.draw(modelSphere,matM4Sphere,matMVP,matVP4TexX,vecEye,0)

        // --------------------------------------------------------------
        // 【2】
        //  深度値の差分をぼかすため
        //  ３つ目のフレームバッファで水平方向のブラーをかける
        // --------------------------------------------------------------

        // フレームバッファをバインド
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[2])

        // フレームバッファをテクスチャとしてバインド
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[1])

        // フレームバッファを初期化
        GLES20.glClearColor(1f, 1f, 1f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 水平方向にブラーをかける
        gaussianShader.draw(modelBoard,matO,0,u_weight,1,renderW.toFloat())

        // --------------------------------------------------------------
        // 【3】
        //  深度値の差分をぼかすため
        //  ４つ目のフレームバッファで垂直方向のブラーをかける
        // --------------------------------------------------------------

        // フレームバッファをバインド
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[3])

        // フレームバッファをテクスチャとしてバインド
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[2])

        // フレームバッファを初期化
        GLES20.glClearColor(1f, 1f, 1f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 水平方向にブラーをかける
        gaussianShader.draw(modelBoard,matO,0,u_weight,0,renderW.toFloat())

        // --------------------------------------------------------------
        // 【4】
        //  全てのオフスクリーンレンダリングが完了したので
        //  フレームバッファをテクスチャとしてバインド
        // --------------------------------------------------------------
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[0])
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[1])
        GLES20.glActiveTexture(GLES20.GL_TEXTURE2)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[3])

        // --------------------------------------------------------------
        // 【5】
        //  最終シーンのレンダリングを開始
        // --------------------------------------------------------------

        // フレームバッファのバインドを解除
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0)

        // canvasを初期化
        GLES20.glClearColor(0f, 0.1f, 0.1f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // トーラスのレンダリング
        Matrix.invertM(matI,0,matM4Torus,0)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM4Torus,0)
        mainShader.draw(modelTorus,matM4Torus,matMVP,matI,matVP4Tex,
                vecLight2,vecCenter,vecEye,vecAmbientColor,2)

        // 球体のレンダリング
        Matrix.invertM(matI,0,matM4Sphere,0)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM4Sphere,0)
        mainShader.draw(modelSphere,matM4Sphere,matMVP,matI,matVP4Tex,
                vecLight2,vecCenter,vecEye,vecAmbientColor,2)

        // ライトの位置を点としてレンダリング
        val modelPoint = Point01Model()
        modelPoint.createPath(mapOf(
                "x" to vecLight2[0],
                "y" to vecLight2[1],
                "z" to vecLight2[2]
        ))
        pointShader.draw(modelPoint,matVP)

        // --------------------------------------------------------------
        // 【6】
        //  オフスクリーンでレンダリングした結果を正射影で最終シーンに合成
        // --------------------------------------------------------------

        // 板ポリゴンのレンダリング
        //   0:裏面の深度値を描画
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-0.8f,-0.8f,0f)
        Matrix.scaleM(matM,0,0.2f,0.2f,1f)
        Matrix.multiplyMM(matMVP,0,matO,0,matM,0)
        orthShader.draw(modelBoard,matMVP,0)

        //   1:深度の差分を描画
        Matrix.translateM(matM,0,2f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matO,0,matM,0)
        orthShader.draw(modelBoard,matMVP,1)

        //   3:ブラーをかけた深度値を描画
        Matrix.translateM(matM,0,2f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matO,0,matM,0)
        orthShader.draw(modelBoard,matMVP,2)
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
            MyGLFunc.createFrameBuffer(renderW,renderH,it,bufFrame,bufDepthRender,frameTexture)
        }
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
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
        modelTorus = Torus01Model()
        modelTorus.createPath(mapOf(
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
        modelSphere = Sphere01Model()
        modelSphere.createPath(mapOf(
                "row"     to 32f,
                "column"  to 32f,
                "radius"  to 0.5f,
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

        // 環境色
        vecAmbientColor[0] = 0.05f
        vecAmbientColor[1] = 0.05f
        vecAmbientColor[2] = 0.05f
        vecAmbientColor[3] = 0f
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
    }
}