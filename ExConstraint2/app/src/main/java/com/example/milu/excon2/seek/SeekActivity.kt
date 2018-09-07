package com.example.milu.excon2.seek

import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.example.milu.excon2.R
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

        ivColor.setColorFilter( Color.rgb(red,green,blue), PorterDuff.Mode.LIGHTEN )    }

    // OnSeekBarChangeListener
    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    // OnSeekBarChangeListener
    override fun onStopTrackingTouch(p0: SeekBar?) {
    }
}
