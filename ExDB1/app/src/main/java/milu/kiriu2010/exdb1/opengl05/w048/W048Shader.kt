package milu.kiriu2010.exdb1.opengl05.w048

import android.opengl.GLES20
import milu.kiriu2010.exdb1.opengl.MyGLFunc

class W048Shader {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec3  a_Normal;
            attribute vec4  a_Color;
            uniform   mat4  u_matMVP;
            uniform   int   u_edge;
            varying   vec3  v_Normal;
            varying   vec4  v_Color;

            void main() {
                vec3 pos      = a_Position;
                if (bool(u_edge)) {
                    pos += a_Normal * 0.05;
                }
                v_Normal      = a_Normal;
                v_Color       = a_Color;
                gl_Position   = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump     float;

            uniform   mat4        u_matINV;
            uniform   vec3        u_vecLight;
            uniform   sampler2D   u_Texture0;
            uniform   vec4        u_EdgeColor;
            varying   vec3        v_Normal;
            varying   vec4        v_Color;

            void main() {
                if (u_EdgeColor.a > 0.0) {
                    gl_FragColor    = u_EdgeColor;
                }
                else {
                    vec3  invLight = normalize(u_matINV * vec4(u_vecLight,0.0)).xyz;
                    float diffuse  = clamp(dot(v_Normal,u_vecLight), 0.0, 1.0);
                    vec4  smpColor = texture2D(u_Texture0, vec2(diffuse,0.0));
                    gl_FragColor   = v_Color * smpColor;
                }
            }
            """.trimIndent()

    fun loadShader(): Int {
        // 頂点シェーダを生成
        val svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        return MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Normal","a_Color") )
    }
}
