package com.example.milu.radiogroup1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_rate.*

class RateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate)

        this.setRatingBarValue( ratingBar.rating )

        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, b ->
            this.setRatingBarValue( rating )
        }
    }

    private fun setRatingBarValue( rating: Float ) {
        lblRate.text = String.format( "星の数は「%1.1f」です", rating )
    }
}
