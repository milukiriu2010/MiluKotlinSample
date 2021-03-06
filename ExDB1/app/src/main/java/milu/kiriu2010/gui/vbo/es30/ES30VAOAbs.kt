package milu.kiriu2010.gui.vbo.es30

import android.opengl.GLES30
import milu.kiriu2010.gui.model.MgModelAbs

// --------------------------------------------------------------
// VAO親玉クラス
// --------------------------------------------------------------
// 2019.05.31
// 2019.06.07 バッファにデータを設定するときの変数usageを追加
// 2019.06.11 VAO追加
// 2019.06.14 バッファにデータを設定するときの変数名を位置用に変更
// --------------------------------------------------------------
abstract class ES30VAOAbs {
    // 描画モデル
    lateinit var model: MgModelAbs
    // VAOのハンドル
    lateinit var hVAO: IntArray
    // VBOのハンドル
    lateinit var hVBO: IntArray
    // IBOのハンドル
    lateinit var hIBO: IntArray

    // VBOバッファにデータを設定するときのusage(位置)
    var usagePos = GLES30.GL_STATIC_DRAW

    abstract fun makeVIBO(modelAbs: MgModelAbs)

    fun deleteVIBO() {
        if (this::hVAO.isInitialized) {
            GLES30.glDeleteVertexArrays(hVAO.size,hVAO,0)
        }
        if (this::hVBO.isInitialized) {
            GLES30.glDeleteBuffers(hVBO.size,hVBO,0)
        }
        if (this::hIBO.isInitialized) {
            GLES30.glDeleteBuffers(hIBO.size,hIBO,0)
        }
    }
}
