package milu.kiriu2010.gui.model

import java.nio.ByteBuffer
import java.nio.ByteOrder

// https://developer.android.com/training/graphics/opengl/shapes
class Square01Model: MgModelAbs() {
    override fun createPath(opt: Map<String, Float>) {
        // 頂点データ
        datPos.addAll(arrayListOf( 0f, 1f,0f))
        datPos.addAll(arrayListOf( 1f, 0f,0f))
        datPos.addAll(arrayListOf(-1f, 0f,0f))
        datPos.addAll(arrayListOf( 0f,-1f,0f))

        // 色データ
        datCol.addAll(arrayListOf(1f,0f,0f,1f))
        datCol.addAll(arrayListOf(0f,1f,0f,1f))
        datCol.addAll(arrayListOf(0f,0f,1f,1f))
        datCol.addAll(arrayListOf(1f,1f,1f,1f))

        // インデックスデータ
        datIdx.addAll(arrayListOf<Short>(0,1,2))
        datIdx.addAll(arrayListOf<Short>(1,2,3))

        allocateBuffer()
    }
}
