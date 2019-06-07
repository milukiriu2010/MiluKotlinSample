package milu.kiriu2010.gui.vbo.es30

import android.opengl.GLES30
import milu.kiriu2010.gui.model.MgModelAbs

// --------------------------------------------------------------
// VBO親玉クラス
// --------------------------------------------------------------
// 2019.05.31
// 2019.06.07 バッファにデータを設定するときの変数usageを追加
// --------------------------------------------------------------
abstract class ES30VBOAbs {
    // VBOのハンドル
    lateinit var hVBO: IntArray
    // IBOのハンドル
    lateinit var hIBO: IntArray

    // VBOバッファにデータを設定するときのusage
    var usageVBO = GLES30.GL_STATIC_DRAW

    abstract fun makeVIBO(model: MgModelAbs)

    fun deleteVIBO() {
        if (this::hVBO.isInitialized) {
            GLES30.glDeleteBuffers(hVBO.size,hVBO,0)
        }
        if (this::hIBO.isInitialized) {
            GLES30.glDeleteBuffers(hIBO.size,hIBO,0)
        }
    }
}
