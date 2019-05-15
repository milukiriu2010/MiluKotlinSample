package milu.kiriu2010.exdb1.opengl02.w029

import milu.kiriu2010.gui.model.MgModelAbs

// ----------------------------------------
// テクスチャパラメータ用モデル
// ----------------------------------------
// https://wgld.org/d/webgl/w028.html
// ----------------------------------------
class W029Model: MgModelAbs() {
    override fun createPath(opt: Map<String, Float>) {
        // 頂点データ
        datPos.addAll(arrayListOf(-1f, 1f,0f))
        datPos.addAll(arrayListOf( 1f, 1f,0f))
        datPos.addAll(arrayListOf(-1f,-1f,0f))
        datPos.addAll(arrayListOf( 1f,-1f,0f))

        // 色データ
        datCol.addAll(arrayListOf(1f,0f,0f,1f))
        datCol.addAll(arrayListOf(0f,1f,0f,1f))
        datCol.addAll(arrayListOf(0f,0f,1f,1f))
        datCol.addAll(arrayListOf(1f,1f,1f,1f))

        // テクスチャ座標
        datTxc.addAll(arrayListOf(0f,0f))
        datTxc.addAll(arrayListOf(1f,0f))
        datTxc.addAll(arrayListOf(0f,1f))
        datTxc.addAll(arrayListOf(1f,1f))

        // 頂点インデックス
        datIdx.addAll(arrayListOf(0,1,2))
        datIdx.addAll(arrayListOf(3,2,1))

        allocateBuffer()
    }
}
