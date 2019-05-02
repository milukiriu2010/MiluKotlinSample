package milu.kiriu2010.exdb1.opengl06.w059

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.MgShader

// 深度値格納用シェーダ
class W059ShaderDepth: MgShader() {
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

            uniform   float     u_depthOffset;
            varying   vec4      v_Position;

            const float near  = 0.1;
            const float far  = 30.0;
            const float linearDepth = 1.0/(far-near);

            // 深度値を32ビット制度に変換している
            vec4 convRGBA(float depth) {
                // 深度値を255倍し、その小数点以下の数値を抜き出し、次に渡す。
                // の繰り返し
                float r = depth;
                float g = fract(r*255.0);
                float b = fract(g*255.0);
                float a = fract(b*255.0);
                // 誤差を相殺するためのバイアスを各要素にかける
                float coef = 1.0/255.0;
                r -= g*coef;
                g -= b*coef;
                b -= a*coef;
                return vec4(r,g,b,a);
            }

            float convCoord(float depth, float offset) {
                float d = clamp(depth+offset, 0.0, 1.0);
                if ( d > 0.6 ) {
                    d = 2.5 * (1.0-d);
                }
                else if ( d >= 0.4 ) {
                    d = 1.0;
                }
                else {
                    d *= 2.5;
                }
                return d;
            }

            // 深度値を色情報に格納する
            void main() {
                float linear    = linearDepth * length(v_Position);
                vec4  convColor = convRGBA(convCoord(linear, u_depthOffset));
                gl_FragColor    = convColor;
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
             u_depthOffset: Float) {
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

        // uniform(深度値オフセット)
        GLES20.glGetUniformLocation(programHandle, "u_depthOffset").also {
            GLES20.glUniform1f(it, u_depthOffset)
        }
        MyGLFunc.checkGlError("u_depthOffset:${model.javaClass.simpleName}")

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
    }
}
