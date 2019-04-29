package milu.kiriu2010.exdb1.opengl05.w049

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.opengl.Matrix
import android.view.MotionEvent
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.basic.MyQuaternion
import milu.kiriu2010.gui.model.Board01Model
import milu.kiriu2010.gui.model.Torus01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import java.lang.RuntimeException
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.sqrt

// 射影テクスチャマッピング
class W049Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画オブジェクト(トーラス)
    private lateinit var drawObjTorus: Torus01Model
    // 描画オブジェクト(板ポリゴン)
    private lateinit var drawObjBoard: Board01Model

    // シェーダ
    private lateinit var shader: W049Shader

    // 画面縦横比
    var ratio: Float = 0f

    // ライトビューの上方向
    val vecLightUp = floatArrayOf(0.577f,0.577f,-0.577f)

    // ビットマップ配列
    val bmpArray = arrayListOf<Bitmap>()

    // テクスチャ配列
    val textures = IntArray(2)

    // モデル変換行列(テクスチャ用)
    private val matM4Tex = FloatArray(16)
    // ビュー変換行列(テクスチャ用)
    private val matV4Tex = FloatArray(16)
    // プロジェクション変換行列(テクスチャ用)
    private val matP4Tex = FloatArray(16)
    // モデル×ビュー×プロジェクション行列(テクスチャ用)
    private val matMVP4Tex = FloatArray(16)

    // ライトの位置補正用係数
    var k = 10f

    override fun onDrawFrame(gl: GL10?) {
        // 回転角度
        angle[0] =(angle[0]+1)%360
        val t1 = angle[0].toFloat()

        // canvasを初期化
        GLES20.glClearColor(0.0f, 0.7f, 0.7f, 1.0f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // ビュー×プロジェクション座標変換行列
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,70f))
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

        // -------------------------------------------------------
        // テクスチャ変換用行列
        // -------------------------------------------------------
        matM4Tex[0] = 0.5f
        matM4Tex[1] = 0f
        matM4Tex[2] = 0f
        matM4Tex[3] = 0f
        matM4Tex[4] = 0f
        matM4Tex[5] = -0.5f
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

        // ライトの距離を調整
        vecLight[0] = -k
        vecLight[1] = k
        vecLight[2] = k

        // ライトから見たビュー座標変換行列
        Matrix.setLookAtM(matV4Tex,0,
                vecLight[0],vecLight[1],vecLight[2],
                vecCenter[0],vecCenter[1],vecCenter[2],
                vecLightUp[0],vecLightUp[1],vecLightUp[2])

        // ライトから見たプロジェクション座標変換行列
        Matrix.perspectiveM(matP4Tex,0,90f,1f,0.1f,150f)

        // ライトから見た座標変換行列を掛け合わせる
        Matrix.multiplyMM(matMVP4Tex,0,matM4Tex,0,matP4Tex,0)
        Matrix.multiplyMM(matM4Tex,0,matMVP4Tex,0,matV4Tex,0)

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
            shader.draw(drawObjTorus,matM,matM4Tex,matMVP,matI,vecLight,0)
        }

        // 板ポリゴンの描画(底面)
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,0f,-10f,0f)
        Matrix.scaleM(matM,0,20f,0f,20f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        Matrix.invertM(matI,0,matM,0)
        shader.draw(drawObjBoard,matM,matM4Tex,matMVP,matI,vecLight,0)

        // 板ポリゴンの描画(奥面)
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,0f,10f,-20f)
        Matrix.rotateM(matM,0,90f,1f,0f,0f)
        Matrix.scaleM(matM,0,20f,0f,20f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        Matrix.invertM(matI,0,matM,0)
        shader.draw(drawObjBoard,matM,matM4Tex,matMVP,matI,vecLight,0)

        // 板ポリゴンの描画(右脇面)
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,20f,10f,0f)
        Matrix.rotateM(matM,0,90f,0f,0f,1f)
        Matrix.scaleM(matM,0,20f,0f,20f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        Matrix.invertM(matI,0,matM,0)
        shader.draw(drawObjBoard,matM,matM4Tex,matMVP,matI,vecLight,0)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        ratio = width.toFloat()/height.toFloat()

        renderW = width
        renderH = height
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // canvasを初期化する色を設定する
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // canvasを初期化する際の深度を設定する
        GLES20.glClearDepthf(1f)

        // カリングと深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)

        // テクスチャに使うビットマップをロード
        GLES20.glGenTextures(1,textures,0)
        MyGLFunc.createTexture(0,textures,bmpArray[0])

        // シェーダプログラム登録
        shader = W049Shader()
        shader.loadShader()

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
        drawObjBoard.createPath(mapOf(
                "pattern" to 49f
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
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
    }
}