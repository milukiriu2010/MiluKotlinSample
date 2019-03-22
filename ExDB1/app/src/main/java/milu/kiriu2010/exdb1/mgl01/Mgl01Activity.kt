package milu.kiriu2010.exdb1.mgl01

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_mgl01.*
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.mgl01.cube01.Cube01Fragment
import milu.kiriu2010.exdb1.mgl01.cube02.Cube02Fragment
import milu.kiriu2010.exdb1.mgl01.cube03.Cube03Fragment
import milu.kiriu2010.exdb1.mgl01.cube04.Cube04Fragment
import milu.kiriu2010.exdb1.mgl01.cube05.Cube05Fragment

class Mgl01Activity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                if (supportFragmentManager.findFragmentByTag("Home") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, MGL01HomeFragment.newInstance(), "Home")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                if (supportFragmentManager.findFragmentByTag("Home") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, MGL01HomeFragment.newInstance(), "Home")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                if (supportFragmentManager.findFragmentByTag("Home") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, MGL01HomeFragment.newInstance(), "Home")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mgl01)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (supportFragmentManager.findFragmentByTag("Home") == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, MGL01HomeFragment.newInstance(), "Home")
                    .commit()
        }

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_mgl01, menu)
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
            // 立方体(フォンシェーディング)
            R.id.mgl01_cube05 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("cube05") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Cube05Fragment.newInstance(), "cube05")
                            .commit()
                }
                true
            }
            // 立方体(反射光)
            R.id.mgl01_cube04 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("cube04") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Cube04Fragment.newInstance(), "cube04")
                            .commit()
                }
                true
            }
            // 立方体(環境光)
            R.id.mgl01_cube03 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("cube03") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Cube03Fragment.newInstance(), "cube03")
                            .commit()
                }
                true
            }
            // 立方体(平行光源)
            R.id.mgl01_cube02 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("cube02") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Cube02Fragment.newInstance(), "cube02")
                            .commit()
                }
                true
            }
            // 立方体
            R.id.mgl01_cube01 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("cube01") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Cube01Fragment.newInstance(), "cube01")
                            .commit()
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
