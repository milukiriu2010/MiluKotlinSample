package milu.kiriu2010.exdb1.opengl04

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_open_gl04.*
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl04.w042.W042Fragment
import milu.kiriu2010.exdb1.opengl04.w042v.WV042Fragment
import milu.kiriu2010.exdb1.opengl04.w043.W043Fragment
import milu.kiriu2010.exdb1.opengl04.w044.W044Fragment
import milu.kiriu2010.exdb1.opengl04.w045.W045Fragment
import milu.kiriu2010.exdb1.opengl04.w046.W046Fragment
import milu.kiriu2010.exdb1.opengl04.w047.W047Fragment

class OpenGL04Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_gl04)

        supportFragmentManager.popBackStack()
        if (supportFragmentManager.findFragmentByTag("xyz") == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, WV042Fragment.newInstance(), "xyz")
                    .commit()
        }

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_opengl04, menu)
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
            // 動的キューブマッピング
            R.id.opengl_w047 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w047") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W047Fragment.newInstance(), "w047")
                            .commit()
                }
                true
            }
            // 屈折マッピング
            R.id.opengl_w046 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w046") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W046Fragment.newInstance(), "w046")
                            .commit()
                }
                true
            }
            // キューブ環境バンプマッピング
            R.id.opengl_w045 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w045") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W045Fragment.newInstance(), "w045")
                            .commit()
                }
                true
            }
            // キューブ環境マッピング
            R.id.opengl_w044 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w044") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W044Fragment.newInstance(), "w044")
                            .commit()
                }
                true
            }
            // 視差マッピング
            R.id.opengl_w043 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w043") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W043Fragment.newInstance(), "w043")
                            .commit()
                }
                true
            }
            // バンプマッピング
            R.id.opengl_wv42 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv42") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, WV042Fragment.newInstance(), "wv42")
                            .commit()
                }
                true
            }
            // バンプマッピング
            R.id.opengl_w042 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w042") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W042Fragment.newInstance(), "w042")
                            .commit()
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
