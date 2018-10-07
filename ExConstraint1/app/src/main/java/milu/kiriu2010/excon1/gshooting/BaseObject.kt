package milu.kiriu2010.excon1.gshooting


import android.graphics.Canvas

abstract class BaseObject {

    internal var status = STATUS_NORMAL

    internal var yPosition: Float = 0.toFloat()
    internal var xPosition: Float = 0.toFloat()


    enum class Type {
        City,
        Droid,
        Bullet,
        Missile
    }

    open lateinit var type: Type

    abstract fun draw(canvas: Canvas)

    open fun isAvailable(width: Int, height: Int): Boolean {
        if (yPosition < 0 || xPosition < 0 || yPosition > height || xPosition > width) {
            return false
        }

        return if (status == STATUS_DESTROYED) {
            false
        } else true

    }

    abstract fun move()

    abstract fun isHit(`object`: BaseObject): Boolean

    fun hit() {
        status = STATUS_DESTROYED
    }

    companion object {
        internal val STATUS_NORMAL = -1
        internal val STATUS_DESTROYED = 0

        internal val MOVE_WEIGHT = 3.0f

        internal fun calcDistance(object1: BaseObject, object2: BaseObject): Double {
            val distX = object1.xPosition - object2.xPosition
            val distY = object1.yPosition - object2.yPosition
            return Math.sqrt(Math.pow(distX.toDouble(), 2.0) + Math.pow(distY.toDouble(), 2.0))
        }
    }
}
