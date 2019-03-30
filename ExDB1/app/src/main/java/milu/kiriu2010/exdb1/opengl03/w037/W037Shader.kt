package milu.kiriu2010.exdb1.opengl03.w037

import android.opengl.GLES20
import milu.kiriu2010.exdb1.opengl.MyGLFunc

class W037Shader {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec4  a_Color;
            uniform   mat4  u_matMVP;
            uniform   float u_pointSize;
            varying   vec4  v_Color;

            void main() {
                v_Color        = a_Color;
                gl_Position    = u_matMVP * vec4(a_Position,1.0);
                gl_PointSize   = u_pointSize;
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            uniform   sampler2D u_Texture0;
            uniform   int       u_useTexture;
            varying   vec4      v_Color;

            void main() {
                vec4 smpColor = vec4(1.0);
                if (bool(u_useTexture)) {
                    smpColor = texture2D(u_Texture0, gl_PointCoord);
                }

                if (smpColor.a == 0.0) {
                    discard;
                }
                else {
                    gl_FragColor  = v_Color * smpColor;
                }
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
