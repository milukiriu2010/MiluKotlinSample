package milu.kiriu2010.exdb1.opengl05.w049v

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.Matrix
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.Board01Model
import milu.kiriu2010.gui.model.Torus01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs
import milu.kiriu2010.gui.vbo.es20.ES20VBOIpnc
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// ------------------------------------------------------------------------------
// 射影テクスチャマッピング
// ------------------------------------------------------------------------------
// テクスチャをまるでスクリーンに投影するかのようにマッピングする
// モデルの影を投影したり、モデルに光学迷彩がかかったように処理できる
// ------------------------------------------------------------------------------
// テクスチャという２次元データを射影変換することで３次元空間上に投影する
// 画像データの原点が左上になるのに対し、テクスチャの原点が左下となることに注意

// またテクスチャ空間は0～1の範囲で座標を表し、原点は左下。
// プロジェクション変換を行う射影空間では、座標の範囲は-1～1で、原点は空間の中心
//
// ・イメージが上下反転してしまう問題
// ・テクスチャ空間と射影空間で座標系が異なる問題
// を対処するために,テクスチャ座標系への変換行列を作成する
//   http://asura.iaigiri.com/OpenGL/gl45.html
// ------------------------------------------------------------------------------
// 物体のローカル座標系からテクスチャ座標系への変換
//
//   [テクスチャ座標変換行列]
//   ×[ライトから見たときの射影行列]
//   ×[ライトから見たときのビュー行列]
//   ×[ワールド行列]
//   ×[ローカル座標系の頂点座標]
// ------------------------------------------------------------------------------
// https://wgld.org/d/webgl/w049.html
// ------------------------------------------------------------------------------
class WV049Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画オブジェクト(トーラス)
    private lateinit var modelTorus: Torus01Model
    // 描画オブジェクト(板ポリゴン)
    private lateinit var modelBoard: Board01Model

    // VBO(トーラス)
    private lateinit var boTorus: ES20VBOAbs
    // VBO(板ポリゴン)
    private lateinit var boBoard: ES20VBOAbs

    // シェーダ
    private lateinit var shader: WV049Shader

    // 画面縦横比
    var ratio: Float = 0f

    // ライトビューの上方向
    val vecLightUp = floatArrayOf(0.577f,0.577f,-0.577f)

    // ビットマップ配列
    val bmpArray = arrayListOf<Bitmap>()

    // テクスチャ配列
    val textures = IntArray(2)

    // ---------------------------------------------
    // ライトを視点とみなした場合の変換行列として使う
    // ---------------------------------------------
    // テクスチャ座標変換行列
    private val matTex = FloatArray(16)
    // ビュー変換行列(ライト視点)
    private val matV4L = FloatArray(16)
    // プロジェクション変換行列(ライト視点)
    private val matP4L = FloatArray(16)
    // ビュー×プロジェクション×テクスチャ座標変換行列(ライト視点)
    private val matVPT4L = FloatArray(16)

    // 光源位置補正用係数(0～20)
    // ライトの位置を原点から遠ざけると、投影されるテクスチャの範囲が大きくなる
    var k = 10f

    init {
        // ビットマップをロード
        bmpArray.clear()
        val bmp0 = BitmapFactory.decodeResource(ctx.resources, R.drawable.texture_w49)
        bmpArray.add(bmp0)

        // -------------------------------------------------------
        // テクスチャ変換用行列
        // -------------------------------------------------------
        // http://asura.iaigiri.com/OpenGL/gl45.html
        // -------------------------------------------------------
        matTex[0]  = 0.5f;  matTex[1]  =    0f;  matTex[2]  = 0f;  matTex[3]  = 0f;
        matTex[4]  =   0f;  matTex[5]  = -0.5f;  matTex[6]  = 0f;  matTex[7]  = 0f;
        matTex[8]  =   0f;  matTex[9]  =    0f;  matTex[10] = 1f;  matTex[11] = 0f;
        matTex[12] = 0.5f;  matTex[13] =  0.5f;  matTex[14] = 0f;  matTex[15] = 1f;
    }

    override fun onDrawFrame(gl: GL10?) {
        // 回転角度
        angle[0] =(angle[0]+1)%360
        val t0 = angle[0].toFloat()

        // canvasを初期化
        GLES20.glClearColor(0.0f, 0.7f, 0.7f, 1.0f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // ビュー×プロジェクション座標変換行列
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,100f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,ratio,0.1f,150f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // テクスチャのバインド
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0])

        // ライトの距離を調整
        //  k = 0 ～ 20
        vecLight[0] = -k
        vecLight[1] =  k
        vecLight[2] =  k

        // ライトから見たビュー座標変換行列
        Matrix.setLookAtM(matV4L,0,
                vecLight[0],vecLight[1],vecLight[2],
                vecCenter[0],vecCenter[1],vecCenter[2],
                vecLightUp[0],vecLightUp[1],vecLightUp[2])

        // ライトから見たプロジェクション座標変換行列
        Matrix.perspectiveM(matP4L,0,90f,1f,0.1f,150f)

        // ライトから見た座標変換行列を掛け合わせ
        // ビュー×プロジェクション×テクスチャ座標変換行列を求める
        val matPT = FloatArray(16)
        Matrix.multiplyMM(matPT,0,matTex,0,matP4L,0)
        Matrix.multiplyMM(matVPT4L,0,matPT,0,matV4L,0)

        // -------------------------------------------------------
        // トーラス描画(10個)
        // -------------------------------------------------------
        (0..9).forEach { i ->
            val trans = FloatArray(3)
            trans[0] = ((i%5).toFloat()-2f)*7f
            trans[1] = (i/5).toFloat()*7f-5f
            trans[2] = ((i%5).toFloat()-2f)*5f

            val angle = (angle[0]+i*36)%360
            val t = angle.toFloat()

            Matrix.setIdentityM(matM,0)
            Matrix.translateM(matM,0,trans[0],trans[1],trans[2])
            Matrix.rotateM(matM,0,t,1f,1f,0f)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            Matrix.invertM(matI,0,matM,0)
            shader.draw(modelTorus,boTorus,matM,matVPT4L,matMVP,matI,vecLight,0)
        }

        // 板ポリゴンの描画(底面)
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,0f,-10f,0f)
        Matrix.scaleM(matM,0,20f,0f,20f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        Matrix.invertM(matI,0,matM,0)
        shader.draw(modelBoard,boBoard,matM,matVPT4L,matMVP,matI,vecLight,0)

        // 板ポリゴンの描画(奥面)
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,0f,10f,-20f)
        Matrix.rotateM(matM,0,90f,1f,0f,0f)
        Matrix.scaleM(matM,0,20f,0f,20f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        Matrix.invertM(matI,0,matM,0)
        shader.draw(modelBoard,boBoard,matM,matVPT4L,matMVP,matI,vecLight,0)

        // 板ポリゴンの描画(右脇面)
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,20f,10f,0f)
        Matrix.rotateM(matM,0,90f,0f,0f,1f)
        Matrix.scaleM(matM,0,20f,0f,20f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        Matrix.invertM(matI,0,matM,0)
        shader.draw(modelBoard,boBoard,matM,matVPT4L,matMVP,matI,vecLight,0)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        ratio = width.toFloat()/height.toFloat()

        renderW = width
        renderH = height
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // 深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)

        // テクスチャに使うビットマップをロード
        GLES20.glGenTextures(1,textures,0)
        MyGLES20Func.createTexture(0,textures,bmpArray[0])

        // シェーダ
        shader = WV049Shader()
        shader.loadShader()

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
                "pattern" to 49f
        ))

        // VBO(トーラス)
        boTorus = ES20VBOIpnc()
        boTorus.makeVIBO(modelTorus)

        // VBO(板ポリゴン)
        boBoard = ES20VBOIpnc()
        boBoard.makeVIBO(modelBoard)

        // 光源座標
        vecLight[0] = -10f
        vecLight[1] =  10f
        vecLight[2] =  10f

        // ライトビューの上方向
        vecLightUp[0] =  0.577f
        vecLightUp[1] =  0.577f
        vecLightUp[2] = -0.577f
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
        boTorus.deleteVIBO()
        boBoard.deleteVIBO()
        shader.deleteShader()

        GLES20.glDeleteTextures(textures.size,textures,0)
    }
}