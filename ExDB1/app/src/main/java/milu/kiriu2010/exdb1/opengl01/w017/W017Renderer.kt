package milu.kiriu2010.exdb1.opengl01.w017

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.Matrix
import android.os.SystemClock
import milu.kiriu2010.gui.model.Triangle01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.Simple00Shader
import milu.kiriu2010.math.MyMathUtil
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// 移動・回転・拡大縮小
// --------------------------------------------------------
// https://wgld.org/d/webgl/w017.html
// https://android.googlesource.com/platform/development/+/master/samples/OpenGL/HelloOpenGLES20/src/com/example/android/opengl/MyGLRenderer.java
// https://android.keicode.com/basics/opengl-drawing-basic-shapes.php
// https://developer.android.com/training/graphics/opengl/draw
class W017Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画モデル
    private lateinit var drawObj: Triangle01Model

    // シェーダ
    private lateinit var shader: Simple00Shader
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

        val x = MyMathUtil.cosf(t0)
        val y = MyMathUtil.sinf(t0)

        // ---------------------------------------------------
        // １つ目のモデル
        // (0,1,0)を中心にZ軸と並行に回転する
        // ---------------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,x,y+1f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shader.draw(drawObj,matMVP)

        // ---------------------------------------------------
        // ２つ目のモデル
        // Y軸を中心に回転する
        // ---------------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,1f,-1f,0f)
        Matrix.rotateM(matM,0,t0,0f,1f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shader.draw(drawObj,matMVP)

        // ---------------------------------------------------
        // ３つ目のモデル
        // 拡大縮小する
        // ---------------------------------------------------
        val s = MyMathUtil.sinf(t0) + 1f
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-1f,-1f,0f)
        Matrix.scaleM(matM,0,s,s,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shader.draw(drawObj,matMVP)
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
        Matrix.perspectiveM(matP, 0, 90f,ratio,0.1f,100f)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        // シェーダプログラム登録
        shader = Simple00Shader()
        shader.loadShader()

        // モデル生成
        drawObj = Triangle01Model()
        drawObj.createPath()

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
    }
}
