package milu.kiriu2010.exdb1.opengl01

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_open_gl.*
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl01.begin00.OpenGL10Fragment
import milu.kiriu2010.exdb1.opengl01.w018.Square01Fragment
import milu.kiriu2010.exdb1.opengl01.w019.Square02Fragment
import milu.kiriu2010.exdb1.opengl01.w020.Torus01Fragment
import milu.kiriu2010.exdb1.opengl01.w021.Torus02Fragment
import milu.kiriu2010.exdb1.opengl01.w022.Torus03Fragment
import milu.kiriu2010.exdb1.opengl01.w023.Torus04Fragment
import milu.kiriu2010.exdb1.opengl01.w024.Torus05Fragment
import milu.kiriu2010.exdb1.opengl01.begin01.Triangle01Fragment
import milu.kiriu2010.exdb1.opengl01.w015.W015Fragment
import milu.kiriu2010.exdb1.opengl01.begin02.Triangle03Fragment
import milu.kiriu2010.exdb1.opengl01.w016.Triangle04Fragment
import milu.kiriu2010.exdb1.opengl01.w017.Triangle05Fragment
import milu.kiriu2010.exdb1.opengl01.w025.Torus06Fragment

class OpenGL01Activity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                if (supportFragmentManager.findFragmentByTag("Home") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, OpenGL01HomeFragment.newInstance(), "Home")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                if (supportFragmentManager.findFragmentByTag("Dashboard") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, OpenGL01DashboardFragment.newInstance(), "Dashboard")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                if (supportFragmentManager.findFragmentByTag("Navi") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, OpenGL01NaviFragment.newInstance(), "Home")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_gl)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (supportFragmentManager.findFragmentByTag("Home") == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, OpenGL01HomeFragment.newInstance(), "Home")
                    .commit()
        }

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_opengl01, menu)
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
            // 点光源
            R.id.opengl_w025 -> {
                if (supportFragmentManager.findFragmentByTag("Torus06") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Torus06Fragment.newInstance(), "Torus06")
                            .commit()
                }
                true
            }
            // フォンシェーディング
            R.id.opengl_w024 -> {
                if (supportFragmentManager.findFragmentByTag("Torus05") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Torus05Fragment.newInstance(), "Torus05")
                            .commit()
                }
                true
            }
            // 反射光
            R.id.opengl_w023 -> {
                if (supportFragmentManager.findFragmentByTag("Torus04") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Torus04Fragment.newInstance(), "Torus04")
                            .commit()
                }
                true
            }
            // 環境光
            R.id.opengl_w022 -> {
                if (supportFragmentManager.findFragmentByTag("Torus03") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Torus03Fragment.newInstance(), "Torus03")
                            .commit()
                }
                true
            }
            // 平行光源
            R.id.opengl_w021 -> {
                if (supportFragmentManager.findFragmentByTag("Torus02") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Torus02Fragment.newInstance(), "Torus02")
                            .commit()
                }
                true
            }
            // トーラス
            R.id.opengl_w020 -> {
                if (supportFragmentManager.findFragmentByTag("Torus01") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Torus01Fragment.newInstance(), "Torus01")
                            .commit()
                }
                true
            }
            // カリングと深度テスト
            R.id.opengl_w019 -> {
                if (supportFragmentManager.findFragmentByTag("Square02") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Square02Fragment.newInstance(), "Square02")
                            .commit()
                }
                true
            }
            // w018_インデックスバッファ
            R.id.opengl_w018 -> {
                if (supportFragmentManager.findFragmentByTag("Square01") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Square01Fragment.newInstance(), "Square01")
                            .commit()
                }
                true
            }
            // w17_移動・回転・拡大/縮小
            R.id.opengl_w017 -> {
                if (supportFragmentManager.findFragmentByTag("Triangle05") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Triangle05Fragment.newInstance(), "Triangle05")
                            .commit()
                }
                true
            }
            // w016_複数モデルレンダリング
            R.id.opengl_w016 -> {
                if (supportFragmentManager.findFragmentByTag("Triangle04") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Triangle04Fragment.newInstance(), "Triangle04")
                            .commit()
                }
                true
            }
            // w015_ポリゴンに色を塗る
            R.id.opengl_w015 -> {
                if (supportFragmentManager.findFragmentByTag("Triangle02") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W015Fragment.newInstance(), "Triangle02")
                            .commit()
                }
                true
            }
            // 初めて_三角形(色+回転)
            R.id.opengl_begin02 -> {
                if (supportFragmentManager.findFragmentByTag("Triangle03") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Triangle03Fragment.newInstance(), "Triangle03")
                            .commit()
                }
                true
            }
            // 初めて_三角形＋正方形
            R.id.opengl_begin01 -> {
                if (supportFragmentManager.findFragmentByTag("Triangle01") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Triangle01Fragment.newInstance(), "Triangle01")
                            .commit()
                }
                true
            }
            // OpenGL 1.0
            R.id.opengl_begin00 -> {
                if (supportFragmentManager.findFragmentByTag("OpenGL1.0") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, OpenGL10Fragment.newInstance(), "OpenGL1.0")
                            .commit()
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
