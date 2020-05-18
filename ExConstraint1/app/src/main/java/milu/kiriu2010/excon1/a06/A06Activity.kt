package milu.kiriu2010.excon1.a06

import android.media.AudioAttributes
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import milu.kiriu2010.excon1.R
import kotlinx.android.synthetic.main.activity_a06.*

// カウントダウン
class A06Activity : AppCompatActivity() {

    private lateinit var soundPool: SoundPool
    private var soundResId = 0

    inner class MyCountDownTimer(
            millisInFuture: Long,
            countDownInterval: Long
    ): CountDownTimer(millisInFuture, countDownInterval) {
        var isRunning = false

        // タイマーの残り時間が０になると、音がなる
        override fun onFinish() {
            // タイマーが切れると0:00を設定する
            tvA06.text = resources.getString(R.string.TV_A06A_END)
            soundPool.play(soundResId,1.0f,100f,0,0,1.0f)
        }

        // タイマーの残り時間を表示
        override fun onTick(millisUntilFinished: Long) {
            val minute = millisUntilFinished/1000L/60L
            val second = millisUntilFinished/1000L%60L
            // M:SS形式でタイマーの残り時間を表示する
            tvA06.text = resources.getString(R.string.TV_A06A_FMT).format(minute,second)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a06)

        // タイマーの初期残時間を1:00とする
        tvA06.text = resources.getString(R.string.TV_A06A_START)
        // タイマー設定(1分)
        val timer = MyCountDownTimer(1*60*1000,100)

        // タイマー開始・停止を実施する
        fabA06.setOnClickListener {
            when (timer.isRunning) {
                // タイマー停止
                true -> timer.apply {
                    isRunning = false
                    cancel()
                    fabA06.setImageResource(R.drawable.ic_a06a)

                    // オーディオ停止
                    soundPool.stop(soundResId)
                }
                // タイマー開始
                false -> timer.apply {
                    isRunning = true
                    start()
                    fabA06.setImageResource(R.drawable.ic_a06b)

                    // オーディオをロードする
                    // ボタン押す度にロードしなおさないと、次回再生されない
                    loadAudio()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // オーディオファイルをロードする
        val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
        soundPool = SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build()
        //soundResId = soundPool.load(this, R.raw.sound_a06,1)
    }

    // オーディオをロードする
    private fun loadAudio() {
        soundResId = soundPool.load(this, R.raw.sound_a06,1)
    }

    override fun onPause() {
        super.onPause()
        soundPool.release()
    }
}
