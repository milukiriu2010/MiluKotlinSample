package milu.kiriu2010.excon2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_boss.*
import milu.kiriu2010.excon2.a0x.A0xActivity
import milu.kiriu2010.excon2.b0x.B0XActivity
import milu.kiriu2010.excon2.c0x.C0XActivity

class BossActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boss)

        // A0X画面を表示
        btnA0X.setOnClickListener {
            val intent = Intent(this, A0xActivity::class.java)
            startActivity(intent)
        }

        // B0X画面を表示
        btnB0X.setOnClickListener {
            val intent = Intent(this, B0XActivity::class.java)
            startActivity(intent)
        }

        // C0X画面を表示
        btnC0X.setOnClickListener {
            val intent = Intent(this, C0XActivity::class.java)
            startActivity(intent)
        }
    }
}
