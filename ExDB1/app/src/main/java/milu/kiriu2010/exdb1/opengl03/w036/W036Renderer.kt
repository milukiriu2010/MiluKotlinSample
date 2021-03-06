package milu.kiriu2010.exdb1.opengl03.w036

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log
import milu.kiriu2010.gui.model.d2.Line01Model
import milu.kiriu2010.gui.model.Sphere01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// --------------------------------------
// 点や線のレンダリング:VBOなし
// OpenGL ES 2.0
// --------------------------------------
// https://wgld.org/d/webgl/w036.html
// --------------------------------------
class W036Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画オブジェクト(球体)
    private lateinit var modelSphere: Sphere01Model
    // 描画オブジェクト(線)
    private lateinit var modelLine: Line01Model

    // シェーダ
    private lateinit var shader: W036Shader

    // 画面縦横比
    var ratio: Float = 0f

    // 点のサイズ
    var u_pointSize = 20f

    // 点のサイズの範囲
    val pointSizeRange = FloatArray(2)

    // 線のプリミティブタイプ
    var lineType = GLES20.GL_LINES

    init {
    }

    override fun onDrawFrame(gl: GL10?) {
        // canvasを初期化
        GLES20.glClearColor(0f,0f,0f,1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        angle[0] =(angle[0]+1)%360
        val t0 = angle[0].toFloat()

        // クォータニオンを行列に適用
        var matQ = qtnNow.toMatIV()

        // カメラの位置
        // ビュー座標変換行列
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        // ビュー座標変換行列にクォータニオンの回転を適用
        Matrix.multiplyMM(matV,0,matV,0,matQ,0)
        // ビュー×プロジェクション
        Matrix.perspectiveM(matP,0,45f,ratio,0.1f,100f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // 球体をレンダリング
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,t0,0f,1f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shader.draw(modelSphere,matMVP,u_pointSize,GLES20.GL_POINTS)

        // 線をレンダリング
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,90f,1f,0f,0f)
        Matrix.scaleM(matM,0,3f,3f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        GLES20.glLineWidth(5f)
        shader.draw(modelLine,matMVP,u_pointSize,lineType)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        ratio = width.toFloat()/height.toFloat()
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // 点の最大ピクセル数をコンソールに出力
        //val pointSizeRange = GLES20.glGetParameter(GLES20.GL_ALIASED_POINT_SIZE_RANGE)
        GLES20.glGetFloatv(GLES20.GL_ALIASED_POINT_SIZE_RANGE,pointSizeRange,0)
        //callback.receive(pointSizeRange)
        Log.d(javaClass.simpleName,"min:${pointSizeRange[0]}")
        Log.d(javaClass.simpleName,"max:${pointSizeRange[1]}")

        // 深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)

        // シェーダ
        shader = W036Shader()
        shader.loadShader()

        // モデル生成(球体)
        modelSphere = Sphere01Model()
        modelSphere.createPath(mapOf(
                "row"    to 16f,
                "column" to 16f,
                "radius" to 2f
        ))

        // モデル生成(線)
        modelLine = Line01Model()
        modelLine.createPath()

        // カメラの座標
        vecEye[0] = 0f
        vecEye[1] = 5f
        vecEye[2] = 10f
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
    }
}