package milu.kiriu2010.exdb1.glsl01

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.glsl01.g001.GLSL01Fragment
import milu.kiriu2010.exdb1.glsl01.g002.GLSL02Fragment
import milu.kiriu2010.exdb1.glsl01.g003.GLSL03Fragment
import milu.kiriu2010.exdb1.glsl01.g004.GLSL04Fragment

class GLSL01Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glsl01)

        supportFragmentManager.popBackStack()
        if (supportFragmentManager.findFragmentByTag("xyz") == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, GLSL04Fragment.newInstance(), "xyz")
                    .commit()
        }

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_glsl01, menu)
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
            // 様々な図形
            R.id.glsl_g004 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("g04") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, GLSL04Fragment.newInstance(), "g04")
                            .commit()
                }
                true
            }
            // 光の玉
            R.id.glsl_g003 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("g03") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, GLSL03Fragment.newInstance(), "g03")
                            .commit()
                }
                true
            }
            // 同心円
            R.id.glsl_g002 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("g02") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, GLSL02Fragment.newInstance(), "g02")
                            .commit()
                }
                true
            }
            // GLSLのみでレンダリング
            R.id.glsl_g001 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("g01") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, GLSL01Fragment.newInstance(), "g01")
                            .commit()
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
