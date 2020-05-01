package milu.kiriu2010.excon2.a0x.a01

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AlertDialog
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_a01.*
import milu.kiriu2010.excon2.R
import java.util.*

// "テキスト⇒音声"に変換
class A01Activity : AppCompatActivity()
    , TextToSpeech.OnInitListener
    , SeekBar.OnSeekBarChangeListener {

    // テキスト読み上げリソース
    private lateinit var tts: TextToSpeech

    // 声の高さ
    private var pitch = 1.0f
    // 読み上げ速度
    private var rate = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a01)

        tts = TextToSpeech(this,this)

        // 声の高さを設定する
        sbA01B.setOnSeekBarChangeListener(this)

        // 読み上げ速度を設定する
        sbA01C.setOnSeekBarChangeListener(this)

        // テキストを読み上げる
        btA01.setOnClickListener {
            // 声の高さを設定する
            if (tts.setPitch(pitch) == TextToSpeech.ERROR) {
                AlertDialog.Builder(this)
                        .setTitle("エラー")
                        .setMessage("pitchエラー(%.1f)".format(pitch))
                        .setPositiveButton("OK",null)
                        .show()
            }
            // 読み上げ速度を設定する
            if (tts.setSpeechRate(rate) == TextToSpeech.ERROR) {
                AlertDialog.Builder(this)
                        .setTitle("エラー")
                        .setMessage("rateエラー(%.1f)".format(rate))
                        .setPositiveButton("OK",null)
                        .show()
            }

            if (tvA01A.text.toString().length > 0) {
                // 読み上げ中ならストップ
                if(tts.isSpeaking) {
                    tts.stop()
                }
                // テキスト読み上げ実施
                tts.speak(tvA01A.text.toString(), TextToSpeech.QUEUE_FLUSH, null, "messageID")
                /*
                if (Build.VERSION.SDK_INT >= 21){
                    tts.speak(tvA01A.text.toString(), TextToSpeech.QUEUE_FLUSH, null, "messageID")
                }
                else {
                    tts.speak(tvA01A.text.toString(), TextToSpeech.QUEUE_FLUSH, null)
                }
                */
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        // テキスト読み上げリソースを解放
        tts.shutdown()
    }

    // テキストを読み上げるための初期化を実施する
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

    // シークバーのカーソル位置が変更されると呼び出される
    // SeekBar.OnSeekBarChangeListener
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        // 声の高さを設定する
        pitch = sbA01B.progress.toFloat()/5f
        // 読み上げ速度を設定する
        rate = sbA01C.progress.toFloat()/5f
    }

    // SeekBar.OnSeekBarChangeListener
    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    // SeekBar.OnSeekBarChangeListener
    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

}
