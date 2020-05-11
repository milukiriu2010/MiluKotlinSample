package milu.kiriu2010.exdb1.es30x02.a09

import android.opengl.GLES30
import milu.kiriu2010.gui.basic.MyGLES30Func
import milu.kiriu2010.gui.shader.es30.ES30MgShader
import milu.kiriu2010.gui.vbo.es30.ES30VAOAbs

// ------------------------------------
// UBO
// OpenGL ES 3.0
// シェーダB
// ------------------------------------
// https://wgld.org/d/webgl2/w009.html
// ------------------------------------
class ES30a09ShaderB: ES30MgShader() {
    // 頂点シェーダ
    private val scv =
            """#version 300 es

            layout (location = 0) in vec3  a_Position;

            layout (std140) uniform matrix {
                mat4 mvp;
            } u_mat;
            uniform  float u_scale;

            void main() {
                gl_Position = u_mat.mvp * vec4(a_Position * u_scale, 1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """#version 300 es

            precision highp   float;

            layout (std140) uniform material {
                vec4 base;
            } u_color;

            out vec4  o_FragColor;

            void main() {
                o_FragColor = vec4(1.0 - u_color.base.rgb, u_color.base.a);
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
        // UBIハンドルに値をセット
        // ----------------------------------------------
        hUBI = IntArray(2)

        // UBO(matrix)
        hUBI[0] = GLES30.glGetUniformBlockIndex(programHandle,"matrix")
        MyGLES30Func.checkGlError("matrix:glGetUniformBlockIndex")
        GLES30.glUniformBlockBinding(programHandle,hUBI[0],0)
        MyGLES30Func.checkGlError("matrix:glUniformBlockBinding")

        // UBO(material)
        hUBI[1] = GLES30.glGetUniformBlockIndex(programHandle,"material")
        MyGLES30Func.checkGlError("material:glGetUniformBlockIndex")
        GLES30.glUniformBlockBinding(programHandle,hUBI[1],1)
        MyGLES30Func.checkGlError("material:glUniformBlockBinding")

        // ----------------------------------------------
        // uniformハンドルに値をセット
        // ----------------------------------------------
        hUNI = IntArray(1)

        // uniform(スケール)
        hUNI[0] = GLES30.glGetUniformLocation(programHandle,"u_scale")
        MyGLES30Func.checkGlError("u_scale:glGetUniformLocation")

        return this
    }

    fun draw(vao: ES30VAOAbs,
             u_scale: Float) {
        //Log.d(javaClass.simpleName,"draw:${model.javaClass.simpleName}")
        val model = vao.model

        GLES30.glUseProgram(programHandle)
        MyGLES30Func.checkGlError2("UseProgram",this,model)
        //Log.d(javaClass.simpleName,"draw:glUseProgram")

        // VAOをバインド
        GLES30.glBindVertexArray(vao.hVAO[0])
        MyGLES30Func.checkGlError2("BindVertexArray",this,model)
        //Log.d(javaClass.simpleName,"draw:glBindVertexArray")

        // uniform(スケール)
        GLES30.glUniform1f(hUNI[0],u_scale)
        MyGLES30Func.checkGlError2("u_scale",this,model)

        // モデルを描画
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, model.datIdx.size, GLES30.GL_UNSIGNED_SHORT, 0)
        MyGLES30Func.checkGlError2("glDrawElements",this,model)

        // リソース解放
        //GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,0)
        //GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER,0)

        // VAO解放
        GLES30.glBindVertexArray(0)
        //MyGLES30Func.checkGlError2("draw:glBindVertexArray0",this,model)
    }
}
