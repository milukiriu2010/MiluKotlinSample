package milu.kiriu2010.exdb1.opengl04.w042

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.view.MotionEvent
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.basic.MyQuaternion
import milu.kiriu2010.gui.model.Sphere01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.sqrt

// バンプマッピング
class W042Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画オブジェクト(球体)
    private lateinit var drawObjSphere: Sphere01Model

    // シェーダ
    private lateinit var shader: W042Shader

    // 画面縦横比
    var ratio: Float = 0f

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

    override fun onDrawFrame(gl: GL10?) {
        // 回転角度
        angle[0] =(angle[0]+2)%360
        angle[1] =(angle[1]+1)%360
        val t1 = angle[0].toFloat()
        val t2 = angle[1].toFloat()

        // canvasを初期化
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // ビュー×プロジェクション座標変換行列
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,5f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,ratio,0.1f,100f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // 球体をレンダリング
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textures[0])
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,-t1,0f,1f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        Matrix.invertM(matI,0,matM,0)
        shader.draw(drawObjSphere,matM,matMVP,matI,vecLight,vecEye,0)
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

        // シェーダプログラム登録
        shader = W042Shader()
        shader.loadShader()

        // テクスチャ作成し、idをtexturesに保存
        GLES20.glGenTextures(1,textures,0)
        MyGLFunc.checkGlError("glGenTextures")
        MyGLFunc.createTexture(0,textures,bmpArray[0])

        // モデル生成(球体)
        drawObjSphere = Sphere01Model()
        drawObjSphere.createPath(mapOf(
                "row" to 32f,
                "column" to 32f,
                "radius" to 1f
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