package milu.kiriu2010.exdb1.opengl01.w019v

import android.content.Context
import android.opengl.GLES20
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.Matrix
import milu.kiriu2010.gui.model.d2.Square01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es20.wvbo.ES20VBOSimple01Shader
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs
import milu.kiriu2010.gui.vbo.es20.ES20VBOIpc
import milu.kiriu2010.math.MyMathUtil

// ---------------------------------------------
// カリングと深度テスト:VBOあり
// ---------------------------------------------
// https://wgld.org/d/webgl/w019.html
// ---------------------------------------------
class WV019Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画モデル
    private lateinit var model: Square01Model

    // シェーダ
    private lateinit var shader: ES20VBOSimple01Shader

    // VBO
    private lateinit var bo: ES20VBOAbs

    var culling = false
    var frontFace = false
    var depthTest = false

    override fun onDrawFrame(gl: GL10) {
        // 回転角度
        angle[0] =(angle[0]+1)%360
        val t0 = angle[0].toFloat()

        // canvasを初期化
        // canvasを初期化する色を設定する
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        // canvasを初期化する際の深度を設定する
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        when (culling) {
            true -> GLES20.glEnable(GLES20.GL_CULL_FACE)
            false -> GLES20.glDisable(GLES20.GL_CULL_FACE)
        }

        when (frontFace) {
            true -> GLES20.glFrontFace(GLES20.GL_CW)
            false -> GLES20.glFrontFace(GLES20.GL_CCW)
        }

        when (depthTest) {
            true -> GLES20.glEnable(GLES20.GL_DEPTH_TEST)
            false -> GLES20.glDisable(GLES20.GL_DEPTH_TEST)
        }

        // ビュー×プロジェクション座標変換行列
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        /*
        // プリミティブをアニメーション
        // 経過秒から回転角度を求める(10秒/周)
        val time = SystemClock.uptimeMillis() % 10000L
        val angleInDegrees = 360.0f / 10000.0f * time.toInt()
        */

        val x = MyMathUtil.cosf(t0) * 1.5f
        val z = MyMathUtil.sinf(t0) * 1.5f

        // ---------------------------------------------------
        // １つ目のモデル
        // X軸による回転
        // ---------------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,x,0f,z)
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shader.draw(model,bo,matMVP)

        // ---------------------------------------------------
        // ２つ目のモデル
        // Y軸による回転
        // ---------------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-x,0f,-z)
        Matrix.rotateM(matM,0,t0,0f,1f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shader.draw(model,bo,matMVP)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat()/height.toFloat()

        /*
        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1f, 1f, 1f, 10f)
        */
        // プロジェクション座標変換行列
        Matrix.perspectiveM(matP, 0, 45f,ratio,0.1f,100f)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        // シェーダ
        shader = ES20VBOSimple01Shader()
        shader.loadShader()

        // モデル生成
        model = Square01Model()
        model.createPath()

        // VBO生成
        bo = ES20VBOIpc()
        bo.makeVIBO(model)

        // カメラの位置
        vecEye[0] = 0f
        vecEye[1] = 0f
        vecEye[2] = 5f

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
