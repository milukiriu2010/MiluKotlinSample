package milu.kiriu2010.exdb1.a2x

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_a2x.*
import milu.kiriu2010.exdb1.R

class A2XActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                if (supportFragmentManager.findFragmentByTag("Home") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA2X, CanvasHomeFragment.newInstance(), "Home")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                if (supportFragmentManager.findFragmentByTag("Dashboard") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA2X, CanvasDashboardFragment.newInstance(), "Dashboard")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                if (supportFragmentManager.findFragmentByTag("Notification") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA2X, CanvasNotificationFragment.newInstance(), "Notification")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a2x)

        // ナビゲーションボタンのリスナーを設定する
        nvA2X.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        // 初期表示用のフラグメントを設定
        if (supportFragmentManager.findFragmentByTag("Home") == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.flA2X, CanvasHomeFragment.newInstance(), "Home")
                    .commit()
        }

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_a2x, menu)
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
        // 引力
            R.id.canvas_15_attract -> {
                if ( supportFragmentManager.findFragmentByTag("Canvas15Attract") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA2X, Canvas15AttractFragment.newInstance(), "Canvas15Attract")
                            .commit()
                }
                true
            }
        // 液体
            R.id.canvas_14_liquid -> {
                if ( supportFragmentManager.findFragmentByTag("Canvas14Liquid") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA2X, Canvas14LiquidFragment.newInstance(), "Canvas14Liquid")
                            .commit()
                }
                true
            }
        // 摩擦
            R.id.canvas_13_friction -> {
                if ( supportFragmentManager.findFragmentByTag("Canvas13Friction") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA2X, Canvas13FrictionFragment.newInstance(), "Canvas13Friction")
                            .commit()
                }
                true
            }
        // 力
            R.id.canvas_12_force -> {
                if ( supportFragmentManager.findFragmentByTag("Canvas12Force") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA2X, Canvas12ForceFragment.newInstance(), "Canvas12Force")
                            .commit()
                }
                true
            }
        // タッチ方向加速(複数)
            R.id.canvas_11_accel_touch_multi -> {
                if ( supportFragmentManager.findFragmentByTag("Canvas11AccelTouchMulti") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA2X, Canvas11AccelTouchMultiFragment.newInstance(), "Canvas11AccelTouchMulti")
                            .commit()
                }
                true
            }
        // タッチ方向加速
            R.id.canvas_10_accel_touch -> {
                if ( supportFragmentManager.findFragmentByTag("Canvas10AccelTouch") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA2X, Canvas10AccelTouchFragment.newInstance(), "Canvas10AccelTouch")
                            .commit()
                }
                true
            }
        // バルーン
            R.id.canvas_09_baloon -> {
                if ( supportFragmentManager.findFragmentByTag("Canvas09Balloon") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA2X, Canvas09BalloonFragment.newInstance(), "Canvas09Ballon")
                            .commit()
                }
                true
            }
        // "多角形"
            R.id.canvas_04_polygon -> {
                if ( supportFragmentManager.findFragmentByTag("Canvas04Polygon") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA2X, Canvas04PolygonFragment.newInstance(), "Canvas04Polygon")
                            .commit()
                }
                true
            }
        // "画像(シェーダ)"
            R.id.canvas_03_bmp_shader -> {
                if ( supportFragmentManager.findFragmentByTag("Canvas03BmpShader") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA2X, Canvas03BmpShaderFragment.newInstance(), "Canvas03BmpShader")
                            .commit()
                }
                true
            }
        // "回転(Y軸)"
            R.id.canvas_02_rotatey -> {
                if ( supportFragmentManager.findFragmentByTag("Canvas02RotateY") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA2X, Canvas02RotateYFragment.newInstance(), "Canvas02RotateY")
                            .commit()
                }
                true
            }
        // "左→右"
            R.id.canvas_01_lr -> {
                if ( supportFragmentManager.findFragmentByTag("Canvas01LR") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA2X, Canvas01LRFragment.newInstance(), "Canvas01LR")
                            .commit()
                }
                true
            }
            else -> return super.onOptionsItemSelected(item!!)
        }
    }
}
