package milu.kiriu2010.exdb1.a0x

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_a0x.*
import milu.kiriu2010.exdb1.R

// オブジェクトのアニメーションを表示するアクティビティ
class A0XActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                changeFragment("Home")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                if ( supportFragmentManager.findFragmentByTag("Dashboard") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA0X, DashboardFragment.newInstance(), "Dashboard")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                if ( supportFragmentManager.findFragmentByTag("Notification") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA0X, NotificationFragment.newInstance(), "Notification")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a0x)

        // ナビゲーションボタンのリスナーを設定する
        nvA0X.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        // 初期表示のフラグメントを設定
        changeFragment("Home")

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_a0x,menu)
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
        // インボリュート
            R.id.anime_12_involute -> {
                if ( supportFragmentManager.findFragmentByTag("Anime12Involute") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA0X, Anime12InvoluteFragment.newInstance(), "Anime12Involute")
                            .commit()
                }
                true
            }
        // レムニスケート
            R.id.anime_11_lemniscate -> {
                if ( supportFragmentManager.findFragmentByTag("Anime11Lemniscate") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA0X, Anime11LemniscateFragment.newInstance(), "Anime11Lemniscate")
                            .commit()
                }
                true
            }
        // リサージュ
            R.id.anime_10_lissajous -> {
                if ( supportFragmentManager.findFragmentByTag("Anime10Lissajous") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA0X, Anime10LissajousFragment.newInstance(), "Anime10Lissajous")
                            .commit()
                }
                true
            }
        // ベルヌーイ
            R.id.anime_09_bernoulli -> {
                if ( supportFragmentManager.findFragmentByTag("Anime09Bernoulli") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA0X, Anime09BernoulliFragment.newInstance(), "Anime09Bernoulli")
                            .commit()
                }
                true
            }
        // カージオイド
            R.id.anime_08_cardioid -> {
                if ( supportFragmentManager.findFragmentByTag("Anime08Cardioid") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA0X, Anime08CardioidFragment.newInstance(), "Anime08Cardioid")
                            .commit()
                }
                true
            }
        // アステロイド
            R.id.anime_07_asteroid -> {
                if ( supportFragmentManager.findFragmentByTag("Anime07Asteroid") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA0X, Anime07AsteroidFragment.newInstance(), "Anime07Asteroid")
                            .commit()
                }
                true
            }
        // サイクロイド
            R.id.anime_06_cycloid -> {
                if ( supportFragmentManager.findFragmentByTag("Anime06Cycloid") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA0X, Anime06CycloidFragment.newInstance(), "Anime06Cycloid")
                            .commit()
                }
                true
            }
        // 8の字
            R.id.anime_5_eight -> {
                if ( supportFragmentManager.findFragmentByTag("Anime5Eight") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA0X, Anime05EightFragment.newInstance(), "Anime5Eight")
                            .commit()
                }
                true
            }
        // "回転"
            R.id.anime_4_rotate -> {
                if ( supportFragmentManager.findFragmentByTag("Anime4Rotate") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA0X, Anime04RotateFragment.newInstance(), "Anime4Rotate")
                            .commit()
                }
                true
            }
        // "バウンス"
            R.id.anime_3_bounce -> {
                if ( supportFragmentManager.findFragmentByTag("Anime3Bounce") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA0X, Anime03BounceFragment.newInstance(), "Anime3Bounce")
                            .commit()
                }
                true
            }
        // "長方形"
            R.id.anime_2_rectangle -> {
                if ( supportFragmentManager.findFragmentByTag("Anime2Rectangle") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flA0X, Anime02RectangleFragment.newInstance(), "Anime2Rectangle")
                            .commit()
                }
                true
            }
            // 左⇒右へアニメーション
            R.id.item_a01 -> {
                changeFragment("a01")
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun changeFragment(tag: String) {
        val fragment = when (tag) {
            // 左⇒右へアニメーション
            "a01" -> A01Fragment.newInstance()
            // 左⇒右へアニメーション
            "Home" -> A01Fragment.newInstance()
            // 左⇒右へアニメーション
            else -> A01Fragment.newInstance()
        }
        // 現在表示しているフラグメントをスタックから外す
        supportFragmentManager.popBackStack()
        // 選択したフラグメントを表示する
        if ( supportFragmentManager.findFragmentByTag(tag) == null ) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.flA0X, fragment, tag)
                    .commit()
        }
    }
}
