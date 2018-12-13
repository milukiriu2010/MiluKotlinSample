package milu.kiriu2010.gui.fractal

import android.graphics.*
import android.graphics.drawable.Drawable
import milu.kiriu2010.math.Complex

// マンデルブロ集合
// https://introcs.cs.princeton.edu/java/32class/Mandelbrot.java.html
// https://introcs.cs.princeton.edu/java/32class/
// -------------------------------------------------------------
// Zn+1 = Zn^2 + c
// Z0 = 0
// n=>無限大で発散しないという条件を満たす複素数c全体が作る集合
// -------------------------------------------------------------
class Mandelbrot3Drawable: Drawable() {

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 1f
    }

    // マンデルブロ集合を描画
    private val imageBitmap = Bitmap.createBitmap(n + margin *2, n + margin +2,Bitmap.Config.ARGB_8888)

    // プロットするカラー
    // n x nの配列
    //private val plotLst: MutableList<MutableList<Int>> = mutableListOf()
    //private val plotLst = Array(n+1,{ arrayOf<Int>(n+1)})
    //private val plotLst = Array(n+1,{Array(n+1,{0})})

    override fun draw(canvas: Canvas) {
        /*
        plotLst.forEachIndexed { x, colorLst ->
            colorLst.forEachIndexed { y, color ->
                linePaint.color = color
                canvas.drawPoint((x+margin).toFloat(),(y+margin).toFloat(),linePaint)
            }
        }
        */
        linePaint.color = Color.BLACK
        canvas.drawBitmap(imageBitmap, margin.toFloat(), margin.toFloat(), linePaint)
    }

    override fun setAlpha(alpha: Int) {
        linePaint.alpha = alpha
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        linePaint.colorFilter = colorFilter
    }

    override fun getIntrinsicWidth() = n + margin *2

    override fun getIntrinsicHeight() = n + margin *2

    fun scanImagenary(posX: Int){
        // ---------------------------------------
        // xc   = -0.5
        // yc   =  0.0
        // size =  2.0
        // n = 512
        // i = 0-511
        // j = 0-511
        // ---------------------------------------
        // x0 = -1.5 ～ 0.5
        // y0 = -1.0 ～ 1.0
        // ---------------------------------------
        // z0 = -1.5-1.0i ～ 0.5+1.0i
        // ---------------------------------------
        // -1.0 + 1.0i は発散しないので、
        // x0 = -1.5 ～ 1.5ぐらいがいいかも
        // y0 = -1.5 ～ 1.5ぐらいがいいかも
        // ---------------------------------------
        val canvas = Canvas(imageBitmap)
        val dv = size / n.toDouble()
        (0..n).forEach { j ->
            // 実数部
            val x0 = -1.5 + dv*posX.toDouble()
            // 虚数部
            val y0 = -1.5 + dv*j.toDouble()
            val z0 = Complex(x0,y0)
            val gray = (iterationMax - mandelbrot(z0, iterationMax))*255/ iterationMax
            val color = Color.rgb(gray,gray,gray)
            //plotLst[posX][j] = color
            linePaint.color = color
            canvas.drawPoint((posX+ margin).toFloat(),(j+ margin).toFloat(),linePaint)
        }

    }

    private fun mandelbrot(z0: Complex, max: Int): Int {
        var z = z0
        (0 until max).forEach { t ->
            // 絶対値が2を超える場合は、発散するとみなす。
            if (z.abs() > 2.0) return t
            z = z.times(z).plus(z0)
        }
        return max
    }

    companion object {
        public const val n = 1000
        // 反復最大回数
        // 色は、RGB(255,255,255)なので、255を最大の反復回数としている
        //private const val iterationMax = 255
        private const val iterationMax = 100
        private const val size = 3.0
        private const val margin = 50
    }
}