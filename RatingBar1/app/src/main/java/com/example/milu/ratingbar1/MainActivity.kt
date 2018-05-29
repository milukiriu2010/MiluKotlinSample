package com.example.milu.ratingbar1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.RatingBar
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
        textView.text = "        "
        textView.setLayoutParams(
                ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT ) )
        testLayout.addView(textView)

        val ratingBar : RatingBar = RatingBar(this)
        testLayout.addView(ratingBar)
        ratingBar.setOnRatingBarChangeListener{
            ratingBar: RatingBar?, rating: Float, fromUser: Boolean ->
                textView.text = String.format("星の数は「%1.1f」です。", rating)
        }

    }
}
