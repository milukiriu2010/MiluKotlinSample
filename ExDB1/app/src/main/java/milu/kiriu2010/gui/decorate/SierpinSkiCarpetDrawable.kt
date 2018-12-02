package milu.kiriu2010.gui.decorate

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

// シェルピンスキー・カーペット
// https://imagenavi.jp/search/detail.asp?id=64002846
// ------------------------------------------------------
//   1 x  1
//   3 x  3
//   9 x  9
//  27 x 27
//  81 x 81
// ------------------------------------------------------
class SierpinSkiCarpetDrawable: Drawable() {

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 1f
    }

    private val plotLst: MutableList<MutableList<Int>> = mutableListOf(mutableListOf(1))

    override fun draw(canvas: Canvas) {
        // 配列のサイズ
        val arraySize = plotLst.size
        // 描画する図形のサイズ
        val len = size/arraySize

        plotLst.forEachIndexed { y, xLst ->
            xLst.forEachIndexed { x, v ->

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

    override fun getIntrinsicWidth() = size

    override fun getIntrinsicHeight() = size

    companion object {
        private const val size = 810
        private const val margin = 50
    }
}
