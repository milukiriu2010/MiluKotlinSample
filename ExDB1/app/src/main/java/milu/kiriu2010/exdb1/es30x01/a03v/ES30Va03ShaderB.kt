package milu.kiriu2010.exdb1.es30x01.a03v

import android.opengl.GLES30
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES30Func
import milu.kiriu2010.gui.shader.es30.ES30MgShader
import milu.kiriu2010.gui.vbo.es30.ES30VBOAbs

// ------------------------------------
// シェーダB
// ------------------------------------
// https://wgld.org/d/webgl2/w003.html
// ------------------------------------
class ES30Va03ShaderB: ES30MgShader() {
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

            precision highp   float;

            uniform  sampler2D u_Texture;

            in  vec2  v_TexCoord;

            out vec4  o_Color;

            void main() {
                o_Color = texture(u_Texture,v_TexCoord);
            }
            """.trimIndent()

    override fun loadShader(): ES30MgShader {
        // 頂点シェーダを生成
        svhandle = MyGLES30Func.loadShader(GLES30.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        sfhandle = MyGLES30Func.loadShader(GLES30.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES30Func.createProgram(svhandle,sfhandle)

        // ----------------------------------------------
        // attributeハンドルに値をセット
        // ----------------------------------------------
        hATTR = IntArray(1)
        // 属性(頂点)
        hATTR[0] = GLES30.glGetAttribLocation(programHandle, "a_Position").also {
            // attribute属性を有効にする
            // ここで呼ばないと描画されない
            GLES30.glEnableVertexAttribArray(it)
            MyGLES30Func.checkGlError("a_Position:glEnableVertexAttribArray")
            // attribute属性を登録
            GLES30.glVertexAttribPointer(it,3,GLES30.GL_FLOAT,false,0,0)
            MyGLES30Func.checkGlError("a_Position:glVertexAttribPointer")
        }
        MyGLES30Func.checkGlError("a_Position:glGetAttribLocation")

        // ----------------------------------------------
        // uniformハンドルに値をセット
        // ----------------------------------------------
        hUNI = IntArray(3)

        // uniform(テクスチャユニット)
        hUNI[0] = GLES30.glGetUniformLocation(programHandle, "u_Texture")
        MyGLES30Func.checkGlError("u_Texture:glGetUniformLocation")

        return this
    }

    fun draw(model: MgModelAbs,
             bo: ES30VBOAbs,
             u_Texture: Int) {

        GLES30.glUseProgram(programHandle)
        MyGLES30Func.checkGlError2("UseProgram",this,model)

        // attribute(頂点)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,bo.hVBO[0])
        GLES30.glVertexAttribPointer(hATTR[0],3,GLES30.GL_FLOAT,false,0,0)
        MyGLES30Func.checkGlError2("a_Position",this,model)

        // uniform(テクスチャ座標)
        GLES30.glUniform1i(hUNI[0], u_Texture)
        MyGLES30Func.checkGlError2("u_Texture",this,model)

        // モデルを描画
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, model.datIdx.size, GLES30.GL_UNSIGNED_SHORT, model.bufIdx)

        // リソース解放
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,0)
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}
