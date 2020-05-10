package milu.kiriu2010.exdb1.mgl01.vbo01

import android.content.Context
import android.opengl.GLES20
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.Matrix
import milu.kiriu2010.gui.model.*
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.math.MyMathUtil

// ---------------------------------------
// VBOで立方体を描画
// 思ったようにいってないが、虹色になってる
// OpenGL ES 2.0
// ---------------------------------------
// http://junkcode.aakaka.com/archives/211
// https://gist.github.com/axm/7463023#file-fborenderer-java
// http://hiroba.main.jp/studio/gles20multitrianglebyrandomcoloredlines/
// ---------------------------------------
class ES20VBO01Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画モデル(静止している立方体)
    private lateinit var modelStill: MgModelAbs
    // 描画モデル(動く立方体)
    private lateinit var modelMotion: MgModelAbs
    // 座標軸モデル
    private lateinit var modelAxis: MgModelAbs

    // VBO/IBO(静止モデル)
    private lateinit var boStill: ES20VBO01viBO
    // VBO/IBO(動く立方体)
    private lateinit var boMotion: ES20VBO01viBO
    // VBO/IBO(座標軸モデル)
    private lateinit var boAxis: ES20VBO01viBO

    // シェーダ(特殊効果なし)
    private lateinit var shaderSimple: ES20VBO01Shader

    override fun onDrawFrame(gl: GL10) {

        // canvasを初期化
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        angle[0] =(angle[0]+1)%360
        val t0 = angle[0].toFloat()

        val x = MyMathUtil.cosf(t0)
        val y = MyMathUtil.sinf(t0)

        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,20f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,1f,0.1f,100f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // ----------------------------------------------
        // 静止したモデルを描画
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // モデル×ビュー×プロジェクション
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.drawVIBO(modelStill,boStill,matMVP,GLES20.GL_LINES)

        // ----------------------------------------------
        // 回転するモデルを描画(右上)
        // ----------------------------------------------
        // モデルを単位行列にする
        Matrix.setIdentityM(matM,0)
        rotateModel0(matM,x,y,0f,t0)
        //rotateModel0(matA.copyOf(),-1f,-1f,-1f,t0)
        //rotateModel0(matA.copyOf(),0f,-1f, 1f,t0)
        //rotateModel0(matA.copyOf(),0f, 1f,-1f,t0)
        //rotateModel0(matA.copyOf(),0f, 1f, 1f,t0)

        // ----------------------------------------------
        // 座標軸を描画
        // ----------------------------------------------
        GLES20.glLineWidth(5f)
        Matrix.setIdentityM(matM,0)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.drawVIBO(modelAxis,boAxis,matMVP,GLES20.GL_LINES)
    }

    private fun rotateModel0(matA: FloatArray, x: Float, y: Float, z: Float, t: Float): FloatArray {
        // モデルを移動する(元の位置に戻す)
        Matrix.translateM(matA,0,-x,-y,-z)
        // モデルを軸を中心に回転させる
        val axisX = if ( x == 0f ) 1f else 0f
        val axisY = if ( y == 0f ) 1f else 0f
        val axisZ = if ( z == 0f ) 1f else 0f
        Matrix.rotateM(matA,0,t,axisX,axisY,axisZ)
        // モデルを移動する(軸上にずらす)
        Matrix.translateM(matA,0,x,y,z)
        // モデル×ビュー×プロジェクション
        Matrix.multiplyMM(matMVP,0,matVP,0,matA,0)
        shaderSimple.drawVIBO(modelMotion,boMotion,matMVP)

        return matA
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        // カリングと深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        GLES20.glEnable(GLES20.GL_CULL_FACE)

        // シェーダ(特殊効果なし)
        shaderSimple = ES20VBO01Shader()
        shaderSimple.loadShader()

        // 描画モデル(静止している立方体)
        modelStill = Cube01Model()
        modelStill.createPath(mapOf(
                "pattern" to 20f,
                "colorR" to 1f,
                "colorG" to 0f,
                "colorB" to 0f,
                "colorA" to 1f
        ))

        // 描画モデル(動く立方体)
        modelMotion = Cube01Model()
        modelMotion.createPath()

        // 座標軸モデル
        modelAxis = Axis01Model()
        modelAxis.createPath( mapOf("scale" to 3f))

        // VBO/IBO(静止モデル)
        boStill = ES20VBO01viBO()
        boStill.makeVIBO(modelStill)

        // VBO/IBO(動く立方体)
        boMotion = ES20VBO01viBO()
        boMotion.makeVIBO(modelMotion)

        // VBO/IBO(座標軸モデル)
        boAxis = ES20VBO01viBO()
        boAxis.makeVIBO(modelAxis)
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
    }
}
