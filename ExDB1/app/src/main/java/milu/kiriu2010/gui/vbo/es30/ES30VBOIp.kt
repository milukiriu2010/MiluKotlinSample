package milu.kiriu2010.gui.vbo.es30

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs

// ------------------------------------------------------------
// VBO(頂点位置)
// IBO
// ------------------------------------------------------------
// 2019.06.05
// 2019.06.07 バッファにデータを設定するときの変数usageを追加
// 2019.06.08 ES30対応
// ------------------------------------------------------------
class ES30VBOIp: ES30VBOAbs() {

    override fun makeVIBO(model: MgModelAbs) {
        // ------------------------------------------------
        // VBOの生成
        // ------------------------------------------------
        hVBO = IntArray(1)
        GLES20.glGenBuffers(1, hVBO,0)

        // 位置
        model.bufPos.position(0)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,hVBO[0])
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,model.bufPos.capacity()*4, model.bufPos,usageVBO)

        // ------------------------------------------------
        // IBOの生成
        // ------------------------------------------------
        hIBO = IntArray(1)
        GLES20.glGenBuffers(1, hIBO,0)

        model.bufIdx.position(0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,hIBO[0])
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER,model.bufIdx.capacity()*2, model.bufIdx,GLES20.GL_STATIC_DRAW)

        // リソース解放
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}
