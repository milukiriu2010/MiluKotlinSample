package milu.kiriu2010.exdb1.opengl03.w033v

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import milu.kiriu2010.gui.model.Torus01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es20.nvbo.ES20PointLight01Shader
import milu.kiriu2010.gui.shader.es20.wvbo.ES20VBOPointLight01Shader
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs
import milu.kiriu2010.gui.vbo.es20.ES20VBOIpnc
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// ---------------------------------------
// クォータニオン:VBOあり
//   マウス座標による回転
// OpenGL ES 2.0
// ---------------------------------------
// https://wgld.org/d/webgl/w033.html
// ---------------------------------------
class WV033Renderer(ctx: Context): MgRenderer(ctx) {

    // モデル(トーラス)
    private lateinit var model: Torus01Model

    // シェーダ(点光源)
    private lateinit var shader: ES20VBOPointLight01Shader

    // VBO(トーラス)
    private lateinit var bo: ES20VBOAbs

    // 画面縦横比
    var ratio: Float = 0f

    override fun onDrawFrame(gl: GL10?) {
        // 回転角度
        angle[0] =(angle[0]+1)%360
        val t0 = angle[0].toFloat()

        // canvasを初期化
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // ビュー×プロジェクション
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // モデルを単位行列にする
        Matrix.setIdentityM(matM,0)
        // モデル座標変換行列×クォータニオンが適用された座標変換行列
        val matQ = qtnNow.toMatIV()
        Matrix.multiplyMM(matM,0,matM,0,matQ,0)
        // モデルを"Y軸"を中心に回転する
        Matrix.rotateM(matM, 0, t0, 0f, 1f, 0f)
        // モデル×ビュー×プロジェクション
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)

        // モデル座標変換行列から逆行列を生成
        Matrix.invertM(matI,0,matM,0)

        // モデルを描画
        shader.draw(model,bo,matMVP,matM,matI,vecLight,vecEye,vecAmbientColor)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        ratio = width.toFloat()/height.toFloat()

        Matrix.perspectiveM(matP,0,60f,ratio,0.1f,100f)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // カリングと深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        GLES20.glEnable(GLES20.GL_CULL_FACE)

        // シェーダ
        shader = ES20VBOPointLight01Shader()
        shader.loadShader()

        // モデル生成
        model = Torus01Model()
        model.createPath()

        // VBO(トーラス)
        bo = ES20VBOIpnc()
        bo.makeVIBO(model)

        // カメラの初期位置
        vecEye[0] = 0f
        vecEye[1] = 0f
        vecEye[2] = 10f

        // カメラの位置
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                0f, 0f, 0f,
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
        bo.deleteVIBO()
        shader.deleteShader()
    }
}