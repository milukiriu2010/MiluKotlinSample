package com.example.milu.excon2.pinch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import com.example.milu.excon2.R
import kotlinx.android.synthetic.main.activity_pinch.*

class PinchActivity : AppCompatActivity() {

    private var scale = 1.0f

    private lateinit var detector: ScaleGestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pinch)

        detector = ScaleGestureDetector( this, object: ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                //return super.onScale(detector)
                detector?.let {
                    scale *= it.scaleFactor
                    imageViewHyottoko.scaleX = scale
                    imageViewHyottoko.scaleY = scale
                    textView.scaleX = scale
                    textView.scaleY = scale
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
