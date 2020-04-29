package milu.kiriu2010.excon1.clock

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import milu.kiriu2010.id.IntentID
import milu.kiriu2010.excon1.R
import milu.kiriu2010.excon1.timezone.TimeZoneAdapter
import milu.kiriu2010.excon1.timezone.TimeZoneSelectActivity
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

            timeZones?.add( timeZone )

            pref.edit().putStringSet("time_zone", timeZones ).apply()

            this.showWorldClocks()
        }
    }

    private fun showWorldClocks(){
        val pref = getSharedPreferences("prefs", Context.MODE_PRIVATE )
        val timeZones = pref.getStringSet( "time_zone", setOf() )

        timeZones?.let {
            lvClock.adapter = TimeZoneAdapter(this, it.toTypedArray() )
        }
    }
}
