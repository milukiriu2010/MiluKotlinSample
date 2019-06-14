package milu.kiriu2010.exdb1.opengl01.w016v

import android.content.Context
import android.opengl.GLES20
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.Matrix
import milu.kiriu2010.gui.model.d2.Triangle01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es20.wvbo.ES20VBOSimple00Shader
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs
import milu.kiriu2010.gui.vbo.es20.ES20VBOpc

// ------------------------------------
// 複数モデルをレンダリング
// ------------------------------------
// https://wgld.org/d/webgl/w016.html
// ------------------------------------
class WV016Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画モデル
    private lateinit var model: Triangle01Model

    // シェーダ
    private lateinit var shader: ES20VBOSimple00Shader

    // VBO
    private lateinit var bo: ES20VBOAbs

    override fun onDrawFrame(gl: GL10) {
        // canvasを初期化
        // canvasを初期化する色を設定する
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        // canvasを初期化する際の深度を設定する
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // ビュー×プロジェクション座標変換行列
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // ---------------------------------------------------
        // １つ目のモデル
        // ---------------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,1.5f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shader.draw(model,bo,matMVP)

        // ---------------------------------------------------
        // ２つ目のモデル
        // ---------------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-1.5f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shader.draw(model,bo,matMVP)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat()/height.toFloat()

        // プロジェクション座標変換行列
        Matrix.perspectiveM(matP, 0, 90f,ratio,0.1f,100f)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        // シェーダプログラム登録
        shader = ES20VBOSimple00Shader()
        shader.loadShader()

        // モデル生成
        model = Triangle01Model()
        model.createPath()

        // VBO生成
        bo = ES20VBOpc()
        bo.makeVIBO(model)

        // カメラの位置
        vecEye[0] = 0f
        vecEye[1] = 0f
        vecEye[2] = 3f

        // ビュー座標変換行列
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
        bo.deleteVIBO()
        shader.deleteShader()
    }
}
