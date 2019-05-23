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
import milu.kiriu2010.exdb1.glsl01.g005.GLSL05Fragment
import milu.kiriu2010.exdb1.glsl01.g006.GLSL06Fragment
import milu.kiriu2010.exdb1.glsl01.g007.GLSL07Fragment
import milu.kiriu2010.exdb1.glsl01.g008.GLSL08Fragment
import milu.kiriu2010.exdb1.glsl01.g009.GLSL09Fragment
import milu.kiriu2010.exdb1.glsl01.g010.GLSL10Fragment

class GLSL01Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glsl01)

        supportFragmentManager.popBackStack()
        if (supportFragmentManager.findFragmentByTag("xyz") == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, GLSL10Fragment.newInstance(), "xyz")
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
            // 球体をライティング
            R.id.glsl_g010 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("g10") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, GLSL10Fragment.newInstance(), "g10")
                            .commit()
                }
                true
            }
            // レイマーチングで球体
            R.id.glsl_g009 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("g09") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, GLSL09Fragment.newInstance(), "g09")
                            .commit()
                }
                true
            }
            // ノイズ
            R.id.glsl_g008 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("g08") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, GLSL08Fragment.newInstance(), "g08")
                            .commit()
                }
                true
            }
            // ノイズ
            R.id.glsl_g007 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("g07") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, GLSL07Fragment.newInstance(), "g07")
                            .commit()
                }
                true
            }
            // ジュリア集合
            R.id.glsl_g006 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("g06") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, GLSL06Fragment.newInstance(), "g06")
                            .commit()
                }
                true
            }
            // マンデルブロ集合
            R.id.glsl_g005 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("g05") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, GLSL05Fragment.newInstance(), "g05")
                            .commit()
                }
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
