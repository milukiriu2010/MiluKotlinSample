package milu.kiriu2010.exdb1.opengl02

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl02.w026.W026Fragment
import milu.kiriu2010.exdb1.opengl02.w026v.WV026Fragment
import milu.kiriu2010.exdb1.opengl02.w027.W027Fragment
import milu.kiriu2010.exdb1.opengl02.w027v.WV027Fragment
import milu.kiriu2010.exdb1.opengl02.w028.W028Fragment
import milu.kiriu2010.exdb1.opengl02.w028v.WV028Fragment
import milu.kiriu2010.exdb1.opengl02.w029.W029Fragment
import milu.kiriu2010.exdb1.opengl02.w029v.WV029Fragment
import milu.kiriu2010.exdb1.opengl02.w030.W030zFragment
import milu.kiriu2010.exdb1.opengl02.w030v.WV030Fragment

class OpenGL02Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_gl02)

        // 初期表示のフラグメントを設定
        changeFragment("wv026")

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_opengl02, menu)
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
            // wv30_ブレンドファクター
            R.id.opengl_wv030 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv30") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL02, WV030Fragment.newInstance(), "wv30")
                            .commit()
                }
                true
            }
            // w030z_ブレンドファクター
            R.id.opengl_w030 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w030z") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL02, W030zFragment.newInstance(), "w030z")
                            .commit()
                }
                true
            }
            // wv29_アルファブレンディング
            R.id.opengl_wv029 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv29") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL02, WV029Fragment.newInstance(), "wv29")
                            .commit()
                }
                true
            }
            // w029_アルファブレンディング
            R.id.opengl_w029 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w029") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL02, W029Fragment.newInstance(), "w029")
                            .commit()
                }
                true
            }
            // w028_テクスチャパラメータ:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv028 -> {
                changeFragment("wv028")
                true
            }
            // w028_テクスチャパラメータ:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w028 -> {
                changeFragment("w028")
                true
            }
            // w027_マルチテクスチャ:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv027 -> {
                changeFragment("wv027")
                true
            }
            // w027_マルチテクスチャ:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w027 -> {
                changeFragment("w027")
                true
            }
            // w026_テクスチャ:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv026 -> {
                changeFragment("wv026")
                true
            }
            // w026_テクスチャ:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w026 -> {
                changeFragment("w026")
                true
            }
            else -> return super.onOptionsItemSelected(item!!)
        }
    }

    // 表示するフラグメントを切り替える
    private fun changeFragment(tag: String) {
        val fragment = when (tag) {
            // w028_テクスチャパラメータ:VBOあり
            // OpenGL ES 2.0
            "wv028" -> WV028Fragment.newInstance()
            // w028_テクスチャパラメータ:VBOなし
            // OpenGL ES 2.0
            "w028" -> W028Fragment.newInstance()
            // w027_マルチテクスチャ:VBOあり
            // OpenGL ES 2.0
            "wv027" -> WV027Fragment.newInstance()
            // w027_マルチテクスチャ:VBOなし
            // OpenGL ES 2.0
            "w027" -> W027Fragment.newInstance()
            // w026_テクスチャ:VBOあり
            // OpenGL ES 2.0
            "wv026" -> WV026Fragment.newInstance()
            // w026_テクスチャ:VBOなし
            // OpenGL ES 2.0
            "w026" -> W026Fragment.newInstance()
            else -> W026Fragment.newInstance()
        }

        // 現在表示しているフラグメントをスタックから外す
        supportFragmentManager.popBackStack()
        // 選択したフラグメントを表示する
        if ( supportFragmentManager.findFragmentByTag(tag) == null ) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.flGL02, fragment, tag)
                    .commit()
        }
    }
}
