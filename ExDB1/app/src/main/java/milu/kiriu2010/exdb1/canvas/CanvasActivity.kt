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
        // 液体
            R.id.canvas_14_liquid -> {
                if ( supportFragmentManager.findFragmentByTag("Canvas14Liquid") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Canvas14LiquidFragment.newInstance(), "Canvas14Liquid")
                            .commit()
                }
                true
            }
        // 摩擦
            R.id.canvas_13_friction -> {
                if ( supportFragmentManager.findFragmentByTag("Canvas13Friction") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Canvas13FrictionFragment.newInstance(), "Canvas13Friction")
                            .commit()
                }
                true
            }
        // 力
            R.id.canvas_12_force -> {
                if ( supportFragmentManager.findFragmentByTag("Canvas12Force") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Canvas12ForceFragment.newInstance(), "Canvas12Force")
                            .commit()
                }
                true
            }
        // タッチ方向加速(複数)
            R.id.canvas_11_accel_touch_multi -> {
                if ( supportFragmentManager.findFragmentByTag("Canvas11AccelTouchMulti") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Canvas11AccelTouchMultiFragment.newInstance(), "Canvas11AccelTouchMulti")
                            .commit()
                }
                true
            }
        // タッチ方向加速
            R.id.canvas_10_accel_touch -> {
                if ( supportFragmentManager.findFragmentByTag("Canvas10AccelTouch") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Canvas10AccelTouchFragment.newInstance(), "Canvas10AccelTouch")
                            .commit()
                }
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
