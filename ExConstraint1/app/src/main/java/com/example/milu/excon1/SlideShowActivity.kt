package com.example.milu.excon1

import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import com.example.milu.intent2.R
import kotlinx.android.synthetic.main.activity_slide_show.*
import kotlin.concurrent.timer

class SlideShowActivity : AppCompatActivity() {
    private val imgLst = listOf(
            R.drawable.slide00,
            R.drawable.slide01,
            R.drawable.slide02,
            R.drawable.slide03,
            R.drawable.slide04,
            R.drawable.slide05,
            R.drawable.slide06,
            R.drawable.slide07,
            R.drawable.slide08,
            R.drawable.slide09
    )

    private var position = 0

    private var isSlideshow = false
    private var handler = Handler()

    private lateinit var player: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide_show)

        imageSwitcher.setFactory { ImageView(this) }
        imageSwitcher.setImageResource(imgLst[0])

        imageView.setOnClickListener {
            it.animate().apply {
                duration = 3000L
                // 5回転
                rotation(360.0f*5.0f )

                // 2.5倍
                /*
                scaleX(2.5f)
                scaleY(2.5f)
                */

                // ボールが飛び跳ねる効果
                /*
                interpolator = BounceInterpolator()
                y(it.y+100.0f)
                */
            }
        }

        prevButton.setOnClickListener {
            imageSwitcher.setInAnimation(this, android.R.anim.fade_in )
            imageSwitcher.setOutAnimation(this,android.R.anim.fade_out )
            movePosition(-1)
        }

        nextButton.setOnClickListener {
            imageSwitcher.setInAnimation(this, android.R.anim.slide_in_left )
            imageSwitcher.setOutAnimation(this,android.R.anim.slide_out_right )
            movePosition(1)
        }

        timer( period = 5000 ) {
            handler.post {
                if ( isSlideshow ) movePosition(1)
            }
        }

        slideshowButton.setOnClickListener {
            isSlideshow = !isSlideshow

            when (isSlideshow) {
                true -> player.start()
                false -> player.apply {
                    pause()
                    seekTo(0)
                }
            }
        }

        player = MediaPlayer.create(this, R.raw.getdown )
        player.isLooping = true
    }

    private fun movePosition(move: Int) {
        position += move
        if ( position >= imgLst.size ) {
            position = 0
        }
        else if ( position < 0 ) {
            position = imgLst.size - 1
        }
        imageSwitcher.setImageResource(imgLst[position])
    }
}
