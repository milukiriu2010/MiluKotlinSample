package milu.kiriu2010.exdb1.mgl01.vbo02

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs

class ES20VBO02viBO {
    // VBOのハンドル
    lateinit var hVBO: IntArray
    // IBOのハンドル
    lateinit var hIBO: IntArray

    fun makeVIBO(model: MgModelAbs) {
        // ------------------------------------------------
        // VBOの生成
        // ------------------------------------------------
        hVBO = IntArray(1)
        GLES20.glGenBuffers(1, hVBO,0)

        // 頂点
        model.bufPos.position(0)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,hVBO[0])
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,model.bufPos.capacity()*4, model.bufPos,GLES20.GL_STATIC_DRAW)

        // リソース解放
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0)
    }

    fun deleteVIBO() {
        if (this::hVBO.isInitialized) {
            GLES20.glDeleteBuffers(hVBO.size,hVBO,0)
        }
        if (this::hIBO.isInitialized) {
            GLES20.glDeleteBuffers(hIBO.size,hIBO,0)
        }
    }
}
