package milu.kiriu2010.excon1.gshooting


import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

class Droid(val bitmap: Bitmap, width: Int, height: Int) : BaseObject() {

    private val paint = Paint()
    val rect: Rect

    override var type: BaseObject.Type
        get() = BaseObject.Type.Droid
        set(value: BaseObject.Type) {
            super.type = value
        }

    init {

        // 画面の下端中央の位置
        val left = (width - bitmap.width) / 2
        val top = height - bitmap.height
        val right = left + bitmap.width
        val bottom = top + bitmap.height
        rect = Rect(left, top, right, bottom)

        yPosition = rect.centerY().toFloat()
        xPosition = rect.centerX().toFloat()
    }

    override fun draw(canvas: Canvas) {
        if (status == BaseObject.STATUS_NORMAL) {
            canvas.drawBitmap(bitmap, rect.left.toFloat(), rect.top.toFloat(), paint)
        }
    }

    override fun isAvailable(width: Int, height: Int): Boolean {
        return true
    }

    override fun move() {}

    override fun isHit(`object`: BaseObject): Boolean {
        return if (`object`.type !== BaseObject.Type.Missile) {
            false
        } else rect.contains(Math.round(`object`.xPosition), Math.round(`object`.yPosition))

    }
}
