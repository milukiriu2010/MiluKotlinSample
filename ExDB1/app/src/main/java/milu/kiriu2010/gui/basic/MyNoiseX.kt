package milu.kiriu2010.gui.basic

import android.graphics.Bitmap
import android.graphics.Color
import kotlin.math.floor
import kotlin.math.pow

// ---------------------------------------
// シームレスなノイズテクスチャを生成
// ---------------------------------------
// パーリンノイズ(perlin noize)と呼ばれる
// ノイズ生成の仕組みを使っている
// ---------------------------------------
// パーティクルフォグで利用
// ---------------------------------------
// https://wgld.org/d/webgl/w061.html
// ---------------------------------------
class MyNoiseX(
        // いくつの階層のノイズを合成するかを表す
        var octave: Int = 5,
        // 最小解像度
        var offset: Int = 3,
        // ノイズをどのような割合で合成するかを決定する
        // たいていの場合、0.5程度の値にする
        // 解像度の低いノイズから順に、パーシステンスの値を元に割合を決定する
        // ------------------------------------------------------------------
        // パーシステンスの値が0.5の場合
        //   4x  4 のノイズにかけるパーシステンス⇒パーシステンスの1乗=0.5
        //   8x  8 のノイズにかけるパーシステンス⇒パーシステンスの2乗=0.25
        //  16x 16 のノイズにかけるパーシステンス⇒パーシステンスの3乗=0.125
        //  32x 32 のノイズにかけるパーシステンス⇒パーシステンスの4乗=0.0625
        //  64x 64 のノイズにかけるパーシステンス⇒パーシステンスの5乗=0.03125
        // 128x128 のノイズにかけるパーシステンス⇒パーシステンスの6乗=0.0015625
        // ------------------------------------------------------------------
        var persistence: Float = 0.5f
) {
    var seed = 1

    fun detail(oct: Int, ofs: Int, per: Float): Boolean {
        if ( oct == 0 )  return false
        if ( ofs == 0 )  return false
        if ( per == 0f ) return false

        if (oct > 1) octave = oct
        if (ofs > 0) offset = ofs
        if ( (per > 0f) and (per <= 1f) ) persistence = per

        return true
    }

    // Xorshiftと呼ばれる疑似乱数生成手法
    //  x:X座標
    //  y:Y座標
    fun rnd(x: Int, y: Int): Float {
        var a = 123456789
        var b = a xor (a shl 11)
        var c = seed + x + seed*y
        var d = c xor (c ushr 19) xor (b xor (b ushr 8))
        var e = (d%0x1000000/0x1000000).toFloat()
        e *= 10000000f
        return e - floor(e)
    }

    // 乱数を均衡化するために使われる補助的なメソッド
    //  x:X座標
    //  y:Y座標
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

    // interpolateメソッドを用いてノイズを線形補完する
    //  x:X座標
    //  y:Y座標
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

    // ノイズ生成を行う
    // ------------------------------------------------------------------
    //  x:X座標
    //  y:Y座標
    //  戻り値: 指定された座標の値を正規化したもの(すなわち0-1の範囲の値)
    // ------------------------------------------------------------------
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

    // シームレスなノイズテクスチャを生成できる
    // ------------------------------------------------------------------
    //  x:X座標
    //  y:Y座標
    //  戻り値: 指定された座標の値を正規化したもの(すなわち0-1の範囲の値)
    // ------------------------------------------------------------------
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
        // 2つの値を線形補間した値を返す
        // ノイズを均衡化するために用いる
        //   a:値1
        //   b:値2
        //   t:補間比率
        fun interpolate(a: Float, b: Float, t: Float): Float {
            return a*t + b*(1f-t)
        }


    }
}
