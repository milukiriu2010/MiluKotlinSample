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

// -------------------------------------
// OpenGL ES 2.0サンプル
// -------------------------------------
// https://wgld.org/d/webgl/w040.html
// ～
// https://wgld.org/d/webgl/w049.html
// -------------------------------------
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
            // 動的キューブマッピング:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv47 -> {
                changeFragment("wv047")
                true
            }
            // 動的キューブマッピング:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w047 -> {
                changeFragment("w047")
                true
            }
            // 屈折マッピング:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv46 -> {
                changeFragment("wv046")
                true
            }
            // 屈折マッピング:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w046 -> {
                changeFragment("w046")
                true
            }
            // キューブ環境バンプマッピング:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv45 -> {
                changeFragment("wv045")
                true
            }
            // キューブ環境バンプマッピング:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w045 -> {
                changeFragment("w045")
                true
            }
            // キューブ環境マッピング:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv44 -> {
                changeFragment("wv044")
                true
            }
            // キューブ環境マッピング:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w044 -> {
                changeFragment("w044")
                true
            }
            // 視差マッピング:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv43 -> {
                changeFragment("wv043")
                true
            }
            // 視差マッピング:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w043 -> {
                changeFragment("w043")
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
            // 動的キューブマッピング:VBOなし
            // OpenGL ES 2.0
            "wv047" -> WV047Fragment.newInstance()
            // 動的キューブマッピング:VBOなし
            // OpenGL ES 2.0
            "w047" -> W047Fragment.newInstance()
            // 屈折マッピング:VBOなし
            // OpenGL ES 2.0
            "wv046" -> WV046Fragment.newInstance()
            // 屈折マッピング:VBOなし
            // OpenGL ES 2.0
            "w046" -> W046Fragment.newInstance()
            // キューブ環境バンプマッピング:VBOあり
            // OpenGL ES 2.0
            "wv045" -> WV045Fragment.newInstance()
            // キューブ環境バンプマッピング:VBOあり
            // OpenGL ES 2.0
            "w045" -> W045Fragment.newInstance()
            // キューブ環境マッピング:VBOあり
            // OpenGL ES 2.0
            "wv044" -> WV044Fragment.newInstance()
            // キューブ環境マッピング:VBOなし
            // OpenGL ES 2.0
            "w044" -> W044Fragment.newInstance()
            // 視差マッピング:VBOあり
            // OpenGL ES 2.0
            "wv043" -> WV043Fragment.newInstance()
            // 視差マッピング:VBOなし
            // OpenGL ES 2.0
            "w043" -> W043Fragment.newInstance()
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
