package com.example.milu.radiogroup1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.RadioButton
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        radioGroup.setOnCheckedChangeListener{
            buttonView , isChecked ->
               when ( buttonView ) {
                   radioButton1 -> textView.text = "RadioButton1がオンです"
                   radioButton2 -> textView.text = "RadioButton2がオンです"
                   else        -> textView.text = "RadioButton3がオンです"
               }
        }
    }
}
