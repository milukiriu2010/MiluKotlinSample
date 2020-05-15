package milu.kiriu2010.exdb1.opengl05.w051

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.es20.ES20MgShader

// -------------------------------------
// シェーダ(深度格納用):VBOなし
// OpenGL ES 2.0
// -------------------------------------
// https://wgld.org/d/webgl/w051.html
// -------------------------------------
class W051ShaderDepth: ES20MgShader() {
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

            // 深度値を色情報に格納する
            void main() {
                vec4 convColor;
                // デプスバッファの深度値を使う
                if (bool(u_depthBuffer)) {
                    convColor = convRGBA(gl_FragCoord.z);
                }
                // 頂点座標からダイレクトに深度に相当する値を生成してフラグメントに描く
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

    override fun loadShader(): ES20MgShader {
        // 頂点シェーダを生成
        val svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES20Func.createProgram(svhandle,sfhandle, arrayOf("a_Position") )
        return this
    }


    fun draw(model: MgModelAbs,
             u_matMVP: FloatArray,
             u_depthBuffer: Int) {
        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UseProgram",this,model)

        // attribute(頂点)
        model.bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_Position",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,u_matMVP,0)
        }
        MyGLES20Func.checkGlError2("u_matMVP",this,model)

        // uniform(深度値を使うかどうか)
        GLES20.glGetUniformLocation(programHandle, "u_depthBuffer").also {
            GLES20.glUniform1i(it, u_depthBuffer)
        }
        MyGLES20Func.checkGlError2("u_depthBuffer",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
    }
}
