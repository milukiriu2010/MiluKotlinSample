package com.example.milu.radiogrouptemperature

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.RadioButton
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // this method is called at button click because we assigned the name to the
    // "OnClick" property of the button
    fun onClick(view: View) {
        when (view.getId()) {
            R.id.button1 -> {
                val celsiusButton = findViewById<View>(R.id.radio0) as RadioButton
                val fahrenheitButton = findViewById<View>(R.id.radio1) as RadioButton
                if (inputValue.getText().length === 0) {
                    Toast.makeText(this, "Please enter a valid number",
                            Toast.LENGTH_LONG).show()
                    return
                }

                val degree = java.lang.Float.parseFloat(inputValue.getText().toString())
                if (celsiusButton.isChecked) {
                    inputValue.setText(ConvertUtil.convertFahrenheitToCelsius(degree).toString())
                    celsiusButton.isChecked = false
                    fahrenheitButton.isChecked = true
                } else {
                    inputValue.setText(ConvertUtil.convertCelsiusToFahrenheit(degree).toString())
                    fahrenheitButton.isChecked = false
                    celsiusButton.isChecked = true
                }
            }
        }
    }
}
