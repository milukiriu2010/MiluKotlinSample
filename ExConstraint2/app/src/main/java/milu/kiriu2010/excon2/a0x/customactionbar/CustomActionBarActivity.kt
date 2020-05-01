package milu.kiriu2010.excon2.a0x.customactionbar

import android.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import milu.kiriu2010.excon2.R
import milu.kiriu2010.excon2.R.layout.action_bar_layout_custom
import kotlinx.android.synthetic.main.action_bar_layout_custom.*

// https://www.journaldev.com/9952/android-custom-action-bar-example-tutorial
class CustomActionBarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_action_bar)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(action_bar_layout_custom)
        //val view: View? = supportActionBar?.customView

        // Back Button
        action_bar_back.setOnClickListener { finish() }

        // Forward Button
        action_bar_forward.setOnClickListener {
            Toast.makeText( this, "Forward Button is clicked.", Toast.LENGTH_LONG).show()
        }
    }
}
