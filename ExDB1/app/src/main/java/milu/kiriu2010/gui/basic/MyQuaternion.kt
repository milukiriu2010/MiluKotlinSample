package milu.kiriu2010.gui.basic

import milu.kiriu2010.math.MyMathUtil
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

    fun toMatIV(): FloatArray {
        var x = q[0]
        var y = q[1]
        var z = q[2]
        var w = q[3]
        var x2 = x+x
        var y2 = y+y
        var z2 = z+z
        var xx = x*x2
        var xy = x*y2
        var xz = x*z2
        var yy = y*y2
        var yz = y*z2
        var zz = z*z2
        var wx = w*x2
        var wy = w*y2
        var wz = w*z2

        return floatArrayOf(
                1f-(yy+zz), // 0
                xy-wz,      // 1
                xz+wy,      // 2
                0f,         // 3
                xy+wz,      // 4
                1-(xx+zz),  // 5
                yz-wx,      // 6
                0f,         // 7
                xz-wy,      // 8
                yz+wx,      // 9
                1-(xx+yy),  // 10
                0f,         // 11
                0f,         // 12
                0f,         // 13
                0f,         // 14
                1f          // 15
        )
    }

    companion object {
        fun rotate( angle: Float, axis: FloatArray ): MyQuaternion {
            val qtn = MyQuaternion()
            var sq = sqrt(axis[0]*axis[0]+axis[1]*axis[1]+axis[2]*axis[2])
            if (sq == 0f) {
                qtn.q[0] = 0f
                qtn.q[1] = 0f
                qtn.q[2] = 0f
                qtn.q[3] = 0f
            }
            else {
                var a = axis[0]
                var b = axis[1]
                var c = axis[2]
                if ( sq != 1f ) {
                    sq = 1f/sq
                    a *= sq
                    b *= sq
                    c *= sq
                }
                var sin = MyMathUtil.sinf(angle/2f)
                qtn.q[0] = a * sin
                qtn.q[1] = b * sin
                qtn.q[2] = c * sin
                qtn.q[3] = MyMathUtil.cosf(angle/2f)
            }

            return qtn
        }

        fun toVecIII(vec: FloatArray, qtn: MyQuaternion): FloatArray {
            var qp = MyQuaternion(floatArrayOf(vec[0],vec[1],vec[2],0f))
            var qr = MyQuaternion(floatArrayOf(qtn.q[0],qtn.q[1],qtn.q[2],qtn.q[3]))
            qr.inverse()
            var qq = MyQuaternion(floatArrayOf(qr.q[0],qr.q[1],qr.q[2],qr.q[3]))
                    .multiply(qp)
            var qs = MyQuaternion(floatArrayOf(qq.q[0],qq.q[1],qq.q[2],qq.q[3]))
                    .multiply(qtn)
            return floatArrayOf(qs.q[0],qs.q[1],qs.q[2])
        }
    }

}