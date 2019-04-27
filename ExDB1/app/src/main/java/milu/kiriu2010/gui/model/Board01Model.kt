package milu.kiriu2010.gui.model

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

// 板ポリゴン
class Board01Model: MgModelAbs() {
    override fun createPath(opt: Map<String, Float>) {
        // 頂点データ
        datPos.addAll(arrayListOf(-1f,1f,0f))
        datPos.addAll(arrayListOf(1f,1f,0f))
        datPos.addAll(arrayListOf(-1f,-1f,0f))
        datPos.addAll(arrayListOf(1f,-1f,0f))

        // 色データ
        (0..3).forEach {
            datCol.addAll(arrayListOf<Float>(1f,1f,1f,1f))
        }

        // インデックスデータ
        datIdx.addAll(arrayListOf<Short>(0,1,2))
        datIdx.addAll(arrayListOf<Short>(3,2,1))

        allocateBuffer()
    }
}