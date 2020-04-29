package milu.kiriu2010.exdb1.draw

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_draw.*

import milu.kiriu2010.exdb1.R

class DrawActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                if (supportFragmentManager.findFragmentByTag("Home") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, DrawHomeFragment.newInstance(), "Home")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                if (supportFragmentManager.findFragmentByTag("Navi") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, DrawNaviFragment.newInstance(), "Navi")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                if (supportFragmentManager.findFragmentByTag("Notify") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, DrawNotifyFragment.newInstance(), "Notify")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw)

        // ナビゲーションボタンのリスナーを設定する
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        // 初期表示のフラグメントを設定
        if (supportFragmentManager.findFragmentByTag("Home") == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, DrawHomeFragment.newInstance(), "Home")
                    .commit()
        }

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_draw, menu)
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
            // 高木曲線
            R.id.draw_13_takagi_curve -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, Draw13TakagiCurveFragment.newInstance())
                        .commit()
                true
            }
            // シェルピンスキー三角形
            R.id.draw_12_sierpin_ski_triangle -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, Draw12SierpinSkiTriangleFragment.newInstance())
                        .commit()
                true
            }
            // シェルピンスキー・カーペット
            R.id.draw_11_sierpin_ski_carpet -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, Draw11SierpinSkiCarpetFragment.newInstance())
                        .commit()
                true
            }
            // ジュリア集合
            R.id.draw_10_juliaset -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, Draw10JuliaSetFragment.newInstance())
                        .commit()
                true
            }
            // ドラゴン曲線
            R.id.draw_09_dragoncurv -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, Draw09DragonCurvFragment.newInstance())
                        .commit()
                true
            }
            // マンデルブロ
            R.id.draw_08_mandelbrot -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, Draw08MandelBrotFragment.newInstance())
                        .commit()
                true
            }
            // コッホツリーラップ
            R.id.draw_07_koch_tree_lap -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, Draw07KochTreeLapFragment.newInstance())
                        .commit()
                true
            }
            // コッホツリー
            R.id.draw_06_koch_tree -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, Draw06KochTreeFragment.newInstance())
                        .commit()
                true
            }
        // コッホ雪片ラップ
            R.id.draw_05_koch_snowflake_lap -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, Draw05SnowFlakeLapFragment.newInstance())
                        .commit()
                true
            }
        // コッホ雪片
            R.id.draw_04_koch_snowflake -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, Draw04SnowFlakeFragment.newInstance())
                        .commit()
                true
            }
        // ポリゴンラップ
            R.id.draw_03_polygon_lap -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, Draw03PolygonLapFragment.newInstance())
                        .commit()
                true
            }
        // 枠アニメ
            R.id.draw_02 -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, Draw02DecorateTextFragment.newInstance())
                        .commit()
                true
            }
        // 影付き
            R.id.draw_01 -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, Draw01Fragment.newInstance())
                        .commit()
                true
            }
            else -> return super.onOptionsItemSelected(item!!)
        }
    }

}
