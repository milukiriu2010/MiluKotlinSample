package milu.kiriu2010.excon2.a0x.a16

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import milu.kiriu2010.excon2.R
import kotlinx.android.synthetic.main.activity_a16.*

// ピンチ　イン・アウト
class A16Activity : AppCompatActivity() {

    private var scale = 1.0f

    private lateinit var detector: ScaleGestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a16)

        detector = ScaleGestureDetector( this, object: ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                //return super.onScale(detector)
                detector?.let {
                    scale *= it.scaleFactor
                    ivA16.scaleX = scale
                    ivA16.scaleY = scale
                    tvA16.scaleX = scale
                    tvA16.scaleY = scale
                }

                return true
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        detector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }
}
