package milu.kiriu2010.exdb1.opengl05.w054

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc

// グレースケール用シェーダ
class W054ShaderSepia {
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
            uniform   int         u_sepiaScale;
            varying   vec2        v_TexCoord;

            const float redScale   = 0.298912;
            const float greenScale = 0.586611;
            const float blueScale  = 0.114478;
            const vec3  monochromeScale = vec3(redScale, greenScale, blueScale);

            const float sRedScale   = 1.07;
            const float sGreenScale = 0.74;
            const float sBlueScale  = 0.43;
            const vec3  sepiaScale = vec3(sRedScale, sGreenScale, sBlueScale);

            void main() {
                vec4 smpColor = texture2D(u_Texture0, v_TexCoord);
                float grayColor = dot(smpColor.rgb, monochromeScale);
                if (bool(u_grayScale)) {
                    smpColor = vec4(vec3(grayColor), 1.0);
                } else if (bool(u_sepiaScale)) {
                    vec3 monoColor = vec3(grayColor) * sepiaScale;
                    smpColor = vec4(monoColor, 1.0);
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
