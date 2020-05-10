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
                changeFragment("a01")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                changeFragment("a07")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                changeFragment("a05")
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
        changeFragment("a01")

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
            R.id.item_a12 -> {
                changeFragment("a12")
                true
            }
            // レムニスケート
            R.id.item_a11 -> {
                changeFragment("a11")
                true
            }
            // リサージュ
            R.id.item_a10 -> {
                changeFragment("a10")
                true
            }
            // ベルヌーイ
            R.id.item_a09 -> {
                changeFragment("a09")
                true
            }
            // カージオイド
            R.id.item_a08 -> {
                changeFragment("a08")
                true
            }
            // アステロイド
            R.id.item_a07 -> {
                changeFragment("a07")
                true
            }
            // サイクロイド
            R.id.item_a06 -> {
                changeFragment("a06")
                true
            }
            // 8の字アニメーション
            R.id.item_a05 -> {
                changeFragment("a05")
                true
            }
            // 回転するアニメーション
            R.id.item_a04 -> {
                changeFragment("a04")
                true
            }
            // バウンスするアニメーション
            R.id.item_a03 -> {
                changeFragment("a03")
                true
            }
            // 長方形の動きをするアニメーション
            R.id.item_a02 -> {
                changeFragment("a02")
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
            // インボリュート
            "a12" -> A12Fragment.newInstance()
            // レムニスケート
            "a11" -> A11Fragment.newInstance()
            // リサージュ
            "a10" -> A10Fragment.newInstance()
            // ベルヌーイ
            "a09" -> A09Fragment.newInstance()
            // カージオイド
            "a08" -> A08Fragment.newInstance()
            // アステロイド
            "a07" -> A07Fragment.newInstance()
            // サイクロイド
            "a06" -> A06Fragment.newInstance()
            // 8の字アニメーション
            "a05" -> A05Fragment.newInstance()
            // 回転するアニメーション
            "a04" -> A04Fragment.newInstance()
            // バウンスするアニメーション
            "a03" -> A03Fragment.newInstance()
            // 長方形の動きをするアニメーション
            "a02" -> A02Fragment.newInstance()
            // 左⇒右へアニメーション
            "a01" -> A01Fragment.newInstance()
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
