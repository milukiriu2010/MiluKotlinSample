package milu.kiriu2010.exdb1.opengl07.w071

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.shader.MgShader

// シェーダ(ズームブラー)
class W071ShaderMapping: MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute float a_Index;

            varying   vec3  v_Color;

            const float frag     = 1.0/16.0;
            const float texShift = 0.5 * frag;

            void main() {
                v_Color   = (normalize(a_Position)+1.0) * 0.5;
                float pu = fract(a_Index*frag)*2.0 - 1.0;
                float pv = floor(a_Index*frag)*frag*2.0 - 1.0;
                gl_Position  = vec4(pu+texShift, pv+texShift, 0.0, 1.0);
                gl_PointSize = 1.0;
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            varying  vec3      v_Color;

            void main() {
                gl_FragColor = vec4(v_Color, 1.0);
            }
            """.trimIndent()


    override fun loadShader(): MgShader {
        // 頂点シェーダを生成
        val svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Index") )
        return this
    }

    fun draw(model: MgModelAbs) {

        GLES20.glUseProgram(programHandle)

        // attribute(頂点)
        model.bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Position:${model.javaClass.simpleName}")

        // attribute(インデックス)
        model.bufIdx.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Index").also {
            GLES20.glVertexAttribPointer(it,1,GLES20.GL_FLOAT,false, 1*4, model.bufIdx)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError2("a_Index",this,model)

        // モデルを描画
        //GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
        GLES20.glDrawArrays(GLES20.GL_POINTS,0,model.datIdx.size)
    }
}
