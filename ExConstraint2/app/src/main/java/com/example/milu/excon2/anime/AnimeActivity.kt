package com.example.milu.excon2.anime

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.CycleInterpolator
import android.view.animation.ScaleAnimation
import com.example.milu.excon2.R
import kotlinx.android.synthetic.main.activity_anime.*

class AnimeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anime)

        btnSTART.setOnClickListener {
            val scale = ScaleAnimation(
                    1.0f,
                    10.0f,
                    1.0f,
                    10.0f,
                    it.width.toFloat()/2,
                    it.height.toFloat()/2
            )
            // アニメーションを行う時間を設定
            scale.duration = 1000;
            // アニメーションを繰り返す回数を設定
            scale.interpolator = CycleInterpolator(0.5f)
            // アニメーションのスタート
            it.startAnimation(scale)

        }
    }
}
