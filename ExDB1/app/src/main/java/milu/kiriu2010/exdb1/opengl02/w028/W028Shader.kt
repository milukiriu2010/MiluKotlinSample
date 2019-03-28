package milu.kiriu2010.exdb1.opengl02.w028

import android.opengl.GLES20
import milu.kiriu2010.exdb1.opengl.MyGLFunc

class W028Shader {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3 a_Position;
            attribute vec4 a_Color;
            attribute vec2 a_TextureCoord;
            uniform   mat4 u_matMVP;
            varying   vec4 v_Color;
            varying   vec2 v_TextureCoord;

            void main() {
                v_Color        = a_Color;
                v_TextureCoord = a_TextureCoord;
                gl_Position    = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump float;

            uniform   sampler2D  u_Texture0;
            uniform   sampler2D  u_Texture1;
            varying   vec4       v_Color;
            varying   vec2       v_TextureCoord;

            void main() {
                vec4 smpColor0 = texture2D(u_Texture0, v_TextureCoord);
                vec4 smpColor1 = texture2D(u_Texture1, v_TextureCoord);
                gl_FragColor   = v_Color * smpColor0 * smpColor1;
            }
            """.trimIndent()

    fun loadShader(): Int {
        // 頂点シェーダを生成
        val svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        return MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Color","a_TextureCoord") )
    }
}