package milu.kiriu2010.gui.vbo.es30

import android.opengl.GLES30
import milu.kiriu2010.gui.basic.MyGLES30Func
import milu.kiriu2010.gui.model.MgModelAbs

// --------------------------------
// VAO
// VBO
//   0:頂点位置
//   1:法線
//   2:テクスチャ座標
//   3:オフセット
// IBO
// --------------------------------
// 2019.06.12
// --------------------------------
class ES30VAOIpnto: ES30VAOAbs() {

    override fun makeVIBO(modelAbs: MgModelAbs) {
        //Log.d(javaClass.simpleName,"makeVIBO:${modelAbs.javaClass.simpleName}")
        model = modelAbs

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
        hVBO = IntArray(4)
        GLES30.glGenBuffers(4, hVBO,0)
        MyGLES30Func.checkGlError("hVBO:glGenBuffers")

        // 位置
        modelAbs.bufPos.position(0)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,hVBO[0])
        MyGLES30Func.checkGlError("a_Position:glBindBuffer")
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,modelAbs.bufPos.capacity()*4, modelAbs.bufPos,usagePos)
        MyGLES30Func.checkGlError("a_Position:glBufferData")
        GLES30.glEnableVertexAttribArray(0)
        MyGLES30Func.checkGlError("a_Position:glEnableVertexAttribArray")
        GLES30.glVertexAttribPointer(0,3,GLES30.GL_FLOAT,false,0,0)
        MyGLES30Func.checkGlError("a_Position:glVertexAttribPointer")
        //GLES30.glVertexAttribPointer(0,3,GLES30.GL_FLOAT,false,3*4,0)

        // 法線
        modelAbs.bufNor.position(0)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,hVBO[1])
        MyGLES30Func.checkGlError("a_Normal:glBindBuffer")
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,modelAbs.bufNor.capacity()*4, modelAbs.bufNor,GLES30.GL_STATIC_DRAW)
        MyGLES30Func.checkGlError("a_Normal:glBufferData")
        GLES30.glEnableVertexAttribArray(1)
        MyGLES30Func.checkGlError("a_Normal:glEnableVertexAttribArray")
        GLES30.glVertexAttribPointer(1,3,GLES30.GL_FLOAT,false,0,0)
        MyGLES30Func.checkGlError("a_Normal:glVertexAttribPointer")
        //GLES30.glVertexAttribPointer(1,3,GLES30.GL_FLOAT,false,3*4,0)

        // テクスチャ座標
        modelAbs.bufTxc.position(0)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,hVBO[2])
        MyGLES30Func.checkGlError("a_TexCoord:glBindBuffer")
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,modelAbs.bufTxc.capacity()*4, modelAbs.bufTxc,GLES30.GL_STATIC_DRAW)
        MyGLES30Func.checkGlError("a_TexCoord:glBufferData")
        GLES30.glEnableVertexAttribArray(2)
        MyGLES30Func.checkGlError("a_TexCoord:glEnableVertexAttribArray")
        GLES30.glVertexAttribPointer(2,2,GLES30.GL_FLOAT,false,0,0)
        MyGLES30Func.checkGlError("a_TexCoord:glVertexAttribPointer")
        //GLES30.glVertexAttribPointer(2,2,GLES30.GL_FLOAT,false,2*4,0)

        // オフセット
        modelAbs.bufOff.position(0)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,hVBO[3])
        MyGLES30Func.checkGlError("a_Offset:glBindBuffer")
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,modelAbs.bufOff.capacity()*4, modelAbs.bufOff,GLES30.GL_STATIC_DRAW)
        MyGLES30Func.checkGlError("a_Offset:glBufferData")
        GLES30.glEnableVertexAttribArray(3)
        MyGLES30Func.checkGlError("a_Offset:glEnableVertexAttribArray")
        GLES30.glVertexAttribPointer(3,3,GLES30.GL_FLOAT,false,0,0)
        MyGLES30Func.checkGlError("a_Offset:glVertexAttribPointer")
        GLES30.glVertexAttribDivisor(3,1)
        MyGLES30Func.checkGlError("a_Offset:glVertexAttribDivisor")

        // ------------------------------------------------
        // IBOの生成
        // ------------------------------------------------
        hIBO = IntArray(1)
        GLES30.glGenBuffers(1, hIBO,0)
        MyGLES30Func.checkGlError("hIBO:glGenBuffers")

        modelAbs.bufIdx.position(0)
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER,hIBO[0])
        MyGLES30Func.checkGlError("idx:glBindBuffer")
        GLES30.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER,modelAbs.bufIdx.capacity()*2, modelAbs.bufIdx,GLES30.GL_STATIC_DRAW)
        MyGLES30Func.checkGlError("idx:glBufferData")

        // リソース解放
        GLES30.glBindVertexArray(0)
        MyGLES30Func.checkGlError("glBindVertexArray")
        //GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,0)
        //GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}
