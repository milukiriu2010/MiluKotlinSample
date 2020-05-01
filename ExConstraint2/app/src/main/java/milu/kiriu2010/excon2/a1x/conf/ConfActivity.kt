package milu.kiriu2010.excon2.a1x.conf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ConfActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_conf)

        supportFragmentManager.beginTransaction()
                .replace(android.R.id.content,ConfFragment.newInstance())
                .commit()
    }
}
