package milu.kiriu2010.exdb1.opengl02

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_open_gl02.*
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl02.labo01.TestGLFragment
import milu.kiriu2010.exdb1.mgl00.pyramid01.Pyramid01Fragment
import milu.kiriu2010.exdb1.opengl02.w026.W026Fragment
import milu.kiriu2010.exdb1.opengl02.w027.W027Fragment
import milu.kiriu2010.exdb1.opengl02.w028.W028Fragment
import milu.kiriu2010.exdb1.opengl02.w029.W029Fragment
import milu.kiriu2010.exdb1.opengl02.w030.W030Fragment

class OpenGL02Activity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                if (supportFragmentManager.findFragmentByTag("Home") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, OpenGL02HomeFragment.newInstance(), "Home")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                if (supportFragmentManager.findFragmentByTag("Dashboard") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, OpenGL02DashFragment.newInstance(), "Dashboard")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                if (supportFragmentManager.findFragmentByTag("Navi") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, OpenGL02NaviFragment.newInstance(), "Navi")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_gl02)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (supportFragmentManager.findFragmentByTag("Home") == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, OpenGL02HomeFragment.newInstance(), "Home")
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
            // w030_ブレンドファクター
            R.id.opengl_w030 -> {
                if (supportFragmentManager.findFragmentByTag("w030") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W030Fragment.newInstance(), "w030")
                            .commit()
                }
                true
            }
            // w029_アルファブレンディング
            R.id.opengl_w029 -> {
                if (supportFragmentManager.findFragmentByTag("w029") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W029Fragment.newInstance(), "w029")
                            .commit()
                }
                true
            }
            // w028_テクスチャパラメータ
            R.id.opengl_w028 -> {
                if (supportFragmentManager.findFragmentByTag("w028") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W028Fragment.newInstance(), "w028")
                            .commit()
                }
                true
            }
            // w027_マルチテクスチャ
            R.id.opengl_w027 -> {
                if (supportFragmentManager.findFragmentByTag("w027") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W027Fragment.newInstance(), "w027")
                            .commit()
                }
                true
            }
            // w026_テクスチャ
            R.id.opengl_w026 -> {
                if (supportFragmentManager.findFragmentByTag("w026") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W026Fragment.newInstance(), "w026")
                            .commit()
                }
                true
            }
            // labo01_テクスチャ
            R.id.opengl_labo01 -> {
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
