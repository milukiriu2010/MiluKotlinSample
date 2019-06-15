package milu.kiriu2010.gui.vbo.es30

import android.opengl.GLES30
import milu.kiriu2010.gui.model.MgModelAbs

// --------------------------------
// VBO
//   0:頂点位置
//   1:法線
//   2:テクスチャ座標
// IBO
// --------------------------------
// 2019.05.31
// --------------------------------
class ES30VBOIpnt: ES30VBOAbs() {

    override fun makeVIBO(model: MgModelAbs) {
        // ------------------------------------------------
        // VBOの生成
        // ------------------------------------------------
        hVBO = IntArray(3)
        GLES30.glGenBuffers(3, hVBO,0)

        // 位置
        model.bufPos.position(0)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,hVBO[0])
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,model.bufPos.capacity()*4, model.bufPos,usagePos)

        // 法線
        model.bufNor.position(0)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,hVBO[1])
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,model.bufNor.capacity()*4, model.bufNor,GLES30.GL_STATIC_DRAW)

        // テクスチャ座標
        model.bufTxc.position(0)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,hVBO[2])
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,model.bufTxc.capacity()*4, model.bufTxc,GLES30.GL_STATIC_DRAW)

        // ------------------------------------------------
        // IBOの生成
        // ------------------------------------------------
        hIBO = IntArray(1)
        GLES30.glGenBuffers(1, hIBO,0)

        model.bufIdx.position(0)
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER,hIBO[0])
        GLES30.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER,model.bufIdx.capacity()*2, model.bufIdx,GLES30.GL_STATIC_DRAW)

        // リソース解放
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,0)
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}
