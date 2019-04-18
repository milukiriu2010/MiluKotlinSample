package milu.kiriu2010.exdb1.opengl05.w053

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc

// グレースケール用シェーダ
class W053ShaderGray {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec2  a_TexCoord;
            uniform   mat4  u_matMVP;
            varying   vec2  v_TexCoord;

            void main() {
                v_TexCoord      = a_TexCoord;
                gl_Position     = u_matMVP   * vec4(a_Position, 1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump     float;

            uniform   sampler2D   u_Texture0;
            uniform   int         u_grayScale;
            varying   vec2        v_TexCoord;

            const float redScale   = 0.298912;
            const float greenScale = 0.586611;
            const float blueScale  = 0.114478;
            const vec3  monochromeScale = vec3(redScale, greenScale, blueScale);

            void main() {
                vec4 smpColor = texture2D(u_Texture0, v_TexCoord);
                if (bool(u_grayScale)) {
                    float grayColor = dot(smpColor.rgb, monochromeScale);
                    smpColor = vec4(vec3(grayColor), 1.0);
                }
                gl_FragColor = smpColor;
            }
            """.trimIndent()

    var programHandle: Int = -1

    fun loadShader(): Int {
        // 頂点シェーダを生成
        val svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_TexCoord") )

        return programHandle
    }
}
