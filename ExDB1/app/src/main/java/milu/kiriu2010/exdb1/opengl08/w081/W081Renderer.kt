package milu.kiriu2010.exdb1.opengl08.w081

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import milu.kiriu2010.gui.model.Sphere01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.math.MyMathUtil
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// ------------------------------------
// VBOを逐次更新
// ------------------------------------
// https://wgld.org/d/webgl/w081.html
// ------------------------------------
class W081Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画オブジェクト(球体)
    private lateinit var modelSphere: Sphere01Model

    // シェーダ
    private lateinit var shader: W081Shader

    // 画面縦横比
    var ratio: Float = 1f

    // 描画点サイズ
    // 10.0-40.0
    var u_pointSize = 25f

    override fun onDrawFrame(gl: GL10?) {
        angle[0] =(angle[0]+1)%360
        val t0 = angle[0].toFloat()
        val scale = MyMathUtil.cosf(t0) + 2.0f

        // モデル生成(球体)
        modelSphere = Sphere01Model()
        modelSphere.createPath(mapOf(
                "row"     to 32f,
                "column"  to 32f,
                "scale"   to scale,
                "radius"  to 1f
        ))

        // フレームバッファを初期化
        GLES20.glClearColor(0.7f,0.7f,0.7f,1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // クォータニオンを行列に適用
        val matQ = qtnNow.toMatIV()

        // ビュー×プロジェクション座標変換行列
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.multiplyMM(matV,0,matV,0,matQ,0)
        Matrix.perspectiveM(matP,0,90f,ratio,0.1f,15f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)


        // -------------------------------------------------------
        // モデル描画
        // -------------------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shader.draw(modelSphere,matMVP,u_pointSize)
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

        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFuncSeparate(GLES20.GL_ONE,GLES20.GL_ONE,GLES20.GL_ONE,GLES20.GL_ONE)

        // シェーダ
        shader = W081Shader()
        shader.loadShader()

        // 視点座標
        vecEye[0] = 0f
        vecEye[1] = 0f
        vecEye[2] = 5f
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
    }
}