package milu.kiriu2010.gui.basic

import android.graphics.Bitmap
import android.graphics.Color
import kotlin.math.floor
import kotlin.math.pow

// ---------------------------------------
// パーティクルフォグで利用
// ---------------------------------------
// https://wgld.org/d/webgl/w061.html
// ---------------------------------------
class MyNoiseX(
        var octave: Int,
        var offset: Int,
        var persistence: Float
) {
    var seed = 1

    fun detail(oct: Int, ofs: Int, per: Float): Boolean {
        if ( oct == 0 )  return false
        if ( ofs == 0 )  return false
        if ( per == 0f ) return false

        if (oct > 1) octave = oct
        if (ofs > 0) offset = ofs
        if ( (per > 0) and (per <= 1) ) persistence = per

        return true
    }


    fun rnd(x: Int, y: Int): Float {
        var a = 123456789
        var b = a xor (a shl 11)
        var c = seed + x + seed*y
        var d = c xor (c ushr 19) xor (b xor (b ushr 8))
        var e = (d%0x1000000/0x1000000).toFloat()
        e *= 10000000f
        return e - floor(e)
    }

    fun srnd(x: Int, y:Int): Float {
        val corners =
            (
                rnd(x-1,y-1) +
                rnd(x+1,y-1) +
                rnd(x-1,y+1) +
                rnd(x+1,y+1)
            ) * 0.03125f
        val sides =
            (
                rnd(x-1, y) +
                rnd(x+1, y) +
                rnd( x,y-1) +
                rnd( x,y+1)
            ) * 0.0625f
        val center = rnd(x,y) * 0.0625f
        return corners + sides + center
    }

    fun irnd(x: Float, y: Float): Float {
        val ix = floor(x).toInt()
        val iy = floor(y).toInt()
        val fx = x - ix.toFloat()
        val fy = y - iy.toFloat()
        val a = srnd(ix,iy)
        val b = srnd(ix+1,iy)
        val c = srnd(ix,iy+1)
        val d = srnd(ix+1,iy+1)
        val e = interpolate(b,a,fx)
        val f = interpolate(d,c,fx)
        return interpolate(f,e,fy)
    }

    fun noise(x: Float, y: Float): Float {
        var t = 0f
        val o = octave + offset
        val w = 2f.pow(o)
        (offset until o).forEach { i ->
            val f = 2f.pow(i)
            val p = persistence.pow(i-offset+1)
            val b = w/f
            t += irnd(x/b,y/b)*p
        }
        return t
    }

    fun snoise(x: Float,y: Float,w: Float): Float {
        val u = x/w
        val v = y/w
        val t = noise(x,y) * u * v +
                noise(x,y+w) * u * (1f-v) +
                noise(x+w,y) * (1f-u) * v +
                noise(x+w,y+w) * (1f-u) * (1f-v)
        return t
    }

    fun createImage(width: Int,data: FloatArray): Bitmap {
        val bmp = Bitmap.createBitmap(width,width,Bitmap.Config.ARGB_8888)

        (0 until width).forEach { i ->
            (0 until width).forEach { j ->
                val d = data[i*width+j]*255
                bmp.setPixel(i,j, Color.argb(255f,d,d,d))
            }
        }

        return bmp
    }

    companion object {
        fun interpolate(a: Float, b: Float, t: Float): Float {
            return a*t + b*(1f-t)
        }


    }
}
