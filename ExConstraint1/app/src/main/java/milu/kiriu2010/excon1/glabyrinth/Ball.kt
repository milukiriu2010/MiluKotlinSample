package milu.kiriu2010.excon1.glabyrinth

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;


class Ball(private val ballBitmap: Bitmap, left: Int, top: Int, scale: Float) {

    private val rect: Rect
    private val srcRect: Rect

    private var listener: OnMoveListener? = null

    fun setOnMoveListener(l: OnMoveListener) {
        listener = l
    }

    constructor(bmp: Bitmap, startBlock: Map.Block, scale: Float)
            : this(bmp, startBlock.rect.left, startBlock.rect.top, scale) {}

    init {

        val right = left + Math.round(ballBitmap.width * scale)
        val bottom = top + Math.round(ballBitmap.height * scale)
        rect = Rect(left, top, right, bottom)

        srcRect = Rect(0, 0, ballBitmap.width, ballBitmap.height)
    }

    internal fun draw(canvas: Canvas) {
        canvas.drawBitmap(ballBitmap, srcRect, rect, PAINT)

    }

    internal fun move(xOffset: Float, yOffset: Float) {
        var xOffset = xOffset
        var yOffset = yOffset

        var align = if (yOffset >= 0) 1 else -1
        while (!tryMoveVertical(yOffset)) {
            yOffset -= align.toFloat()
        }

        align = if (xOffset >= 0) 1 else -1
        while (!tryMoveHorizontal(xOffset)) {
            xOffset -= align.toFloat()
        }
    }

    private fun tryMoveHorizontal(xOffset: Float): Boolean {
        val left = rect.left + Math.round(xOffset)
        val right = left + rect.width()

        if (!listener!!.canMove(left, rect.top, right, rect.bottom)) {
            return false
        }

        rect.left = left
        rect.right = right
        return true
    }

    private fun tryMoveVertical(yOffset: Float): Boolean {
        val top = rect.top + Math.round(yOffset)
        val bottom = top + rect.height()

        if (!listener!!.canMove(rect.left, top, rect.right, bottom)) {
            return false
        }

        rect.top = top
        rect.bottom = bottom
        return true
    }

    interface OnMoveListener {
        fun canMove(left: Int, top: Int, right: Int, bottom: Int): Boolean
    }

    companion object {

        private val PAINT = Paint()
    }

}

