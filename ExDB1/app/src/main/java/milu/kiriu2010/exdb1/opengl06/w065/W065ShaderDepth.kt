package milu.kiriu2010.exdb1.opengl06.w065

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.MgShader

// シェーダ(裏面深度値レンダリング)
class W065ShaderDepth: MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            uniform   mat4  u_matM;
            uniform   mat4  u_matMVP;
            uniform   vec3  u_vecEye;
            varying   vec4  v_Color;

            const float near = 0.1;
            const float far  = 15.0;
            const float linearDepth = 1.0/(far-near);

            void main() {
                vec3  pos   = (u_matM*vec4(a_Position,1.0)).xyz;
                float depth = length(u_vecEye-pos) * linearDepth;
                v_Color     = vec4(vec3(depth), 1.0);
                gl_Position = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            varying vec4 v_Color;

            void main() {
                gl_FragColor = v_Color;
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
             u_matM: FloatArray,
             u_matMVP: FloatArray,
             u_vecEye: FloatArray) {
        GLES20.glUseProgram(programHandle)

        // attribute(頂点)
        model.bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Position:${model.javaClass.simpleName}")

        // uniform(モデル座標変換行列)
        GLES20.glGetUniformLocation(programHandle,"u_matM").also {
            GLES20.glUniformMatrix4fv(it,1,false,u_matM,0)
        }
        MyGLFunc.checkGlError("u_matM:${model.javaClass.simpleName}")

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,u_matMVP,0)
        }
        MyGLFunc.checkGlError("u_matMVP:${model.javaClass.simpleName}")

        // uniform(視点座標)
        GLES20.glGetUniformLocation(programHandle,"u_vecEye").also {
            GLES20.glUniform3fv(it,1,u_vecEye,0)
        }
        MyGLFunc.checkGlError("u_vecEye:${model.javaClass.simpleName}")

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
    }
}
