package milu.kiriu2010.exdb1.a0x

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_a0x.*
import milu.kiriu2010.exdb1.R

// オブジェクトのアニメーションを表示するアクティビティ
class A0XActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                changeFragment(A01Fragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                changeFragment(A07Fragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                changeFragment(A05Fragment.newInstance())
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
        changeFragment(A02Fragment.newInstance())

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
                changeFragment(A12Fragment.newInstance())
                true
            }
            // レムニスケート
            R.id.item_a11 -> {
                changeFragment(A11Fragment.newInstance())
                true
            }
            // リサージュ
            R.id.item_a10 -> {
                changeFragment(A10Fragment.newInstance())
                true
            }
            // ベルヌーイ
            R.id.item_a09 -> {
                changeFragment(A09Fragment.newInstance())
                true
            }
            // カージオイド
            R.id.item_a08 -> {
                changeFragment(A08Fragment.newInstance())
                true
            }
            // アステロイド
            R.id.item_a07 -> {
                changeFragment(A07Fragment.newInstance())
                true
            }
            // サイクロイド
            R.id.item_a06 -> {
                changeFragment(A06Fragment.newInstance())
                true
            }
            // 8の字アニメーション
            R.id.item_a05 -> {
                changeFragment(A05Fragment.newInstance())
                true
            }
            // 回転するアニメーション
            R.id.item_a04 -> {
                changeFragment(A04Fragment.newInstance())
                true
            }
            // バウンスするアニメーション
            R.id.item_a03 -> {
                changeFragment(A03Fragment.newInstance())
                true
            }
            // 長方形の動きをするアニメーション
            R.id.item_a02 -> {
                changeFragment(A02Fragment.newInstance())
                true
            }
            // 左⇒右へアニメーション
            R.id.item_a01 -> {
                changeFragment(A01Fragment.newInstance())
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun changeFragment(fragment: Fragment) {
        // 現在表示しているフラグメントをスタックから外す
        supportFragmentManager.popBackStack()
        // 選択したフラグメントを表示する
        if ( supportFragmentManager.findFragmentByTag(fragment.javaClass.simpleName) == null ) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.flA0X, fragment, fragment.javaClass.simpleName)
                    .commit()
        }
    }
}
