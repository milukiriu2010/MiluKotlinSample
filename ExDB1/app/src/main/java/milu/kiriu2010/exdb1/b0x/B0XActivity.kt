package milu.kiriu2010.exdb1.b0x

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_b0x.*

import milu.kiriu2010.exdb1.R

// shapeを用いたサンプルを表示するアクティビティ
class B0XActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                changeFragment("b01")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                changeFragment("b02")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                changeFragment("b03")
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
        changeFragment("b02")

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
                changeFragment("b13")
                true
            }
            // シェルピンスキー三角形
            R.id.item_b12 -> {
                changeFragment("b12")
                true
            }
            // シェルピンスキー・カーペット
            R.id.item_b11 -> {
                changeFragment("b11")
                true
            }
            // ジュリア集合
            R.id.item_b10 -> {
                changeFragment("b10")
                true
            }
            // ドラゴン曲線
            R.id.item_b09 -> {
                changeFragment("b09")
                true
            }
            // マンデルブロ
            R.id.item_b08 -> {
                changeFragment("b08")
                true
            }
            // コッホツリーラップ
            R.id.item_b07 -> {
                changeFragment("b07")
                true
            }
            // コッホツリー
            R.id.item_b06 -> {
                changeFragment("b06")
                true
            }
            // コッホ雪片ラップ
            R.id.item_b05 -> {
                changeFragment("b05")
                true
            }
            // コッホ雪片
            R.id.item_b04 -> {
                changeFragment("b04")
                true
            }
            // ポリゴンラップ
            R.id.item_b03 -> {
                changeFragment("b03")
                true
            }
            // 枠アニメ
            R.id.item_b02 -> {
                changeFragment("b02")
                true
            }
            // 影付きボタン
            R.id.item_b01 -> {
                changeFragment("b01")
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    // 表示するフラグメントを切り替える
    private fun changeFragment(tag: String) {
        val fragment = when (tag) {
            // 高木曲線
            "b13" -> B13Fragment.newInstance()
            // シェルピンスキー三角形
            "b12" -> B12Fragment.newInstance()
            // シェルピンスキー・カーペット
            "b11" -> B11Fragment.newInstance()
            // ジュリア集合
            "b10" -> B10Fragment.newInstance()
            // ドラゴン曲線
            "b09" -> B09Fragment.newInstance()
            // マンデルブロ
            "b08" -> B08Fragment.newInstance()
            // コッホツリーラップ
            "b07" -> B07Fragment.newInstance()
            // コッホツリー
            "b06" -> B06Fragment.newInstance()
            // コッホ雪片ラップ
            "b05" -> B05Fragment.newInstance()
            // コッホ雪片
            "b04" -> B04Fragment.newInstance()
            // ポリゴンラップ
            "b03" -> B03Fragment.newInstance()
            // 枠アニメ
            "b02" -> B02Fragment.newInstance()
            // 影付きボタン
            "b01" -> B01Fragment.newInstance()
            // 影付きボタン
            else -> B01Fragment.newInstance()
        }

        // 現在表示しているフラグメントをスタックから外す
        supportFragmentManager.popBackStack()
        // 選択したフラグメントを表示する
        if ( supportFragmentManager.findFragmentByTag(tag) == null ) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.flB0X, fragment, tag)
                    .commit()
        }
    }
}
