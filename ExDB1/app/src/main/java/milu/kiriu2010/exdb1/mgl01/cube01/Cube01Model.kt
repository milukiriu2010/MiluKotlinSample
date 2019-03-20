package milu.kiriu2010.exdb1.mgl01.cube01

import android.opengl.GLES20
import milu.kiriu2010.exdb1.opengl.MyGLFunc
import milu.kiriu2010.math.MyMathUtil
import java.lang.RuntimeException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Cube01Model {

    // プログラムハンドル
    var programHandle: Int = 0

    // 頂点シェーダ
    private val scv =
            """
            attribute vec3 a_Position;
            attribute vec4 a_Color;
            uniform   mat4 u_matMVP;
            varying   vec4 v_Color;

            void main() {
                v_Color     = a_Color;
                gl_Position = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump float;
            varying vec4 v_Color;

            void main() {
                gl_FragColor = v_Color;
            }
            """.trimIndent()

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
        // 頂点シェーダを生成
        val svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Color") )

        // 頂点データ(正面)
        datPos.addAll(arrayListOf(-1f,-1f,1f))  // v0
        datPos.addAll(arrayListOf(1f,-1f,1f))   // v1
        datPos.addAll(arrayListOf(-1f,1f,1f))   // v2
        datPos.addAll(arrayListOf(1f,1f,1f))    // v3
        datPos.addAll(arrayListOf(1f,-1f,1f))   // v4,v1
        datPos.addAll(arrayListOf(1f,-1f,-1f))   // v5
        datPos.addAll(arrayListOf(1f,1f,1f))    // v6,v3
        datPos.addAll(arrayListOf(1f,1f,-1f))   // v7
        datPos.addAll(arrayListOf(1f,-1f,-1f))   // v8,v5
        datPos.addAll(arrayListOf(-1f,-1f,-1f))   // v9
        datPos.addAll(arrayListOf(1f,1f,-1f))   // v10,v7
        datPos.addAll(arrayListOf(-1f,1f,-1f))   // v11
        datPos.addAll(arrayListOf(-1f,-1f,-1f))   // v12,v9
        datPos.addAll(arrayListOf(-1f,-1f,1f))  // v13,v0
        datPos.addAll(arrayListOf(-1f,1f,-1f))   // v14,v11
        datPos.addAll(arrayListOf(-1f,1f,1f))   // v15,v2
        datPos.addAll(arrayListOf(-1f,-1f,-1f))   // v16,v12,v9
        datPos.addAll(arrayListOf(1f,-1f,-1f))   // v17,v8,v5
        datPos.addAll(arrayListOf(-1f,-1f,1f))  // v18,v13,v0
        datPos.addAll(arrayListOf(1f,-1f,1f))   // v19,v4,v1
        datPos.addAll(arrayListOf(-1f,1f,1f))   // v20,v15,v2
        datPos.addAll(arrayListOf(1f,1f,1f))    // v21,v6,v3
        datPos.addAll(arrayListOf(-1f,1f,-1f))   // v22,v14,v11
        datPos.addAll(arrayListOf(1f,1f,-1f))   // v23,v10,v7


        // 法線データ(正面)
        (0..3).forEach {
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 0, 3, 6 ) )
        }
        (4..7).forEach {
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 0, 3, 6 ) )
        }
        (8..11).forEach {
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 0, 3, 6 ) )
        }
        (12..15).forEach {
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 0, 3, 6 ) )
        }
        (16..19).forEach {
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 0, 3, 6 ) )
        }
        (20..23).forEach {
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 0, 3, 6 ) )
        }

        // 色データ(正面)
        (0..3).forEach {
            datCol.addAll( arrayListOf<Float>(1f,0f,0f,1f))
        }
        (4..7).forEach {
            datCol.addAll( arrayListOf<Float>(1f,0.5f,0f,1f))
        }
        (8..11).forEach {
            datCol.addAll( arrayListOf<Float>(1f,1f,0f,1f))
        }
        (12..15).forEach {
            datCol.addAll( arrayListOf<Float>(0f,1f,0f,1f))
        }
        (16..19).forEach {
            datCol.addAll( arrayListOf<Float>(0f,1f,1f,1f))
        }
        (20..23).forEach {
            datCol.addAll( arrayListOf<Float>(0f,0f,1f,1f))
        }

        // インデックスデータ(正面)
        datIdx.addAll(arrayListOf(0,1,2))
        datIdx.addAll(arrayListOf(1,3,2))
        datIdx.addAll(arrayListOf(4,5,6))
        datIdx.addAll(arrayListOf(5,7,6))
        datIdx.addAll(arrayListOf(8,9,10))
        datIdx.addAll(arrayListOf(9,11,10))
        datIdx.addAll(arrayListOf(12,13,14))
        datIdx.addAll(arrayListOf(13,15,14))
        datIdx.addAll(arrayListOf(16,17,18))
        datIdx.addAll(arrayListOf(17,19,18))
        datIdx.addAll(arrayListOf(20,21,22))
        datIdx.addAll(arrayListOf(21,23,22))


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

    fun draw(matMVP: FloatArray) {

        // 頂点
        bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Position")

        /*
        // 法線
        bufNor.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Normal").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, bufNor)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Normal")
        */

        // 色
        bufCol.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Color").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 4*4, bufCol)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Color")

        // モデル×ビュー×プロジェクション
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, datIdx.size, GLES20.GL_UNSIGNED_SHORT, bufIdx)
    }
}