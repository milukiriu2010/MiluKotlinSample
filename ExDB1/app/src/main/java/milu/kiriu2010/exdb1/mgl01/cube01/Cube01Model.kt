package milu.kiriu2010.exdb1.mgl01.cube01

import android.opengl.GLES20
import milu.kiriu2010.exdb1.opengl.MyGLFunc
import java.lang.RuntimeException

class Cube01Model {

    // プログラムハンドル
    var programHandle: Int = 0

    // 頂点シェーダ
    private val scv =
            """
            attribute vec3 a_Position;
            attribute vec4 a_Color;
            uniform   mat4 u_matMVP;
            varying   vec4 v_Color;

            void main() {
                v_Color     = a_Color;
                gl_Position = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump float;
            varying vec4 v_Color;

            void main() {
                gl_FragColor = v_Color;
            }
            """.trimIndent()

    init {
        // 頂点シェーダを生成
        val svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Color") )
    }

}