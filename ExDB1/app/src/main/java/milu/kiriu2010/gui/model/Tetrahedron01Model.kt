package milu.kiriu2010.gui.model

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.math.MyMathUtil
import java.lang.RuntimeException
import java.nio.*


// https://wgld.org/d/webgl/w026.html
class Tetrahedron01Model: MgModelAbs() {

    override fun createPath( opt: Map<String,Float> ){
        // 頂点データ
        datPos.addAll(arrayListOf(-1f,1f,-1f))  // v0
        datPos.addAll(arrayListOf(-1f,-1f,1f))  // v1
        datPos.addAll(arrayListOf(1f,1f,1f))    // v2
        datPos.addAll(arrayListOf(1f,-1f,-1f))  // v3
        datPos.addAll(arrayListOf(-1f,-1f,1f))  // v4,v1
        datPos.addAll(arrayListOf(-1f,1f,-1f))  // v5,v0
        datPos.addAll(arrayListOf(-1f,-1f,1f))  // v6,v4,v1
        datPos.addAll(arrayListOf(1f,-1f,-1f))  // v7,v3
        datPos.addAll(arrayListOf(1f,1f,1f))    // v8,v2
        datPos.addAll(arrayListOf(-1f,1f,-1f))  // v9,v5,v0
        datPos.addAll(arrayListOf(1f,1f,1f))    // v10,v8,v2
        datPos.addAll(arrayListOf(1f,-1f,-1f))  // v11,v7,v3

        // 法線データ
        (0..2).forEach {
            // (v1-v0) x (v2-v0)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*1, 3*2, 3*0 ) )
        }
        (3..5).forEach {
            // (v4-v3) x (v5-v3)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*4, 3*5, 3*3 ) )
        }
        (6..8).forEach {
            // (v7-v6) x (v8-v6)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*7, 3*8, 3*6 ) )
        }
        (9..11).forEach {
            // (v10-v9) x (v11-v9)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*10, 3*11, 3*9 ) )
        }

        // 色データ
        (0..2).forEach {
            datCol.addAll(arrayListOf<Float>(1f,0f,0f,1f))
        }
        (3..5).forEach {
            datCol.addAll(arrayListOf<Float>(0f,1f,0f,1f))
        }
        (6..8).forEach {
            datCol.addAll(arrayListOf<Float>(0f,0f,1f,1f))
        }
        (9..11).forEach {
            datCol.addAll(arrayListOf<Float>(1f,1f,0f,1f))
        }

        // インデックスデータ
        datIdx.addAll(arrayListOf<Short>(0,1,2))
        datIdx.addAll(arrayListOf<Short>(3,4,5))
        datIdx.addAll(arrayListOf<Short>(6,7,8))
        datIdx.addAll(arrayListOf<Short>(9,10,11))

        allocateBuffer()
    }
}
