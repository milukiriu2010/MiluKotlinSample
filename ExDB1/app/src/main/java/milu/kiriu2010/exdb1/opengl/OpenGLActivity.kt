package milu.kiriu2010.exdb1.opengl

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_open_gl.*
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl.cube01.OpenGL10Fragment
import milu.kiriu2010.exdb1.opengl.square01.Square01Fragment
import milu.kiriu2010.exdb1.opengl.square02.Square02Fragment
import milu.kiriu2010.exdb1.opengl.torus01.Torus01Fragment
import milu.kiriu2010.exdb1.opengl.torus02.Torus02Fragment
import milu.kiriu2010.exdb1.opengl.triangle01.Triangle01Fragment
import milu.kiriu2010.exdb1.opengl.triangle02.Triangle02Fragment
import milu.kiriu2010.exdb1.opengl.triangle03.Triangle03Fragment
import milu.kiriu2010.exdb1.opengl.triangle04.Triangle04Fragment
import milu.kiriu2010.exdb1.opengl.triangle05.Triangle05Fragment

class OpenGLActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                if (supportFragmentManager.findFragmentByTag("Home") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, OpenGLHomeFragment.newInstance(), "Home")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                if (supportFragmentManager.findFragmentByTag("Dashboard") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, OpenGLDashboardFragment.newInstance(), "Dashboard")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                if (supportFragmentManager.findFragmentByTag("Navi") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, OpenGLNaviFragment.newInstance(), "Home")
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
                    .replace(R.id.frameLayout, OpenGLHomeFragment.newInstance(), "Home")
                    .commit()
        }

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_opengl, menu)
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
            // トーラス(平行光)
            R.id.opengl_09_torus -> {
                if (supportFragmentManager.findFragmentByTag("Torus02") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Torus02Fragment.newInstance(), "Torus02")
                            .commit()
                }
                true
            }
            // トーラス
            R.id.opengl_08_torus -> {
                if (supportFragmentManager.findFragmentByTag("Torus01") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Torus01Fragment.newInstance(), "Torus01")
                            .commit()
                }
                true
            }
            // 四角形(カリング)
            R.id.opengl_07_square_cull -> {
                if (supportFragmentManager.findFragmentByTag("Square02") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Square02Fragment.newInstance(), "Square02")
                            .commit()
                }
                true
            }
            // 四角形
            R.id.opengl_06_square -> {
                if (supportFragmentManager.findFragmentByTag("Square01") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Square01Fragment.newInstance(), "Square01")
                            .commit()
                }
                true
            }
            // 三角形(拡大・縮小)
            R.id.opengl_05_triangle_scale -> {
                if (supportFragmentManager.findFragmentByTag("Triangle05") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Triangle05Fragment.newInstance(), "Triangle05")
                            .commit()
                }
                true
            }
            // 三角形(分身)
            R.id.opengl_04_triangle_copy -> {
                if (supportFragmentManager.findFragmentByTag("Triangle04") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Triangle04Fragment.newInstance(), "Triangle04")
                            .commit()
                }
                true
            }
            // 三角形(色+回転)
            R.id.opengl_03_triangle_color -> {
                if (supportFragmentManager.findFragmentByTag("Triangle03") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Triangle03Fragment.newInstance(), "Triangle03")
                            .commit()
                }
                true
            }
            // 三角形(色)
            R.id.opengl_02_triangle_color -> {
                if (supportFragmentManager.findFragmentByTag("Triangle02") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Triangle02Fragment.newInstance(), "Triangle02")
                            .commit()
                }
                true
            }
            // 三角形＋正方形
            R.id.opengl_01_triangle_square -> {
                if (supportFragmentManager.findFragmentByTag("Triangle01") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Triangle01Fragment.newInstance(), "Triangle01")
                            .commit()
                }
                true
            }
            // OpenGL 1.0
            R.id.opengl_00_opengl10 -> {
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
