package milu.kiriu2010.excon2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_boss.*
import milu.kiriu2010.excon2.screen1.MainActivity
import milu.kiriu2010.excon2.screen2.Main2Activity

class BossActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boss)

        // メイン画面を表示
        btnMain.transformationMethod = null
        btnMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // メイン画面2を表示
        btnMain2.transformationMethod = null
        btnMain2.setOnClickListener {
            val intent = Intent(this, Main2Activity::class.java)
            startActivity(intent)
        }
    }
}
