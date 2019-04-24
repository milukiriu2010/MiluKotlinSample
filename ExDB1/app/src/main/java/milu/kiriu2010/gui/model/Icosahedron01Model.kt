package milu.kiriu2010.gui.model

import milu.kiriu2010.math.MyMathUtil
import java.nio.*


// 正二十面体
// https://github.com/8q/Android-OpenGL-Icosahedron/blob/master/GL1/src/com/example/gl1/MyIcosa.java
class Icosahedron01Model: MgModelAbs() {

    override fun createPath( opt: Map<String,Float> ) {
        val goldR = MyMathUtil.GOLDEN_RATIO

        var scale = opt["scale"] ?: 1f

        // 頂点
        val va = arrayListOf(scale,0f,-goldR*scale)
        val vb = arrayListOf(-scale,0f,-goldR*scale)
        val vc = arrayListOf(0f,-goldR*scale,-scale)
        val vd = arrayListOf(-goldR*scale,-scale,0f)
        val ve = arrayListOf(goldR*scale,-scale,0f)
        val vf = arrayListOf(0f,goldR*scale,-scale)
        val vg = arrayListOf(goldR*scale,scale,0f)
        val vh = arrayListOf(-goldR*scale,scale,0f)
        val vi = arrayListOf(0f,-goldR*scale,scale)
        val vj = arrayListOf(-scale,0f,goldR*scale)
        val vk = arrayListOf(scale,0f,goldR*scale)
        val vl = arrayListOf(0f,goldR*scale,scale)

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
        (0..59).forEach { i ->
            // (B-A) x (C-A)
            val m=i/3
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*(3*m+2), 3*(3*m+1), 3*(3*m+0) ) )
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
            when (it%3) {
                0 -> datIdx.add(it.toShort())
                1 -> datIdx.add((it+1).toShort())
                2 -> datIdx.add((it-1).toShort())
            }
        }

        allocateBuffer()
    }

}