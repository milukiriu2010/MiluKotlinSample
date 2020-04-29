package milu.kiriu2010.excon2.screen1.seek

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import milu.kiriu2010.excon2.R
import kotlinx.android.synthetic.main.activity_seek.*

class SeekActivity : AppCompatActivity(), OnSeekBarChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seek)

        seekBarRed.setOnSeekBarChangeListener(this)
        seekBarGreen.setOnSeekBarChangeListener(this)
        seekBarBlue.setOnSeekBarChangeListener(this)
    }

    // OnSeekBarChangeListener
    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        val red   = seekBarRed.progress
        val green = seekBarGreen.progress
        val blue  = seekBarBlue.progress

        ivColor.setColorFilter( Color.rgb(red,green,blue), PorterDuff.Mode.LIGHTEN )
    }

    // OnSeekBarChangeListener
    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    // OnSeekBarChangeListener
    override fun onStopTrackingTouch(p0: SeekBar?) {
    }
}
