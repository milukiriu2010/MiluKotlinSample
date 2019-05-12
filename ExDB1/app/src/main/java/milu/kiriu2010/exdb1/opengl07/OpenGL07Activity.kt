package milu.kiriu2010.exdb1.opengl07

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl07.w071.W071Fragment

class OpenGL07Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_gl07)

        supportFragmentManager.popBackStack()
        if (supportFragmentManager.findFragmentByTag("xyz") == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, W071Fragment.newInstance(), "xyz")
                    .commit()
        }

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }


}
