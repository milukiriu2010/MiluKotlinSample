package milu.kiriu2010.exdb1.opengl

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class MyGLView: GLSurfaceView {

    private val myRenderer = MyRenderer()

    constructor(ctx: Context): super(ctx) {
        setRenderer(myRenderer)
    }

    /* @JvmOverloads */
    constructor(
            ctx: Context,
            attrs: AttributeSet? = null)
            : super(ctx, attrs) {
        setRenderer(myRenderer)
    }
}
