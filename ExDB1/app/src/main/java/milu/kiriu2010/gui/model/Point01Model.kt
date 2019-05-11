package milu.kiriu2010.gui.model

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.math.MyMathUtil
import java.lang.RuntimeException
import java.nio.*


// 点
class Point01Model: MgModelAbs() {

    override fun createPath( opt: Map<String,Float> ){
        var x = opt["x"] ?: 0f
        var y = opt["y"] ?: 0f
        var z = opt["z"] ?: 0f

        // 頂点データ
        datPos.add(x)
        datPos.add(y)
        datPos.add(z)

        allocateBuffer()
    }
}
