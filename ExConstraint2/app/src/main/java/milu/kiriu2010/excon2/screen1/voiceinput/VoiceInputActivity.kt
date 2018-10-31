package milu.kiriu2010.excon2.screen1.voiceinput

import android.content.ActivityNotFoundException
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_voice_input.*
import milu.kiriu2010.excon2.R
import java.lang.StringBuilder

class VoiceInputActivity : AppCompatActivity() {

    val REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice_input)

        // 音声入力開始
        btnSpeech.setOnClickListener {
            try {
                // 音声認識の準備
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "音声を入力してください")
                startActivityForResult(intent, REQUEST_CODE)
            }
            catch (ex: ActivityNotFoundException) {
                Toast.makeText(this, "Not found Activity", Toast.LENGTH_LONG)
            }
        }

        // クリア
        btnClear.setOnClickListener {
            editTextVoiceInput.text.clear()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d( javaClass.simpleName, "requestCode[$requestCode]resultCode[$resultCode]")

        if ( (requestCode == REQUEST_CODE) and (resultCode == RESULT_OK) ) {
            // 認識結果を取得
            val candidates = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            val sb = StringBuilder()
            candidates?.forEachIndexed { index, s ->
                sb.append( "%d:%s\n".format(index,s) )
            }

            editTextVoiceInput.setText( sb.toString() )
        }
    }

}
