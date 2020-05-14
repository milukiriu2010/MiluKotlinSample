package milu.kiriu2010.excon2.a0x.a25

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import milu.kiriu2010.excon2.R
import kotlinx.android.synthetic.main.activity_a25.*

// シークバー
class A25Activity : AppCompatActivity(), OnSeekBarChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a25)

        sbA25Red.setOnSeekBarChangeListener(this)
        sbA25Green.setOnSeekBarChangeListener(this)
        sbA25Blue.setOnSeekBarChangeListener(this)
    }

    // OnSeekBarChangeListener
    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        val red   = sbA25Red.progress
        val green = sbA25Green.progress
        val blue  = sbA25Blue.progress

        ivA25.setColorFilter( Color.rgb(red,green,blue), PorterDuff.Mode.LIGHTEN )
    }

    // OnSeekBarChangeListener
    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    // OnSeekBarChangeListener
    override fun onStopTrackingTouch(p0: SeekBar?) {
    }
}
