package milu.kiriu2010.exdb1.glsl01.g009

import android.content.Context
import android.opengl.GLES20
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.Matrix
import android.os.SystemClock
import milu.kiriu2010.gui.model.Square01Model
import milu.kiriu2010.gui.renderer.MgRenderer

// -------------------------------------------
// レイマーチングで球体を描く
// -------------------------------------------
// https://wgld.org/d/glsl/g009.html
// -------------------------------------------
// https://wgld.org/d/glsl/g008.html
// -----------------------------------------------------
class GLSL09Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画モデル
    private lateinit var model: Square01Model

    // シェーダ
    private lateinit var shader: GLSL09Shader

    // 時間管理
    private var startTime = SystemClock.uptimeMillis()
    // サンプルが動作する際に、どの程度時間が経過しているのかをシェーダに渡す
    private var u_time = 0f

    override fun onDrawFrame(gl: GL10) {
        // canvasを初期化
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        // サンプルが動作する際に、どの程度時間が経過しているのかをシェーダに渡す
        u_time = (SystemClock.uptimeMillis() - startTime).toFloat() * 0.001f

        // 描画
        shader.draw(model,
                u_time,
                floatArrayOf(touchP.x,touchP.y),
                floatArrayOf(renderW.toFloat(),renderH.toFloat())
        )
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        renderW = width
        renderH = height

        startTime = SystemClock.uptimeMillis()
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {

        // シェーダ
        shader = GLSL09Shader()
        shader.loadShader()

        // モデル生成
        model = Square01Model()
        model.createPath(mapOf(
                "pattern" to 1f
        ))

        // タッチ位置
        // 左上が原点で0.0～1.0とする
        touchP.also {
            it.x = 0.5f
            it.y = 0.5f
        }
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
    }
}
