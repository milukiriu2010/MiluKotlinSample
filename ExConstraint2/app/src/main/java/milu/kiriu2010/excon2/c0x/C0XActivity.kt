package milu.kiriu2010.excon2.c0x

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_c0x.*
import milu.kiriu2010.excon2.R
import milu.kiriu2010.excon2.c0x.c01.C01Activity
import milu.kiriu2010.excon2.c0x.c02.C02Activity
import milu.kiriu2010.excon2.c0x.c03.C03Activity
import milu.kiriu2010.excon2.c0x.c04.C04Activity
import milu.kiriu2010.excon2.c0x.c05.C05Activity
import milu.kiriu2010.excon2.c0x.c06.C06Activity
import milu.kiriu2010.excon2.c0x.c07.C07Activity
import milu.kiriu2010.excon2.c0x.c08.C08Activity
import milu.kiriu2010.excon2.c0x.c09.C09Activity

class C0XActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c0x)

        // ViewModel
        btnC01.setOnClickListener {
            val intent = Intent(this, C01Activity::class.java)
            startActivity(intent)
        }

        // LiveData
        btnC02.setOnClickListener {
            val intent = Intent(this, C02Activity::class.java)
            startActivity(intent)
        }

        // DataBinding
        btnC03.setOnClickListener {
            val intent = Intent(this, C03Activity::class.java)
            startActivity(intent)
        }

        // 文字認識(キャプチャー一覧から)
        btnC04.setOnClickListener {
            val intent = Intent(this, C04Activity::class.java)
            startActivity(intent)
        }

        // 文字認識(カメラから)
        btnC05.setOnClickListener {
            val intent = Intent(this, C05Activity::class.java)
            startActivity(intent)
        }

        // NFC有効無効
        btnC06.setOnClickListener {
            val intent = Intent(this, C06Activity::class.java)
            startActivity(intent)
        }

        // NFCでカードIDを取得
        btnC07.setOnClickListener {
            val intent = Intent(this, C07Activity::class.java)
            startActivity(intent)
        }

        // NFCメッセージを取得
        btnC08.setOnClickListener {
            val intent = Intent(this, C08Activity::class.java)
            startActivity(intent)
        }

        // データバインディング双方向通信
        btnC09.setOnClickListener {
            val intent = Intent(this, C09Activity::class.java)
            startActivity(intent)
        }
    }
}