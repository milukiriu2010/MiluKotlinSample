package milu.kiriu2010.exdb1.mgl01.qtn01

import android.content.Context
import android.opengl.GLES20
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.Matrix
import android.util.Log
import android.view.MotionEvent
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.gui.basic.MyQuaternion
import milu.kiriu2010.gui.model.*
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.PointLight01Shader
import milu.kiriu2010.math.MyMathUtil
import kotlin.math.sqrt


class Qtn01Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画モデル
    private lateinit var model: MgModelAbs

    // シェーダ
    private lateinit var shaderPointLight: PointLight01Shader

    override fun onDrawFrame(gl: GL10) {

        // canvasを初期化
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        angle[0] =(angle[0]+1)%360
        val t1 = angle[0].toFloat()

        val x = MyMathUtil.cosf(t1)
        val y = MyMathUtil.sinf(t1)

        // -----------------------------------------------
        // クォータニオン⇒座標変換行列の検証
        // -----------------------------------------------
        //// クォータニオン生成
        //xQtn = MyQuaternion.rotate(90f, floatArrayOf(1f,0f,0f))


        // クォータニオンによる回転が適用された状態の座標変換行列を取得する
        val matQ = qtnNow.toMatIV()

        // ビュー×プロジェクション
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // モデルを単位行列にする
        Matrix.setIdentityM(matM,0)
        Matrix.multiplyMM(matM,0,matM,0,matQ,0)
        // モデルをZ軸を中心に公転させる
        //Matrix.translateM(matM,0,x,y,0f)
        // モデルをY軸を中心に自転させる
        Matrix.rotateM(matM,0,t1,0f,1f,0f)
        // モデル×ビュー×プロジェクション
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)

        // モデル座標変換行列から逆行列を生成
        Matrix.invertM(matI,0,matM,0)

        // モデルを描画
        shaderPointLight.draw(model,matMVP,matM,matI,vecLight,vecEye,vecAmbientColor)
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
        vecEye[2]  = 10f
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])

        // シェーダプログラム登録(平行光源)
        shaderPointLight = PointLight01Shader()
        shaderPointLight.loadShader()

        // モデル生成
        model = Torus01Model()
        model.createPath()
    }

    /*
    // w: キャンバスの幅
    // h: キャンバスの高さ
    fun receiveTouch(ev: MotionEvent, w: Int, h: Int ) {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                touchP.x = ev.x
                touchP.y = ev.y
                tQtn = xQtn.copy()
                Log.d(javaClass.simpleName,"ACTION_DOWN:${tQtn}")
                return
            }
        }

        //Log.d(javaClass.simpleName,"ev:${ev.action}:${tQtn}")

        // キャンバスの対角線の長さの逆数
        var wh = 1f/ sqrt((w*w+h*h).toFloat())
        // タッチ点からみた座標
        var x = ev.x - touchP.x
        var y = ev.y - touchP.y
        var sq = sqrt(x*x+y*y)
        // 回転角
        var r = sq*wh*360f
        // 単位化する
        if ( (sq != 1f) and ( sq != 0f ) ) {
            sq = 1f/sq
            x *= sq
            y *= sq
        }

        // 回転角と回転軸ベクトルからクォータニオンを生成
        // OpenGLでは、Y座標が上のため、yにマイナスをつけない
        //xQtn = MyQuaternion.rotate(r, floatArrayOf(y,x,0f))
        val tmpQtn = MyQuaternion.rotate(r, floatArrayOf(y,x,0f))
        xQtn = tmpQtn.multiply(tQtn)

        Log.d(javaClass.simpleName,"xQtn:${xQtn}")
        Log.d(javaClass.simpleName,"tQtn:${tQtn}")

    }
    */

    /*
    // w: キャンバスの幅
    // h: キャンバスの高さ
    fun receiveTouch(ev: MotionEvent, w: Int, h: Int ) {
        // キャンバスの対角線の長さの逆数
        var wh = 1f/ sqrt((w*w+h*h).toFloat())
        // canvasの中心点からみたタッチ点の相対位置
        var x = ev.x - w.toFloat()*0.5f
        var y = ev.y - h.toFloat()*0.5f
        var sq = sqrt(x*x+y*y)
        //var r = sq*2f*PI.toFloat()*wh
        // 回転角
        var r = sq*wh*360f
        // 単位化する
        if ( (sq != 1f) and ( sq != 0f ) ) {
            sq = 1f/sq
            x *= sq
            y *= sq
        }

        // 回転角と回転軸ベクトルからクォータニオンを生成
        // OpenGLでは、Y座標が上のため、yにマイナスをつけない
        xQtn = MyQuaternion.rotate(r, floatArrayOf(y,x,0f))
    }
    */

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
    }
}
