package com.example.milu.intent2

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_world_clock.*
import java.util.*

class WorldClockActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_world_clock)

        val timeZone = TimeZone.getDefault()
        txtTimeZone.text = timeZone.displayName

        btnAdd.setOnClickListener {
            val intent = Intent( this, TimeZoneSelectActivity::class.java)
            startActivityForResult(intent, IntentID.ID_TIMEZONE_SELECT.value)
        }
    }
}
