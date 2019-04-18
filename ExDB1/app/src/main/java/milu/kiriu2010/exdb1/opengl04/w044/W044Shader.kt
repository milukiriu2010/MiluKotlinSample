package milu.kiriu2010.exdb1.opengl04.w044

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc

class W044Shader {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec3  a_Normal;
            attribute vec4  a_Color;
            uniform   mat4  u_matM;
            uniform   mat4  u_matMVP;
            varying   vec3  v_Position;
            varying   vec3  v_Normal;
            varying   vec4  v_Color;

            void main() {
                v_Position    = (u_matM * vec4(a_Position,1.0)).xyz;
                v_Normal      = (u_matM * vec4(a_Normal  ,0.0)).xyz;
                v_Color       = a_Color;
                gl_Position   = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump     float;

            uniform   vec3        u_vecEye;
            uniform   samplerCube u_CubeTexture;
            uniform   int         u_Reflection;
            varying   vec3        v_Position;
            varying   vec3        v_Normal;
            varying   vec4        v_Color;

            void main() {
                vec3  ref;
                if (bool(u_Reflection)) {
                    ref = reflect(v_Position - u_vecEye, v_Normal);
                }
                else {
                    ref = v_Normal;
                }
                vec4  envColor  = textureCube(u_CubeTexture, ref);
                vec4  destColor = v_Color * envColor;
                gl_FragColor    = destColor;
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
