package milu.kiriu2010.exdb1.opengl04

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_open_gl04.*
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl04.w042.W042Fragment
import milu.kiriu2010.exdb1.opengl04.w042v.WV042Fragment
import milu.kiriu2010.exdb1.opengl04.w043.W043Fragment
import milu.kiriu2010.exdb1.opengl04.w043v.WV043Fragment
import milu.kiriu2010.exdb1.opengl04.w044.W044Fragment
import milu.kiriu2010.exdb1.opengl04.w044v.WV044Fragment
import milu.kiriu2010.exdb1.opengl04.w045.W045Fragment
import milu.kiriu2010.exdb1.opengl04.w045v.WV045Fragment
import milu.kiriu2010.exdb1.opengl04.w046.W046Fragment
import milu.kiriu2010.exdb1.opengl04.w046v.WV046Fragment
import milu.kiriu2010.exdb1.opengl04.w047.W047Fragment
import milu.kiriu2010.exdb1.opengl04.w047v.WV047Fragment

class OpenGL04Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_gl04)

        // 初期表示のフラグメントを設定
        changeFragment("wv042")

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
            R.id.opengl_wv47 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv47") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL04, WV047Fragment.newInstance(), "wv47")
                            .commit()
                }
                true
            }
            // 動的キューブマッピング
            R.id.opengl_w047 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w047") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL04, W047Fragment.newInstance(), "w047")
                            .commit()
                }
                true
            }
            // 屈折マッピング
            R.id.opengl_wv46 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv46") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL04, WV046Fragment.newInstance(), "wv46")
                            .commit()
                }
                true
            }
            // 屈折マッピング
            R.id.opengl_w046 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w046") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL04, W046Fragment.newInstance(), "w046")
                            .commit()
                }
                true
            }
            // キューブ環境バンプマッピング
            R.id.opengl_wv45 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv45") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL04, WV045Fragment.newInstance(), "wv45")
                            .commit()
                }
                true
            }
            // キューブ環境バンプマッピング
            R.id.opengl_w045 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w045") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL04, W045Fragment.newInstance(), "w045")
                            .commit()
                }
                true
            }
            // キューブ環境マッピング
            R.id.opengl_wv44 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv44") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL04, WV044Fragment.newInstance(), "w44")
                            .commit()
                }
                true
            }
            // キューブ環境マッピング
            R.id.opengl_w044 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w044") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL04, W044Fragment.newInstance(), "w044")
                            .commit()
                }
                true
            }
            // 視差マッピング
            R.id.opengl_wv43 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv43") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL04, WV043Fragment.newInstance(), "wv43")
                            .commit()
                }
                true
            }
            // 視差マッピング
            R.id.opengl_w043 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w043") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL04, W043Fragment.newInstance(), "w043")
                            .commit()
                }
                true
            }
            // w042_バンプマッピング:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv42 -> {
                changeFragment("wv042")
                true
            }
            // w042_バンプマッピング:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w042 -> {
                changeFragment("w042")
                true
            }
            else -> return super.onOptionsItemSelected(item!!)
        }
    }


    // 表示するフラグメントを切り替える
    private fun changeFragment(tag: String) {
        val fragment = when (tag) {
            // w042_バンプマッピング:VBOあり
            // OpenGL ES 2.0
            "wv042" -> WV042Fragment.newInstance()
            // w042_バンプマッピング:VBOなし
            // OpenGL ES 2.0
            "w042" -> W042Fragment.newInstance()
            // w042_バンプマッピング:VBOなし
            // OpenGL ES 2.0
            else -> W042Fragment.newInstance()
        }

        // 現在表示しているフラグメントをスタックから外す
        supportFragmentManager.popBackStack()
        // 選択したフラグメントを表示する
        if ( supportFragmentManager.findFragmentByTag(tag) == null ) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.flGL04, fragment, tag)
                    .commit()
        }
    }
}
