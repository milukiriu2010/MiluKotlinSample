package milu.kiriu2010.gui.shader.es30

import android.opengl.GLES30

// --------------------------------------
// GLSL ES20用シェーダ
// --------------------------------------
// 2019.05.28
// --------------------------------------
abstract class ES30MgShader {
    // 頂点シェーダのハンドル
    var svhandle: Int = -1
    // フラグメントシェーダのハンドル
    var sfhandle: Int = -1

    // シェーダが登録されたプログラムオブジェクトのハンドル
    protected var programHandle = -1

    // シェーダを削除
    fun deleteShader() {
        if ( svhandle != -1 ) {
            GLES30.glDeleteShader(svhandle)
        }
        if ( sfhandle != -1 ) {
            GLES30.glDeleteShader(sfhandle)
        }
        if ( programHandle != -1 ) {
            GLES30.glDeleteProgram(programHandle)
        }
    }

    abstract fun loadShader(): ES30MgShader
}
