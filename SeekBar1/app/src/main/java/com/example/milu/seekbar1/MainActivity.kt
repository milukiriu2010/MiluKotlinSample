package com.example.milu.seekbar1

import android.support.v7.app.AppCompatActivity
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import kotlinx.android.synthetic.main.activity_main.*

// https://www.tutorialkart.com/kotlin-android/android-seekbar-kotlin-example/
class MainActivity() : AppCompatActivity(),OnSeekBarChangeListener {

    //private val seekBarRed:   SeekBar = SeekBar(this)
    //private val seekBarGreen: SeekBar = SeekBar(this)
    //private val seekBarBlue:  SeekBar = SeekBar(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBarRed.setOnSeekBarChangeListener(this)
        seekBarGreen.setOnSeekBarChangeListener(this)
        seekBarBlue.setOnSeekBarChangeListener(this)
    }

    override fun onProgressChanged( seekBar: SeekBar, progress: Int, fromUser: Boolean ){
        val red   = seekBarRed.progress
        val green = seekBarGreen.progress
        val blue  = seekBarBlue.progress

        imageView.setColorFilter( Color.rgb(red,green,blue), PorterDuff.Mode.LIGHTEN )
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        // called when tracking the seekbar is started
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        // called when tracking the seekbar is stopped
    }
}
