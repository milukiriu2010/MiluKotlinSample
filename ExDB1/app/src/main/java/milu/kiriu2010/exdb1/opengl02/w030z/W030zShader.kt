package milu.kiriu2010.exdb1.opengl02.w030z

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc

class W030zShader {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec4  a_Color;
            attribute vec2  a_TextureCoord;
            uniform   mat4  u_matMVP;
            uniform   float u_vertexAlpha;
            varying   vec4  v_Color;
            varying   vec2  v_TextureCoord;

            void main() {
                v_Color        = vec4(a_Color.rgb, a_Color.a * u_vertexAlpha);
                v_TextureCoord = a_TextureCoord;
                gl_Position    = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump float;

            uniform   sampler2D  u_Texture0;
            uniform   int        u_useTexture;
            varying   vec4       v_Color;
            varying   vec2       v_TextureCoord;

            void main() {
                vec4 destColor = vec4(0.0);
                if (bool(u_useTexture)) {
                    vec4 smpColor = texture2D(u_Texture0, v_TextureCoord);
                    destColor = v_Color * smpColor;
                }
                else {
                    destColor = v_Color;
                }
                gl_FragColor   = destColor;
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
