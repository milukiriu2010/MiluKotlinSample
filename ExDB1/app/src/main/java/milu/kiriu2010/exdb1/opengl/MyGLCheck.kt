package milu.kiriu2010.exdb1.opengl

import android.opengl.GLES20
import android.util.Log

class MyGLCheck {

    companion object {
        private const val TAG = "MyGLCheck"

        fun checkGlError( str: String ) {
            var error = GLES20.glGetError()
            while ( error != GLES20.GL_NO_ERROR ) {
                Log.d(TAG, "${str}:${error}")
                error = GLES20.glGetError()
            }
        }

        // プログラムの情報を表示する
        fun printProgramInfoLog(programHandle: Int) {
            Log.d(TAG,"===============================")
            val len = IntArray(1)
            GLES20.glGetProgramiv(programHandle,GLES20.GL_INFO_LOG_LENGTH,len,0)
            if ( len[0] > 0 ) {
                Log.d(TAG,GLES20.glGetProgramInfoLog(programHandle))
            }
            else {
                Log.d(TAG,"cannot allocate log.")
            }
        }

    }
}