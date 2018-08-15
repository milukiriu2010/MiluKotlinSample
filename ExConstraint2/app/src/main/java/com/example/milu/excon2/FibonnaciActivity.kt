package com.example.milu.excon2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import kotlinx.android.synthetic.main.activity_fibonnaci.*

// http://tekeye.uk/android/examples/saving-activity-state
class FibonnaciActivity : AppCompatActivity() {

    //Stores previous Fibonacci number
    var number_state = 1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fibonnaci)

        number_state = savedInstanceState?.getLong("PREVIOUS") ?: 1L;
        tvFNVal.setText( number_state.toString() )

        btnNext.setOnClickListener {
            //Load previous number from the state
            val previous_number = number_state
            //Update the state (from the screen) prior to next calculation
            number_state =  tvFNVal.text.toString().toLong()
            //Calculate next Fibonacci number and update screen
            tvFNVal.setText( (number_state + previous_number).toString() )
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putLong("PREVIOUS", number_state)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        number_state = savedInstanceState?.getLong("PREVIOUS") ?: return
    }
}
