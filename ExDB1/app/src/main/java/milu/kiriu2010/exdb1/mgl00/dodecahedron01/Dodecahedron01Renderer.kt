package milu.kiriu2010.exdb1.mgl00.dodecahedron01

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.Matrix
import milu.kiriu2010.gui.model.Dodecahedron01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.DirectionalLight01Shader
import milu.kiriu2010.gui.shader.Simple01Shader
import milu.kiriu2010.math.MyMathUtil


class Dodecahedron01Renderer: MgRenderer() {
    // 描画モデル
    private lateinit var drawObj: Dodecahedron01Model

    // シェーダ
    private lateinit var shaderSimple: Simple01Shader
    private lateinit var shaderDirectionalLight: DirectionalLight01Shader

    override fun onDrawFrame(gl: GL10) {
        // 深度テスト
        if ( isDepth ) {
            GLES20.glEnable(GLES20.GL_DEPTH_TEST)
            GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        }
        else {
            GLES20.glDisable(GLES20.GL_DEPTH_TEST)
        }

        // カリング
        // カリングをonにすると、奥が広がってるようにみえる。なぜ？
        if ( isCull ) {
            GLES20.glEnable(GLES20.GL_CULL_FACE)
        }
        else {
            GLES20.glDisable(GLES20.GL_CULL_FACE)
        }

        // canvasを初期化
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        if ( rotateSwitch ) {
            angle1 = (angle1 + 1) % 360
        }
        val t1 = angle1.toFloat()

        val x = MyMathUtil.cosf(t1)
        val y = MyMathUtil.sinf(t1)

        // ビュー×プロジェクション
        Matrix.multiplyMM(matT,0,matP,0,matV,0)

        // モデルを単位行列にする
        Matrix.setIdentityM(matM,0)
        // モデルを平行移動する
        Matrix.translateM(matM,0,x,y,0f)
        // モデルを"Y軸"を中心に回転する
        //if ( rotateSwitch ) {
            Matrix.rotateM(matM,0,t1,0f,1f,0f)
        //}
        // モデルを"X軸45度Y軸45度/Z軸45度"を中心に回転する
        //Matrix.rotateM(matM,0,t1,1f,1f,1f)
        // モデル×ビュー×プロジェクション
        Matrix.multiplyMM(matMVP,0,matT,0,matM,0)

        // モデル座標変換行列から逆行列を生成
        Matrix.invertM(matI,0,matM,0)

        // モデルを描画
        when (shaderSwitch) {
            0 -> shaderSimple.draw(drawObj,matMVP)
            1 -> shaderDirectionalLight.draw(drawObj,matMVP,matI,vecLight)
            else -> shaderSimple.draw(drawObj,matMVP)
        }
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat()/height.toFloat()

        Matrix.perspectiveM(matP,0,60f,ratio,0.1f,100f)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        // canvasを初期化する色を設定する
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // canvasを初期化する際の深度を設定する
        GLES20.glClearDepthf(1f)

        // カリングと深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        GLES20.glEnable(GLES20.GL_CULL_FACE)

        // カメラの位置
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])

        // シェーダプログラム登録(エフェクトなし)
        shaderSimple = Simple01Shader()
        shaderSimple.loadShader()

        // シェーダプログラム登録(平行光源)
        shaderDirectionalLight = DirectionalLight01Shader()
        shaderDirectionalLight.loadShader()

        // モデル生成
        drawObj = Dodecahedron01Model()
    }
}
