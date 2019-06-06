package milu.kiriu2010.exdb1.opengl07.w071v

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.IntBuffer

// --------------------------------
// VBO
//   インデックス
// IBO
// --------------------------------
// 2019.06.06
// --------------------------------
class ES20VBOIi: ES20VBOAbs() {

    override fun makeVIBO(model: MgModelAbs) {
        // ------------------------------------------------
        // VBOの生成
        // ------------------------------------------------
        hVBO = IntArray(1)
        GLES20.glGenBuffers(1, hVBO,0)

        // インデックス
        /*
        val buf = IntBuffer.allocate(1)
        buf.put(model.datIdx.size/3)
        buf.position(0)
        */
        val data = (model.datIdx.size/3).toFloat()
        val buf = ByteBuffer.allocateDirect(4).run {
            order(ByteOrder.nativeOrder())

            asFloatBuffer().apply {
                put(floatArrayOf(data))
                position(0)
            }
        }
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,hVBO[0])
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,buf.capacity()*4, buf,GLES20.GL_STATIC_DRAW)

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
