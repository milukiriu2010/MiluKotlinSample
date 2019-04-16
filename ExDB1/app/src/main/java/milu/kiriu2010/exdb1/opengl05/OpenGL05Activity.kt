package milu.kiriu2010.exdb1.opengl05

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_open_gl05.*
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl05.w048.W048Fragment
import milu.kiriu2010.exdb1.opengl05.w049.W049Fragment
import milu.kiriu2010.exdb1.opengl05.w050.W050Fragment
import milu.kiriu2010.exdb1.opengl05.w051.W051Fragment
import milu.kiriu2010.exdb1.opengl05.w052.W052Fragment
import milu.kiriu2010.exdb1.opengl05.w053.W053Fragment
import milu.kiriu2010.exdb1.opengl05.w054.W054Fragment
import milu.kiriu2010.exdb1.opengl05.w055.W055Fragment

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
            // sobelフィルタ
            R.id.opengl_w055 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w055") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W055Fragment.newInstance(), "w055")
                            .commit()
                }
                true
            }
            // セピア調
            R.id.opengl_w054 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w054") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W054Fragment.newInstance(), "w054")
                            .commit()
                }
                true
            }
            // グレースケール
            R.id.opengl_w053 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w053") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W053Fragment.newInstance(), "w053")
                            .commit()
                }
                true
            }
            // 高解像度車道マップ
            R.id.opengl_w052 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w052") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W052Fragment.newInstance(), "w052")
                            .commit()
                }
                true
            }
            // シャドウマッピング
            R.id.opengl_w051 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w051") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W051Fragment.newInstance(), "w051")
                            .commit()
                }
                true
            }
            // 光学迷彩
            R.id.opengl_w050 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w050") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W050Fragment.newInstance(), "w050")
                            .commit()
                }
                true
            }
            // 射影テクスチャマッピング
            R.id.opengl_w049 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w049") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W049Fragment.newInstance(), "w049")
                            .commit()
                }
                true
            }
            // トゥーンレンダリング
            R.id.opengl_w048 -> {
                supportFragmentManager.popBackStack()
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
