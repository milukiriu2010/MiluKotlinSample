package milu.kiriu2010.gui.model

import java.nio.FloatBuffer
import java.nio.ShortBuffer

abstract class ModelAbs {
    // 頂点バッファ
    lateinit var bufPos: FloatBuffer
    // 法線バッファ
    lateinit var bufNor: FloatBuffer
    // 色バッファ
    lateinit var bufCol: FloatBuffer
    // テクスチャコードバッファ
    lateinit var bufTxc: FloatBuffer
    // インデックスバッファ
    lateinit var bufIdx: ShortBuffer

    // 頂点データ
    val datPos = arrayListOf<Float>()
    // 法線データ
    val datNor = arrayListOf<Float>()
    // 色データ
    val datCol = arrayListOf<Float>()
    // テクスチャコードデータ
    val datTxc = arrayListOf<Float>()
    // インデックスデータ
    val datIdx = arrayListOf<Short>()

}