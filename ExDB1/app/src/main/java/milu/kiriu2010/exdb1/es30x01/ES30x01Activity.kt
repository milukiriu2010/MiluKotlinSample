package milu.kiriu2010.exdb1.es30x01

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.es30x01.rot01es30.ES30Rotate01Fragment
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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_es30x01, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // アクションバーのアイコンがタップされると呼ばれる
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            // 前画面に戻る
            android.R.id.home -> {
                finish()
                true
            }
            // GLSL ES3.0
            R.id.es30_a03 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("a03") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, A03Fragment.newInstance(), "a03")
                            .commit()
                }
                true
            }
            // 回転(立方体)01_ES30
            R.id.es30_cube_rotate01 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("rot01es30") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, ES30Rotate01Fragment.newInstance(), "rot01es30")
                            .commit()
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
