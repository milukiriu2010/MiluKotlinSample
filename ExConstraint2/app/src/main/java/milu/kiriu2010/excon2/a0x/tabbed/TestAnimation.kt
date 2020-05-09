package milu.kiriu2010.excon2.a0x.tabbed

import android.view.animation.Animation
import android.view.animation.Transformation
import milu.kiriu2010.excon2.a0x.a19.CanvasBasicView

// https://akira-watson.com/android/canvas-animation.html
class TestAnimation(
        var canvasBasicView: CanvasBasicView,
        var currentPosition: Int = 0,
        var endPosition: Int = 0 )
        : Animation() {

    // endPositionに向かって移動する
    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        //super.applyTransformation(interpolatedTime, t)
        // interpolatedTime: 0.f -> 1.0f
        //Log.d( javaClass.simpleName, "interpolatedTime[$interpolatedTime]")

        val pp = (endPosition-currentPosition)*interpolatedTime

        // 矩形のY軸位置をセット
        canvasBasicView.yval = pp.toInt()
        //canvasBasicView.requestLayout()
        canvasBasicView.invalidate()
    }


}