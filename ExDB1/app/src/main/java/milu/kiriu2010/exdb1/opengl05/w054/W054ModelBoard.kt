package milu.kiriu2010.exdb1.opengl05.w054

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

// 板ポリゴン
// https://wgld.org/d/webgl/w049.html
class W054ModelBoard {
    // 頂点バッファ
    lateinit var bufPos: FloatBuffer
    // 法線バッファ
    lateinit var bufNor: FloatBuffer
    // 色バッファ
    lateinit var bufCol: FloatBuffer
    // テクスチャコードバッファ
    lateinit var bufTxc: FloatBuffer
    // インデックスバッファ
    lateinit var bufIdx: ShortBuffer

    // 頂点データ
    val datPos = arrayListOf<Float>()
    // 法線データ
    val datNor = arrayListOf<Float>()
    // 色データ
    val datCol = arrayListOf<Float>()
    // テクスチャコードデータ
    val datTxc = arrayListOf<Float>()
    // インデックスデータ
    val datIdx = arrayListOf<Short>()

    init {
        // 頂点データ
        datPos.addAll(listOf(-1f,1f,0f))
        datPos.addAll(listOf(1f,1f,0f))
        datPos.addAll(listOf(-1f,-1f,0f))
        datPos.addAll(listOf(1f,-1f,0f))

        // テクスチャ座標データ
        datTxc.addAll(listOf(0f,0f));
        datTxc.addAll(listOf(1f,0f));
        datTxc.addAll(listOf(0f,1f));
        datTxc.addAll(listOf(1f,1f));

        // インデックスデータ
        datIdx.addAll(listOf(0,2,1))
        datIdx.addAll(listOf(2,3,1))


        // 頂点バッファ
        bufPos = ByteBuffer.allocateDirect(datPos.toArray().size * 4).run {
            order(ByteOrder.nativeOrder())

            asFloatBuffer().apply {
                put(datPos.toFloatArray())
                position(0)
            }
        }

        // 法線バッファ
        bufNor = ByteBuffer.allocateDirect(datNor.toArray().size * 4).run {
            order(ByteOrder.nativeOrder())

            asFloatBuffer().apply {
                put(datNor.toFloatArray())
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

        // テクスチャコードバッファ
        bufTxc = ByteBuffer.allocateDirect(datTxc.toArray().size * 4).run {
            order(ByteOrder.nativeOrder())

            asFloatBuffer().apply {
                put(datTxc.toFloatArray())
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
             u_Texture0: Int,
             u_grayScale: Int,
             u_sepiaScale: Int) {

        GLES20.glUseProgram(programHandle)
        MyGLFunc.checkGlError("Board:Draw:UseProgram")

        // attribute(頂点)
        bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("Board:Draw:a_Position")

        // attribute(テクスチャ座標)
        bufTxc.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_TexCoord").also {
            GLES20.glVertexAttribPointer(it,2,GLES20.GL_FLOAT,false, 2*4, bufTxc)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("Board:Draw:a_TexCoord")

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLFunc.checkGlError("Board:Draw:u_matMVP")

        // uniform(テクスチャ0)
        GLES20.glGetUniformLocation(programHandle, "u_Texture0").also {
            GLES20.glUniform1i(it, u_Texture0)
        }
        MyGLFunc.checkGlError("u_Texture0")

        // uniform(グレースケールを使うかどうか)
        GLES20.glGetUniformLocation(programHandle, "u_grayScale").also {
            GLES20.glUniform1i(it, u_grayScale)
        }
        MyGLFunc.checkGlError("Board:Draw:u_grayScale")

        // uniform(セピア調にするかどうか)
        GLES20.glGetUniformLocation(programHandle, "u_sepiaScale").also {
            GLES20.glUniform1i(it, u_sepiaScale)
        }
        MyGLFunc.checkGlError("Board:Draw:u_sepiaScale")

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, datIdx.size, GLES20.GL_UNSIGNED_SHORT, bufIdx)
    }

}