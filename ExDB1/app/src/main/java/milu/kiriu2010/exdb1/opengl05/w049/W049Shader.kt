package milu.kiriu2010.exdb1.opengl05.w049

import android.opengl.GLES20
import milu.kiriu2010.exdb1.opengl.MyGLFunc

class W049Shader {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec3  a_Normal;
            attribute vec4  a_Color;
            uniform   mat4  u_matM;
            uniform   mat4  u_matTex;
            uniform   mat4  u_matMVP;
            varying   vec3  v_Position;
            varying   vec3  v_Normal;
            varying   vec4  v_Color;
            varying   vec4  v_TexCoord;

            void main() {
                v_Position    = (u_matM * vec4(a_Position,1.0)).xyz;
                v_Normal      = a_Normal;
                v_Color       = a_Color;
                v_TexCoord    = u_matTex * vec4(v_Position,1.0);
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
            varying   vec3        v_Position;
            varying   vec3        v_Normal;
            varying   vec4        v_Color;
            varying   vec4        v_TexCoord;

            void main() {
                vec3  light    = u_vecLight - v_Position;
                vec3  invLight = normalize(u_matINV * vec4(light,0.0)).xyz;
                float diffuse  = clamp(dot(v_Normal,invLight), 0.1, 1.0);
                vec4  smpColor = texture2DProj(u_Texture0, v_TexCoord);
                gl_FragColor   = v_Color * vec4(vec3(diffuse), 1.0) * smpColor;
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
