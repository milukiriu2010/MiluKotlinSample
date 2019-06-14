package milu.kiriu2010.exdb1.mgl01.vbo02

import android.content.Context
import android.opengl.GLES20
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.Matrix
import milu.kiriu2010.gui.model.d2.Triangle01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs
import milu.kiriu2010.gui.vbo.es20.ES20VBOp

// ----------------------------------------------
// VBOで描画
// ----------------------------------------------
// 三角形(色なし)
// ----------------------------------------------
class ES20VBO02Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画モデル
    private lateinit var model: Triangle01Model

    // シェーダ
    private lateinit var shader: ES20VBO02Shader

    // VBO
    private lateinit var bo: ES20VBOAbs

    override fun onDrawFrame(gl: GL10) {
        // デフォルトバッファを初期化
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        angle[0] =(angle[0]+1)%360
        val t0 = angle[0].toFloat()

        Matrix.setIdentityM(matM,0)
        Matrix.setRotateM(matM, 0, t0, 0f, 0f, 1.0f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)

        // Draw triangle
        shader.drawVIBO(model,bo,matMVP)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat()/height.toFloat()

        // プロジェクション座標変換行列
        Matrix.perspectiveM(matP, 0, 90f,ratio,0.1f,100f)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        // シェーダ
        shader = ES20VBO02Shader()
        shader.loadShader()

        // モデル生成
        model = Triangle01Model()
        model.createPath()

        // VBO生成
        bo = ES20VBOp()
        bo.makeVIBO(model)

        // カメラの位置
        vecEye[0] = 0f
        vecEye[1] = 1f
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
