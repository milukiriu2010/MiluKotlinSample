package milu.kiriu2010.exdb1.opengl01

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log

class MyGL02View: GLSurfaceView {

    //private val myRenderer = MyCube01Renderer()

    constructor(ctx: Context): super(ctx) {
        /*
        setRenderer(myRenderer)
        // 何かあった場合だけ描画するモード
        //this.renderMode = RENDERMODE_WHEN_DIRTY
        // 再描画
        //this.requestRender()
        */
    }

    /* @JvmOverloads */
    constructor(
            ctx: Context,
            attrs: AttributeSet? = null)
            : super(ctx, attrs) {
        /*
        setRenderer(myRenderer)
        */
    }

    init {
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)
        Log.d(javaClass.simpleName,"setEGLContextClient(2)")
    }
}
