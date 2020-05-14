package milu.kiriu2010.excon2.b0x

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_b0x.*
import milu.kiriu2010.excon2.R
import milu.kiriu2010.excon2.b0x.b06.B06Activity
import milu.kiriu2010.excon2.b0x.b09.B09Activity
import milu.kiriu2010.excon2.b0x.b04.B04Activity
import milu.kiriu2010.excon2.b0x.b10.B10Activity
import milu.kiriu2010.excon2.b0x.b01.B01Activity
import milu.kiriu2010.excon2.b0x.b05.B05Activity
import milu.kiriu2010.excon2.b0x.b03.B03Activity
import milu.kiriu2010.excon2.b0x.b08.B08Activity
import milu.kiriu2010.excon2.b0x.b07.B07Activity

class B0XActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b0x)

        // 設定画面
        btnB01.setOnClickListener {
            val intent = Intent(this,B01Activity::class.java)
            startActivity(intent)
        }

        // ギャラリー表示
        btnB02.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_PICK
            startActivity(intent)
        }

        // ギャラリー表示２
        btnB03.setOnClickListener {
            val intent = Intent(this, B03Activity::class.java)
            startActivity(intent)
        }

        // カメラの画像をキャプチャー
        btnB04.setOnClickListener {
            val intent = Intent(this, B04Activity::class.java)
            startActivity(intent)
        }

        // ドラッグ＆ドロップ
        btnB05.setOnClickListener {
            val intent = Intent(this, B05Activity::class.java)
            startActivity(intent)
        }

        // Bearing
        btnB06.setOnClickListener {
            val intent = Intent(this, B06Activity::class.java)
            startActivity(intent)
        }

        // 起動中アプリ一覧を表示
        btnB07.setOnClickListener {
            val intent = Intent(this, B07Activity::class.java)
            startActivity(intent)
        }

        // 着信音
        btnB08.setOnClickListener {
            val intent = Intent(this, B08Activity::class.java)
            startActivity(intent)
        }

        // カメラ起動＆撮影し、ビューに張り付ける
        btnB09.setOnClickListener {
            val intent = Intent(this, B09Activity::class.java)
            startActivity(intent)
        }

        // コンパス
        btnB10.setOnClickListener {
            val intent = Intent(this, B10Activity::class.java)
            startActivity(intent)
        }

    }
}
