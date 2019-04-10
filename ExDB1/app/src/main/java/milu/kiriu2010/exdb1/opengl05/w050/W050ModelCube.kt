package milu.kiriu2010.exdb1.opengl05.w050

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLUtils
import milu.kiriu2010.exdb1.opengl.MyGLFunc
import milu.kiriu2010.gui.basic.MyColor
import java.lang.RuntimeException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

// フレームバッファ
// https://wgld.org/d/webgl/w040.html
class W050ModelCube {
    // 頂点バッファ
    private lateinit var bufPos: FloatBuffer
    // 法線バッファ
    private lateinit var bufNor: FloatBuffer
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
    // テクスチャコードデータ
    private val datTxc = arrayListOf<Float>()
    // インデックスデータ
    private val datIdx = arrayListOf<Short>()

    init {
        // 立方体のデータを生成
        createPath(2f, floatArrayOf(1f,1f,1f,1f))

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
             matM: FloatArray,
             matMVP: FloatArray,
             u_vecEye: FloatArray,
             u_CubeTexture: Int,
             u_Reflection: Int) {

        // attribute(頂点)
        bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Position")

        // attribute(法線)
        bufNor.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Normal").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, bufNor)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Normal")

        // attribute(色)
        bufCol.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Color").also {
            GLES20.glVertexAttribPointer(it,4,GLES20.GL_FLOAT,false, 4*4, bufCol)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Color")

        // uniform(モデル)
        GLES20.glGetUniformLocation(programHandle,"u_matM").also {
            GLES20.glUniformMatrix4fv(it,1,false,matM,0)
        }
        MyGLFunc.checkGlError("u_matM")

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLFunc.checkGlError("u_matMVP")

        // uniform(視点座標)
        GLES20.glGetUniformLocation(programHandle,"u_vecEye").also {
            GLES20.glUniform3fv(it,1,u_vecEye,0)
        }

        // uniform(キューブテクスチャ)
        GLES20.glGetUniformLocation(programHandle, "u_CubeTexture").also {
            GLES20.glUniform1i(it, u_CubeTexture)
        }
        MyGLFunc.checkGlError("u_CubeTexture")

        // uniform(反射するかどうか)
        GLES20.glGetUniformLocation(programHandle,"u_Reflection").also {
            GLES20.glUniform1i(it,u_Reflection)
        }

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, datIdx.size, GLES20.GL_UNSIGNED_SHORT, bufIdx)
    }

    // 立方体の頂点データを生成
    private fun createPath( side: Float, color: FloatArray? = null ) {
        datPos.clear()
        datNor.clear()
        datCol.clear()
        datTxc.clear()
        datIdx.clear()

        val hs = side * 0.5f

        // 頂点データ
        // 0-3
        datPos.addAll(arrayListOf(-hs,-hs,hs))
        datPos.addAll(arrayListOf(hs,-hs,hs))
        datPos.addAll(arrayListOf(hs,hs,hs))
        datPos.addAll(arrayListOf(-hs,hs,hs))
        // 4-7
        datPos.addAll(arrayListOf(-hs,-hs,-hs))
        datPos.addAll(arrayListOf(-hs,hs,-hs))
        datPos.addAll(arrayListOf(hs,hs,-hs))
        datPos.addAll(arrayListOf(hs,-hs,-hs))
        // 8-11
        datPos.addAll(arrayListOf(-hs,hs,-hs))
        datPos.addAll(arrayListOf(-hs,hs,hs))
        datPos.addAll(arrayListOf(hs,hs,hs))
        datPos.addAll(arrayListOf(hs,hs,-hs))
        // 12-15
        datPos.addAll(arrayListOf(-hs,-hs,-hs))
        datPos.addAll(arrayListOf(hs,-hs,-hs))
        datPos.addAll(arrayListOf(hs,-hs,hs))
        datPos.addAll(arrayListOf(-hs,-hs,hs))
        // 16-19
        datPos.addAll(arrayListOf(hs,-hs,-hs))
        datPos.addAll(arrayListOf(hs,hs,-hs))
        datPos.addAll(arrayListOf(hs,hs,hs))
        datPos.addAll(arrayListOf(hs,-hs,hs))
        // 20-23
        datPos.addAll(arrayListOf(-hs,-hs,-hs))
        datPos.addAll(arrayListOf(-hs,-hs,hs))
        datPos.addAll(arrayListOf(-hs,hs,hs))
        datPos.addAll(arrayListOf(-hs,hs,-hs))

        // 法線データ
        // 0-3
        datNor.addAll(arrayListOf(-1f,-1f,1f))
        datNor.addAll(arrayListOf(1f,-1f,1f))
        datNor.addAll(arrayListOf(1f,1f,1f))
        datNor.addAll(arrayListOf(-1f,1f,1f))
        // 4-7
        datNor.addAll(arrayListOf(-1f,-1f,-1f))
        datNor.addAll(arrayListOf(-1f,1f,-1f))
        datNor.addAll(arrayListOf(1f,1f,-1f))
        datNor.addAll(arrayListOf(1f,-1f,-1f))
        // 8-11
        datNor.addAll(arrayListOf(-1f,1f,-1f))
        datNor.addAll(arrayListOf(-1f,1f,1f))
        datNor.addAll(arrayListOf(1f,1f,1f))
        datNor.addAll(arrayListOf(1f,1f,-1f))
        // 12-15
        datNor.addAll(arrayListOf(-1f,-1f,-1f))
        datNor.addAll(arrayListOf(1f,-1f,-1f))
        datNor.addAll(arrayListOf(1f,-1f,1f))
        datNor.addAll(arrayListOf(-1f,-1f,1f))
        // 16-19
        datNor.addAll(arrayListOf(1f,-1f,-1f))
        datNor.addAll(arrayListOf(1f,1f,-1f))
        datNor.addAll(arrayListOf(1f,1f,1f))
        datNor.addAll(arrayListOf(1f,-1f,1f))
        // 20-23
        datNor.addAll(arrayListOf(-1f,-1f,-1f))
        datNor.addAll(arrayListOf(-1f,-1f,1f))
        datNor.addAll(arrayListOf(-1f,1f,1f))
        datNor.addAll(arrayListOf(-1f,1f,-1f))

        // 色データ
        (0..datPos.size/3).forEach { i ->
            if ( color != null ) {
                datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
            }
            else {
                val tc = MyColor.hsva(360/datPos.size/3*i,1f,1f,1f)
                datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
            }
        }

        // テクスチャコードのデータ
        (0..5).forEach {
            datTxc.addAll(arrayListOf(0f,0f))
            datTxc.addAll(arrayListOf(1f,0f))
            datTxc.addAll(arrayListOf(1f,1f))
            datTxc.addAll(arrayListOf(0f,1f))
        }

        // インデックスデータ
        datIdx.addAll(arrayListOf(0,1,2))
        datIdx.addAll(arrayListOf(0,2,3))
        datIdx.addAll(arrayListOf(4,5,6))
        datIdx.addAll(arrayListOf(4,6,7))
        datIdx.addAll(arrayListOf(8,9,10))
        datIdx.addAll(arrayListOf(8,10,11))
        datIdx.addAll(arrayListOf(12,13,14))
        datIdx.addAll(arrayListOf(12,14,15))
        datIdx.addAll(arrayListOf(16,17,18))
        datIdx.addAll(arrayListOf(16,18,19))
        datIdx.addAll(arrayListOf(20,21,22))
        datIdx.addAll(arrayListOf(20,22,23))

    }

    fun activateTexture(id: Int, textures: IntArray, bmp: Bitmap, doRecycle: Boolean = false) {
        // 有効にするテクスチャユニットを指定
        when (id) {
            0 -> GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
            1 -> GLES20.glActiveTexture(GLES20.GL_TEXTURE1)
        }

        // テクスチャをバインドする
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[id])
        MyGLFunc.checkGlError("glBindTexture")

        // 縮小時の補完設定
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        // 拡大時の補完設定
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)

        // ビットマップをテクスチャに設定
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0)
        MyGLFunc.checkGlError("texImage2D")

        // ミップマップを生成
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)

        // テクスチャのバインドを無効化
        //GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, null)

        //if ( doRecycle ) bmp.recycle()

        if (textures[id] == 0) {
            throw RuntimeException("Error loading texture[${id}]")
        }
    }

}