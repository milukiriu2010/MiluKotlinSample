package milu.kiriu2010.excon1.a03

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import milu.kiriu2010.excon1.R
import kotlinx.android.synthetic.main.activity_time_zone_select.*

class TimeZoneSelectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_zone_select)

        // tap "Cancel" button
        setResult(Activity.RESULT_CANCELED)

        val adapter = TimeZoneAdapter(this)
        lvClock.adapter = adapter


        lvClock.setOnItemClickListener { _, _, position, _ ->
            val timeZone = adapter.getItem(position) as String

            val result = Intent()
            result.putExtra("timeZone", timeZone )

            setResult( Activity.RESULT_OK, result )

            // close this activity
            finish()
        }
    }
}
