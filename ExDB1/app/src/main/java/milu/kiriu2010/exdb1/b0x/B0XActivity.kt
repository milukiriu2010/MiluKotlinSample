package milu.kiriu2010.exdb1.b0x

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_b0x.*

import milu.kiriu2010.exdb1.R

// shapeを用いたサンプルを表示するアクティビティ
class B0XActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                changeFragment(B01Fragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                changeFragment(B02Fragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                changeFragment(B03Fragment.newInstance())
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
        changeFragment(B02Fragment.newInstance())

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
                changeFragment(B13Fragment.newInstance())
                true
            }
            // シェルピンスキー三角形
            R.id.item_b12 -> {
                changeFragment(B12Fragment.newInstance())
                true
            }
            // シェルピンスキー・カーペット
            R.id.item_b11 -> {
                changeFragment(B11Fragment.newInstance())
                true
            }
            // ジュリア集合
            R.id.item_b10 -> {
                changeFragment(B10Fragment.newInstance())
                true
            }
            // ドラゴン曲線
            R.id.item_b09 -> {
                changeFragment(B09Fragment.newInstance())
                true
            }
            // マンデルブロ
            R.id.item_b08 -> {
                changeFragment(B08Fragment.newInstance())
                true
            }
            // コッホツリーラップ
            R.id.item_b07 -> {
                changeFragment(B07Fragment.newInstance())
                true
            }
            // コッホツリー
            R.id.item_b06 -> {
                changeFragment(B06Fragment.newInstance())
                true
            }
            // コッホ雪片ラップ
            R.id.item_b05 -> {
                changeFragment(B05Fragment.newInstance())
                true
            }
            // コッホ雪片
            R.id.item_b04 -> {
                changeFragment(B04Fragment.newInstance())
                true
            }
            // ポリゴンラップ
            R.id.item_b03 -> {
                changeFragment(B03Fragment.newInstance())
                true
            }
            // 枠アニメ
            R.id.item_b02 -> {
                changeFragment(B02Fragment.newInstance())
                true
            }
            // 影付きボタン
            R.id.item_b01 -> {
                changeFragment(B01Fragment.newInstance())
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    // 表示するフラグメントを切り替える
    private fun changeFragment(fragment: Fragment) {
        // 現在表示しているフラグメントをスタックから外す
        supportFragmentManager.popBackStack()
        // 選択したフラグメントを表示する
        if ( supportFragmentManager.findFragmentByTag(fragment.javaClass.simpleName) == null ) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.flB0X, fragment, fragment.javaClass.simpleName)
                    .commit()
        }
    }
}
