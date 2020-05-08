package milu.kiriu2010.excon2.a0x.a12

import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import milu.kiriu2010.excon2.R
import kotlinx.android.synthetic.main.activity_a12.*

// 信号のアニメーション
class A12Activity : AppCompatActivity() {

    lateinit var lightsAnimation: AnimationDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a12)

        lightsAnimation = ivA12.drawable as AnimationDrawable
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        lightsAnimation.start()
    }
}
