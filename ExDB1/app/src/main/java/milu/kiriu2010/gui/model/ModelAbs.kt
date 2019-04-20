package milu.kiriu2010.gui.model

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

abstract class MgModelAbs {
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

    protected fun allocateBuffer() {
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

        // テクスチャ座標バッファ
        bufTxc = ByteBuffer.allocateDirect(datTxc.toArray().size * 4).run {
            order(ByteOrder.nativeOrder())

            asFloatBuffer().apply {
                put(datTxc.toFloatArray())
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

    abstract fun createPath( opt: Map<String,Float> = mapOf() )
}