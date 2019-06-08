package milu.kiriu2010.exdb1.es30x01

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.es30x01.rot01es30.ES30Rotate01Fragment
import milu.kiriu2010.exdb1.es30x01.a03.A03Fragment
import milu.kiriu2010.exdb1.es30x01.a03v.AV03Fragment
import milu.kiriu2010.exdb1.es30x01.a04.A04Fragment
import milu.kiriu2010.exdb1.es30x01.a05.A05Fragment
import milu.kiriu2010.exdb1.es30x01.a06.A06Fragment

class ES30x01Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_es30x01)


        supportFragmentManager.popBackStack()
        if (supportFragmentManager.findFragmentByTag("xyz") == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, AV03Fragment.newInstance(), "xyz")
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
            // VAO
            R.id.es30_a06 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("a06") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, A06Fragment.newInstance(), "a06")
                            .commit()
                }
                true
            }
            // flat補間
            R.id.es30_a05 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("a05") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, A05Fragment.newInstance(), "a05")
                            .commit()
                }
                true
            }
            // GLSL ES3.0 layout
            R.id.es30_a04 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("a04") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, A04Fragment.newInstance(), "a04")
                            .commit()
                }
                true
            }
            // GLSL ES3.0
            R.id.es30_a03 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("av03") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, AV03Fragment.newInstance(), "av03")
                            .commit()
                }
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
