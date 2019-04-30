package milu.kiriu2010.exdb1.opengl05.w051

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.MgShader

// 深度格納用シェーダ
class W051ShaderDepth: MgShader() {
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
                b -= a*coef;
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

    override fun loadShader(): MgShader {
        // 頂点シェーダを生成
        val svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position") )
        return this
    }


    fun draw(model: MgModelAbs,
             u_matMVP: FloatArray,
             u_depthBuffer: Int) {
        GLES20.glUseProgram(programHandle)

        // attribute(頂点)
        model.bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Position:${model.javaClass.simpleName}")

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,u_matMVP,0)
        }
        MyGLFunc.checkGlError("u_matMVP:${model.javaClass.simpleName}")

        // uniform(深度値を使うかどうか)
        GLES20.glGetUniformLocation(programHandle, "u_depthBuffer").also {
            GLES20.glUniform1i(it, u_depthBuffer)
        }
        MyGLFunc.checkGlError("u_depthBuffer:${model.javaClass.simpleName}")

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
    }
}
