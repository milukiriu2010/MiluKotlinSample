package milu.kiriu2010.excon1.gshooting


import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class Bullet internal constructor(val alignX: Float, rect: Rect) : BaseObject() {

    private val paint = Paint()

    override var type: BaseObject.Type
        get() = BaseObject.Type.Bullet
        set(value: BaseObject.Type) {
            super.type = value
        }

    init {
        yPosition = rect.centerY().toFloat()
        xPosition = rect.centerX().toFloat()

        paint.color = Color.RED
    }

    override fun move() {
        yPosition -= 1 * BaseObject.MOVE_WEIGHT
        xPosition += alignX * BaseObject.MOVE_WEIGHT
    }

    override fun isHit(`object`: BaseObject): Boolean {
        return if (`object`.type !== BaseObject.Type.Missile) {
            false
        } else calcDistance(this, `object`) < SIZE

    }

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(xPosition, yPosition, SIZE, paint)
    }

    companion object {

        private val SIZE = 15f
    }
}
