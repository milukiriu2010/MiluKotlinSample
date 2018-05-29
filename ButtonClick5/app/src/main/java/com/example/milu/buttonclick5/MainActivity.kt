package com.example.milu.buttonclick5

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.view.animation.CycleInterpolator
import android.view.animation.ScaleAnimation

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val testLayout : LinearLayout = LinearLayout(this.getApplication())
        testLayout.setLayoutParams(
                ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT ) )
        testLayout.orientation = LinearLayout.VERTICAL
        this.setContentView(testLayout)

        val textView : TextView = TextView(this)
        textView.text = ""
        textView.setLayoutParams(
                ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT ) )
        testLayout.addView(textView)

        val button : Button = Button(this)
        button.text = "Button"
        //button.layoutParams.width = 300
        //button.layoutParams.height = 100
        button.setLayoutParams(
                ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT ) )
        testLayout.addView(button)
        button.setOnClickListener{
            val scale = ScaleAnimation(
                    1.0f,
                    10.0f,
                    1.0f,
                    10.0f,
                    it.width.toFloat()/2,
                    it.height.toFloat()/2
            )
            // アニメーションを行う時間を設定
            scale.duration = 1000;
            // アニメーションを繰り返す回数を設定
            scale.interpolator = CycleInterpolator(0.5f)
            // アニメーションのスタート
            it.startAnimation(scale)
        }
    }
}
