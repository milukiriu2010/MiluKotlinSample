package milu.kiriu2010.exdb1.opengl05.w051

import android.opengl.GLES20
import milu.kiriu2010.exdb1.opengl.MyGLFunc

// 深度格納用シェーダ
class W051ShaderDepth {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            uniform   mat4  u_matMVP;
            varying   vec4  v_Position;

            void main() {
                v_Position  = u_matMVP * vec4(a_Position, 1.0);
                gl_Position = v_Position;
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            uniform   int       u_depthBuffer;
            varying   vec4      v_Position;

            vec4 convRGBA(float depth) {
                float r = depth;
                float g = fract(r*255.0);
                float b = fract(g*255.0);
                float a = fract(b*255.0);
                float coef = 1.0/255.0;
                r -= g*coef;
                g -= b*coef;
                b -= a*coe;
                return vec4(r,g,b,a);
            }

            void main() {
                vec4 convColor;
                if (bool(u_depthBuffer)) {
                    convColor = convRGBA(gl_FragCoord.z);
                }
                else {
                    float near = 0.1;
                    float far  = 150.0;
                    float linearDepth = 1.0/(far-near);
                    linearDepth *= length(v_Position);
                    convColor = convRGBA(linearDepth);
                }
                gl_FragColor   = convColor;
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
