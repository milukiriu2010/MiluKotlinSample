package milu.kiriu2010.exdb1.es30x01.a06

import android.opengl.GLES30
import android.util.Log
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES30Func
import milu.kiriu2010.gui.shader.es30.ES30MgShader
import milu.kiriu2010.gui.vbo.es30.ES30VAOAbs

// -----------------------------------------
// GLSL ES 3.0(VAO)
// OpenGL ES 3.0
// -----------------------------------------
// https://wgld.org/d/webgl2/w006.html
// -----------------------------------------
// シェーダB
// -----------------------------------------
// https://wgld.org/d/webgl2/w006.html
// https://github.com/danginsburg/opengles3-book/blob/master/Android_Java/Chapter_6/VertexArrayObjects/src/com/openglesbook/VertexArrayObjects/VAORenderer.java
// -----------------------------------------
class ES30a06ShaderB: ES30MgShader() {
    // 頂点シェーダ
    private val scv =
            """#version 300 es

            layout (location = 0) in  vec3  a_Position;

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

        /*
        // ----------------------------------------------
        // attributeハンドルに値をセット
        // ----------------------------------------------
        hATTR = intArrayOf(0)
        // 属性(頂点)
        // attribute属性を有効にする
        // ここで呼ばないと描画されない
        GLES30.glEnableVertexAttribArray(hATTR[0])
        MyGLES30Func.checkGlError("a_Position:glEnableVertexAttribArray")
        // attribute属性を登録
        GLES30.glVertexAttribPointer(hATTR[0],3,GLES30.GL_FLOAT,false,0,0)
        MyGLES30Func.checkGlError("a_Position:glVertexAttribPointer")
        */

        // ----------------------------------------------
        // uniformハンドルに値をセット
        // ----------------------------------------------
        hUNI = IntArray(1)

        // uniform(テクスチャユニット)
        hUNI[0] = GLES30.glGetUniformLocation(programHandle, "u_Texture")
        MyGLES30Func.checkGlError("u_Texture:glGetUniformLocation")

        return this
    }

    fun draw(vao: ES30VAOAbs,
             u_Texture: Int) {
        //Log.d(javaClass.simpleName,"draw:${model.javaClass.simpleName}")
        val model = vao.model

        GLES30.glUseProgram(programHandle)
        MyGLES30Func.checkGlError2("UseProgram",this,model)

        // VAOをバインド
        GLES30.glBindVertexArray(vao.hVAO[0])
        MyGLES30Func.checkGlError2("BindVertexArray",this,model)

        // uniform(テクスチャ座標)
        GLES30.glUniform1i(hUNI[0], u_Texture)
        MyGLES30Func.checkGlError2("u_Texture",this,model)

        // モデルを描画
        //GLES30.glDrawElements(GLES30.GL_TRIANGLES, model.datIdx.size, GLES30.GL_UNSIGNED_SHORT, model.bufIdx)
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, model.datIdx.size, GLES30.GL_UNSIGNED_SHORT, 0)

        // リソース解放
        //GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,0)
        //GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER,0)

        // VAO解放
        GLES30.glBindVertexArray(0)
    }
}
