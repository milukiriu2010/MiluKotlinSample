package milu.kiriu2010.exdb1.a1x

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_a1x.*

import milu.kiriu2010.exdb1.R

// shapeを用いたサンプルを表示するアクティビティ
class A1XActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                changeFragment("Home")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                if (supportFragmentManager.findFragmentByTag("Navi") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA1X, DrawNaviFragment.newInstance(), "Navi")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                if (supportFragmentManager.findFragmentByTag("Notify") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA1X, DrawNotifyFragment.newInstance(), "Notify")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a1x)

        // ナビゲーションボタンのリスナーを設定する
        nvA1X.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        // 初期表示のフラグメントを設定
        changeFragment("Home")

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_a1x, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // アクションバーのアイコンがタップされると呼ばれる
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // 前画面に戻る
            android.R.id.home -> {
                finish()
                true
            }
            // 高木曲線
            R.id.draw_13_takagi_curve -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.flA1X, Draw13TakagiCurveFragment.newInstance())
                        .commit()
                true
            }
            // シェルピンスキー三角形
            R.id.draw_12_sierpin_ski_triangle -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.flA1X, Draw12SierpinSkiTriangleFragment.newInstance())
                        .commit()
                true
            }
            // シェルピンスキー・カーペット
            R.id.draw_11_sierpin_ski_carpet -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.flA1X, Draw11SierpinSkiCarpetFragment.newInstance())
                        .commit()
                true
            }
            // ジュリア集合
            R.id.draw_10_juliaset -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.flA1X, Draw10JuliaSetFragment.newInstance())
                        .commit()
                true
            }
            // ドラゴン曲線
            R.id.draw_09_dragoncurv -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.flA1X, Draw09DragonCurvFragment.newInstance())
                        .commit()
                true
            }
            // マンデルブロ
            R.id.draw_08_mandelbrot -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.flA1X, Draw08MandelBrotFragment.newInstance())
                        .commit()
                true
            }
            // コッホツリーラップ
            R.id.draw_07_koch_tree_lap -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.flA1X, Draw07KochTreeLapFragment.newInstance())
                        .commit()
                true
            }
            // コッホツリー
            R.id.draw_06_koch_tree -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.flA1X, Draw06KochTreeFragment.newInstance())
                        .commit()
                true
            }
            // コッホ雪片ラップ
            R.id.draw_05_koch_snowflake_lap -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.flA1X, Draw05SnowFlakeLapFragment.newInstance())
                        .commit()
                true
            }
            // コッホ雪片
            R.id.draw_04_koch_snowflake -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.flA1X, Draw04SnowFlakeFragment.newInstance())
                        .commit()
                true
            }
            // ポリゴンラップ
            R.id.draw_03_polygon_lap -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.flA1X, Draw03PolygonLapFragment.newInstance())
                        .commit()
                true
            }
            // 枠アニメ
            R.id.draw_02 -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.flA1X, Draw02DecorateTextFragment.newInstance())
                        .commit()
                true
            }
            // 影付きボタン
            R.id.item_a11 -> {
                changeFragment("a11")
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    // 表示するフラグメントを切り替える
    private fun changeFragment(tag: String) {
        val fragment = when (tag) {
            // 影付きボタン
            "a11" -> A11Fragment.newInstance()
            // 影付きボタン
            "Home" -> A11Fragment.newInstance()
            // 影付きボタン
            else -> A11Fragment.newInstance()
        }

        // 現在表示しているフラグメントをスタックから外す
        supportFragmentManager.popBackStack()
        // 選択したフラグメントを表示する
        if ( supportFragmentManager.findFragmentByTag(tag) == null ) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.flA1X, fragment, tag)
                    .commit()
        }
    }
}
