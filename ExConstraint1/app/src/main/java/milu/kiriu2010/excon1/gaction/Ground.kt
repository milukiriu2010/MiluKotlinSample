package milu.kiriu2010.excon1.gaction

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

open class Ground(left: Int, top: Int, right: Int, bottom: Int) {
    private val paint = Paint()

    internal val rect: Rect

    val isAvailable: Boolean
        get() = rect.right > 0

    open val isSolid: Boolean
        get() = true

    init {
        this.rect = Rect(left, top, right, bottom)

        paint.color = Color.rgb(153, 76, 0) // 茶色
    }

    open fun draw(canvas: Canvas) {
        canvas.drawRect(rect, paint)
    }

    fun move(moveToLeft: Int) {
        rect.offset(-moveToLeft, 0)
    }

    fun isShown(width: Int, height: Int): Boolean {
        return rect.intersects(0, 0, width, height)
    }

}