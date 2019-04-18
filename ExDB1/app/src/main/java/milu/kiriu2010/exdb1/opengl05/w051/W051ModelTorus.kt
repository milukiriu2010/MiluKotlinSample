package milu.kiriu2010.exdb1.opengl05.w051

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLUtils
import milu.kiriu2010.gui.basic.MyGLFunc
import java.lang.RuntimeException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin

// ステンシルバッファでアウトライン
// https://wgld.org/d/webgl/w039.html
class W051ModelTorus {
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
        // トーラスのデータを生成
        createPath(16,16,1.0f,2.0f, floatArrayOf(1f,1f,1f,1f))

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

    fun drawShaderDepth(programHandle: Int,
                        matLight: FloatArray,
                        u_depthBuffer: Int) {

        // attribute(頂点)
        bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("Torus:DrawDepth:a_Position")

        /*
        // attribute(法線)
        bufNor.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Normal").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, bufNor)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("Torus:DrawDepth:a_Normal")

        // attribute(色)
        bufCol.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Color").also {
            GLES20.glVertexAttribPointer(it,4,GLES20.GL_FLOAT,false, 4*4, bufCol)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("Torus:DrawDepth:a_Color")
        */

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matLight,0)
        }
        MyGLFunc.checkGlError("u_matMVP")

        // uniform(深度値を使うかどうか)
        GLES20.glGetUniformLocation(programHandle, "u_depthBuffer").also {
            GLES20.glUniform1i(it, u_depthBuffer)
        }
        MyGLFunc.checkGlError("Torus:DrawDepth:u_depthBuffer:${u_depthBuffer}")

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, datIdx.size, GLES20.GL_UNSIGNED_SHORT, bufIdx)
    }

    fun draw(programHandle: Int,
             matM: FloatArray,
             matMVP: FloatArray,
             matINV: FloatArray,
             matTex: FloatArray,
             matLight: FloatArray,
             u_vecLight: FloatArray,
             u_Texture0: Int,
             u_depthBuffer: Int) {

        // attribute(頂点)
        bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("Torus:Draw:a_Position")

        // attribute(法線)
        bufNor.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Normal").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, bufNor)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("Torus:Draw:a_Normal")

        // attribute(色)
        bufCol.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Color").also {
            GLES20.glVertexAttribPointer(it,4,GLES20.GL_FLOAT,false, 4*4, bufCol)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("Torus:Draw:a_Color")

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

        // uniform(逆行列)
        GLES20.glGetUniformLocation(programHandle,"u_matINV").also {
            GLES20.glUniformMatrix4fv(it,1,false,matINV,0)
        }
        MyGLFunc.checkGlError("u_matINV")

        // uniform(テクスチャ射影変換用行列)
        GLES20.glGetUniformLocation(programHandle,"u_matTex").also {
            GLES20.glUniformMatrix4fv(it,1,false,matTex,0)
        }
        MyGLFunc.checkGlError("u_matTex")

        // uniform(ライト視点の座標変換行列)
        GLES20.glGetUniformLocation(programHandle,"u_matLight").also {
            GLES20.glUniformMatrix4fv(it,1,false,matLight,0)
        }
        MyGLFunc.checkGlError("u_matLight")

        // uniform(ライティング)
        GLES20.glGetUniformLocation(programHandle,"u_vecLight").also {
            GLES20.glUniform3fv(it,1,u_vecLight,0)
        }
        MyGLFunc.checkGlError("u_vecLight")

        // uniform(テクスチャ0)
        GLES20.glGetUniformLocation(programHandle, "u_Texture0").also {
            GLES20.glUniform1i(it, u_Texture0)
        }
        MyGLFunc.checkGlError("u_Texture0")

        // uniform(深度値を使うかどうか)
        GLES20.glGetUniformLocation(programHandle, "u_depthBuffer").also {
            GLES20.glUniform1i(it, u_depthBuffer)
        }
        MyGLFunc.checkGlError("Torus:Draw:u_depthBuffer")

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, datIdx.size, GLES20.GL_UNSIGNED_SHORT, bufIdx)
    }


    // ----------------------------------------------------------------------
    // 第一引数はパイプを形成する円をいくつの頂点で表現するのかを指定します。
    // 大きな数値を指定すればパイプの断面が円形に近づきますが、
    // 逆に小さな数値を指定すればパイプの断面はカクカクになっていきます。
    // ----------------------------------------------------------------------
    // 第二引数はパイプをどれくらい分割するのかを指定します。
    // この数値を大きくすると、トーラスは滑らかな輪を形成するようになり、
    // 小さな数値を指定すればカクカクの輪になります。
    // ----------------------------------------------------------------------
    // 第三引数は生成されるパイプそのものの半径です。
    // ----------------------------------------------------------------------
    // 第四引数が原点からパイプの中心までの距離になります。
    // ----------------------------------------------------------------------
    private fun createPath(row: Int, column: Int, irad: Float, orad: Float, colorArray: FloatArray? = null) {
        datPos.clear()
        datNor.clear()
        datCol.clear()
        datTxc.clear()
        datIdx.clear()

        (0..row).forEach { i ->
            var r = PI.toFloat() *2f/row.toFloat()*i.toFloat()
            var rr = cos(r)
            var ry = sin(r)
            (0..column).forEach { ii ->
                val tr = PI.toFloat() *2f/column.toFloat()*ii.toFloat()
                val tx = (rr*irad+orad)*cos(tr)
                val ty = ry*irad
                val tz = (rr*irad+orad)*sin(tr)
                val rx = rr * cos(tr)
                val rz = rr * sin(tr)
                datPos.addAll(arrayListOf<Float>(tx,ty,tz))
                datNor.addAll(arrayListOf<Float>(rx,ry,rz))
                if (colorArray != null) {
                    datCol.addAll(arrayListOf<Float>(colorArray[0],colorArray[1],colorArray[2],colorArray[3]))
                }
                else {
                    val tc = hsva(360/column*ii,1f,1f,1f)
                    datCol.addAll(arrayListOf<Float>(tc[0],tc[1],tc[2],tc[3]))
                }
                val rs = 1f/column.toFloat()*ii.toFloat()
                var rt = 1f/row.toFloat()*ii.toFloat()+0.5f
                if (rt>1f) {
                    rt -= 1f
                }
                rt = 1f-rt
                datTxc.addAll(arrayListOf(rs,rt))
            }

            (0 until row).forEach { i ->
                (0 until column).forEach { ii ->
                    val r = (column+1)*i+ii
                    datIdx.addAll(arrayListOf<Short>(r.toShort(),(r+column+1).toShort(),(r+1).toShort()))
                    datIdx.addAll(arrayListOf<Short>((r+column+1).toShort(),(r+column+2).toShort(),(r+1).toShort()))
                }
            }
        }
    }

    // ---------------------------------------------------------
    // HSVカラー取得用関数
    // ---------------------------------------------------------
    // HSV では、色相は 0 ～ 360 の範囲に収まっている必要がありますが、
    // それ以上に大きな数値を指定しても計算が破綻しないように関数内で処理しています。
    // また、彩度や明度に不正な値が指定されている場合には正しい値を返しませんので注意しましょう。
    // 彩度・明度・透明度はいずれも 0 ～ 1 の範囲で指定してください
    // ---------------------------------------------------------
    // h: 色相(0-360)
    // s: 彩度(0.0-1.0)
    // v: 明度(0.0-1.0)
    // a: 透明度(0.0-1.0)
    // ---------------------------------------------------------
    private fun hsva(h: Int, s: Float, v: Float, a: Float): ArrayList<Float> {
        val color = arrayListOf<Float>()
        if ( (s > 1f) or (v > 1f) or (a > 1f) ) return color

        val th = h%360
        val i = floor(th.toFloat()/60f)
        val f = th.toFloat()/60f - i
        val m = v*(1f-s)
        val n = v*(1f-s*f)
        val k = v*(1-s*(1-f))
        if ( ((s>0f) === false) and ((s<0f) === false) ) {
            color.addAll(arrayListOf<Float>(v,v,v,a))
        }
        else {
            var r = arrayListOf<Float>(v,n,m,m,k,v)
            var g = arrayListOf<Float>(k,v,v,n,m,m)
            var b = arrayListOf<Float>(m,m,k,v,v,n)
            color.addAll(arrayListOf<Float>(r[i.toInt()],g[i.toInt()],b[i.toInt()],a))
        }
        return color
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