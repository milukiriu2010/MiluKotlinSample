package milu.kiriu2010.excon2.a0x.traffic

import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import milu.kiriu2010.excon2.R
import kotlinx.android.synthetic.main.activity_traffic_light.*

class TrafficLightActivity : AppCompatActivity() {

    lateinit var lightsAnimation: AnimationDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traffic_light)

        lightsAnimation = ivTL.drawable as AnimationDrawable
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        lightsAnimation.start()
    }
}
