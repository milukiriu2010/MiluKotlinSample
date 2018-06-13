package com.example.milu.radiogrouptemperature

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