package milu.kiriu2010.exdb1.opengl

import android.opengl.GLES20
import android.util.Log

class MyGLCheck {
    companion object {
        fun checkGlError( str: String ) {
            var error = GLES20.glGetError()
            while ( error != GLES20.GL_NO_ERROR ) {
                Log.d("MyGLCheck", "${str}:${error}")
                error = GLES20.glGetError()
            }
        }
    }
}