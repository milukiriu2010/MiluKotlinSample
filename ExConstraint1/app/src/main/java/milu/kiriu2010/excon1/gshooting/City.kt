package milu.kiriu2010.excon1.gshooting


import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class City(width: Int, height: Int) : BaseObject() {

    private val paint = Paint()

    val rect: Rect

    override var type: BaseObject.Type
        get() = BaseObject.Type.City
        set(value: BaseObject.Type) {
            super.type = value
        }

    init {

        paint.color = Color.LTGRAY

        // 画面の下端全域
        val left = 0
        val top = height - CITY_HEIGHT
        rect = Rect(left, top, width, height)

        yPosition = rect.centerY().toFloat()
        xPosition = rect.centerX().toFloat()
    }

    override fun draw(canvas: Canvas) {
        if (status == BaseObject.STATUS_NORMAL) {
            canvas.drawRect(rect, paint)
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

    companion object {

        private val CITY_HEIGHT = 50
    }
}
