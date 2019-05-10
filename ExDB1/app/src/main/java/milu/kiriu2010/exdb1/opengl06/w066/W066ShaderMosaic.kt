package milu.kiriu2010.exdb1.opengl06.w066

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.shader.MgShader

// シェーダ(モザイク)
class W066ShaderMosaic: MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec2  a_TexCoord;
            uniform   mat4  u_matMVP;
            varying   vec2  v_TexCoord;

            void main() {
                v_TexCoord  = a_TexCoord;
                gl_Position = u_matMVP   * vec4(a_Position, 1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            uniform  sampler2D u_Texture;
            uniform  float     u_renderWH;
            varying  vec2      v_TexCoord;

            // スクリーン上を区切るボックスのサイズを8x8に設定している
            void main() {
                // 512x512の描画領域を扱う場合
                //float tFrag = 1.0/512.0;
                //float nFrag = 1.0/64.0;

                float tFrag = 1.0/u_renderWH;
                // 色を出力する際、正規化するための係数
                //float nFrag = 1.0/(u_renderWH/8.0);
                float nFrag = 1.0/64.0;

                vec4  destColor = vec4(0.0);
                vec2  fc = vec2(gl_FragCoord.s, u_renderWH - gl_FragCoord.t);
                float offsetX = mod(fc.s, 8.0);
                float offsetY = mod(fc.t, 8.0);

                for (float x = 0.0; x <= 7.0; x += 1.0) {
                    for (float y = 0.0; y <= 7.0; y += 1.0) {
                        destColor += texture2D( u_Texture, (fc + vec2(x-offsetX,y-offsetY)) * tFrag );
                    }
                }

                gl_FragColor = destColor * nFrag;
            }
            """.trimIndent()


    override fun loadShader(): MgShader {
        // 頂点シェーダを生成
        val svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_TexCoord") )
        return this
    }

    fun draw(model: MgModelAbs,
             matMVP: FloatArray,
             u_Texture: Int,
             u_renderWH: Float) {

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
        GLES20.glGetAttribLocation(programHandle,"a_TexCoord").also {
            GLES20.glVertexAttribPointer(it,2,GLES20.GL_FLOAT,false, 2*4, model.bufTxc)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_TexCoord:${model.javaClass.simpleName}")

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLFunc.checkGlError("u_matMVP:${model.javaClass.simpleName}")

        // uniform(テクスチャ)
        GLES20.glGetUniformLocation(programHandle, "u_Texture").also {
            GLES20.glUniform1i(it, u_Texture)
        }
        MyGLFunc.checkGlError("u_Texture:${model.javaClass.simpleName}")

        // uniform(画像の大きさ)
        GLES20.glGetUniformLocation(programHandle, "u_renderWH").also {
            GLES20.glUniform1f(it, u_renderWH)
        }
        MyGLFunc.checkGlError("u_renderWH:${model.javaClass.simpleName}")

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
    }
}
