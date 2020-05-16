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
                changeFragment("c01")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                changeFragment("c02")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                changeFragment("c03")
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
        changeFragment("c01")

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
                changeFragment("c15")
                true
            }
            // SurfaceView上で液体を表現
            R.id.item_c14 -> {
                changeFragment("c14")
                true
            }
            // SurfaceView上で摩擦を表現
            R.id.item_c13 -> {
                changeFragment("c13")
                true
            }
            // SurfaceView上で力を表現
            R.id.item_c12 -> {
                changeFragment("c12")
                true
            }
            // SurfaceView上でタッチ方向加速(複数)
            R.id.item_c11 -> {
                changeFragment("c11")
                true
            }
            // SurfaceView上でタッチ方向加速
            R.id.item_c10 -> {
                changeFragment("c10")
                true
            }
            // SurfaceView上でバルーンを描画
            R.id.item_c09 -> {
                changeFragment("c09")
                true
            }
            // SurfaceView上で"多角形"を描画
            R.id.item_c04 -> {
                changeFragment("c04")
                true
            }
            // SurfaceView上で"画像(シェーダ)"
            R.id.item_c03 -> {
                changeFragment("c03")
                true
            }
            // SurfaceView上で画像を"回転(Y軸)"
            R.id.item_c02 -> {
                changeFragment("c02")
                true
            }
            // SurfaceView上で画像を"左⇒右"へ移動する
            R.id.item_c01 -> {
                changeFragment("c01")
                true
            }
            else -> return super.onOptionsItemSelected(item!!)
        }
    }

    // 表示するフラグメントを切り替える
    private fun changeFragment(tag: String) {
        val fragment: Fragment = when (tag) {
            // SurfaceView上で引力を表現
            "c15" -> C15Fragment.newInstance()
            // SurfaceView上で液体を表現
            "c14" -> C14Fragment.newInstance()
            // SurfaceView上で摩擦を表現
            "c13" -> C13Fragment.newInstance()
            // SurfaceView上で力を表現
            "c12" -> C12Fragment.newInstance()
            // SurfaceView上でタッチ方向加速(複数)
            "c11" -> C11Fragment.newInstance()
            // SurfaceView上でタッチ方向加速
            "c10" -> C10Fragment.newInstance()
            // SurfaceView上でバルーンを描画
            "c09" -> C09Fragment.newInstance()
            // SurfaceView上で"多角形"を描画
            "c04" -> C04Fragment.newInstance()
            // SurfaceView上で"画像(シェーダ)"
            "c03" -> C03Fragment.newInstance()
            // SurfaceView上で画像を"回転(Y軸)"
            "c02" -> C02Fragment.newInstance()
            // SurfaceView上で画像を"左⇒右"へ移動する
            "c01" -> C01Fragment.newInstance()
            // SurfaceView上で画像を"左⇒右"へ移動する
            else -> C01Fragment.newInstance()
        }

        // 現在表示しているフラグメントをスタックから外す
        supportFragmentManager.popBackStack()
        // 選択したフラグメントを表示する
        if ( supportFragmentManager.findFragmentByTag(tag) == null ) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.flC0X, fragment, tag)
                    .commit()
        }
    }
}
