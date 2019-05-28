package milu.kiriu2010.exdb1.es30x01.a03

import android.opengl.GLES30
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES30Func
import milu.kiriu2010.gui.shader.MgShader

// ------------------------------------
// シェーダB
// ------------------------------------
// https://wgld.org/d/webgl2/w003.html
// ------------------------------------
class A03ShaderB: MgShader() {
    // 頂点シェーダ
    private val scv =
            """#version 300 es

            in  vec3  a_Position;

            out vec2  v_TexCoord;

            void main() {
                v_TexCoord  = ((a_Position+1.0)*0.5).xy;
                gl_Position = vec4(a_Position, 1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """#version 300 es

            precision mediump   float;

            uniform  sampler2D u_Texture;

            in  vec2  v_TexCoord;

            out vec4  o_OutColor;

            void main() {
                o_OutColor = texture(u_Texture,v_TexCoord);
            }
            """.trimIndent()

    override fun loadShader(): MgShader {
        // 頂点シェーダを生成
        svhandle = MyGLES30Func.loadShader(GLES30.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        sfhandle = MyGLES30Func.loadShader(GLES30.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES30Func.createProgram(svhandle,sfhandle, arrayOf("a_Position") )
        return this
    }

    fun draw(model: MgModelAbs,
             u_Texture: Int) {

        GLES30.glUseProgram(programHandle)
        MyGLES30Func.checkGlError2("UseProgram",this,model)

        // attribute(頂点)
        model.bufPos.position(0)
        val hPosition = GLES30.glGetAttribLocation(programHandle,"a_Position").also {
            if ( it != -1 ) {
                GLES30.glVertexAttribPointer(it,3,GLES30.GL_FLOAT,false, 3*4, model.bufPos)
                GLES30.glEnableVertexAttribArray(it)
            }
        }
        MyGLES30Func.checkGlError2("a_Position",this,model)

        // uniform(テクスチャ座標)
        GLES30.glGetUniformLocation(programHandle, "u_Texture").also {
            GLES30.glUniform1i(it, u_Texture)
        }
        MyGLES30Func.checkGlError2("u_Texture",this,model)

        // モデルを描画
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, model.datIdx.size, GLES30.GL_UNSIGNED_SHORT, model.bufIdx)

        // リソース解放
        if ( hPosition != -1 ) {
            GLES30.glDisableVertexAttribArray(hPosition)
        }
    }
}
