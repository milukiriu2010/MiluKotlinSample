package milu.kiriu2010.gui.basic

import kotlin.math.sqrt

// https://wgld.org/d/webgl/w032.html
// のライブラリをまねた
data class MyQuaternion(
    val q: FloatArray = floatArrayOf(0f,0f,0f,0f)
) {

    fun identity(): MyQuaternion {
        q[0] = 0f
        q[1] = 0f
        q[2] = 0f
        q[3] = 1f

        return this
    }

    fun inverse(): MyQuaternion {
        q[0] = -q[0]
        q[1] = -q[1]
        q[2] = -q[2]
        q[3] = q[3]

        return this
    }

    fun normalize(): MyQuaternion {
        val x = q[0]
        val y = q[1]
        val z = q[2]
        val w = q[3]
        val l = sqrt(x*x+y*y+z*z+w*w)
        if ( l === 0f ) {
            q[0] = 0f
            q[1] = 0f
            q[2] = 0f
            q[3] = 0f
        }
        else {
            q[0] /= l
            q[1] /= l
            q[2] /= l
            q[3] /= l
        }

        return this
    }

    fun multiply( qtn: MyQuaternion ): MyQuaternion {
        val ax = q[0]
        val ay = q[1]
        val az = q[2]
        val aw = q[3]
        val bx = qtn.q[0]
        val by = qtn.q[1]
        val bz = qtn.q[2]
        val bw = qtn.q[3]

        q[0] = ax * bw + aw * bx + ay * bz - az * by
        q[1] = ay * bw + aw * by + az * bx - ax * bz
        q[2] = az * bw + aw * bz + ax * by - ay * bx
        q[3] = aw * bw - ax * bx - ay * by - az * bz

        return this
    }


}