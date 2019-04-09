package milu.kiriu2010.exdb1.opengl05

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_open_gl05.*
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl05.w048.W048Fragment

class OpenGL05Activity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("Home") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, OpenGL05HomeFragment.newInstance(), "Home")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("Home") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, OpenGL05HomeFragment.newInstance(), "Home")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("Home") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, OpenGL05HomeFragment.newInstance(), "Home")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_gl05)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (supportFragmentManager.findFragmentByTag("Home") == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, OpenGL05HomeFragment.newInstance(), "Home")
                    .commit()
        }

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_opengl05, menu)
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
            // トゥーンレンダリング
            R.id.opengl_w048 -> {
                if (supportFragmentManager.findFragmentByTag("w048") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W048Fragment.newInstance(), "w048")
                            .commit()
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
