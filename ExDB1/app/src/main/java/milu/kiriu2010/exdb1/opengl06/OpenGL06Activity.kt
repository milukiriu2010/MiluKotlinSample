package milu.kiriu2010.exdb1.opengl06

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl06.w060.W060Fragment
import milu.kiriu2010.exdb1.opengl06.w060v.WV060Fragment
import milu.kiriu2010.exdb1.opengl06.w061.W061Fragment
import milu.kiriu2010.exdb1.opengl06.w061v.WV061Fragment
import milu.kiriu2010.exdb1.opengl06.w062.W062Fragment
import milu.kiriu2010.exdb1.opengl06.w062v.WV062Fragment
import milu.kiriu2010.exdb1.opengl06.w063.W063Fragment
import milu.kiriu2010.exdb1.opengl06.w063v.WV063Fragment
import milu.kiriu2010.exdb1.opengl06.w064.W064Fragment
import milu.kiriu2010.exdb1.opengl06.w064v.WV064Fragment
import milu.kiriu2010.exdb1.opengl06.w065.W065Fragment
import milu.kiriu2010.exdb1.opengl06.w065v.WV065Fragment
import milu.kiriu2010.exdb1.opengl06.w066.W066Fragment
import milu.kiriu2010.exdb1.opengl06.w066v.WV066Fragment
import milu.kiriu2010.exdb1.opengl06.w067.W067Fragment
import milu.kiriu2010.exdb1.opengl06.w067v.WV067Fragment
import milu.kiriu2010.exdb1.opengl06.w068.W068Fragment
import milu.kiriu2010.exdb1.opengl06.w068v.WV068Fragment

// -------------------------------------
// OpenGL ES 2.0サンプル
// -------------------------------------
// https://wgld.org/d/webgl/w060.html
// ～
// https://wgld.org/d/webgl/w069.html
// -------------------------------------
class OpenGL06Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_gl06)

        // 初期表示のフラグメントを設定
        changeFragment("wv060")

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_opengl06, menu)
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
            // ゴッドレイ
            R.id.opengl_wv68 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv68") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL06, WV068Fragment.newInstance(), "wv68")
                            .commit()
                }
                true
            }
            // ゴッドレイ
            R.id.opengl_w068 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w068") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL06, W068Fragment.newInstance(), "w068")
                            .commit()
                }
                true
            }
            // ズームブラー
            R.id.opengl_wv67 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv67") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL06, WV067Fragment.newInstance(), "wv67")
                            .commit()
                }
                true
            }
            // ズームブラー
            R.id.opengl_w067 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w067") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL06, W067Fragment.newInstance(), "w067")
                            .commit()
                }
                true
            }
            // モザイクフィルタ
            R.id.opengl_wv66 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv66") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL06, WV066Fragment.newInstance(), "wv66")
                            .commit()
                }
                true
            }
            // モザイクフィルタ
            R.id.opengl_w066 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w066") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL06, W066Fragment.newInstance(), "w066")
                            .commit()
                }
                true
            }
            // 後光 表面化散乱
            R.id.opengl_wv65 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv65") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL06, WV065Fragment.newInstance(), "wv65")
                            .commit()
                }
                true
            }
            // 後光 表面化散乱
            R.id.opengl_w065 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w065") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL06, W065Fragment.newInstance(), "w065")
                            .commit()
                }
                true
            }
            // リムライティング
            R.id.opengl_wv64 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv64") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL06, WV064Fragment.newInstance(), "wv64")
                            .commit()
                }
                true
            }
            // リムライティング
            R.id.opengl_w064 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w064") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL06, W064Fragment.newInstance(), "w064")
                            .commit()
                }
                true
            }
            // 半球ライティング
            R.id.opengl_wv63 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv63") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL06, WV063Fragment.newInstance(), "wv63")
                            .commit()
                }
                true
            }
            // 半球ライティング
            R.id.opengl_w063 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w063") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL06, W063Fragment.newInstance(), "w063")
                            .commit()
                }
                true
            }
            // ステンシル鏡面反射
            R.id.opengl_wv62 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv62") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL06, WV062Fragment.newInstance(), "wv62")
                            .commit()
                }
                true
            }
            // ステンシル鏡面反射
            R.id.opengl_w062 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w062") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL06, W062Fragment.newInstance(), "w062")
                            .commit()
                }
                true
            }
            // パーティクルフォグ
            R.id.opengl_wv61 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv61") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL06, WV061Fragment.newInstance(), "wv61")
                            .commit()
                }
                true
            }
            // パーティクルフォグ
            R.id.opengl_w061 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w061") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL06, W061Fragment.newInstance(), "w061")
                            .commit()
                }
                true
            }
            // w060_フォグ距離:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv60 -> {
                changeFragment("wv060")
                true
            }
            // w060_フォグ距離:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w060 -> {
                changeFragment("w060")
                true
            }
            else -> return super.onOptionsItemSelected(item!!)
        }
    }

    // 表示するフラグメントを切り替える
    private fun changeFragment(tag: String) {
        val fragment = when (tag) {
            // w060_フォグ距離:VBOあり
            // OpenGL ES 2.0
            "wv060" -> WV060Fragment.newInstance()
            // w060_フォグ距離:VBOなし
            // OpenGL ES 2.0
            "w060" -> W060Fragment.newInstance()
            else -> W060Fragment.newInstance()
        }

        // 現在表示しているフラグメントをスタックから外す
        supportFragmentManager.popBackStack()
        // 選択したフラグメントを表示する
        if ( supportFragmentManager.findFragmentByTag(tag) == null ) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.flGL06, fragment, tag)
                    .commit()
        }
    }
}
