package com.example.milu.excon1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.milu.excon1.R
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

        this.showWorldClocks()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (
                (requestCode == IntentID.ID_TIMEZONE_SELECT.value) &&
                ( resultCode == Activity.RESULT_OK ) &&
                ( data != null )
                ) {
            val timeZone = data.getStringExtra("timeZone" )

            val pref = getSharedPreferences( "prefs", Context.MODE_PRIVATE )
            val timeZones = pref.getStringSet("time_zone", mutableSetOf() )

            timeZones.add( timeZone )

            pref.edit().putStringSet("time_zone", timeZones ).apply()

            this.showWorldClocks()
        }
    }

    private fun showWorldClocks(){
        val pref = getSharedPreferences("prefs", Context.MODE_PRIVATE )
        val timeZones = pref.getStringSet( "time_zone", setOf() )

        lvClock.adapter = TimeZoneAdapter(this, timeZones.toTypedArray())
    }
}
