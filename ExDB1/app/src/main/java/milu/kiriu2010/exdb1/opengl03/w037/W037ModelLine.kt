package milu.kiriu2010.exdb1.opengl03.w037

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

// ポイントスプライト
// https://wgld.org/d/webgl/w037.html
class W037ModelLine {
    // 頂点バッファ
    private lateinit var bufPos: FloatBuffer
    // 色バッファ
    private lateinit var bufCol: FloatBuffer
    // テクスチャコードバッファ
    private lateinit var bufTxc: FloatBuffer
    // インデックスバッファ
    private lateinit var bufIdx: ShortBuffer

    // 頂点データ
    private val datPos = arrayListOf<Float>()
    // 法線データ
    private val datNor = arrayListOf<Float>()
    // 色データ
    private val datCol = arrayListOf<Float>()
    // インデックスデータ
    private val datIdx = arrayListOf<Short>()

    init {
        // 頂点データ
        datPos.addAll(arrayListOf(-1f,-1f,0f))
        datPos.addAll(arrayListOf(1f,-1f,0f))
        datPos.addAll(arrayListOf(-1f,1f,0f))
        datPos.addAll(arrayListOf(1f,1f,0f))

        // 色データ
        datCol.addAll(arrayListOf(1f,1f,1f,1f))
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

        // インデックスバッファ
        bufIdx = ByteBuffer.allocateDirect(datIdx.toArray().size * 2).run {
            order(ByteOrder.nativeOrder())

            asShortBuffer().apply {
                put(datIdx.toShortArray())
                position(0)
            }
        }
    }

    fun draw(programHandle: Int,
             matMVP: FloatArray,
             u_pointSize: Float,
             u_Texture0: Int,
             u_useTexture: Int,
             lineType: Int ) {

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

        // uniform(描画点のサイズ)
        GLES20.glGetUniformLocation(programHandle,"u_pointSize").also {
            GLES20.glUniform1f(it,u_pointSize)
        }
        MyGLFunc.checkGlError("u_pointSize")

        /*
        // uniform(テクスチャ0)
        GLES20.glGetUniformLocation(programHandle, "u_Texture0").also {
            GLES20.glUniform1i(it, u_Texture0)
        }
        MyGLFunc.checkGlError("u_Texture0")
        */

        // uniform(テクスチャを使うかどうか)
        GLES20.glGetUniformLocation(programHandle, "u_useTexture").also {
            GLES20.glUniform1i(it, u_useTexture)
        }
        MyGLFunc.checkGlError("u_useTexture")

        // モデルを描画
        GLES20.glDrawArrays(lineType, 0, datPos.size/3)
        // モデルを描画
        //GLES20.glDrawElements(GLES20.GL_TRIANGLES, datIdx.size, GLES20.GL_UNSIGNED_SHORT, bufIdx)
    }

}
