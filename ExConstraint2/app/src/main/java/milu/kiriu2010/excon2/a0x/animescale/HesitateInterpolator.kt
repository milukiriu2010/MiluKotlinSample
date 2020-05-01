package milu.kiriu2010.excon2.a0x.animescale

import android.view.animation.Interpolator

// http://cogitolearning.co.uk/2013/10/android-animations-tutorial-5-more-on-interpolators/
class HesitateInterpolator : Interpolator {
    override fun getInterpolation(t: Float): Float {
        val x = 2.0f*t-1.0f
        return 0.5f*(x*x*x+1.0f)
    }
}