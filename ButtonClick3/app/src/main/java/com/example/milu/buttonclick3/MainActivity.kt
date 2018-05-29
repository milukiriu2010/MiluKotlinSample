package com.example.milu.buttonclick3

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.ImageButton
import android.view.View.OnClickListener
import android.widget.Toast
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageButton.setOnClickListener()
        {
            Toast.makeText(
                    this@MainActivity,
                    "Button Pushed.",
                    Toast.LENGTH_SHORT
            ).show()
        }
    }
}
