package milu.kiriu2010.exdb1.opengl05.w053

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

// ステンシルバッファでアウトライン
// https://wgld.org/d/webgl/w039.html
class W053ModelTorus {
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

    fun drawScreen(programHandle: Int,
                   matMVP: FloatArray,
                   matINV: FloatArray,
                   u_vecLight: FloatArray,
                   u_vecEye: FloatArray,
                   u_ambientColor: FloatArray) {

        GLES20.glUseProgram(programHandle)
        MyGLFunc.checkGlError("Torus:Draw:UseProgram")

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

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLFunc.checkGlError("Torus:Draw:u_matMVP")

        // uniform(逆行列)
        GLES20.glGetUniformLocation(programHandle,"u_matINV").also {
            GLES20.glUniformMatrix4fv(it,1,false,matINV,0)
        }
        MyGLFunc.checkGlError("Torus:Draw:u_matINV")

        // uniform(ライティング)
        GLES20.glGetUniformLocation(programHandle,"u_vecLight").also {
            GLES20.glUniform3fv(it,1,u_vecLight,0)
        }
        MyGLFunc.checkGlError("Torus:Draw:u_vecLight")

        // uniform(視点座標)
        GLES20.glGetUniformLocation(programHandle,"u_vecEye").also {
            GLES20.glUniform3fv(it,1,u_vecEye,0)
        }
        MyGLFunc.checkGlError("Torus:Draw:u_vecEye")

        // uniform(環境色)
        GLES20.glGetUniformLocation(programHandle, "u_ambientColor").also {
            GLES20.glUniform4fv(it, 1,u_ambientColor,0)
        }
        MyGLFunc.checkGlError("Torus:Draw:u_ambientColor")

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
}