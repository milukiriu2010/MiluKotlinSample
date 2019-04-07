package milu.kiriu2010.exdb1.opengl04

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_open_gl04.*
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl04.w042.W042Fragment
import milu.kiriu2010.exdb1.opengl04.w043.W043Fragment
import milu.kiriu2010.exdb1.opengl04.w044.W044Fragment

class OpenGL04Activity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("Home") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, OpenGL04HomeFragment.newInstance(), "Home")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("Dash") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, OpenGL04HomeFragment.newInstance(), "Dash")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("Noti") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, OpenGL04HomeFragment.newInstance(), "Noti")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_gl04)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (supportFragmentManager.findFragmentByTag("Home") == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, OpenGL04HomeFragment.newInstance(), "Home")
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
