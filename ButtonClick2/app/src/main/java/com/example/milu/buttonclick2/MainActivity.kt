package com.example.milu.buttonclick2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Button
import android.view.View.OnClickListener
import android.widget.Toast
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // https://stackoverflow.com/questions/44301301/android-how-to-achieve-setonclicklistener-in-kotlin?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
        button.setOnClickListener()
        {
            Toast.makeText(
                    this@MainActivity,
                    "Button Pushed.",
                    Toast.LENGTH_SHORT
            ).show()
        }

    }
}
