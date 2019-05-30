package milu.kiriu2010.exdb1.mgl01.vbo01

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.shader.es20.ES20MgShader

// ------------------------------------------
// 特殊効果なし(VBO)
// ------------------------------------------
// 2019.05.30
// ------------------------------------------
class ES20VBO01Shader: ES20MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3 a_Position;
            attribute vec4 a_Color;
            uniform   mat4 u_matMVP;
            varying   vec4 v_Color;

            void main() {
                v_Color        = a_Color;
                gl_Position    = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump float;
            varying   vec4 v_Color;

            void main() {
                gl_FragColor   = v_Color;
            }
            """.trimIndent()

    // 属性のハンドル
    private lateinit var hATTR: IntArray

    override fun loadShader(): ES20MgShader {
        // 頂点シェーダを生成
        svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES20Func.createProgram(svhandle,sfhandle, arrayOf())

        // 属性ハンドルに値をセット
        hATTR = IntArray(2)
        // 属性(頂点)の配列を有効にする
        hATTR[0] = GLES20.glGetAttribLocation(programHandle, "a_Position").also {
            GLES20.glEnableVertexAttribArray(it)
        }
        // 属性(色)の配列を有効にする
        hATTR[1] = GLES20.glGetAttribLocation(programHandle, "a_Color").also{
            GLES20.glEnableVertexAttribArray(it)
        }

        return this
    }

    // 面塗りつぶし
    fun draw(model: MgModelAbs,
             u_matMVP: FloatArray,
             mode: Int = GLES20.GL_TRIANGLES) {
        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UseProgram",this,model)

        // attribute(頂点)
        model.bufPos.position(0)
        // get handle to vertex shader's vPosition member
        val hPosition = GLES20.glGetAttribLocation(programHandle, "a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false,3*4,model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_Position",this,model)

        // attribute(色)
        model.bufCol.position(0)
        val hColor = GLES20.glGetAttribLocation(programHandle,"a_Color").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 4*4, model.bufCol)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_Color",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,u_matMVP,0)
        }

        // モデルを描画
        when (mode) {
            // 面を描画
            GLES20.GL_TRIANGLES -> {
                GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
            }
            // 線を描画
            GLES20.GL_LINES -> {
                val cnt = model.datPos.size/3
                GLES20.glDrawArrays(GLES20.GL_LINES,0,cnt)
            }
            // 線を描画
            GLES20.GL_LINE_STRIP -> {
                val cnt = model.datPos.size/3
                GLES20.glDrawArrays(GLES20.GL_LINE_STRIP,0,cnt)
            }
        }

        // リソース解放
        GLES20.glDisableVertexAttribArray(hPosition)
        GLES20.glDisableVertexAttribArray(hColor)
    }

    // 面塗りつぶし
    fun drawVIBO(model: MgModelAbs,
                bo: ES20viBO,
             u_matMVP: FloatArray,
             mode: Int = GLES20.GL_TRIANGLES) {
        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UseProgram",this,model)

        // attribute(頂点)
        model.bufPos.position(0)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[0])
        //GLES20.glVertexAttribPointer(hATTR[0],3,GLES20.GL_FLOAT,false,3*4,model.bufPos)
        GLES20.glVertexAttribPointer(hATTR[0],3,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_Position",this,model)

        // attribute(色)
        model.bufCol.position(0)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[1])
        //GLES20.glVertexAttribPointer(hATTR[1],3,GLES20.GL_FLOAT,false,3*4,model.bufCol)
        GLES20.glVertexAttribPointer(hATTR[1],3,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_Color",this,model)

        // インデックス
        model.bufIdx.position(0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,bo.hIBO[0])

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,u_matMVP,0)
        }

        // モデルを描画
        when (mode) {
            // 面を描画
            GLES20.GL_TRIANGLES -> {
                //GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
                GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, 0)
            }
            // 線を描画
            GLES20.GL_LINES -> {
                val cnt = model.datPos.size/3
                GLES20.glDrawArrays(GLES20.GL_LINES,0,cnt)
            }
            // 線を描画
            GLES20.GL_LINE_STRIP -> {
                val cnt = model.datPos.size/3
                GLES20.glDrawArrays(GLES20.GL_LINE_STRIP,0,cnt)
            }
        }

        // リソース解放
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}
