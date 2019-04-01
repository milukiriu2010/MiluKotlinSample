package milu.kiriu2010.exdb1.opengl03

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_open_gl03.*
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl03.w032.W032Fragment
import milu.kiriu2010.exdb1.opengl03.w033.W033Fragment
import milu.kiriu2010.exdb1.opengl03.w034.W034Fragment
import milu.kiriu2010.exdb1.opengl03.w035.W035Fragment
import milu.kiriu2010.exdb1.opengl03.w036.W036Fragment
import milu.kiriu2010.exdb1.opengl03.w037.W037Fragment
import milu.kiriu2010.exdb1.opengl03.w038.W038Fragment
import milu.kiriu2010.exdb1.opengl03.w039.W039Fragment
import milu.kiriu2010.exdb1.opengl03.w040.W040Fragment
import milu.kiriu2010.exdb1.opengl03.w040x.W040XFragment

class OpenGL03Activity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("Home") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, OpenGL03HomeFragment.newInstance(), "Home")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("Home") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, OpenGL03HomeFragment.newInstance(), "Home")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("Home") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, OpenGL03HomeFragment.newInstance(), "Home")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_gl03)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (supportFragmentManager.findFragmentByTag("Home") == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, OpenGL03HomeFragment.newInstance(), "Home")
                    .commit()
        }

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_opengl03, menu)
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
            // フレームバッファx
            R.id.opengl_w040x -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w040x") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W040XFragment.newInstance(), "w040x")
                            .commit()
                }
                true
            }
            // フレームバッファ
            R.id.opengl_w040 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w040") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W040Fragment.newInstance(), "w040")
                            .commit()
                }
                true
            }
            // ステンシルバッファでアウトライン
            R.id.opengl_w039 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w039") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W039Fragment.newInstance(), "w039")
                            .commit()
                }
                true
            }
            // ステンシルバッファ
            R.id.opengl_w038 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w038") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W038Fragment.newInstance(), "w038")
                            .commit()
                }
                true
            }
            // ポイントスプライト
            R.id.opengl_w037 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w037") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W037Fragment.newInstance(), "w037")
                            .commit()
                }
                true
            }
            // 点や線のレンダリング
            R.id.opengl_w036 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w036") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W036Fragment.newInstance(), "w036")
                            .commit()
                }
                true
            }
            // クォータニオン(ビルボード)
            R.id.opengl_w035 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w035") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W035Fragment.newInstance(), "w035")
                            .commit()
                }
                true
            }
            // クォータニオン(球面線形補間)
            R.id.opengl_w034 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w034") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W034Fragment.newInstance(), "w034")
                            .commit()
                }
                true
            }
            // クォータニオン(タッチで回転)
            R.id.opengl_w033 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w033") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W033Fragment.newInstance(), "w033")
                            .commit()
                }
                true
            }
            // クォータニオン
            R.id.opengl_w032 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w032") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W032Fragment.newInstance(), "w032")
                            .commit()
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
