package milu.kiriu2010.exdb1.opengl03.w040

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc

class W040Shader {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec3  a_Normal;
            attribute vec4  a_Color;
            attribute vec2  a_TextureCoord;
            uniform   mat4  u_matM;
            uniform   mat4  u_matMVP;
            uniform   mat4  u_matINV;
            uniform   vec3  u_vecLight;
            uniform   bool  u_useLight;
            varying   vec4  v_Color;
            varying   vec2  v_TextureCoord;

            void main() {
                if (u_useLight) {
                    vec3  invLight = normalize(u_matINV * vec4(u_vecLight, 0.0)).xyz;
                    float diffuse  = clamp(dot(a_Normal, invLight), 0.2, 1.0);
                    v_Color        = vec4(a_Color.xyz * vec3(diffuse), 1.0);
                }
                else {
                    v_Color        = a_Color;
                }
                v_TextureCoord = a_TextureCoord;
                gl_Position    = u_matMVP * vec4(a_Position, 1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            uniform   sampler2D u_Texture0;
            varying   vec4      v_Color;
            varying   vec2      v_TextureCoord;

            void main() {
                vec4 smpColor = texture2D(u_Texture0, v_TextureCoord);
                gl_FragColor  = v_Color * smpColor;
            }
            """.trimIndent()

    fun loadShader(): Int {
        // 頂点シェーダを生成
        val svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        return MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Normal","a_Color","a_TextureCoord") )
    }
}
