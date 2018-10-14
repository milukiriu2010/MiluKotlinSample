package milu.kiriu2010.excon2.text2speech

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v7.app.AlertDialog
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_text2_speech.*
import milu.kiriu2010.excon2.R
import java.util.*

class Text2SpeechActivity : AppCompatActivity()
    , TextToSpeech.OnInitListener
    , SeekBar.OnSeekBarChangeListener {

    private lateinit var tts: TextToSpeech

    // 声の高さ
    private var pitch = 1.0f
    private var rate = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text2_speech)

        tts = TextToSpeech(this,this)

        // 読み上げるテキスト
        editTextSpeech.setText( "かーーめーーはーーめーーはーーーー" )

        // 声の高さ
        seekBarPitch.setOnSeekBarChangeListener(this)

        // 読み上げ速度
        seekBarRate.setOnSeekBarChangeListener(this)

        // テキストを読み上げる
        btnSpeech.setOnClickListener {
            if (tts.setPitch(pitch) == TextToSpeech.ERROR) {
                AlertDialog.Builder(this)
                        .setTitle("エラー")
                        .setMessage("pitchエラー(%.1f)".format(pitch))
                        .setPositiveButton("OK",null)
                        .show()
            }
            if (tts.setSpeechRate(rate) == TextToSpeech.ERROR) {
                AlertDialog.Builder(this)
                        .setTitle("エラー")
                        .setMessage("rateエラー(%.1f)".format(rate))
                        .setPositiveButton("OK",null)
                        .show()
            }

            //val sp = editTextSpeech.text as SpannableString
            //if (sp.toString().length > 0) {
            if (editTextSpeech.text.toString().length > 0) {
                // 読み上げ中ならストップ
                if(tts.isSpeaking) {
                    tts.stop()
                }
                // テキスト読み上げ
                if (Build.VERSION.SDK_INT >= 21){
                    tts.speak(editTextSpeech.text.toString(), TextToSpeech.QUEUE_FLUSH, null, "messageID")
                }
                else {
                    tts.speak(editTextSpeech.text.toString(), TextToSpeech.QUEUE_FLUSH, null)
                }
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        // リソースを解放
        tts.shutdown()
    }

    // TextToSpeech.OnInitListener
    override fun onInit(status: Int) {
        if ( status == TextToSpeech.SUCCESS ) {
            val locale = Locale.getDefault()
            // システム設定の言語で読み上げ可能な場合
            if ( tts.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE ) {
                tts.language = locale
            }
            // システム設定の言語で読み上げ不可な場合
            else {
                AlertDialog.Builder(this)
                        .setTitle("エラー")
                        .setMessage("読み上げ不可(未対応言語)")
                        .setPositiveButton("OK",null)
                        .show()
            }
        }
        else {
            AlertDialog.Builder(this)
                    .setTitle("エラー")
                    .setMessage("読み上げ不可(初期化エラー)")
                    .setPositiveButton("OK",null)
                    .show()
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        pitch = seekBarPitch.progress.toFloat()/5f
        rate = seekBarRate.progress.toFloat()/5f
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

}
