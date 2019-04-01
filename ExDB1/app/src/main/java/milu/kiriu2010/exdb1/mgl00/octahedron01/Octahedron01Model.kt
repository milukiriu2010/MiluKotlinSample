package milu.kiriu2010.exdb1.mgl00.octahedron01

import android.opengl.GLES20
import milu.kiriu2010.exdb1.opengl.MyGLFunc
import milu.kiriu2010.math.MyMathUtil
import java.lang.RuntimeException
import java.nio.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


// https://wgld.org/d/webgl/w026.html
class Octahedron01Model {
    // 頂点バッファ
    private lateinit var bufPos: FloatBuffer
    // 法線バッファ
    private lateinit var bufNor: FloatBuffer
    // 色バッファ
    private lateinit var bufCol: FloatBuffer
    // インデックスバッファ
    private lateinit var bufIdx: ShortBuffer

    private val datPos = arrayListOf<Float>()
    private val datNor = arrayListOf<Float>()
    private val datCol = arrayListOf<Float>()
    private val datIdx = arrayListOf<Short>()

    init {
        val sq2 = sqrt(2f)

        // 頂点データ
        datPos.addAll(arrayListOf(0f,-sq2,0f))   // v0
        datPos.addAll(arrayListOf(1f,0f,1f))    // v1
        datPos.addAll(arrayListOf(-1f,0f,1f))   // v2
        datPos.addAll(arrayListOf(1f,0f,1f))    // v3,v1
        datPos.addAll(arrayListOf(0f,sq2,0f))    // v4
        datPos.addAll(arrayListOf(-1f,0f,1f))   // v5,v2
        datPos.addAll(arrayListOf(1f,0f,1f))    // v6,v3,v1
        datPos.addAll(arrayListOf(1f,0f,-1f))   // v7
        datPos.addAll(arrayListOf(0f,sq2,0f))    // v8,v4
        datPos.addAll(arrayListOf(1f,0f,-1f))   // v9,v7
        datPos.addAll(arrayListOf(-1f,0f,-1f))  // v10
        datPos.addAll(arrayListOf(0f,sq2,0f))    // v11,v8,v4
        datPos.addAll(arrayListOf(1f,0f,-1f))   // v12,v9,v7
        datPos.addAll(arrayListOf(0f,-sq2,0f))   // v13,v0
        datPos.addAll(arrayListOf(-1f,0f,-1f))  // v14,v10
        datPos.addAll(arrayListOf(0f,-sq2,0f))   // v15,v13,v0
        datPos.addAll(arrayListOf(-1f,0f,1f))   // v16,v5,v2
        datPos.addAll(arrayListOf(-1f,0f,-1f))  // v17,v14,v10
        datPos.addAll(arrayListOf(0f,sq2,0f))    // v18,v11,v8,v4
        datPos.addAll(arrayListOf(-1f,0f,-1f))  // v19,v17,v14,v10
        datPos.addAll(arrayListOf(-1f,0f,1f))   // v20,v16,v5,v2
        datPos.addAll(arrayListOf(0f,-sq2,0f))   // v21,v15,v13,v0
        datPos.addAll(arrayListOf(1f,0f,-1f))   // v22,v12,v9,v7
        datPos.addAll(arrayListOf(1f,0f,1f))    // v23,v6,v3,v1

        // 法線データ
        (0..2).forEach {
            // (v1-v0) x (v2-v0)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*1, 3*2, 3*0 ) )
        }
        (3..5).forEach {
            // (v4-v3) x (v5-v3)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*4, 3*5, 3*3 ) )
        }
        (6..8).forEach {
            // (v7-v6) x (v8-v6)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*7, 3*8, 3*6 ) )
        }
        (9..11).forEach {
            // (v10-v9) x (v11-v9)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*10, 3*11, 3*9 ) )
        }
        (12..14).forEach {
            // (v13-v12) x (v14-v12)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*13, 3*14, 3*12 ) )
        }
        (15..17).forEach {
            // (v16-v15) x (v17-v15)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*16, 3*17, 3*15 ) )
        }
        (18..20).forEach {
            // (v19-v18) x (v20-v18)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*19, 3*20, 3*18 ) )
        }
        (21..23).forEach {
            // (v22-v21) x (v23-v21)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*22, 3*23, 3*21 ) )
        }

        // 色データ
        (0..2).forEach {
            datCol.addAll(arrayListOf<Float>(1f,0f,0f,1f))
        }
        (3..5).forEach {
            datCol.addAll(arrayListOf<Float>(1f,0.5f,0f,1f))
        }
        (6..8).forEach {
            datCol.addAll(arrayListOf<Float>(1f,1f,0f,1f))
        }
        (9..11).forEach {
            datCol.addAll(arrayListOf<Float>(0.5f,1f,0f,1f))
        }
        (12..14).forEach {
            datCol.addAll(arrayListOf<Float>(0f,1f,0f,1f))
        }
        (15..17).forEach {
            datCol.addAll(arrayListOf<Float>(0f,1f,0.5f,1f))
        }
        (18..20).forEach {
            datCol.addAll(arrayListOf<Float>(0f,1f,1f,1f))
        }
        (21..23).forEach {
            datCol.addAll(arrayListOf<Float>(0f,0.5f,1f,1f))
        }

        // インデックスデータ
        datIdx.addAll(arrayListOf<Short>(0,1,2))
        datIdx.addAll(arrayListOf<Short>(3,4,5))
        datIdx.addAll(arrayListOf<Short>(6,7,8))
        datIdx.addAll(arrayListOf<Short>(9,10,11))
        datIdx.addAll(arrayListOf<Short>(12,13,14))
        datIdx.addAll(arrayListOf<Short>(15,16,17))
        datIdx.addAll(arrayListOf<Short>(18,19,20))
        datIdx.addAll(arrayListOf<Short>(21,22,23))

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

    fun loadShader(type: Int, shaderCode: String): Int {
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        return GLES20.glCreateShader(type).also { shader ->

            // add the source code to the shader and compile it
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)

            // コンパイル結果のチェック
            val compileStatus = IntArray(1)
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus,0)
            if ( compileStatus[0] == 0 ) {
                // コンパイル失敗
                GLES20.glDeleteShader(shader)
                throw RuntimeException("Compile Error:"+shaderCode)
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
        // get handle to vertex shader's vPosition member
        GLES20.glGetAttribLocation(programHandle, "a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false,3*4,bufPos)
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
}
