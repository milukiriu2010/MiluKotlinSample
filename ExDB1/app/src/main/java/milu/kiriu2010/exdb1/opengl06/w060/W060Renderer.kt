package milu.kiriu2010.exdb1.opengl06.w060

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import milu.kiriu2010.gui.color.MgColor
import milu.kiriu2010.gui.model.Torus01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// --------------------------------------------------------------
// 距離フォグ
//   カメラからの距離に応じて、
//   あたかも視界が遮られているかのようにモデルに色づけをする
// --------------------------------------------------------------
// https://wgld.org/d/webgl/w060.html
// --------------------------------------------------------------
class W060Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画オブジェクト(トーラス)
    private lateinit var modelTorus: Torus01Model

    // シェーダ(メイン)
    private lateinit var mainShader: W060ShaderMain

    // 画面縦横比
    var ratio: Float = 0f

    // 完全にフォグが掛かりモデルが見えなくなってしまう位置
    var u_fogStart = 0.5f
    // 完全にフォグが掛かりモデルが見えなくなってしまう位置
    var u_fogEnd   = 1f

    override fun onDrawFrame(gl: GL10?) {
        angle[0] =(angle[0]+1)%360

        // 各トーラスの傾き
        val angleF = FloatArray(10)
        (0..9).forEach { i ->
            angleF[i] = ((angle[0]+40*i)%360).toFloat()
        }

        // ビュー×プロジェクション座標変換行列
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,15f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,90f,ratio,0.1f,30f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // フレームバッファを初期化
        GLES20.glClearColor(0.75f, 0.75f, 0.75f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // -------------------------------------------------------
        // モデルをレンダリング(トーラス)(8個)
        // -------------------------------------------------------
        GLES20.glCullFace(GLES20.GL_BACK)
        (0..8).forEach { i ->
            val amb = MgColor.hsva(i*40,1f,1f,1f)
            Matrix.setIdentityM(matM,0)
            Matrix.rotateM(matM,0,i.toFloat()*40f,0f,1f,0f)
            Matrix.translateM(matM,0,0f,0f,10f)
            Matrix.rotateM(matM,0,angleF[i],1f,1f,0f)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            Matrix.invertM(matI,0,matM,0)
            mainShader.draw(modelTorus,matM,matMVP,matI,vecLight,vecEye,amb.toFloatArray(),
                    u_fogStart,u_fogEnd, floatArrayOf(0.75f,0.75f,0.75f,1f) )
        }
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
        mainShader = W060ShaderMain()
        mainShader.loadShader()

        // モデル生成(トーラス)
        modelTorus = Torus01Model()
        modelTorus.createPath(mapOf(
                "row"     to 32f,
                "column"  to 32f,
                "iradius" to 0.75f,
                "oradius" to 1.75f,
                "colorR"  to 1f,
                "colorG"  to 1f,
                "colorB"  to 1f,
                "colorA"  to 1f
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