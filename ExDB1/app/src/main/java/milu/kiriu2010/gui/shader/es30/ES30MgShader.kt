package milu.kiriu2010.gui.shader.es30

import android.opengl.GLES30
import android.util.Log
import milu.kiriu2010.gui.basic.MyGLES30Func

// -----------------------------------------
// GLSL ES20用シェーダ
// -----------------------------------------
// 2019.05.28
// 2019.06.07 attribute/uniformハンドル追加
// -----------------------------------------
abstract class ES30MgShader {
    // 頂点シェーダのハンドル
    var svhandle: Int = -1
    // フラグメントシェーダのハンドル
    var sfhandle: Int = -1

    // シェーダが登録されたプログラムオブジェクトのハンドル
    protected var programHandle = -1

    // attributeのハンドル
    lateinit var hATTR: IntArray
    // uniformのハンドル
    lateinit var hUNI: IntArray

    // シェーダを削除
    fun deleteShader() {
        if ( this::hATTR.isInitialized ) {
            (0 until hATTR.size).forEach {
                Log.d(javaClass.simpleName,"glDisableVertexAttribArray:${hATTR[it]}")
                GLES30.glDisableVertexAttribArray(hATTR[it])
                MyGLES30Func.checkGlError("glDisableVertexAttribArray:${hATTR[it]}")
            }
        }
        if ( svhandle != -1 ) {
            Log.d(javaClass.simpleName,"glDeleteShader:sv:$svhandle")
            GLES30.glDeleteShader(svhandle)
            MyGLES30Func.checkGlError("glDeleteShader:sv:$svhandle")
        }
        if ( sfhandle != -1 ) {
            Log.d(javaClass.simpleName,"glDeleteShader:sf:$sfhandle")
            GLES30.glDeleteShader(sfhandle)
            MyGLES30Func.checkGlError("glDeleteShader:sf:$sfhandle")
        }
        if ( programHandle != -1 ) {
            Log.d(javaClass.simpleName,"glDeleteProgram:$programHandle")
            GLES30.glDeleteProgram(programHandle)
            MyGLES30Func.checkGlError("glDeleteProgram:$programHandle")
        }
    }

    abstract fun loadShader(): ES30MgShader
}
