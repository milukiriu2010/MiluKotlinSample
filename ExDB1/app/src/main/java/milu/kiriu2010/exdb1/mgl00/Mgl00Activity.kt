package milu.kiriu2010.exdb1.mgl00

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_mgl00.*
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.mgl00.dodecahedron01.Dodecahedron01Fragment
import milu.kiriu2010.exdb1.mgl00.octahedron01.Octahedron01Fragment
import milu.kiriu2010.exdb1.mgl00.pyramid01.Pyramid01Fragment

class Mgl00Activity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("Home") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, MGL00HomeFragment.newInstance(), "Home")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("Home") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, MGL00HomeFragment.newInstance(), "Home")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("Home") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, MGL00HomeFragment.newInstance(), "Home")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mgl00)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (supportFragmentManager.findFragmentByTag("Home") == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, MGL00HomeFragment.newInstance(), "Home")
                    .commit()
        }

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_mgl00, menu)
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
            // 正十二面体(点光源)
            R.id.opengl_dodecahedron01 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("Dodecahedron01Model") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Dodecahedron01Fragment.newInstance(), "Dodecahedron01Model")
                            .commit()
                }
                true
            }
            // 正八面体(点光源)
            R.id.opengl_octahedron01 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("Octahedron01Model") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Octahedron01Fragment.newInstance(), "Octahedron01Model")
                            .commit()
                }
                true
            }
            // 正四面体(点光源)
            R.id.opengl_pyramid01 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("Pyramid01Model") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Pyramid01Fragment.newInstance(), "Pyramid01Model")
                            .commit()
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
