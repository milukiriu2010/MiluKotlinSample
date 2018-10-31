package milu.kiriu2010.excon2

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main2.*
import milu.kiriu2010.excon2.screen2.conf.ConfActivity

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
    }
}
