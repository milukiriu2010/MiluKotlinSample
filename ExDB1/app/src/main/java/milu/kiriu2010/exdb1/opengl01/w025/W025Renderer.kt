package milu.kiriu2010.exdb1.opengl01.w025

import android.content.Context
import android.opengl.GLES20
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.Matrix
import milu.kiriu2010.gui.model.Sphere01Model
import milu.kiriu2010.gui.model.Torus01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.PointLight01Shader
import milu.kiriu2010.math.MyMathUtil

// ---------------------------------------------------
// 点光源によるライティング
// ---------------------------------------------------
// https://wgld.org/d/webgl/w025.html
// ---------------------------------------------------
class W025Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画モデル(トーラス)
    private lateinit var modelTorus: Torus01Model
    // 描画モデル(球体)
    private lateinit var modelSphere: Sphere01Model

    // シェーダ(点光源)
    private lateinit var shader: PointLight01Shader

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

        // ビュー×プロジェクション座標変換行列
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        val tx = MyMathUtil.cosf(t0) * 3.5f
        val ty = MyMathUtil.sinf(t0) * 3.5f
        val tz = MyMathUtil.sinf(t0) * 3.5f

        // モデル座標変換行列の生成
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,tx,-ty,-tz)
        Matrix.rotateM(matM,0,t0,0f,1f,1f)
        Matrix.invertM(matI,0,matM,0)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)

        // モデル描画(トーラス)
        shader.draw(modelTorus,matMVP,matM,matI,vecLight,vecEye,vecAmbientColor)

        // モデル座標返還行列の生成
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-tx,ty,tz)
        Matrix.rotateM(matM,0,t0,0f,1f,1f)
        Matrix.invertM(matI,0,matM,0)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)

        // モデル描画(球体)
        shader.draw(modelSphere,matMVP,matM,matI,vecLight,vecEye,vecAmbientColor)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat()/height.toFloat()

        Matrix.perspectiveM(matP,0,45f,ratio,0.1f,100f)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        // カリングと深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        GLES20.glEnable(GLES20.GL_CULL_FACE)

        // カメラの位置
        vecEye[0] = 0f
        vecEye[1] = 0f
        vecEye[2] = 20f

        // 環境光
        vecAmbientColor[0] = 0.1f
        vecAmbientColor[1] = 0.1f
        vecAmbientColor[2] = 0.1f
        vecAmbientColor[3] = 1f

        // ビュー座標変換行列
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])

        // シェーダ(点光源)
        shader = PointLight01Shader()
        shader.loadShader()

        // 描画モデル(トーラス)
        modelTorus = Torus01Model()
        modelTorus.createPath(mapOf(
                "row"     to 32f,
                "column"  to 32f,
                "iradius" to 0.5f,
                "oradius" to 1.5f,
                "colorR"  to 0.75f,
                "colorG"  to 0.25f,
                "colorB"  to 0.25f,
                "colorA"  to 1f
        ))

        // 描画モデル(球体)
        modelSphere = Sphere01Model()
        modelSphere.createPath(mapOf(
                "row"  to 32f,
                "column" to 32f,
                "radius" to 2f,
                "colorR"  to 0.25f,
                "colorG"  to 0.25f,
                "colorB"  to 0.75f,
                "colorA"  to 1f
        ))
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
    }
}
