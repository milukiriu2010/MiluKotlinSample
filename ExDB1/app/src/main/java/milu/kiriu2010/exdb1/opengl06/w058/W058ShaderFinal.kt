package milu.kiriu2010.exdb1.opengl06.w058

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.MgShader

// シェーダ(最終結果)
//   レンダリングされた全てのシーンを合成する
class W058ShaderFinal: MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec2  a_TextureCoord;
            uniform   mat4  u_matMVP;
            varying   vec2  v_TextureCoord;

            void main() {
                v_TextureCoord = a_TextureCoord;
                gl_Position    = u_matMVP   * vec4(a_Position, 1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            uniform   sampler2D   u_Texture1;
            uniform   sampler2D   u_Texture2;
            uniform   int         u_glare;
            varying   vec2        v_TextureCoord;

            void main() {
                vec4 destColor = texture2D(u_Texture1, v_TextureCoord);
                vec4 smpColor  = texture2D(u_Texture2, vec2(v_TextureCoord.s,1.0-v_TextureCoord.t));

                if ( bool(u_glare) ) {
                    destColor += smpColor * 2.0;
                }
                gl_FragColor = destColor;
            }
            """.trimIndent()

    override fun loadShader(): MgShader {
        // 頂点シェーダを生成
        val svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_TextureCoord") )
        return this
    }


    fun draw(model: MgModelAbs,
             matMVP: FloatArray,
             u_Texture1: Int,
             u_Texture2: Int,
             u_glare: Int) {
        GLES20.glUseProgram(programHandle)

        // attribute(頂点)
        model.bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Position:${model.javaClass.simpleName}")

        // attribute(テクスチャ座標)
        model.bufTxc.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_TextureCoord").also {
            GLES20.glVertexAttribPointer(it,2,GLES20.GL_FLOAT,false, 2*4, model.bufTxc)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_TextureCoord:${model.javaClass.simpleName}")

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLFunc.checkGlError("u_matMVP:${model.javaClass.simpleName}")

        // uniform(テクスチャ)
        GLES20.glGetUniformLocation(programHandle, "u_Texture1").also {
            GLES20.glUniform1i(it, u_Texture1)
        }
        MyGLFunc.checkGlError("u_Texture1:${model.javaClass.simpleName}")

        // uniform(テクスチャ)
        GLES20.glGetUniformLocation(programHandle, "u_Texture2").also {
            GLES20.glUniform1i(it, u_Texture2)
        }
        MyGLFunc.checkGlError("u_Texture2:${model.javaClass.simpleName}")

        // uniform(グレアをかけるかどうか)
        GLES20.glGetUniformLocation(programHandle, "u_glare").also {
            GLES20.glUniform1i(it, u_glare)
        }
        MyGLFunc.checkGlError("u_glare:${model.javaClass.simpleName}")

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
    }
}
