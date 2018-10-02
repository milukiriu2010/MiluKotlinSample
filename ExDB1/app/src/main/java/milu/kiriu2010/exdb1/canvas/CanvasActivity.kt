package milu.kiriu2010.exdb1.canvas

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_canvas.*
import milu.kiriu2010.exdb1.R

class CanvasActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                if (supportFragmentManager.findFragmentByTag("Home") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, CanvasHomeFragment.newInstance(), "Home")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                if (supportFragmentManager.findFragmentByTag("Dashboard") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, CanvasDashboardFragment.newInstance(), "Dashboard")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                if (supportFragmentManager.findFragmentByTag("Notification") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, CanvasNotificationFragment.newInstance(), "Notification")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas)

        // ナビゲーションボタンのリスナーを設定する
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        // 初期表示用のフラグメントを設定
        if (supportFragmentManager.findFragmentByTag("Home") == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, CanvasHomeFragment.newInstance(), "Home")
                    .commit()
        }

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_canvas, menu)
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
        // バルーン
            R.id.canvas_09_baloon -> {
                if ( supportFragmentManager.findFragmentByTag("Canvas09Balloon") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Canvas09BalloonFragment.newInstance(), "Canvas09Ballon")
                            .commit()
                }
                true
            }
        // "左→右"
            R.id.canvas_01_lr -> {
                if ( supportFragmentManager.findFragmentByTag("Canvas01LR") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Canvas01LRFragment.newInstance(), "Canvas01LR")
                            .commit()
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
