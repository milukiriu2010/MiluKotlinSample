package milu.kiriu2010.excon2.b0x.b01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

// 設定
class B01Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_conf)

        supportFragmentManager.beginTransaction()
                .replace(android.R.id.content,B01Fragment.newInstance())
                .commit()
    }
}
