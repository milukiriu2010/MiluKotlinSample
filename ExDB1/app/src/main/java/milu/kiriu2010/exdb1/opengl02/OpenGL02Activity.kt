package milu.kiriu2010.exdb1.opengl02

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_open_gl02.*
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl02.labo01.TestGLFragment
import milu.kiriu2010.exdb1.opengl02.jayce07.Jayce07Fragment
import milu.kiriu2010.exdb1.opengl02.noise01.Noise01Fragment
import milu.kiriu2010.exdb1.opengl02.w026.W026Fragment
import milu.kiriu2010.exdb1.opengl02.w027.W027Fragment
import milu.kiriu2010.exdb1.opengl02.w028.W028Fragment
import milu.kiriu2010.exdb1.opengl02.w029.W029Fragment
import milu.kiriu2010.exdb1.opengl02.w030z.W030zFragment

class OpenGL02Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_gl02)

        supportFragmentManager.popBackStack()
        if (supportFragmentManager.findFragmentByTag("xyz") == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, W028Fragment.newInstance(), "xyz")
                    .commit()
        }

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
            // w030z_ブレンドファクター
            R.id.opengl_w030z -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w030z") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W030zFragment.newInstance(), "w030z")
                            .commit()
                }
                true
            }
            // w029_アルファブレンディング
            R.id.opengl_w029 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w029") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W029Fragment.newInstance(), "w029")
                            .commit()
                }
                true
            }
            // w028_テクスチャパラメータ
            R.id.opengl_w028 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w028") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W028Fragment.newInstance(), "w028")
                            .commit()
                }
                true
            }
            // w027_マルチテクスチャ
            R.id.opengl_w027 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w027") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W027Fragment.newInstance(), "w027")
                            .commit()
                }
                true
            }
            // w026_テクスチャ
            R.id.opengl_w026 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w026") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W026Fragment.newInstance(), "w026")
                            .commit()
                }
                true
            }
            // noise01_ノイズテクスチャ
            R.id.opengl_noise01 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("noise01") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Noise01Fragment.newInstance(), "noise01")
                            .commit()
                }
                true
            }
            // jayce07_フレームバッファ
            R.id.opengl_jayce07 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("jayce07") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Jayce07Fragment.newInstance(), "jayce07")
                            .commit()
                }
                true
            }
            // labo01_テクスチャ
            R.id.opengl_labo01 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("Labo01") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, TestGLFragment.newInstance(), "Labo01")
                            .commit()
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
