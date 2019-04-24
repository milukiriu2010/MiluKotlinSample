package milu.kiriu2010.gui.model

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.math.MyMathUtil
import java.lang.RuntimeException
import java.nio.*


// 座標軸
class Axis01Model: MgModelAbs() {

    override fun createPath( opt: Map<String,Float> ){
        // 頂点データ
        // X軸
        datPos.addAll(arrayListOf(  -2f,   0f,  0f))   // X:0
        datPos.addAll(arrayListOf(   2f,   0f,  0f))   // X:1
        datPos.addAll(arrayListOf( 1.9f, 0.1f,  0f))   // X:2
        datPos.addAll(arrayListOf(   2f,   0f,  0f))   // X:3
        datPos.addAll(arrayListOf( 1.9f,-0.1f,  0f))   // X:4
        datPos.addAll(arrayListOf(   2f,   0f,  0f))   // X:5
        // Y軸
        datPos.addAll(arrayListOf(   0f,  -2f,  0f))   // Y:0
        datPos.addAll(arrayListOf(   0f,   2f,  0f))   // Y:1
        datPos.addAll(arrayListOf( 0.1f, 1.9f,  0f))   // Y:2
        datPos.addAll(arrayListOf(   0f,   2f,  0f))   // Y:3
        datPos.addAll(arrayListOf(-0.1f, 1.9f,  0f))   // Y:4
        datPos.addAll(arrayListOf(   0f,   2f,  0f))   // Y:5
        // Z軸
        datPos.addAll(arrayListOf(   0f,   0f, -2f))   // Z:0
        datPos.addAll(arrayListOf(   0f,   0f,  2f))   // Z:1
        datPos.addAll(arrayListOf(   0f, 0.1f,1.9f))   // Z:2
        datPos.addAll(arrayListOf(   0f,   0f,  2f))   // Z:3
        datPos.addAll(arrayListOf(   0f,-0.1f,1.9f))   // Z:4
        datPos.addAll(arrayListOf(   0f,   0f,  2f))   // Z:5


        // 色データ
        // X軸
        (0..5).forEach {
            datCol.addAll(arrayListOf<Float>(1f,0f,0f,1f))
        }
        // Y軸
        (6..11).forEach {
            datCol.addAll(arrayListOf<Float>(0f,1f,0f,1f))
        }
        // Z軸
        (12..17).forEach {
            datCol.addAll(arrayListOf<Float>(0f,0f,1f,1f))
        }

        allocateBuffer()
    }
}
