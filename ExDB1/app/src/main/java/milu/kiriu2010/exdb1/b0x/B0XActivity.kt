package milu.kiriu2010.exdb1.b0x

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_b0x.*

import milu.kiriu2010.exdb1.R

// shapeを用いたサンプルを表示するアクティビティ
class B0XActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                changeFragment("b01")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                changeFragment("b02")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                changeFragment("b03")
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b0x)

        // ナビゲーションボタンのリスナーを設定する
        nvB0X.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        // 初期表示のフラグメントを設定
        changeFragment("b02")

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_b0x, menu)
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
            R.id.item_b13 -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.flB0X, Draw13TakagiCurveFragment.newInstance())
                        .commit()
                true
            }
            // シェルピンスキー三角形
            R.id.item_b12 -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.flB0X, Draw12SierpinSkiTriangleFragment.newInstance())
                        .commit()
                true
            }
            // シェルピンスキー・カーペット
            R.id.item_b11 -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.flB0X, Draw11SierpinSkiCarpetFragment.newInstance())
                        .commit()
                true
            }
            // ジュリア集合
            R.id.item_b10 -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.flB0X, Draw10JuliaSetFragment.newInstance())
                        .commit()
                true
            }
            // ドラゴン曲線
            R.id.item_b09 -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.flB0X, Draw09DragonCurvFragment.newInstance())
                        .commit()
                true
            }
            // マンデルブロ
            R.id.item_b08 -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.flB0X, Draw08MandelBrotFragment.newInstance())
                        .commit()
                true
            }
            // コッホツリーラップ
            R.id.item_b07 -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.flB0X, Draw07KochTreeLapFragment.newInstance())
                        .commit()
                true
            }
            // コッホツリー
            R.id.item_b06 -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.flB0X, Draw06KochTreeFragment.newInstance())
                        .commit()
                true
            }
            // コッホ雪片ラップ
            R.id.item_b05 -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.flB0X, Draw05SnowFlakeLapFragment.newInstance())
                        .commit()
                true
            }
            // コッホ雪片
            R.id.item_b04 -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.flB0X, Draw04SnowFlakeFragment.newInstance())
                        .commit()
                true
            }
            // ポリゴンラップ
            R.id.item_b03 -> {
                changeFragment("b03")
                true
            }
            // 枠アニメ
            R.id.item_b02 -> {
                changeFragment("b02")
                true
            }
            // 影付きボタン
            R.id.item_b01 -> {
                changeFragment("b01")
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    // 表示するフラグメントを切り替える
    private fun changeFragment(tag: String) {
        val fragment = when (tag) {
            // 影付きボタン
            "b01" -> B01Fragment.newInstance()
            // 枠アニメ
            "b02" -> B02Fragment.newInstance()
            // ポリゴンラップ
            "b03" -> B03Fragment.newInstance()
            // 影付きボタン
            else -> B01Fragment.newInstance()
        }

        // 現在表示しているフラグメントをスタックから外す
        supportFragmentManager.popBackStack()
        // 選択したフラグメントを表示する
        if ( supportFragmentManager.findFragmentByTag(tag) == null ) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.flB0X, fragment, tag)
                    .commit()
        }
    }
}
