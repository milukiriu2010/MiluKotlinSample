package milu.kiriu2010.excon1.gaction

import android.graphics.Canvas

class Blank(left: Int, top: Int, right: Int, bottom: Int) : Ground(left, top, right, bottom) {

    override val isSolid: Boolean
        get() = false

    override fun draw(canvas: Canvas) {}
}
