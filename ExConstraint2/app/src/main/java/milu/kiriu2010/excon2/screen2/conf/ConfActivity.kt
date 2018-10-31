package milu.kiriu2010.excon2.screen2.conf

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import milu.kiriu2010.excon2.R

class ConfActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_conf)

        supportFragmentManager.beginTransaction()
                .replace(android.R.id.content,ConfFragment.newInstance())
                .commit()
    }
}
