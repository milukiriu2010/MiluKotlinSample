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
import milu.kiriu2010.exdb1.opengl06.w069.W069Fragment
import milu.kiriu2010.exdb1.opengl06.w069v.WV069Fragment

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
            // 正しい深度値を適用したシャドウマッピング:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv69 -> {
                changeFragment("wv069")
                true
            }
            // 正しい深度値を適用したシャドウマッピング:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w069 -> {
                changeFragment("w069")
                true
            }
            // ゴッドレイ:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv68 -> {
                changeFragment("wv068")
                true
            }
            // ゴッドレイ:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w068 -> {
                changeFragment("w068")
                true
            }
            // ズームブラー:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv67 -> {
                changeFragment("wv067")
                true
            }
            // ズームブラー:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w067 -> {
                changeFragment("w067")
                true
            }
            // モザイクフィルタ:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv66 -> {
                changeFragment("wv066")
                true
            }
            // モザイクフィルタ:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w066 -> {
                changeFragment("w066")
                true
            }
            // 後光 表面化散乱:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv65 -> {
                changeFragment("wv065")
                true
            }
            // 後光 表面化散乱:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w065 -> {
                changeFragment("w065")
                true
            }
            // リムライティング:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv64 -> {
                changeFragment("wv064")
                true
            }
            // リムライティング:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w064 -> {
                changeFragment("w064")
                true
            }
            // 半球ライティング:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv63 -> {
                changeFragment("wv063")
                true
            }
            // 半球ライティング:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w063 -> {
                changeFragment("w063")
                true
            }
            // ステンシル鏡面反射:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv62 -> {
                changeFragment("wv062")
                true
            }
            // ステンシル鏡面反射:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w062 -> {
                changeFragment("w062")
                true
            }
            // パーティクルフォグ:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv61 -> {
                changeFragment("wv061")
                true
            }
            // パーティクルフォグ:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w061 -> {
                changeFragment("w061")
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
            // 正しい深度値を適用したシャドウマッピング:VBOあり
            // OpenGL ES 2.0
            "wv069" -> WV069Fragment.newInstance()
            // 正しい深度値を適用したシャドウマッピング:VBOなし
            // OpenGL ES 2.0
            "w069" -> W069Fragment.newInstance()
            // ゴッドレイ:VBOあり
            // OpenGL ES 2.0
            "wv068" -> WV068Fragment.newInstance()
            // ゴッドレイ:VBOなし
            // OpenGL ES 2.0
            "w068" -> W068Fragment.newInstance()
            // ズームブラー:VBOあり
            // OpenGL ES 2.0
            "wv067" -> WV067Fragment.newInstance()
            // ズームブラー:VBOあり
            // OpenGL ES 2.0
            "w067" -> W067Fragment.newInstance()
            // モザイクフィルタ:VBOあり
            // OpenGL ES 2.0
            "wv066" -> WV066Fragment.newInstance()
            // モザイクフィルタ:VBOなし
            // OpenGL ES 2.0
            "w066" -> W066Fragment.newInstance()
            // 後光 表面化散乱:VBOあり
            // OpenGL ES 2.0
            "wv065" -> WV065Fragment.newInstance()
            // 後光 表面化散乱:VBOなし
            // OpenGL ES 2.0
            "w065" -> W065Fragment.newInstance()
            // リムライティング:VBOあり
            // OpenGL ES 2.0
            "wv064" -> WV064Fragment.newInstance()
            // リムライティング:VBOなし
            // OpenGL ES 2.0
            "w064" -> W064Fragment.newInstance()
            // 半球ライティング:VBOあり
            // OpenGL ES 2.0
            "wv063" -> WV063Fragment.newInstance()
            // 半球ライティング:VBOなし
            // OpenGL ES 2.0
            "w063" -> W063Fragment.newInstance()
            // ステンシル鏡面反射:VBOあり
            // OpenGL ES 2.0
            "wv062" -> WV062Fragment.newInstance()
            // ステンシル鏡面反射:VBOあり
            // OpenGL ES 2.0
            "w062" -> W062Fragment.newInstance()
            // パーティクルフォグ:VBOなし
            // OpenGL ES 2.0
            "wv061" -> WV061Fragment.newInstance()
            // パーティクルフォグ:VBOなし
            // OpenGL ES 2.0
            "w061" -> W061Fragment.newInstance()
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
