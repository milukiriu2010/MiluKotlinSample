package milu.kiriu2010.exdb1.opengl01

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

        // シェーダの情報を表ｊいする
        fun printShaderInfoLog(shaderHandle: Int) {
            Log.d(TAG,"=== shader compile ============================")
            // シェーダのコンパイル時のログの内容を取得する
            Log.d(TAG,GLES20.glGetShaderInfoLog(shaderHandle))
            /*
            val len = IntArray(1)
            GLES20.glGetProgramiv(shaderHandle,GLES20.GL_INFO_LOG_LENGTH,len,0)
            // シェーダのコンパイル時のログの内容を取得する
            if ( len[0] > 0 ) {
                Log.d(TAG,GLES20.glGetShaderInfoLog(shaderHandle))
            }
            else {
                Log.d(TAG,"cannot allocate log for shader.")
            }
            */
        }

        // プログラムの情報を表示する
        fun printProgramInfoLog(programHandle: Int) {
            Log.d(TAG,"=== shader link ============================")
            // シェーダのリンク時のログの内容を取得する
            Log.d(TAG,GLES20.glGetProgramInfoLog(programHandle))
            /*
            val len = IntArray(1)
            GLES20.glGetProgramiv(programHandle,GLES20.GL_INFO_LOG_LENGTH,len,0)
            // シェーダのリンク時のログの内容を取得する
            if ( len[0] > 0 ) {
                Log.d(TAG,GLES20.glGetProgramInfoLog(programHandle))
            }
            else {
                Log.d(TAG,"cannot allocate log for program.")
            }
            */
        }

    }
}