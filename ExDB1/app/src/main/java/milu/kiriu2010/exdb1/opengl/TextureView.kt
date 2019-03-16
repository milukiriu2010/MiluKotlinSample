package milu.kiriu2010.exdb1.opengl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.opengl.GLSurfaceView
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.util.Log

class TextureView: GLSurfaceView {
    constructor(ctx: Context): super(ctx) {
    }

    /* @JvmOverloads */
    constructor(
            ctx: Context,
            attrs: AttributeSet? = null)
            : super(ctx, attrs) {

    }

    init {
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)
        Log.d(javaClass.simpleName,"setEGLContextClient(2)")
    }
}
