package com.example.milu.excon1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.milu.excon1.R
import kotlinx.android.synthetic.main.activity_list_time_zone.*
import java.util.*

class ListTimeZoneActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_time_zone)

        val timeZones = TimeZone.getAvailableIDs()

        val adapter = ArrayAdapter<String>(this, R.layout.list_time_zone_row, R.id.timeZone, timeZones)

        timeZoneList.adapter = adapter

        timeZoneList.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText( this, adapter.getItem(position), Toast.LENGTH_LONG ).show()
        }
    }
}
