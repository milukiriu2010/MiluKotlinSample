package milu.kiriu2010.exdb1.opengl

import java.nio.FloatBuffer
import java.nio.ShortBuffer

abstract class ModelAbs {
    // 頂点バッファ
    lateinit var bufPos: FloatBuffer
    // 法線バッファ
    lateinit var bufNor: FloatBuffer
    // 色バッファ
    lateinit var bufCol: FloatBuffer
    // インデックスバッファ
    lateinit var bufIdx: ShortBuffer

    val datPos = arrayListOf<Float>()
    val datNor = arrayListOf<Float>()
    val datCol = arrayListOf<Float>()
    val datIdx = arrayListOf<Short>()

}