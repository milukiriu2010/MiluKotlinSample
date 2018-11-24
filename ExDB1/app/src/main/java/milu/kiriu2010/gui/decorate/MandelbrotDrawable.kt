package milu.kiriu2010.gui.decorate

import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.RequiresApi
import milu.kiriu2010.math.Complex

// マンデルブロ集合
// https://introcs.cs.princeton.edu/java/32class/Mandelbrot.java.html
// https://introcs.cs.princeton.edu/java/32class/
// -------------------------------------------------------------
// Zn+1 = Zn^2 + c
// Z0 = 0
// n=>無限大で発散しないという条件を満たす複素数c全体が作る集合
// -------------------------------------------------------------
@RequiresApi(Build.VERSION_CODES.O)
class MandelbrotDrawable: Drawable() {

    private val cornerEffect = CornerPathEffect(8f)

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 1f
        //pathEffect = cornerEffect
    }

    private val dotPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    // プロットするカラー
    // n x n
    private val plotLst: MutableList<MutableList<Color>> = mutableListOf()

    override fun draw(canvas: Canvas) {
        plotLst.forEachIndexed { x, colorLst ->
            colorLst.forEachIndexed { y, color ->
                linePaint.color = color.toArgb()
                canvas.drawPoint(x.toFloat()+margin.toFloat(),y.toFloat()+margin.toFloat(),linePaint)
            }
        }
    }

    override fun setAlpha(alpha: Int) {
        linePaint.alpha = alpha
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        linePaint.colorFilter = colorFilter
    }

    override fun getIntrinsicWidth() = n+margin*2

    override fun getIntrinsicHeight() = n+margin*2

    init {
        (0  until n).forEach { i ->
            val colorLst = mutableListOf<Color>()

            (0 until n).forEach { j ->
                // 実数部
                val x0 = xc - size/2.0 + size*i.toDouble()/n
                // 虚数部
                val y0 = yc - size/2.0 + size*j.toDouble()/ n
                val z0 = Complex(x0,y0)
                val gray = iterationMax - mandelbrot(z0, iterationMax)
                val color = Color.valueOf(gray.toFloat(),gray.toFloat(),gray.toFloat())
                colorLst.add(color)
            }

            plotLst.add(colorLst)
        }
    }

    private fun mandelbrot(z0: Complex, max: Int): Int {
        var z = z0
        (0 until max).forEach { t ->
            if (z.abs() > 2.0) return t
            z = z.times(z).plus(z0)
        }
        return max
    }

    companion object {
        private const val n = 512
        // 反復最大回数
        private const val iterationMax = 255
        //private const val iterationMax = 100
        // Plots the size-by-size region of the Mandelbrot set, centered on (xc, yc)
        private const val xc = -0.5
        private const val yc = 0.0
        private const val size = 2.0
        private const val margin = 50
    }
}