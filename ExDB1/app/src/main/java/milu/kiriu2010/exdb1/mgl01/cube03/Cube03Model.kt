package milu.kiriu2010.exdb1.mgl01.cube03

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.math.MyMathUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

// 環境光
// https://doc.qt.io/qt-5/qtopengl-cube-example.html
class Cube03Model {

    // プログラムハンドル
    var programHandle: Int = 0

    // 頂点シェーダ
    private val scv =
            """
            attribute vec3 a_Position;
            attribute vec3 a_Normal;
            attribute vec4 a_Color;
            uniform   mat4 u_matMVP;
            uniform   mat4 u_matINV;
            // 平行光源
            uniform   vec3 u_vecLight;
            // 環境光の色
            uniform   vec4 u_vecAmbientColor;
            varying   vec4 v_Color;

            void main() {
                // 光の逆ベクトル
                vec3  invLight = normalize(u_matINV * vec4(u_vecLight,0.0)).xyz;
                // 拡散度("頂点の法線ベクトル"と"光の逆ベクトル"の内積をとり、0.1～1.0の値を返す？
                float diffuse  = clamp(dot(a_Normal,invLight), 0.2, 1.0);
                // 環境光の色を加える
                v_Color        = a_Color * vec4(vec3(diffuse), 1.0) + u_vecAmbientColor;
                gl_Position    = u_matMVP * vec4(a_Position,1.0);
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
        programHandle = MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Normal","a_Color") )

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
            // (v1-v0) x (v2-v0)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*1, 3*2, 3*0 ) )
        }
        (4..7).forEach {
            // (v5-v4) x (v6-v4)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*5, 3*6, 3*4 ) )
        }
        (8..11).forEach {
            // (v9-v8) x (v10-v8)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*9, 3*10, 3*8 ) )
        }
        (12..15).forEach {
            // (v13-v12) x (v14-v12)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*13, 3*14, 3*12 ) )
        }
        (16..19).forEach {
            // (v18-v16) x (v17-v16)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*17, 3*18, 3*16 ) )
        }
        (20..23).forEach {
            // (v21-v20) x (v22-v20)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*21, 3*22, 3*20 ) )
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

    fun draw(matMVP: FloatArray,
             matI: FloatArray,
             vecLight: FloatArray,
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
        // uniform(逆行列)
        GLES20.glGetUniformLocation(programHandle,"u_matINV").also {
            GLES20.glUniformMatrix4fv(it,1,false,matI,0)
        }
        // uniform(平行光源)
        GLES20.glGetUniformLocation(programHandle,"u_vecLight").also {
            GLES20.glUniform3fv(it,1,vecLight,0)
        }
        // uniform(環境光の色)
        GLES20.glGetUniformLocation(programHandle,"u_vecAmbientColor").also {
            GLES20.glUniform4fv(it,1,vecAmbientColor,0)
        }

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, datIdx.size, GLES20.GL_UNSIGNED_SHORT, bufIdx)
    }
}