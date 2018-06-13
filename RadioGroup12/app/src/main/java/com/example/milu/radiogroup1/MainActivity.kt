package com.example.milu.radiogroup1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        gender.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { radioGroup, checkedId ->
                val radio: RadioButton = findViewById(checkedId) as RadioButton
                Toast.makeText(applicationContext, "On checked change : ${radio.text}", Toast.LENGTH_LONG).show()
            })
    }
}
