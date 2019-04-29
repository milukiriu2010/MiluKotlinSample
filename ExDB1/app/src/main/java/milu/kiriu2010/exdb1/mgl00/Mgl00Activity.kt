package milu.kiriu2010.exdb1.mgl00

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_mgl00.*
import milu.kiriu2010.exdb1.R

class Mgl00Activity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("Home") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, DepthCull01Fragment.newInstance(), "Home")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("Home") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, DepthCull01Fragment.newInstance(), "Home")
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("Home") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, DepthCull01Fragment.newInstance(), "Home")
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
                    .replace(R.id.frameLayout, DepthCull01Fragment.newInstance(), "Home")
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
            // トーラス
            R.id.opengl_torus01 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("Torus01Model") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, DepthCull01Fragment.newInstance(6), "Torus01Model")
                            .commit()
                }
                true
            }
            // 球
            R.id.opengl_sphere01 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("Sphere01Model") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, DepthCull01Fragment.newInstance(5), "Sphere01Model")
                            .commit()
                }
                true
            }
            // 正二十面体
            R.id.opengl_icosahedron01 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("Icosahedron01Model") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, DepthCull01Fragment.newInstance(4), "Icosahedron01Model")
                            .commit()
                }
                true
            }
            // 正十二面体
            R.id.opengl_dodecahedron01 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("Dodecahedron01Model") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, DepthCull01Fragment.newInstance(3), "Dodecahedron01Model")
                            .commit()
                }
                true
            }
            // 正八面体
            R.id.opengl_octahedron01 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("Octahedron01Model") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, DepthCull01Fragment.newInstance(2), "Octahedron01Model")
                            .commit()
                }
                true
            }
            // 立方体
            R.id.opengl_cube01 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("Cube01Model") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, DepthCull01Fragment.newInstance(1), "Cube01Model")
                            .commit()
                }
                true
            }
            // 正四面体
            R.id.opengl_tetrahedron01 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("Tetrahedron01Model") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, DepthCull01Fragment.newInstance(), "Tetrahedron01Model")
                            .commit()
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
