package milu.kiriu2010.exdb1.opengl03.w037

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLUtils
import milu.kiriu2010.exdb1.opengl.MyGLFunc
import milu.kiriu2010.gui.basic.MyColor
import milu.kiriu2010.math.MyMathUtil
import java.lang.RuntimeException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin

// ポイントスプライト
// https://wgld.org/d/webgl/w037.html
class W037ModelSphere {
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
        // 球体のデータを生成
        createPath(16,16,2f)

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

        /*
        // テクスチャコードバッファ
        bufTxc = ByteBuffer.allocateDirect(datTxc.toArray().size * 4).run {
            order(ByteOrder.nativeOrder())

            asFloatBuffer().apply {
                put(datTxc.toFloatArray())
                position(0)
            }
        }
        */

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
             u_useTexture: Int) {

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
            GLES20.glUniform1f(it,u_pointSize*3f)
        }
        MyGLFunc.checkGlError("u_pointSize")

        // uniform(テクスチャ0)
        GLES20.glGetUniformLocation(programHandle, "u_Texture0").also {
            GLES20.glUniform1i(it, u_Texture0)
        }
        MyGLFunc.checkGlError("u_Texture0")

        // uniform(テクスチャを使うかどうか)
        GLES20.glGetUniformLocation(programHandle, "u_useTexture").also {
            GLES20.glUniform1i(it, u_useTexture)
        }
        MyGLFunc.checkGlError("u_useTexture")

        // モデルを描画
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, datPos.size/3)
        // モデルを描画
        //GLES20.glDrawElements(GLES20.GL_TRIANGLES, datIdx.size, GLES20.GL_UNSIGNED_SHORT, bufIdx)
    }

    // 球体の頂点データを生成
    private fun createPath( row: Int, column: Int, rad: Float, color: FloatArray? = null ) {
        datPos.clear()
        datNor.clear()
        datCol.clear()
        datIdx.clear()

        (0..row).forEach { i ->
            var r = PI.toFloat() / row.toFloat() * i.toFloat()
            var ry = cos(r)
            var rr = sin(r)
            (0..column).forEach {  ii ->
                var tr = PI.toFloat() * 2f/column.toFloat() * ii.toFloat()
                var tx = rr * rad * cos(tr)
                var ty = ry * rad;
                var tz = rr * rad * sin(tr)
                var rx = rr * cos(tr)
                var rz = rr * sin(tr)
                if ( color != null ) {
                    datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
                }
                else {
                    var tc = MyColor.hsva(360/row*i,1f,1f,1f)
                    datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
                }
                datPos.addAll(arrayListOf(tx,ty,tz))
                datNor.addAll(arrayListOf(rx,ry,rz))
            }

            (0 until row).forEach { i ->
                (0 until column).forEach { ii ->
                    val r = (column+1)*i+ii
                    datIdx.addAll(arrayListOf<Short>(r.toShort(),(r+1).toShort(),(r+column+2).toShort()))
                    datIdx.addAll(arrayListOf<Short>(r.toShort(),(r+column+2).toShort(),(r+column+1).toShort()))
                }
            }
        }
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