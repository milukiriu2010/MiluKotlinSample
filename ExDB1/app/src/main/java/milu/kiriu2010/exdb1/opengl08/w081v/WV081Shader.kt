package milu.kiriu2010.exdb1.opengl08.w081v

import android.opengl.GLES20
import android.util.Log
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.shader.es20.ES20MgShader
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs
import milu.kiriu2010.math.MyMathUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

// ------------------------------------
// シェーダ(VBOを逐次更新):VBO使用
// ------------------------------------
// https://wgld.org/d/webgl/w081.html
// ------------------------------------
class WV081Shader: ES20MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            uniform   mat4  u_matMVP;
            uniform   float u_pointSize;

            void main() {
                gl_Position  = u_matMVP   * vec4(a_Position, 1.0);
                gl_PointSize = u_pointSize;
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            void main() {
                gl_FragColor = vec4(0.0, 0.7, 1.0, 1.0);
            }
            """.trimIndent()

    override fun loadShader(): ES20MgShader {
        // 頂点シェーダを生成
        svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES20Func.createProgram(svhandle,sfhandle)

        // ----------------------------------------------
        // attributeハンドルに値をセット
        // ----------------------------------------------
        hATTR = IntArray(1)
        // 属性(頂点)
        hATTR[0] = GLES20.glGetAttribLocation(programHandle, "a_Position").also {
            // attribute属性を有効にする
            // ここで呼ばないと描画されない
            GLES20.glEnableVertexAttribArray(it)
            MyGLES20Func.checkGlError("a_Position:glEnableVertexAttribArray")
            // attribute属性を登録
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false,0,0)
            MyGLES20Func.checkGlError("a_Position:glVertexAttribPointer")
        }
        MyGLES20Func.checkGlError("a_Position:glGetAttribLocation")

        // ----------------------------------------------
        // uniformハンドルに値をセット
        // ----------------------------------------------
        hUNI = IntArray(2)

        // uniform(モデル×ビュー×プロジェクション)
        hUNI[0] = GLES20.glGetUniformLocation(programHandle,"u_matMVP")
        MyGLES20Func.checkGlError("u_matMVP:glGetUniformLocation")

        // uniform(描画点の大きさ)
        hUNI[1] = GLES20.glGetUniformLocation(programHandle,"u_pointSize")
        MyGLES20Func.checkGlError("u_pointSize:glGetUniformLocation")

        return this
    }

    fun draw(model: MgModelAbs,
             bo: ES20VBOAbs,
             u_matMVP: FloatArray,
             u_pointSize: Float,
             t0: Float) {

        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UseProgram",this,model)

        // attribute(頂点)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[0])
        GLES20.glVertexAttribPointer(hATTR[0],3,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_Position",this,model)

        // 点を更新
        model.bufPos.position(0)
        val size = model.bufPos.limit()
        // size=3267
        //Log.d(javaClass.simpleName,"size[$size]")
        val buf = ByteBuffer.allocateDirect(model.datPos.toArray().size*4).run {
            order(ByteOrder.nativeOrder())

            asFloatBuffer().apply {
                put(model.datPos.toFloatArray())
                position(0)
            }
        }
        (0 until size step 3).forEach { i ->
            val t = MyMathUtil.cosf(t0)
            val x = model.datPos[i]
            val y = model.datPos[i+1]
            val z = model.datPos[i+2]
            buf.put(i,x+x*t)
            buf.put(i+1,y+y*t)
            buf.put(i+2,z+z*t)
        }
        buf.position(0)
        GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER,0,buf.capacity()*4,buf)
        MyGLES20Func.checkGlError2("a_Position:glBufferSubData",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glUniformMatrix4fv(hUNI[0],1,false,u_matMVP,0)
        MyGLES20Func.checkGlError2("u_matMVP",this,model)

        // uniform(描画点の大きさ)
        GLES20.glUniform1f(hUNI[1], u_pointSize)
        MyGLES20Func.checkGlError2("u_pointSize",this,model)

        // モデルを描画
        GLES20.glDrawArrays(GLES20.GL_POINTS,0,model.datPos.size/3)

        // リソース解放
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}
