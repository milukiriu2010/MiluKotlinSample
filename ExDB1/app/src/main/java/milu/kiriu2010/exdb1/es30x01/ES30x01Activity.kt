package milu.kiriu2010.exdb1.es30x01

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.es30x01.a03.A03Fragment

class ES30x01Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_es30x01)


        supportFragmentManager.popBackStack()
        if (supportFragmentManager.findFragmentByTag("xyz") == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, A03Fragment.newInstance(), "xyz")
                    .commit()
        }

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }
}
