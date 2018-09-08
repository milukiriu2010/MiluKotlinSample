package milu.kiriu2010.excon2.temperature

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import milu.kiriu2010.excon2.R
import kotlinx.android.synthetic.main.activity_temperature.*

class TemperatureActivity : AppCompatActivity() {
//class TemperatureActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temperature)
        rbCelcius.isChecked = true
    }

    // this method is called at button click because we assigned the name to the
    // "OnClick" property of the button
    fun onClick(view: View) {

        Log.d( this.javaClass.toString(), "=== TemperatureActivity Click ================" )
        when (view.id) {
            R.id.btnCAL -> {
                if ( txtSRC.text.length === 0 ) {
                    Toast.makeText( this, "Please enter a valid number.", Toast.LENGTH_LONG ).show()
                }
                else {
                    val degree = java.lang.Float.parseFloat(txtSRC.text.toString())
                    if ( rbCelcius.isChecked ) {
                        txtDST.setText(ConvertUtil.convertCelsiusToFahrenheit(degree).toString())
                    } else {
                        txtDST.setText(ConvertUtil.convertFahrenheitToCelsius(degree).toString())
                    }
                }
            }
        }
    }

    class ConvertUtil{
        companion object {
            fun convertFahrenheitToCelsius(fahrenheit: Float): Float {
                return (fahrenheit - 32) * 5 / 9
            }

            // converts to fahrenheit
            fun convertCelsiusToFahrenheit(celsius: Float): Float {
                return celsius * 9 / 5 + 32
            }
        }
    }
}
