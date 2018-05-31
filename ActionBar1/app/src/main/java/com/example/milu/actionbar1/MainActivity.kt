package com.example.milu.actionbar1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.ActionBar
import android.app.Activity
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener{
            val actionBar = this.supportActionBar
            actionBar!!.title = "Hello, ActionBar!!"
            actionBar!!.subtitle = "and, Subtitle!"
        }
    }
}
