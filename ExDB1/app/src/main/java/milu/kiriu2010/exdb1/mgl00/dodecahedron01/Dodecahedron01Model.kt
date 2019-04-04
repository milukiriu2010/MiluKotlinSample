package milu.kiriu2010.exdb1.mgl00.dodecahedron01

import android.opengl.GLES20
import milu.kiriu2010.exdb1.opengl.MyGLFunc
import milu.kiriu2010.math.MyMathUtil
import java.lang.RuntimeException
import java.nio.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


// 正十二面体
class Dodecahedron01Model {
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

        val goldR = MyMathUtil.GOLDEN_RATIO
        val cos36f = MyMathUtil.COS36F
        val sin36f = MyMathUtil.SIN36F
        val cos72f = MyMathUtil.COS72F
        val sin72f = MyMathUtil.SIN72F

        // 頂点データ
        // ABC
        datPos.addAll(arrayListOf(1f,0f,0f))              // A:0
        datPos.addAll(arrayListOf(cos72f,sin72f,0f))      // B:1
        datPos.addAll(arrayListOf(-cos36f,sin36f,0f))     // C:2
        // ACD
        datPos.addAll(arrayListOf(1f,0f,0f))              // A:3
        datPos.addAll(arrayListOf(-cos36f,sin36f,0f))     // C:4
        datPos.addAll(arrayListOf(-cos36f,-sin36f,0f))    // D:5
        // ADE
        datPos.addAll(arrayListOf(1f,0f,0f))              // A:6
        datPos.addAll(arrayListOf(-cos36f,-sin36f,0f))    // D:7
        datPos.addAll(arrayListOf(cos72f,-sin72f,0f))     // E:8
        // BHI
        datPos.addAll(arrayListOf(cos72f,sin72f,0f))                  // B:9
        datPos.addAll(arrayListOf(goldR*cos72f,goldR*sin72f,1f))      // H:10
        datPos.addAll(arrayListOf(-goldR*cos72f,goldR*sin72f,goldR))  // I:11
        // BIJ
        datPos.addAll(arrayListOf(cos72f,sin72f,0f))                  // B:12
        datPos.addAll(arrayListOf(-goldR*cos72f,goldR*sin72f,goldR))  // I:13
        datPos.addAll(arrayListOf(-goldR*cos36f,goldR*sin36f,1f))     // J:14
        // BJC
        datPos.addAll(arrayListOf(cos72f,sin72f,0f))                  // B:15
        datPos.addAll(arrayListOf(-goldR*cos36f,goldR*sin36f,1f))     // J:16
        datPos.addAll(arrayListOf(-cos36f,sin36f,0f))                 // C:17
        // CJK
        datPos.addAll(arrayListOf(-cos36f,sin36f,0f))                 // C:18
        datPos.addAll(arrayListOf(-goldR*cos36f,goldR*sin36f,1f))     // J:19
        datPos.addAll(arrayListOf(-goldR,0f,goldR))                   // K:20
        // CKL
        datPos.addAll(arrayListOf(-cos36f,sin36f,0f))                 // C:21
        datPos.addAll(arrayListOf(-goldR,0f,goldR))                   // K:22
        datPos.addAll(arrayListOf(-goldR*cos36f,-goldR*sin36f,1f))    // L:23
        // CLD
        datPos.addAll(arrayListOf(-cos36f,sin36f,0f))                 // C:24
        datPos.addAll(arrayListOf(-goldR*cos36f,-goldR*sin36f,1f))    // L:25
        datPos.addAll(arrayListOf(-cos36f,-sin36f,0f))                // D:26
        // DLM
        datPos.addAll(arrayListOf(-cos36f,-sin36f,0f))                // D:27
        datPos.addAll(arrayListOf(-goldR*cos36f,-goldR*sin36f,1f))    // L:28
        datPos.addAll(arrayListOf(-goldR*cos72f,-goldR*sin72f,goldR)) // M:29
        // DMN
        datPos.addAll(arrayListOf(-cos36f,-sin36f,0f))                // D:30
        datPos.addAll(arrayListOf(-goldR*cos72f,-goldR*sin72f,goldR)) // M:31
        datPos.addAll(arrayListOf(goldR*cos72f,-goldR*sin72f,1f))     // N:32
        // DNE
        datPos.addAll(arrayListOf(-cos36f,-sin36f,0f))                // D:33
        datPos.addAll(arrayListOf(goldR*cos72f,-goldR*sin72f,1f))     // N:34
        datPos.addAll(arrayListOf(cos72f,-sin72f,0f))                 // E:35
        // ENO
        datPos.addAll(arrayListOf(cos72f,-sin72f,0f))                 // E:36
        datPos.addAll(arrayListOf(goldR*cos72f,-goldR*sin72f,1f))     // N:37
        datPos.addAll(arrayListOf(goldR*cos36f,-goldR*sin36f,goldR))  // O:38
        // EOF
        datPos.addAll(arrayListOf(cos72f,-sin72f,0f))                 // E:39
        datPos.addAll(arrayListOf(goldR*cos36f,-goldR*sin36f,goldR))  // O:40
        datPos.addAll(arrayListOf(goldR,0f,1f))                       // F:41
        // EFA
        datPos.addAll(arrayListOf(cos72f,-sin72f,0f))                 // E:42
        datPos.addAll(arrayListOf(goldR,0f,1f))                       // F:43
        datPos.addAll(arrayListOf(1f,0f,0f))                          // A:44
        // AFG
        datPos.addAll(arrayListOf(1f,0f,0f))                          // A:45
        datPos.addAll(arrayListOf(goldR,0f,1f))                       // F:46
        datPos.addAll(arrayListOf(goldR*cos36f,goldR*sin36f,goldR))   // G:47
        // AGH
        datPos.addAll(arrayListOf(1f,0f,0f))                          // A:48
        datPos.addAll(arrayListOf(goldR*cos36f,goldR*sin36f,goldR))   // G:49
        datPos.addAll(arrayListOf(goldR*cos72f,goldR*sin72f,1f))      // H:50
        // AHB
        datPos.addAll(arrayListOf(1f,0f,0f))                          // A:51
        datPos.addAll(arrayListOf(goldR*cos72f,goldR*sin72f,1f))      // H:52
        datPos.addAll(arrayListOf(cos72f,sin72f,0f))                  // B:53
        // RQP
        datPos.addAll(arrayListOf(cos36f,-sin36f,goldR+1f))                 // R:54
        datPos.addAll(arrayListOf(-cos72f,-sin72f,goldR+1f))                // Q:55
        datPos.addAll(arrayListOf(-1f,0f,goldR+1f))                         // P:56
        // RPT
        datPos.addAll(arrayListOf(cos36f,-sin36f,goldR+1f))                 // R:57
        datPos.addAll(arrayListOf(-1f,0f,goldR+1f))                         // P:58
        datPos.addAll(arrayListOf(-cos72f,sin72f,goldR+1f))                 // T:59
        // RTS
        datPos.addAll(arrayListOf(cos36f,-sin36f,goldR+1f))                 // R:60
        datPos.addAll(arrayListOf(-cos72f,sin72f,goldR+1f))                 // T:61
        datPos.addAll(arrayListOf(cos36f,sin36f,goldR+1f))                  // S:62
        // QML
        datPos.addAll(arrayListOf(-cos72f,-sin72f,goldR+1f))                // Q:63
        datPos.addAll(arrayListOf(-goldR*cos72f,-goldR*sin72f,goldR))       // M:64
        datPos.addAll(arrayListOf(-goldR*cos36f,-goldR*sin36f,1f))          // L:65
        // QLK
        datPos.addAll(arrayListOf(-cos72f,-sin72f,goldR+1f))                // Q:66
        datPos.addAll(arrayListOf(-goldR*cos36f,-goldR*sin36f,1f))          // L:67
        datPos.addAll(arrayListOf(-goldR,0f,goldR))                         // K:68
        // QKP
        datPos.addAll(arrayListOf(-cos72f,-sin72f,goldR+1f))                // Q:69
        datPos.addAll(arrayListOf(-goldR,0f,goldR))                         // K:70
        datPos.addAll(arrayListOf(-1f,0f,goldR+1f))                         // P:71
        // PKJ
        datPos.addAll(arrayListOf(-1f,0f,goldR+1f))                         // P:72
        datPos.addAll(arrayListOf(-goldR,0f,goldR))                         // K:73
        datPos.addAll(arrayListOf(-goldR*cos36f,goldR*sin36f,1f))           // J:74
        // PJI
        datPos.addAll(arrayListOf(-1f,0f,goldR+1f))                         // P:75
        datPos.addAll(arrayListOf(-goldR*cos36f,goldR*sin36f,1f))           // J:76
        datPos.addAll(arrayListOf(-goldR*cos72f,goldR*sin72f,goldR))        // I:77
        // PIT
        datPos.addAll(arrayListOf(-1f,0f,goldR+1f))                         // P:78
        datPos.addAll(arrayListOf(-goldR*cos72f,goldR*sin72f,goldR))        // I:79
        datPos.addAll(arrayListOf(-cos72f,sin72f,goldR+1f))                 // T:80
        // TIH
        datPos.addAll(arrayListOf(-cos72f,sin72f,goldR+1f))                 // T:81
        datPos.addAll(arrayListOf(-goldR*cos72f,goldR*sin72f,goldR))        // I:82
        datPos.addAll(arrayListOf(goldR*cos72f,goldR*sin72f,1f))            // H:83
        // THG
        datPos.addAll(arrayListOf(-cos72f,sin72f,goldR+1f))                 // T:84
        datPos.addAll(arrayListOf(goldR*cos72f,goldR*sin72f,1f))            // H:85
        datPos.addAll(arrayListOf(goldR*cos36f,goldR*sin36f,goldR))         // G:86
        // TGS
        datPos.addAll(arrayListOf(-cos72f,sin72f,goldR+1f))                 // T:87
        datPos.addAll(arrayListOf(goldR*cos36f,goldR*sin36f,goldR))         // G:88
        datPos.addAll(arrayListOf(cos36f,sin36f,goldR+1f))                  // S:89
        // SGF
        datPos.addAll(arrayListOf(cos36f,sin36f,goldR+1f))                  // S:90
        datPos.addAll(arrayListOf(goldR*cos36f,goldR*sin36f,goldR))         // G:91
        datPos.addAll(arrayListOf(goldR,0f,1f))                             // F:92
        // SFO
        datPos.addAll(arrayListOf(cos36f,sin36f,goldR+1f))                  // S:93
        datPos.addAll(arrayListOf(goldR,0f,1f))                             // F:94
        datPos.addAll(arrayListOf(goldR*cos36f,-goldR*sin36f,goldR))        // O:95
        // SOR
        datPos.addAll(arrayListOf(cos36f,sin36f,goldR+1f))                  // S:96
        datPos.addAll(arrayListOf(goldR*cos36f,-goldR*sin36f,goldR))        // O:97
        datPos.addAll(arrayListOf(cos36f,-sin36f,goldR+1f))                 // R:98
        // RON
        datPos.addAll(arrayListOf(cos36f,-sin36f,goldR+1f))                 // R:99
        datPos.addAll(arrayListOf(goldR*cos36f,-goldR*sin36f,goldR))        // O:100
        datPos.addAll(arrayListOf(goldR*cos72f,-goldR*sin72f,1f))           // N:101
        // RNM
        datPos.addAll(arrayListOf(cos36f,-sin36f,goldR+1f))                 // R:102
        datPos.addAll(arrayListOf(goldR*cos72f,-goldR*sin72f,1f))           // N:103
        datPos.addAll(arrayListOf(-goldR*cos72f,-goldR*sin72f,goldR))       // M:104
        // RMQ
        datPos.addAll(arrayListOf(cos36f,-sin36f,goldR+1f))                 // R:105
        datPos.addAll(arrayListOf(-goldR*cos72f,-goldR*sin72f,goldR))       // M:106
        datPos.addAll(arrayListOf(-cos72f,-sin72f,goldR+1f))                // Q:107


        // 法線データ
        // ABCDE
        (0..8).forEach { i ->
            // (B-A) x (C-A)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 1, 2, 0 ) )
        }
        // BHIJC
        (9..17).forEach { i ->
            // (H-B) x (I-B)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 10, 11, 9 ) )
        }
        // CJKLD
        (18..26).forEach { i ->
            // (J-C) x (K-C)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 19, 20, 18 ) )
        }
        // DLMNE
        (27..35).forEach { i ->
            // (L-D) x (M-D)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 28, 29, 27 ) )
        }
        // ENOFA
        (36..44).forEach { i ->
            // (N-E) x (O-E)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 37, 38, 36 ) )
        }
        // AFGHB
        (45..53).forEach { i ->
            // (F-A) x (G-A)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 46, 47, 45 ) )
        }
        // RQPTS
        (54..62).forEach { i ->
            // (Q-R) x (P-R)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 55, 56, 54 ) )
        }
        // QMLKP
        (63..71).forEach { i ->
            // (M-Q) x (L-Q)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 64, 65, 63 ) )
        }
        // PKJIT
        (72..80).forEach { i ->
            // (K-P) x (J-P)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 73, 74, 72 ) )
        }
        // TIHGS
        (81..89).forEach { i ->
            // (I-T) x (H-T)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 82, 83, 81 ) )
        }
        // SGFOR
        (90..98).forEach { i ->
            // (G-S) x (F-S)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 91, 92, 90 ) )
        }
        // RONMQ
        (99..107).forEach { i ->
            // (O-R) x (N-R)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 100, 101, 99 ) )
        }

        // 色データ
        // ABCDE(赤)
        (0..8).forEach {
            datCol.addAll(arrayListOf<Float>(1f,0f,0f,1f))
        }
        // BHIJC(オレンジ)
        (9..17).forEach {
            datCol.addAll(arrayListOf<Float>(1f,0.5f,0f,1f))
        }
        // CJKLD(黄色)
        (18..26).forEach {
            datCol.addAll(arrayListOf<Float>(1f,1f,0f,1f))
        }
        // DLMNE(黄緑)
        (27..35).forEach {
            datCol.addAll(arrayListOf<Float>(0.5f,1f,0f,1f))
        }
        // ENOFA(緑)
        (36..44).forEach {
            datCol.addAll(arrayListOf<Float>(0f,1f,0f,1f))
        }
        // AFGHB
        (45..53).forEach {
            datCol.addAll(arrayListOf<Float>(0f,1f,0.5f,1f))
        }
        // RQPTS
        (54..62).forEach {
            datCol.addAll(arrayListOf<Float>(0f,1f,1f,1f))
        }
        // QMLKP
        (63..71).forEach {
            datCol.addAll(arrayListOf<Float>(0f,0.5f,1f,1f))
        }
        // PKJIT
        (72..80).forEach {
            datCol.addAll(arrayListOf<Float>(0f,0f,1f,1f))
        }
        // TIHGS
        (81..89).forEach {
            datCol.addAll(arrayListOf<Float>(0.5f,0f,1f,1f))
        }
        // SGFOR
        (90..98).forEach {
            datCol.addAll(arrayListOf<Float>(1f,0f,1f,1f))
        }
        // RONMQ
        (99..107).forEach {
            datCol.addAll(arrayListOf<Float>(1f,0f,0.5f,1f))
        }

        // インデックスデータ
        (0..107).forEach {
            datIdx.add(it.toShort())
        }

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
