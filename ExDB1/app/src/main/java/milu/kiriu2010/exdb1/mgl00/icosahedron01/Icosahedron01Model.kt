package milu.kiriu2010.exdb1.mgl00.icosahedron01

import android.opengl.GLES20
import milu.kiriu2010.exdb1.opengl.ModelAbs
import milu.kiriu2010.exdb1.opengl.MyGLFunc
import milu.kiriu2010.math.MyMathUtil
import java.lang.RuntimeException
import java.nio.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


// 正二十面体
class Icosahedron01Model: ModelAbs() {

    init {
        val goldR = MyMathUtil.GOLDEN_RATIO

        // 頂点
        val va = arrayListOf(1f,0f,-goldR)
        val vb = arrayListOf(-1f,0f,-goldR)
        val vc = arrayListOf(0f,-goldR,-1f)
        val vd = arrayListOf(-goldR,-1f,0f)
        val ve = arrayListOf(goldR,-1f,0f)
        val vf = arrayListOf(0f,goldR,-1f)
        val vg = arrayListOf(goldR,1f,0f)
        val vh = arrayListOf(-goldR,1f,0f)
        val vi = arrayListOf(0f,-goldR,1f)
        val vj = arrayListOf(-1f,0f,goldR)
        val vk = arrayListOf(1f,0f,goldR)
        val vl = arrayListOf(0f,goldR,1f)

        // 頂点データ
        // ABC
        datPos.addAll(ArrayList<Float>(va))      // A:0
        datPos.addAll(ArrayList<Float>(vb))      // B:1
        datPos.addAll(ArrayList<Float>(vc))      // C:2
        // ACE
        datPos.addAll(ArrayList<Float>(va))      // A:3
        datPos.addAll(ArrayList<Float>(vc))      // C:4
        datPos.addAll(ArrayList<Float>(ve))      // E:5
        // AEG
        datPos.addAll(ArrayList<Float>(va))      // A:6
        datPos.addAll(ArrayList<Float>(ve))      // E:7
        datPos.addAll(ArrayList<Float>(vg))      // G:8
        // AGF
        datPos.addAll(ArrayList<Float>(va))      // A:9
        datPos.addAll(ArrayList<Float>(vg))      // G:10
        datPos.addAll(ArrayList<Float>(vf))      // F:11
        // AFB
        datPos.addAll(ArrayList<Float>(va))      // A:12
        datPos.addAll(ArrayList<Float>(vf))      // F:13
        datPos.addAll(ArrayList<Float>(vb))      // B:14
        // BFH
        datPos.addAll(ArrayList<Float>(vb))      // B:15
        datPos.addAll(ArrayList<Float>(vf))      // F:16
        datPos.addAll(ArrayList<Float>(vh))      // H:17
        // BHD
        datPos.addAll(ArrayList<Float>(vb))      // B:18
        datPos.addAll(ArrayList<Float>(vh))      // H:19
        datPos.addAll(ArrayList<Float>(vd))      // D:20
        // BDC
        datPos.addAll(ArrayList<Float>(vb))      // B:21
        datPos.addAll(ArrayList<Float>(vd))      // D:22
        datPos.addAll(ArrayList<Float>(vc))      // C:23
        // CDI
        datPos.addAll(ArrayList<Float>(vc))      // C:24
        datPos.addAll(ArrayList<Float>(vd))      // D:25
        datPos.addAll(ArrayList<Float>(vi))      // I:26
        // CIE
        datPos.addAll(ArrayList<Float>(vc))      // C:27
        datPos.addAll(ArrayList<Float>(vi))      // I:28
        datPos.addAll(ArrayList<Float>(ve))      // E:29
        // EIK
        datPos.addAll(ArrayList<Float>(ve))      // E:30
        datPos.addAll(ArrayList<Float>(vi))      // I:31
        datPos.addAll(ArrayList<Float>(vk))      // K:32
        // EKG
        datPos.addAll(ArrayList<Float>(ve))      // E:33
        datPos.addAll(ArrayList<Float>(vk))      // K:34
        datPos.addAll(ArrayList<Float>(vg))      // G:35
        // GKL
        datPos.addAll(ArrayList<Float>(vg))      // G:36
        datPos.addAll(ArrayList<Float>(vk))      // K:37
        datPos.addAll(ArrayList<Float>(vl))      // L:38
        // GLF
        datPos.addAll(ArrayList<Float>(vg))      // G:39
        datPos.addAll(ArrayList<Float>(vl))      // L:40
        datPos.addAll(ArrayList<Float>(vf))      // F:41
        // KIJ
        datPos.addAll(ArrayList<Float>(vk))      // K:42
        datPos.addAll(ArrayList<Float>(vi))      // I:43
        datPos.addAll(ArrayList<Float>(vj))      // J:44
        // KJL
        datPos.addAll(ArrayList<Float>(vk))      // K:45
        datPos.addAll(ArrayList<Float>(vj))      // J:46
        datPos.addAll(ArrayList<Float>(vl))      // L:47
        // LJH
        datPos.addAll(ArrayList<Float>(vl))      // L:48
        datPos.addAll(ArrayList<Float>(vj))      // J:49
        datPos.addAll(ArrayList<Float>(vh))      // H:50
        // LHF
        datPos.addAll(ArrayList<Float>(vl))      // L:51
        datPos.addAll(ArrayList<Float>(vh))      // H:52
        datPos.addAll(ArrayList<Float>(vf))      // F:53
        // JID
        datPos.addAll(ArrayList<Float>(vj))      // J:54
        datPos.addAll(ArrayList<Float>(vi))      // I:55
        datPos.addAll(ArrayList<Float>(vd))      // D:56
        // JDH
        datPos.addAll(ArrayList<Float>(vj))      // J:57
        datPos.addAll(ArrayList<Float>(vd))      // D:58
        datPos.addAll(ArrayList<Float>(vh))      // H:59


        // 法線データ
        (0..19).forEach { i ->
            // (B-A) x (C-A)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 1+3*i, 2+3*i, 0+3*i ) )
        }

        // 色データ
        // ABC(赤)
        (0..2).forEach {
            datCol.addAll(arrayListOf<Float>(1f,0f,0f,1f))
        }
        // ACE
        (3..5).forEach {
            datCol.addAll(arrayListOf<Float>(1f,0.25f,0f,1f))
        }
        // AEG(だいだい)
        (6..8).forEach {
            datCol.addAll(arrayListOf<Float>(1f,0.5f,0f,1f))
        }
        // AGF
        (9..11).forEach {
            datCol.addAll(arrayListOf<Float>(1f,0.75f,0f,1f))
        }
        // AFB(黄色)
        (12..14).forEach {
            datCol.addAll(arrayListOf<Float>(1f,1f,0f,1f))
        }
        // BFH
        (15..17).forEach {
            datCol.addAll(arrayListOf<Float>(0.75f,1f,0f,1f))
        }
        // BHD
        (18..20).forEach {
            datCol.addAll(arrayListOf<Float>(0.5f,1f,0f,1f))
        }
        // BDC
        (21..23).forEach {
            datCol.addAll(arrayListOf<Float>(0.25f,1f,0f,1f))
        }
        // CDI
        (24..26).forEach {
            datCol.addAll(arrayListOf<Float>(0f,1f,0f,1f))
        }
        // CIE
        (27..29).forEach {
            datCol.addAll(arrayListOf<Float>(0f,1f,0.25f,1f))
        }
        // EIK
        (30..32).forEach {
            datCol.addAll(arrayListOf<Float>(0f,1f,0.5f,1f))
        }
        // EKG
        (33..35).forEach {
            datCol.addAll(arrayListOf<Float>(0f,1f,0.75f,1f))
        }
        // GKL(水色)
        (36..38).forEach {
            datCol.addAll(arrayListOf<Float>(0f,1f,1f,1f))
        }
        // GLF
        (39..41).forEach {
            datCol.addAll(arrayListOf<Float>(0f,0.75f,1f,1f))
        }
        // KIJ
        (42..44).forEach {
            datCol.addAll(arrayListOf<Float>(0f,0.5f,1f,1f))
        }
        // KJL
        (45..47).forEach {
            datCol.addAll(arrayListOf<Float>(0f,0.25f,1f,1f))
        }
        // LJH(青)
        (48..50).forEach {
            datCol.addAll(arrayListOf<Float>(0f,0f,1f,1f))
        }
        // LHF
        (51..53).forEach {
            datCol.addAll(arrayListOf<Float>(0.25f,0f,1f,1f))
        }
        // JID
        (54..56).forEach {
            datCol.addAll(arrayListOf<Float>(0.5f,0f,1f,1f))
        }
        // JDH
        (57..59).forEach {
            datCol.addAll(arrayListOf<Float>(0.75f,0f,1f,1f))
        }

        // インデックスデータ
        (0..59).forEach {
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

    /*
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
    */
}
