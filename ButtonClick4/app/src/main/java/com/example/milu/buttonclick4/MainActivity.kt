package com.example.milu.buttonclick4

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

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
        button.setLayoutParams(
                ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT ) )
        testLayout.addView(button)
        //button.setOnClickListener{ textView.text = "Button Pushed." }
        /*
        button.setOnTouchListener( object: View.OnTouchListener {
            override fun onTouch(v: View?, e: MotionEvent?): Boolean
            {
                if (e?.getAction() == MotionEvent.ACTION_DOWN) {
                    textView.text = "Button Pushed."
                } else if (e?.getAction() == MotionEvent.ACTION_UP) {
                    textView.text = "Button Released."
                }
                return true;
            }
        })
        */
        button.setOnTouchListener{
            v: View?, e: MotionEvent? ->
                if (e?.getAction() == MotionEvent.ACTION_DOWN) {
                    textView.text = "Button Pushed."
                } else if (e?.getAction() == MotionEvent.ACTION_UP) {
                    textView.text = "Button Released."
                }
            true
        }

    }
}
