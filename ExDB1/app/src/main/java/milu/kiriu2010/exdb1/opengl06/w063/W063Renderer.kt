package milu.kiriu2010.exdb1.opengl06.w063

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import milu.kiriu2010.gui.model.Sphere01Model
import milu.kiriu2010.gui.model.Torus01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -----------------------------------------------------
// 半球ライティング:VBOなし
// OpenGL ES 2.0
// -----------------------------------------------------
// https://wgld.org/d/webgl/w062.html
// -----------------------------------------------------
class W063Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画オブジェクト(トーラス)
    private lateinit var modelTorus: Torus01Model
    // 描画オブジェクト(球体)
    private lateinit var modelSphere: Sphere01Model

    // シェーダ(メイン)
    private lateinit var mainShader: W063ShaderMain

    // 画面縦横比
    var ratio: Float = 1f

    // 映り込み係数
    //   0.0 - 1.0
    var u_alpha = 0.5f

    // 天空の向き
    var vecSky = floatArrayOf(0f,1f,0f)

    // 天空の色
    var colorSky = floatArrayOf(0f,0f,1f,1f)

    // 地面の色
    var colorGround = floatArrayOf(0.3f,0.2f,0.1f,1f)


    override fun onDrawFrame(gl: GL10?) {
        angle[0] =(angle[0]+1)%360
        val t0 = angle[0].toFloat()

        // ビュー×プロジェクション座標変換行列
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,5f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,ratio,0.1f,10f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // canvasを初期化
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // レンダリング(トーラス)
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.rotateM(matM,0,t0,0f,1f,0f)
        Matrix.rotateM(matM,0,90f,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        Matrix.invertM(matI,0,matM,0)
        mainShader.draw(modelTorus,matM,matMVP,matI,
                vecSky,vecLight,vecEye, colorSky, colorGround )

        // レンダリング(球体)
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        Matrix.invertM(matI,0,matM,0)
        mainShader.draw(modelSphere,matM,matMVP,matI,
                vecSky,vecLight,vecEye, colorSky, colorGround )
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        ratio = width.toFloat()/height.toFloat()

        renderW = width
        renderH = height
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // カリングと深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        GLES20.glEnable(GLES20.GL_CULL_FACE)

        // シェーダ(メイン)
        mainShader = W063ShaderMain()
        mainShader.loadShader()

        // モデル生成(トーラス)
        modelTorus = Torus01Model()
        modelTorus.createPath(mapOf(
                "row"     to 32f,
                "column"  to 32f,
                "iradius" to 0.25f,
                "oradius" to 0.5f,
                "colorR" to 0.7f,
                "colorG" to 0.7f,
                "colorB" to 0.7f,
                "colorA" to 1f
        ))

        // モデル生成(球体)
        modelSphere = Sphere01Model()
        modelSphere.createPath(mapOf(
                "row" to 32f,
                "column" to 32f,
                "radius" to 0.75f,
                "colorR" to 0.7f,
                "colorG" to 0.7f,
                "colorB" to 0.7f,
                "colorA" to 1f
        ))

        // 光源位置
        vecLight[0] = -0.577f
        vecLight[1] =  0.577f
        vecLight[2] =  0.577f
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
    }
}