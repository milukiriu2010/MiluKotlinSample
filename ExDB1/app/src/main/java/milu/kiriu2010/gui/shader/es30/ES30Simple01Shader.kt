package milu.kiriu2010.gui.shader.es30

import android.opengl.GLES30
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES30Func

// ------------------------------------------
// 特殊効果なし
// ------------------------------------------
// 2019.04.27 コメントアウト
// 2019.05.22 リソース解放
// ------------------------------------------
class ES30Simple01Shader: ES30MgShader() {
    // 頂点シェーダ
    private val scv =
            """#version 300 es
            in vec3 a_Position;
            in vec4 a_Color;

            uniform  mat4 u_matMVP;

            out vec4 v_Color;

            void main() {
                v_Color        = a_Color;
                gl_Position    = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """#version 300 es
            precision mediump float;

            in vec4 v_Color;

            out vec4 o_Color;

            void main() {
                o_Color = v_Color;
            }
            """.trimIndent()

    override fun loadShader(): ES30MgShader {
        // 頂点シェーダを生成
        svhandle = MyGLES30Func.loadShader(GLES30.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        sfhandle = MyGLES30Func.loadShader(GLES30.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES30Func.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Color") )

        return this
    }

    // 面塗りつぶし
    fun draw(model: MgModelAbs,
             u_matMVP: FloatArray,
             mode: Int = GLES30.GL_TRIANGLES) {
        GLES30.glUseProgram(programHandle)
        MyGLES30Func.checkGlError2("UseProgram",this,model)

        // attribute(頂点)
        model.bufPos.position(0)
        // get handle to vertex shader's vPosition member
        val hPosition = GLES30.glGetAttribLocation(programHandle, "a_Position").also {
            GLES30.glVertexAttribPointer(it,3,GLES30.GL_FLOAT,false,3*4,model.bufPos)
            GLES30.glEnableVertexAttribArray(it)
        }
        MyGLES30Func.checkGlError2("a_Position",this,model)

        // attribute(色)
        model.bufCol.position(0)
        val hColor = GLES30.glGetAttribLocation(programHandle,"a_Color").also {
            GLES30.glVertexAttribPointer(it,3,GLES30.GL_FLOAT,false, 4*4, model.bufCol)
            GLES30.glEnableVertexAttribArray(it)
        }
        MyGLES30Func.checkGlError2("a_Color",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES30.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES30.glUniformMatrix4fv(it,1,false,u_matMVP,0)
        }

        // モデルを描画
        when (mode) {
            // 面を描画
            GLES30.GL_TRIANGLES -> {
                GLES30.glDrawElements(GLES30.GL_TRIANGLES, model.datIdx.size, GLES30.GL_UNSIGNED_SHORT, model.bufIdx)
            }
            // 線を描画
            GLES30.GL_LINES -> {
                val cnt = model.datPos.size/3
                GLES30.glDrawArrays(GLES30.GL_LINES,0,cnt)
            }
            // 線を描画
            GLES30.GL_LINE_STRIP -> {
                val cnt = model.datPos.size/3
                GLES30.glDrawArrays(GLES30.GL_LINE_STRIP,0,cnt)
            }
        }

        // リソース解放
        GLES30.glDisableVertexAttribArray(hPosition)
        GLES30.glDisableVertexAttribArray(hColor)
    }

}
