package milu.kiriu2010.excon2.a0x.a10

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_a10.*
import milu.kiriu2010.excon2.R
import java.lang.StringBuilder

// 音声入力
class A10Activity : AppCompatActivity() {

    val REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a10)

        // 音声入力開始
        btnA10A.setOnClickListener {
            try {
                // 音声認識の準備
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "音声を入力してください")
                startActivityForResult(intent, REQUEST_CODE)
            }
            catch (ex: ActivityNotFoundException) {
                Toast.makeText(this, "Not found Activity", Toast.LENGTH_LONG).show()
            }
        }

        // クリア
        btnA10B.setOnClickListener {
            tvA10.text.clear()
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

            // 音声からテキスト変換されたものを張り付ける
            tvA10.setText( sb.toString() )
        }
    }

}
