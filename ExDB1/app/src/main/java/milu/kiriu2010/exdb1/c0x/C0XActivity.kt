package milu.kiriu2010.exdb1.c0x

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_c0x.*
import milu.kiriu2010.exdb1.R

class C0XActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                changeFragment(C01Fragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                changeFragment(C02Fragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                changeFragment(C03Fragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c0x)

        // ナビゲーションボタンのリスナーを設定する
        nvC0X.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        // 初期表示用のフラグメントを設定
        changeFragment(C01Fragment.newInstance())

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_c0x, menu)
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
            // SurfaceView上で引力を表現
            R.id.item_c15 -> {
                changeFragment(C15Fragment.newInstance())
                true
            }
            // SurfaceView上で液体を表現
            R.id.item_c14 -> {
                changeFragment(C14Fragment.newInstance())
                true
            }
            // SurfaceView上で摩擦を表現
            R.id.item_c13 -> {
                changeFragment(C13Fragment.newInstance())
                true
            }
            // SurfaceView上で力を表現
            R.id.item_c12 -> {
                changeFragment(C12Fragment.newInstance())
                true
            }
            // SurfaceView上でタッチ方向加速(複数)
            R.id.item_c11 -> {
                changeFragment(C11Fragment.newInstance())
                true
            }
            // SurfaceView上でタッチ方向加速
            R.id.item_c10 -> {
                changeFragment(C10Fragment.newInstance())
                true
            }
            // SurfaceView上でバルーンを描画
            R.id.item_c09 -> {
                changeFragment(C09Fragment.newInstance())
                true
            }
            // SurfaceView上で"多角形"を描画
            R.id.item_c04 -> {
                changeFragment(C04Fragment.newInstance())
                true
            }
            // SurfaceView上で"画像(シェーダ)"
            R.id.item_c03 -> {
                changeFragment(C03Fragment.newInstance())
                true
            }
            // SurfaceView上で画像を"回転(Y軸)"
            R.id.item_c02 -> {
                changeFragment(C02Fragment.newInstance())
                true
            }
            // SurfaceView上で画像を"左⇒右"へ移動する
            R.id.item_c01 -> {
                changeFragment(C01Fragment.newInstance())
                true
            }
            else -> return super.onOptionsItemSelected(item!!)
        }
    }

    // 表示するフラグメントを切り替える
    private fun changeFragment(fragment: Fragment) {
        // 現在表示しているフラグメントをスタックから外す
        supportFragmentManager.popBackStack()
        // 選択したフラグメントを表示する
        if ( supportFragmentManager.findFragmentByTag(fragment.javaClass.simpleName) == null ) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.flC0X, fragment, fragment.javaClass.simpleName)
                    .commit()
        }
    }
}
