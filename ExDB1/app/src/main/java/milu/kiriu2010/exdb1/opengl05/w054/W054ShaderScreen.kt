package milu.kiriu2010.exdb1.opengl05.w054

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc

// スクリーンレンダリング用シェーダ
class W054ShaderScreen {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec3  a_Normal;
            attribute vec4  a_Color;
            uniform   mat4  u_matMVP;
            uniform   mat4  u_matINV;
            uniform   vec3  u_vecLight;
            uniform   vec3  u_vecEye;
            uniform   vec4  u_ambientColor;
            varying   vec4  v_Color;

            void main() {
                vec3   invLight = normalize(u_matINV * vec4(u_vecLight, 0.0)).xyz;
                vec3   invEye   = normalize(u_matINV * vec4(u_vecEye  , 0.0)).xyz;
                vec3   halfLE   = normalize(invLight + invEye);
                float  diffuse  = clamp(dot(a_Normal,invLight), 0.0, 1.0);
                float  specular = pow(clamp(dot(a_Normal, halfLE), 0.0, 1.0), 50.0);
                vec4   amb      = a_Color * u_ambientColor;
                v_Color         = amb * vec4(vec3(diffuse),1.0) + vec4(vec3(specular),1.0);
                gl_Position     = u_matMVP   * vec4(a_Position, 1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            varying   vec4      v_Color;

            void main() {
                gl_FragColor = v_Color;
            }
            """.trimIndent()

    var programHandle: Int = -1

    fun loadShader(): Int {
        // 頂点シェーダを生成
        val svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Normal","a_Color") )

        return programHandle
    }
}
