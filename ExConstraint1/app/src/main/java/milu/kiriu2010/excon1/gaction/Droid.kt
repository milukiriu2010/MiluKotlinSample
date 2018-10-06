package milu.kiriu2010.excon1.gaction


import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

class Droid(private val bitmap: Bitmap, left: Int, top: Int, private val callback: Callback) {

    private val paint = Paint()

    internal val rect: Rect
    private val drawRect = Rect()

    private var acceleration = 0f

    interface Callback {

        fun getDistanceFromGround(droid: Droid): Int
    }

    init {
        val rectLeft = left + COLLISION_MARGIN_LEFT
        val rectRight = left + BLOCK_SIZE - COLLISION_MARGIN_RIGHT
        this.rect = Rect(rectLeft, top, rectRight, top + BLOCK_SIZE)
    }

    fun draw(canvas: Canvas) {

        var src = BITMAP_SRC_RUNNING
        if (acceleration != 0f) {
            src = BITMAP_SRC_JUMPING
        }

        drawRect.set(rect)
        drawRect.left -= COLLISION_MARGIN_LEFT

        canvas.drawBitmap(bitmap, src, drawRect, paint)
    }

    fun jump(time: Float) {
        acceleration = time * WEIGHT
    }

    fun move() {

        acceleration -= GRAVITY

        val distanceFromGround = callback.getDistanceFromGround(this)
        if (acceleration < 0 && acceleration < -distanceFromGround) {
            acceleration = (-distanceFromGround).toFloat()
        }

        rect.offset(0, -Math.round(acceleration))
    }

    fun shutdown() {
        acceleration = 0f
    }

    companion object {

        private val GRAVITY = 0.8f
        private val WEIGHT = GRAVITY * 60

        private val COLLISION_MARGIN_LEFT = 35
        private val COLLISION_MARGIN_RIGHT = 15

        private val BLOCK_SIZE = 153

        private val BITMAP_SRC_JUMPING = Rect(
                BLOCK_SIZE, 0, BLOCK_SIZE * 2, BLOCK_SIZE)
        private val BITMAP_SRC_RUNNING = Rect(
                0, 0, BLOCK_SIZE, BLOCK_SIZE)
    }

}
