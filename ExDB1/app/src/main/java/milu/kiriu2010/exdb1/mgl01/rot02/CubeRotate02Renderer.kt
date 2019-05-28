package milu.kiriu2010.exdb1.mgl01.rot02

import android.content.Context
import android.opengl.GLES20
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.Matrix
import milu.kiriu2010.gui.model.*
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es20.ES20Simple01Shader
import milu.kiriu2010.math.MyMathUtil
import kotlin.math.sqrt


class CubeRotate02Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画モデル(静止している立方体)
    private lateinit var modelStill: MgModelAbs
    // 描画モデル(動く立方体)
    private lateinit var modelMotion: MgModelAbs
    // 座標軸モデル
    private lateinit var axisModel: MgModelAbs

    // シェーダ(特殊効果なし)
    private lateinit var shaderSimple: ES20Simple01Shader

    private val sqrt2 = sqrt(2f)

    override fun onDrawFrame(gl: GL10) {

        // canvasを初期化
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        angle[0] =(angle[0]+1)%360
        val t1 = angle[0].toFloat()

        val x = MyMathUtil.cosf(t1)
        val y = MyMathUtil.sinf(t1)

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
        shaderSimple.draw(modelStill,matMVP,GLES20.GL_LINES)

        // ----------------------------------------------
        // 回転するモデルを描画(右上)
        // ----------------------------------------------
        // モデルを単位行列にする
        Matrix.setIdentityM(matM,0)
        val matA = rotateModel0(matM,-1f,-1f,0f,t1)
        rotateModel0(matA.copyOf(),0f,-1f,-1f,t1)
        rotateModel0(matA.copyOf(),0f,-1f, 1f,t1)
        rotateModel0(matA.copyOf(),0f, 1f,-1f,t1)
        rotateModel0(matA.copyOf(),0f, 1f, 1f,t1)

        // 点で回転してしまう
        //rotateModel0A(matA,0f,-1f,-1f,t1)
        //rotateModel0(matA,0f,-1f,-1f,t1)
        /*
        rotateModel0(matA,0f,-1f, 1f,t1)
        rotateModel0(matA,0f, 1f,-1f,t1)
        rotateModel0(matA,0f, 1f, 1f,t1)
        */
        /*
        // モデルを単位行列にする
        Matrix.setIdentityM(matM,0)
        rotateModel1(matM,-1f,-1f,0f,t1,2)
        */
        /*
        // モデルを単位行列にする
        Matrix.setIdentityM(matM,0)
        // モデルを移動する(元の位置に戻す)
        Matrix.translateM(matM,0,1f,1f,0f)
        // モデルをZ軸を中心に回転させる
        Matrix.rotateM(matM,0,t1,0f,0f,1f)
        // モデルを移動する(上にずらす)
        Matrix.translateM(matM,0,-1f,-1f,0F)
        // モデル×ビュー×プロジェクション
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(modelMotion,matMVP)
        */

        /*
        // ----------------------------------------------
        // 回転するモデルを描画(右下)
        // ----------------------------------------------
        // モデルを単位行列にする
        Matrix.setIdentityM(matM,0)
        // モデルを移動する(元の位置に戻す)
        Matrix.translateM(matM,0,1f,-1f,0f)
        // モデルをZ軸を中心に回転させる
        Matrix.rotateM(matM,0,t1,0f,0f,1f)
        // モデルを移動する(上にずらす)
        Matrix.translateM(matM,0,-1f,1f,0F)
        // モデル×ビュー×プロジェクション
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(modelMotion,matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(左上)
        // ----------------------------------------------
        // モデルを単位行列にする
        Matrix.setIdentityM(matM,0)
        // モデルを移動する(元の位置に戻す)
        Matrix.translateM(matM,0,-1f,1f,0f)
        // モデルをZ軸を中心に回転させる
        Matrix.rotateM(matM,0,t1,0f,0f,1f)
        // モデルを移動する(上にずらす)
        Matrix.translateM(matM,0,1f,-1f,0F)
        // モデル×ビュー×プロジェクション
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(modelMotion,matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(左下)
        // ----------------------------------------------
        // モデルを単位行列にする
        Matrix.setIdentityM(matM,0)
        // モデルを移動する(元の位置に戻す)
        Matrix.translateM(matM,0,-1f,-1f,0f)
        // モデルをZ軸を中心に回転させる
        Matrix.rotateM(matM,0,t1,0f,0f,1f)
        // モデルを移動する(上にずらす)
        Matrix.translateM(matM,0,1f,1f,0f)
        // モデル×ビュー×プロジェクション
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(modelMotion,matMVP)
        */

        // ----------------------------------------------
        // 座標軸を描画
        // ----------------------------------------------
        GLES20.glLineWidth(5f)
        Matrix.setIdentityM(matM,0)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(axisModel,matMVP,GLES20.GL_LINES)
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
        shaderSimple.draw(modelMotion,matMVP)

        return matA
    }

    private fun rotateModel0A(matA: FloatArray, x: Float, y: Float, z: Float, t: Float): FloatArray {

        val matB = FloatArray(16)
        val matC = FloatArray(16)
        Matrix.setIdentityM(matB,0)
        // モデルを移動する(元の位置に戻す)
        Matrix.translateM(matB,0,-x,-y,-z)
        // モデルを軸を中心に回転させる
        val axisX = if ( x == 0f ) 1f else 0f
        val axisY = if ( y == 0f ) 1f else 0f
        val axisZ = if ( z == 0f ) 1f else 0f
        Matrix.rotateM(matB,0,t,axisX,axisY,axisZ)
        // モデルを移動する(軸上にずらす)
        Matrix.translateM(matB,0,x,y,z)
        Matrix.multiplyMM(matC,0,matB,0,matA,0)
        // モデル×ビュー×プロジェクション
        Matrix.multiplyMM(matMVP,0,matVP,0,matC,0)
        shaderSimple.draw(modelMotion,matMVP)

        return matA
    }

    private fun rotateModel01(l0: FloatArray, l1: FloatArray, ax1: FloatArray, t: Float) {

        val axis0 = FloatArray(3)
        axis0[0] = if ( l0[0] == 0f ) 1f else 0f
        axis0[1] = if ( l0[1] == 0f ) 1f else 0f
        axis0[2] = if ( l0[2] == 0f ) 1f else 0f

        val axis1 = FloatArray(3)
        axis1[0] = if ( l1[0] == 0f ) 1f else 0f
        axis1[1] = if ( l1[1] == 0f ) 1f else 0f
        axis1[2] = if ( l1[2] == 0f ) 1f else 0f


        // モデルを単位行列にする
        Matrix.setIdentityM(matM,0)
        // モデルを移動する(元の位置に戻す)
        Matrix.translateM(matM,0,-l0[0],-l0[1],-l0[2])


        // モデルを移動する(元の位置に戻す)
        Matrix.translateM(matM,0,-l1[0],-l1[1],-l1[2])
        // モデルを軸を中心に回転させる
        Matrix.rotateM(matM,0,t,ax1[0],ax1[1],ax1[2])
        // モデルを移動する(軸上にずらす)
        Matrix.translateM(matM,0,l1[0],l1[1],l1[2])


        // モデルを移動する(軸上にずらす)
        Matrix.translateM(matM,0,l0[0],l0[1],l0[2])
        // モデル×ビュー×プロジェクション
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(modelMotion,matMVP)
    }

    private fun rotateModel1(matA: FloatArray, x0: Float, y0: Float, z0: Float, t: Float, n: Int) {
        if ( n <= 0 ) return

        val matB = FloatArray(16)
        val matC = FloatArray(16)
        Matrix.setIdentityM(matB,0)
        // モデルを移動する(元の位置に戻す)
        Matrix.translateM(matB,0,-x0,-y0,-z0)
        // モデルを軸を中心に回転させる
        val axisX = if ( x0 == 0f ) 1f else 0f
        val axisY = if ( y0 == 0f ) 1f else 0f
        val axisZ = if ( z0 == 0f ) 1f else 0f
        Matrix.rotateM(matB,0,t,axisX,axisY,axisZ)
        // モデルを移動する(軸上にずらす)
        Matrix.translateM(matB,0,x0,y0,z0)
        Matrix.multiplyMM(matC,0,matB,0,matA,0)
        // モデル×ビュー×プロジェクション
        Matrix.multiplyMM(matMVP,0,matVP,0,matC,0)
        shaderSimple.draw(modelMotion,matMVP)

        val matD = FloatArray(16)
        Matrix.setIdentityM(matD,0)
        // モデルを軸を中心に回転させる
        Matrix.rotateM(matD,0,t,axisX,axisY,axisZ)
        // モデルを移動する(軸上にずらす)
        Matrix.translateM(matD,0,x0,y0,z0)

        /*
        // 次回の移動座標
        if ( axisX == 1f ) {
            rotateModel1(matC,1f,0f,1f,t,n-1)
        }
        else if ( axisY == 1f ) {
            rotateModel1(matC,1f,1f,0f,t,n-1)
        }
        else if ( axisZ == 1f ) {
            rotateModel1(matB,0f,1f,1f,t,n-1)
        }
        */
        rotateModel1(matD,0f,-1f,-1f,t,n-1)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat()/height.toFloat()

        //Matrix.perspectiveM(matP,0,60f,ratio,0.1f,100f)
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

        /*
        // カメラの位置
        vecEye[0]  = 0f
        vecEye[1]  = 0f
        vecEye[2]  = 10f

        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
                */

        // シェーダ(特殊効果なし)
        shaderSimple = ES20Simple01Shader()
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
        axisModel = Axis01Model()
        axisModel.createPath( mapOf("scale" to 3f))
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
    }
}
