package com.example.milu.excon2.dice

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.milu.excon2.R
import kotlinx.android.synthetic.main.activity_dice_roller.*
import java.util.*

// http://tekeye.uk/android/examples/android-dice-code
class DiceRollerActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_dice_roller)

        //Our function to initialise sound playing
        initSound()

        // minimum 23
        ivDICE.setOnClickListener {
            if ( !rolling ) {
                rolling = true
                //Show rolling image
                ivDICE.setImageResource(R.drawable.dado3droll)
                //Start rolling sound
                diceSound.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f )
                //Pause to allow image to update
                //timer.schedule(Roll(), 400)
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
                1 -> ivDICE.setImageResource(R.drawable.dado1)
                2 -> ivDICE.setImageResource(R.drawable.dado2)
                3 -> ivDICE.setImageResource(R.drawable.dado3)
                4 -> ivDICE.setImageResource(R.drawable.dado4)
                5 -> ivDICE.setImageResource(R.drawable.dado5)
                6 -> ivDICE.setImageResource(R.drawable.dado6)
            }

            // "it.what"はRollからのメッセージ
            if ( it.what >= repeatCntMax ) {
                timer.cancel()
                // user can press again
                rolling = false
                repeatCnt= 0
            }
            true
        }
    }

    // New code to initialise sound playback
    private fun initSound() {
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

        //Load the dice sound
        soundId = diceSound.load(this, R.raw.shake_dice,1 )
    }

    //When pause completed message sent to callback
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
