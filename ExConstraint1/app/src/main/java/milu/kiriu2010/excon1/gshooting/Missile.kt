package milu.kiriu2010.excon1.gshooting


import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class Missile internal constructor(fromX: Int, val alignX: Float) : BaseObject() {

    private val paint = Paint()

    override var type: BaseObject.Type
        get() = BaseObject.Type.Missile
        set(value: BaseObject.Type) {
            super.type = value
        }

    init {
        yPosition = 0f
        xPosition = fromX.toFloat()

        paint.color = Color.BLUE
    }

    override fun move() {
        yPosition += 1 * BaseObject.MOVE_WEIGHT
        xPosition += alignX * BaseObject.MOVE_WEIGHT
    }

    override fun isHit(`object`: BaseObject): Boolean {
        return if (`object`.type === BaseObject.Type.Missile) {
            false
        } else calcDistance(this, `object`) < SIZE

    }

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(xPosition, yPosition, SIZE, paint)
    }

    companion object {

        private val SIZE = 10f
    }
}
