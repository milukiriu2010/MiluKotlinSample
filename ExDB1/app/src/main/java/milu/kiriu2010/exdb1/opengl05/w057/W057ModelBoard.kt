package milu.kiriu2010.exdb1.opengl05.w057

import milu.kiriu2010.gui.model.MgModelAbs
import java.nio.ByteBuffer
import java.nio.ByteOrder

// 板ポリゴン
// https://wgld.org/d/webgl/w049.html
class W057ModelBoard: MgModelAbs() {
    override fun createPath(opt: Map<String, Float>) {
        // 頂点データ
        datPos.addAll(listOf(-1f,1f,0f))
        datPos.addAll(listOf(1f,1f,0f))
        datPos.addAll(listOf(-1f,-1f,0f))
        datPos.addAll(listOf(1f,-1f,0f))

        // テクスチャ座標データ
        datTxc.addAll(listOf(0f,0f));
        datTxc.addAll(listOf(1f,0f));
        datTxc.addAll(listOf(0f,1f));
        datTxc.addAll(listOf(1f,1f));

        // インデックスデータ
        datIdx.addAll(listOf(0,2,1))
        datIdx.addAll(listOf(2,3,1))


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

        // テクスチャコードバッファ
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


}