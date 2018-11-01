package milu.kiriu2010.excon2.screen2

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main2.*
import milu.kiriu2010.excon2.R
import milu.kiriu2010.excon2.screen2.conf.ConfActivity
import milu.kiriu2010.excon2.screen2.gallery2.Gallery2Activity

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        // 設定画面
        btnConf.transformationMethod = null
        btnConf.setOnClickListener {
            val intent = Intent(this,ConfActivity::class.java)
            startActivity(intent)
        }

        // ギャラリー表示
        btnGallery.transformationMethod = null
        btnGallery.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_PICK
            startActivity(intent)
        }

        // ギャラリー表示２
        btnGallery2.setOnClickListener {
            val intent = Intent(this, Gallery2Activity::class.java)
            startActivity(intent)
        }
    }
}
