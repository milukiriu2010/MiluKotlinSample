package milu.kiriu2010.exdb1.es30x01.a06

import android.opengl.GLES30
import milu.kiriu2010.gui.model.MgModelAbs

// -------------------------------
// VAO for ShaderA用
// -------------------------------
class A06VaoA {

    // VertexBufferObject Ids
    val mVBOIds = IntArray(4)

    // VertexArrayObject Id
    val mVAOIds = IntArray(1)

    fun createVAO(model: MgModelAbs) {
        // ----------------------------------------------
        // VBO生成
        // ----------------------------------------------
        GLES30.glGenBuffers(4,mVBOIds,0)

        // ----------------------------------------------
        // VAO生成
        // ----------------------------------------------
        GLES30.glGenVertexArrays(1,mVAOIds,0)
        GLES30.glBindVertexArray(mVAOIds[0])

        // in(頂点)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,mVBOIds[0])
        model.bufPos.position(0)
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,model.datPos.size*4,model.bufPos, GLES30.GL_STATIC_DRAW)
        GLES30.glEnableVertexAttribArray(0)
        GLES30.glVertexAttribPointer(0,3, GLES30.GL_FLOAT,false,3*4,model.bufPos)

        // in(法線)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,mVBOIds[1])
        model.bufNor.position(0)
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,model.datNor.size*4,model.bufNor, GLES30.GL_STATIC_DRAW)
        GLES30.glEnableVertexAttribArray(1)
        GLES30.glVertexAttribPointer(0,3, GLES30.GL_FLOAT,false,3*4,model.bufNor)

        // in(テクスチャ座標)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,mVBOIds[2])
        model.bufTxc.position(0)
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,model.datTxc.size*4,model.bufTxc, GLES30.GL_STATIC_DRAW)
        GLES30.glEnableVertexAttribArray(1)
        GLES30.glVertexAttribPointer(0,2, GLES30.GL_FLOAT,false,2*4,model.bufTxc)

        // 頂点インデックス
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER,mVBOIds[3])
        model.bufIdx.position(0)
        GLES30.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER,model.datIdx.size*2,model.bufIdx, GLES30.GL_STATIC_DRAW)

        // VAO解放
        GLES30.glBindVertexArray(0)
    }
}