package milu.kiriu2010.gui.model

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

// --------------------------------------
// 板ポリゴン
// --------------------------------------
// 2019.04.30  51:XZ平面(右回り)
// 2019.05.01  53:XY平面(左回り)
// --------------------------------------
class Board01Model: MgModelAbs() {

    override fun createPath( opt: Map<String,Float> ) {
        datPos.clear()
        datNor.clear()
        datCol.clear()
        datTxc.clear()
        datIdx.clear()

        val pattern = opt["pattern"]?.toInt() ?: 1

        when ( pattern ) {
            // XY平面(右回り)
            1 -> createPathPattern1(opt)
            // XY平面(左回り)
            53 -> createPathPattern53(opt)
            // XZ平面(左回り)
            49 -> createPathPattern49(opt)
            // XZ平面(右回り)
            51 -> createPathPattern51(opt)
            else -> createPathPattern1(opt)
        }

        // バッファ割り当て
        allocateBuffer()
    }

    // XY平面(右回り)
    private fun createPathPattern1(opt: Map<String, Float>) {
        val color = FloatArray(4)
        color[0] = opt["colorR"] ?: 1f
        color[1] = opt["colorG"] ?: 1f
        color[2] = opt["colorB"] ?: 1f
        color[3] = opt["colorA"] ?: 1f

        // 頂点データ
        datPos.addAll(arrayListOf(-1f, 1f,0f))
        datPos.addAll(arrayListOf( 1f, 1f,0f))
        datPos.addAll(arrayListOf(-1f,-1f,0f))
        datPos.addAll(arrayListOf( 1f,-1f,0f))

        // 法線データ
        (0..3).forEach {
            datNor.addAll(arrayListOf(0f,0f,1f))
        }

        // 色データ
        (0..3).forEach {
            datCol.addAll(arrayListOf<Float>(color[0],color[1],color[2],color[3]))
        }

        // インデックスデータ
        datIdx.addAll(arrayListOf<Short>(0,1,2))
        datIdx.addAll(arrayListOf<Short>(3,2,1))
    }

    // XY平面(左回り)
    //  w53,w54,w55
    private fun createPathPattern53(opt: Map<String, Float>) {
        val color = FloatArray(4)
        color[0] = opt["colorR"] ?: 1f
        color[1] = opt["colorG"] ?: 1f
        color[2] = opt["colorB"] ?: 1f
        color[3] = opt["colorA"] ?: 1f

        // 頂点データ
        datPos.addAll(arrayListOf(-1f, 1f,0f))
        datPos.addAll(arrayListOf( 1f, 1f,0f))
        datPos.addAll(arrayListOf(-1f,-1f,0f))
        datPos.addAll(arrayListOf( 1f,-1f,0f))

        // 法線データ
        (0..3).forEach {
            datNor.addAll(arrayListOf(0f,0f,1f))
        }

        // 色データ
        (0..3).forEach {
            datCol.addAll(arrayListOf<Float>(color[0],color[1],color[2],color[3]))
        }

        // テクスチャ座標
        (0..3).forEach {
            datTxc.addAll(arrayListOf(0f,0f))
            datTxc.addAll(arrayListOf(1f,0f))
            datTxc.addAll(arrayListOf(0f,1f))
            datTxc.addAll(arrayListOf(1f,1f))
        }

        // インデックスデータ
        datIdx.addAll(arrayListOf<Short>(0,2,1))
        datIdx.addAll(arrayListOf<Short>(2,3,1))
    }

    // XZ平面(左回り)
    //  w49
    private fun createPathPattern49(opt: Map<String, Float>) {
        val color = FloatArray(4)
        color[0] = opt["colorR"] ?: 1f
        color[1] = opt["colorG"] ?: 1f
        color[2] = opt["colorB"] ?: 1f
        color[3] = opt["colorA"] ?: 1f

        // 頂点データ
        datPos.addAll(arrayListOf(-1f, 0f,-1f))
        datPos.addAll(arrayListOf( 1f, 0f,-1f))
        datPos.addAll(arrayListOf(-1f, 0f, 1f))
        datPos.addAll(arrayListOf( 1f, 0f, 1f))

        // 法線データ
        (0..3).forEach {
            datNor.addAll(arrayListOf(0f,1f,0f))
        }

        // 色データ
        (0..3).forEach {
            datCol.addAll(arrayListOf<Float>(color[0],color[1],color[2],color[3]))
        }

        // インデックスデータ
        datIdx.addAll(arrayListOf<Short>(0,1,2))
        datIdx.addAll(arrayListOf<Short>(3,2,1))
    }

    // XZ平面(右回り)
    //  w51
    private fun createPathPattern51(opt: Map<String, Float>) {
        val color = FloatArray(4)
        color[0] = opt["colorR"] ?: 1f
        color[1] = opt["colorG"] ?: 1f
        color[2] = opt["colorB"] ?: 1f
        color[3] = opt["colorA"] ?: 1f

        // 頂点データ
        datPos.addAll(arrayListOf(-1f, 0f,-1f))
        datPos.addAll(arrayListOf( 1f, 0f,-1f))
        datPos.addAll(arrayListOf(-1f, 0f, 1f))
        datPos.addAll(arrayListOf( 1f, 0f, 1f))

        // 法線データ
        (0..3).forEach {
            datNor.addAll(arrayListOf(0f,1f,0f))
        }

        // 色データ
        (0..3).forEach {
            datCol.addAll(arrayListOf<Float>(color[0],color[1],color[2],color[3]))
        }

        // インデックスデータ
        datIdx.addAll(arrayListOf<Short>(0,2,1))
        datIdx.addAll(arrayListOf<Short>(3,1,2))
    }
}