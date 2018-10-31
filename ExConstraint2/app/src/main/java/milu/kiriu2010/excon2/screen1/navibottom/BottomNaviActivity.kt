package milu.kiriu2010.excon2.screen1.navibottom

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_bottom_navi.*
import milu.kiriu2010.excon2.R

class BottomNaviActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                if ( supportFragmentManager.findFragmentByTag("Home") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, HomeFragment.newInstance(), "Home")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                if ( supportFragmentManager.findFragmentByTag("Dashboard") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, DashboardFragment.newInstance(), "Dashboard")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                if ( supportFragmentManager.findFragmentByTag("Notification") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, NotificationFragment.newInstance(), "Notification")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navi)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        // 初期表示のフラグメントを設定
        if ( supportFragmentManager.findFragmentByTag("Home") == null ) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, HomeFragment.newInstance(), "Home")
                    .commit()
        }

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_anime,menu)
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
            // アステロイド
            R.id.anime_07_asteroid -> {
                if ( supportFragmentManager.findFragmentByTag("Anime07Asteroid") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Anime07AsteroidFragment.newInstance(), "Anime07Asteroid")
                            .commit()
                }
                true
            }
            // サイクロイド
            R.id.anime_06_cycloid -> {
                if ( supportFragmentManager.findFragmentByTag("Anime06Cycloid") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Anime06CycloidFragment.newInstance(), "Anime06Cycloid")
                            .commit()
                }
                true
            }
            // 8の字
            R.id.anime_5_eight -> {
                if ( supportFragmentManager.findFragmentByTag("Anime5Eight") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Anime5EightFragment.newInstance(), "Anime5Eight")
                            .commit()
                }
                true
            }
            // "回転"
            R.id.anime_4_rotate -> {
                if ( supportFragmentManager.findFragmentByTag("Anime4Rotate") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Anime4RotateFragment.newInstance(), "Anime4Rotate")
                            .commit()
                }
                true
            }
            // "バウンス"
            R.id.anime_3_bounce -> {
                if ( supportFragmentManager.findFragmentByTag("Anime3Bounce") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Anime3BounceFragment.newInstance(), "Anime3Bounce")
                            .commit()
                }
                true
            }
            // "長方形"
            R.id.anime_2_rectangle -> {
                if ( supportFragmentManager.findFragmentByTag("Anime2Rectangle") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Anime2RectangleFragment.newInstance(), "Anime2Rectangle")
                            .commit()
                }
                true
            }
            // "左→右"
            R.id.anime_1_lr -> {
                if ( supportFragmentManager.findFragmentByTag("Anime1LR") == null ) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Anime1LRFragment.newInstance(), "Anime1LR")
                            .commit()
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
