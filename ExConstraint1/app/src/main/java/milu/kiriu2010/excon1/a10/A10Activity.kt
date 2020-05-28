package milu.kiriu2010.excon1.a10

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_a10.*
import milu.kiriu2010.excon1.R
import kotlin.concurrent.timer

// スライドショー
class A10Activity : AppCompatActivity() {
    private val imgLst = listOf(
            R.drawable.a10_slide00,
            R.drawable.a10_slide01,
            R.drawable.a10_slide02,
            R.drawable.a10_slide03,
            R.drawable.a10_slide04,
            R.drawable.a10_slide05,
            R.drawable.a10_slide06,
            R.drawable.a10_slide07,
            R.drawable.a10_slide08,
            R.drawable.a10_slide09
    )

    private var position = 0

    private var isSlideshow = false
    private var handler = Handler()

    private lateinit var player: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a10)

        isA10.setFactory { ImageView(this) }
        isA10.setImageResource(imgLst[0])

        // 画像をクリックすると、アニメーションを実行する
        ivA10.setOnClickListener {
            // 回転を初期化
            // これを入れないと、連打しても回転しない
            it.rotation = 0f

            // 3秒間の間に５回転する
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

        // 前ボタン
        // フェードイン・フェードアウトで写真を入れ替える
        btnA10A.setOnClickListener {
            isA10.setInAnimation(this, android.R.anim.fade_in )
            isA10.setOutAnimation(this,android.R.anim.fade_out )
            movePosition(-1)
        }

        // 次ボタン
        // 左⇒右へ写真が流れる
        btnA10B.setOnClickListener {
            isA10.setInAnimation(this, android.R.anim.slide_in_left )
            isA10.setOutAnimation(this,android.R.anim.slide_out_right )
            movePosition(1)
        }

        // 5秒ごとに写真を入れ替える
        timer( period = 5000 ) {
            handler.post {
                if ( isSlideshow ) movePosition(1)
            }
        }

        // スライドショーが流れているときは、音楽を流す
        btnA10C.setOnClickListener {
            isSlideshow = !isSlideshow

            when (isSlideshow) {
                true -> player.start()
                false -> player.apply {
                    pause()
                    seekTo(0)
                }
            }
        }

        player = MediaPlayer.create(this, R.raw.audio_a10)
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
        isA10.setImageResource(imgLst[position])
    }
}
