package milu.kiriu2010.gui.fractal

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.drawable.Drawable
import milu.kiriu2010.gui.basic.MyPoint

// ---------------------------------------------------------------------
// Takagi Curve
// https://ja.wikipedia.org/wiki/%E9%AB%98%E6%9C%A8%E6%9B%B2%E7%B7%9A
// ---------------------------------------------------------------------
class TakagiCurve01Drawable: Drawable() {

    // ------------------------------------------------
    // Drawing Area
    // ------------------------------------------------
    companion object {
        private const val side: Int = 800
        private const val margin: Int = 50
    }

    // ------------------------------------------------
    // Initial points for Takagi Curve
    //   n = 0 => triangle
    // ------------------------------------------------
    val initPointLst = mutableListOf<MyPoint>().apply {
        add(MyPoint(0,0))
        add(MyPoint(side/2,side/2))
        add(MyPoint(0,side))
    }

    // ------------------------------------------------
    // Paint for frame
    // ------------------------------------------------
    private val framePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 10f
    }

    // ------------------------------------------------
    // Paint for Takagi Curve
    // ------------------------------------------------
    private val curvePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    override fun draw(canvas: Canvas) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setAlpha(alpha: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getOpacity(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}