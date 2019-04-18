package milu.kiriu2010.exdb1.opengl01.w015

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

// https://developer.android.com/training/graphics/opengl/shapes
class W015Model {
    // 頂点バッファ
    private lateinit var bufPos: FloatBuffer
    // 色バッファ
    private lateinit var bufCol: FloatBuffer

    // 頂点データ
    private val datPos = arrayListOf<Float>()
    // 色データ
    private val datCol = arrayListOf<Float>()

    init {
        // 頂点データ
        datPos.addAll(arrayListOf(-0.5f,-0.25f,0f))
        datPos.addAll(arrayListOf(0.5f,-0.25f,0f))
        datPos.addAll(arrayListOf(0f,0.559016994f,0f))

        // 色データ
        datCol.addAll(arrayListOf(1f,0f,0f,1f))
        datCol.addAll(arrayListOf(0f,1f,0f,1f))
        datCol.addAll(arrayListOf(0f,0f,1f,1f))

        // 頂点バッファ
        bufPos = ByteBuffer.allocateDirect(datPos.toArray().size * 4).run {
            order(ByteOrder.nativeOrder())

            asFloatBuffer().apply {
                put(datPos.toFloatArray())
                position(0)
            }
        }

        // 色バッファ
        bufCol = ByteBuffer.allocateDirect(datCol.toArray().size * 4).run {
            order(ByteOrder.nativeOrder())

            asFloatBuffer().apply {
                put(datCol.toFloatArray())
                position(0)
            }
        }
    }

    fun draw(programHandle: Int,
             matMVP: FloatArray
    ) {
        // attribute(頂点)
        bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Position")

        // attribute(色)
        bufCol.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Color").also {
            GLES20.glVertexAttribPointer(it,4,GLES20.GL_FLOAT,false, 4*4, bufCol)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Color")

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLFunc.checkGlError("u_matMVP")

        // モデル描画
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3)

        // 頂点配列を無効化
        GLES20.glDisableVertexAttribArray(programHandle)
    }
}
