package milu.kiriu2010.gui.vbo.es30

import android.opengl.GLES30
import android.util.Log
import milu.kiriu2010.gui.basic.MyGLES30Func
import milu.kiriu2010.gui.model.MgModelAbs

// --------------------------------
// VAO
// VBO
//   0:頂点位置
//   1:法線
//   2:テクスチャ座標
// IBO
// --------------------------------
// 2019.05.31
// 2019.06.11 VAO追加
// --------------------------------
class ES30VAOIpnt: ES30VAOAbs() {

    override fun makeVIBO(model: MgModelAbs) {
        //Log.d(javaClass.simpleName,"makeVIBO:${model.javaClass.simpleName}")

        // ------------------------------------------------
        // VAOの生成
        // ------------------------------------------------
        hVAO = IntArray(1)
        GLES30.glGenVertexArrays(1,hVAO,0)
        MyGLES30Func.checkGlError("glGenVertexArrays")
        GLES30.glBindVertexArray(hVAO[0])
        MyGLES30Func.checkGlError("glBindVertexArray")

        // ------------------------------------------------
        // VBOの生成
        // ------------------------------------------------
        hVBO = IntArray(3)
        GLES30.glGenBuffers(3, hVBO,0)
        MyGLES30Func.checkGlError("hVBO:glGenBuffers")

        // 位置
        model.bufPos.position(0)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,hVBO[0])
        MyGLES30Func.checkGlError("a_Position:glBindBuffer")
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,model.bufPos.capacity()*4, model.bufPos,usageVBO)
        MyGLES30Func.checkGlError("a_Position:glBufferData")
        GLES30.glEnableVertexAttribArray(0)
        MyGLES30Func.checkGlError("a_Position:glEnableVertexAttribArray")
        GLES30.glVertexAttribPointer(0,3,GLES30.GL_FLOAT,false,0,0)
        MyGLES30Func.checkGlError("a_Position:glVertexAttribPointer")
        //GLES30.glVertexAttribPointer(0,3,GLES30.GL_FLOAT,false,3*4,0)

        // 法線
        model.bufNor.position(0)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,hVBO[1])
        MyGLES30Func.checkGlError("a_Normal:glBindBuffer")
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,model.bufNor.capacity()*4, model.bufNor,usageVBO)
        MyGLES30Func.checkGlError("a_Normal:glBufferData")
        GLES30.glEnableVertexAttribArray(1)
        MyGLES30Func.checkGlError("a_Normal:glEnableVertexAttribArray")
        GLES30.glVertexAttribPointer(1,3,GLES30.GL_FLOAT,false,0,0)
        MyGLES30Func.checkGlError("a_Normal:glVertexAttribPointer")
        //GLES30.glVertexAttribPointer(1,3,GLES30.GL_FLOAT,false,3*4,0)

        // テクスチャ座標
        model.bufTxc.position(0)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,hVBO[2])
        MyGLES30Func.checkGlError("a_TexCoord:glBindBuffer")
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,model.bufTxc.capacity()*4, model.bufTxc,usageVBO)
        MyGLES30Func.checkGlError("a_TexCoord:glBufferData")
        GLES30.glEnableVertexAttribArray(2)
        MyGLES30Func.checkGlError("a_TexCoord:glEnableVertexAttribArray")
        GLES30.glVertexAttribPointer(2,2,GLES30.GL_FLOAT,false,0,0)
        MyGLES30Func.checkGlError("a_TexCoord:glVertexAttribPointer")
        //GLES30.glVertexAttribPointer(2,2,GLES30.GL_FLOAT,false,2*4,0)

        // ------------------------------------------------
        // IBOの生成
        // ------------------------------------------------
        hIBO = IntArray(1)
        GLES30.glGenBuffers(1, hIBO,0)
        MyGLES30Func.checkGlError("hIBO:glGenBuffers")

        model.bufIdx.position(0)
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER,hIBO[0])
        MyGLES30Func.checkGlError("idx:glBindBuffer")
        GLES30.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER,model.bufIdx.capacity()*2, model.bufIdx,GLES30.GL_STATIC_DRAW)
        MyGLES30Func.checkGlError("idx:glBufferData")

        // リソース解放
        GLES30.glBindVertexArray(0)
        MyGLES30Func.checkGlError("glBindVertexArray")
        //GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,0)
        //GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}
