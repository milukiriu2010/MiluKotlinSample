package milu.kiriu2010.exdb1.opengl03.w034

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin

// クォータニオンと球面線形補間
// https://wgld.org/d/webgl/w034.html
// https://doc.qt.io/qt-5/qtopengl-cube-example.html
class W034Model {
    // 頂点バッファ
    private lateinit var bufPos: FloatBuffer
    // 法線バッファ
    private lateinit var bufNor: FloatBuffer
    // 色バッファ
    private lateinit var bufCol: FloatBuffer
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
        // トーラスの頂点データを生成
        //createPath(32,32,0.5f,1.5f, floatArrayOf(0.75f,0.25f,0.25f,1f))
        //createPath(32,32,0.5f,1.5f)
        createPath(32,32,0.5f,1.5f, floatArrayOf(0.5f,0.5f,0.5f,1f))

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
             matM: FloatArray,
             matI: FloatArray,
             vecLight: FloatArray,
             vecEye: FloatArray,
             vecAmbientColor: FloatArray) {

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
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 4*4, bufCol)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Color")

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        // uniform(座標行列)
        GLES20.glGetUniformLocation(programHandle,"u_matM").also {
            GLES20.glUniformMatrix4fv(it,1,false,matM,0)
        }
        // uniform(逆行列)
        GLES20.glGetUniformLocation(programHandle,"u_matINV").also {
            GLES20.glUniformMatrix4fv(it,1,false,matI,0)
        }
        // uniform(平行光源)
        GLES20.glGetUniformLocation(programHandle,"u_vecLight").also {
            GLES20.glUniform3fv(it,1,vecLight,0)
        }
        // uniform(視線ベクトル)
        GLES20.glGetUniformLocation(programHandle,"u_vecEye").also {
            GLES20.glUniform3fv(it,1,vecEye,0)
        }
        // uniform(環境光の色)
        GLES20.glGetUniformLocation(programHandle,"u_vecAmbientColor").also {
            GLES20.glUniform4fv(it,1,vecAmbientColor,0)
        }

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
        datIdx.clear()

        (0..row).forEach { i ->
            var r = PI.toFloat() *2f/row.toFloat()*i.toFloat()
            var rr = cos(r)
            var ry = sin(r)
            (0..column).forEach { ii ->
                val tr = PI.toFloat() *2f/column.toFloat()*ii.toFloat()
                val tx = (rr*irad+orad)* cos(tr)
                val ty = ry*irad
                val tz = (rr*irad+orad)* sin(tr)
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
            }
        }

        (0 until row).forEach { i ->
            (0 until column).forEach { ii ->
                val r = (column+1)*i+ii
                datIdx.addAll(arrayListOf<Short>(r.toShort(),(r+column+1).toShort(),(r+1).toShort()))
                datIdx.addAll(arrayListOf<Short>((r+column+1).toShort(),(r+column+2).toShort(),(r+1).toShort()))
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