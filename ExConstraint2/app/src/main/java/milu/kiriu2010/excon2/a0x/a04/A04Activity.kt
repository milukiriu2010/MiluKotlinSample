package milu.kiriu2010.excon2.a0x.a04

import android.media.AudioAttributes
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import milu.kiriu2010.excon2.R
import kotlinx.android.synthetic.main.activity_a04.*
import java.util.*

// サイコロを振る
// http://tekeye.uk/android/examples/android-dice-code
class A04Activity : AppCompatActivity() {
    // generate random numbers
    private val rng = Random()
    //For dice sound playing
    lateinit var diceSound: SoundPool
    //Used to control sound stream return by SoundPool
    var soundId: Int = -1
    //Post message to start roll
    lateinit var handler: Handler
    //Used to implement feedback to user
    var timer = Timer()
    //Is die rolling?
    private var rolling = false
    private var repeatCnt = 0
    private val repeatCntMax = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a04)

        // 音再生の初期化
        initSound()

        // イメージをクリックすると、サイコロが回る
        // minimum 23
        ivA04.setOnClickListener {
            if ( !rolling ) {
                rolling = true
                //Show rolling image
                ivA04.setImageResource(R.drawable.ic_a04g)
                //Start rolling sound
                diceSound.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f )
                // タイマー開始
                timer = Timer()
                timer.scheduleAtFixedRate(Roll(), 600, 200)
            }
        }

        //link handler to callback
        handler = Handler() {
            //Get roll result
            //Remember nextInt returns 0 to 5 for argument of 6
            //hence + 1
            when (rng.nextInt(6)+1) {
                1 -> ivA04.setImageResource(R.drawable.ic_a04a)
                2 -> ivA04.setImageResource(R.drawable.ic_a04b)
                3 -> ivA04.setImageResource(R.drawable.ic_a04c)
                4 -> ivA04.setImageResource(R.drawable.ic_a04d)
                5 -> ivA04.setImageResource(R.drawable.ic_a04e)
                6 -> ivA04.setImageResource(R.drawable.ic_a04f)
            }

            // "it.what"はRollからのメッセージ
            // 規定回数(5回)を超えた場合、サイコロを停止する
            // すなわち、サイコロは5回回る
            if ( it.what >= repeatCntMax ) {
                timer.cancel()
                // user can press again
                rolling = false
                repeatCnt= 0
            }
            true
        }
    }

    // 音再生の初期化
    private fun initSound() {
        // Use the newer SoundPool.Builder
        // Set the audio attributes, SONIFICATION is for interaction events
        // uses builder pattern
        val aa = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        //default max streams is 1
        //also uses builder pattern
        diceSound = SoundPool.Builder().setAudioAttributes(aa).build()
        /*
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            // Use the newer SoundPool.Builder
            // Set the audio attributes, SONIFICATION is for interaction events
            // uses builder pattern
            val aa = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            //default max streams is 1
            //also uses builder pattern
            diceSound = SoundPool.Builder().setAudioAttributes(aa).build()

        }
        else {
            // Running on device earlier than Lollipop
            // Use the older SoundPool constructor
            @Suppress("DEPRECATION")
            diceSound = SoundPool(1,AudioManager.STREAM_MUSIC,0)
        }
         */

        // サイコロの音をロード
        soundId = diceSound.load(this, R.raw.sound_a04,1 )
    }

    // When pause completed message sent to callback
    inner class Roll: TimerTask() {
        override fun run() {
            handler.sendEmptyMessage(repeatCnt++)
        }
    }

    override fun onPause() {
        super.onPause()
        diceSound.pause(soundId)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
}
