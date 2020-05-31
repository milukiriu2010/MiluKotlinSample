package milu.kiriu2010.excon1.a11

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_a11_audio.*
import milu.kiriu2010.excon1.R
import java.io.File

// タップされたファイルがオーディオの場合、再生するアクティビティ
// https://stackoverflow.com/questions/16715003/simple-mediaplayer-play-mp3-from-file-path
class A11AudioActivity : AppCompatActivity() {

    private val mediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a11_audio)

        // 前の画面から渡されたデータを読み込む
        val file = intent.getSerializableExtra("file") as? File ?: return

        // ファイルパス
        tvA11PathAudio.text = file.absolutePath

        // ファイルサイズ
        tvA11SizeAudio.text = file.length().toString()

        // オーディオファイルを設定
        mediaPlayer.setDataSource(file.absolutePath)
        mediaPlayer.prepare()

        // 再生
        btnA11AudioStart.setOnClickListener {
            mediaPlayer.start()
        }

        // 停止
        btnA11AudioStop.setOnClickListener {
            mediaPlayer.stop()
            // いったん停止すると、再度prepareしないとだめかもしれない
        }

        // 一時停止
        btnA11AudioPause.setOnClickListener {
            mediaPlayer.pause()
        }
    }

    override fun onBackPressed() {
        // リソース解放
        mediaPlayer.release()
        super.onBackPressed()
    }
}