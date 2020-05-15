package milu.kiriu2010.exdb1.opengl02

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl02.w020.W020Fragment
import milu.kiriu2010.exdb1.opengl02.w020v.WV020Fragment
import milu.kiriu2010.exdb1.opengl02.w021.W021Fragment
import milu.kiriu2010.exdb1.opengl02.w021v.WV021Fragment
import milu.kiriu2010.exdb1.opengl02.w022.W022Fragment
import milu.kiriu2010.exdb1.opengl02.w022v.WV022Fragment
import milu.kiriu2010.exdb1.opengl02.w023.W023Fragment
import milu.kiriu2010.exdb1.opengl02.w023v.WV023Fragment
import milu.kiriu2010.exdb1.opengl02.w024.W024Fragment
import milu.kiriu2010.exdb1.opengl02.w024v.WV024Fragment
import milu.kiriu2010.exdb1.opengl02.w025.W025Fragment
import milu.kiriu2010.exdb1.opengl02.w025v.WV025Fragment
import milu.kiriu2010.exdb1.opengl02.w026.W026Fragment
import milu.kiriu2010.exdb1.opengl02.w026v.WV026Fragment
import milu.kiriu2010.exdb1.opengl02.w027.W027Fragment
import milu.kiriu2010.exdb1.opengl02.w027v.WV027Fragment
import milu.kiriu2010.exdb1.opengl02.w028.W028Fragment
import milu.kiriu2010.exdb1.opengl02.w028v.WV028Fragment
import milu.kiriu2010.exdb1.opengl02.w029.W029Fragment
import milu.kiriu2010.exdb1.opengl02.w029v.WV029Fragment
import milu.kiriu2010.exdb1.opengl02.w030.W030zFragment
import milu.kiriu2010.exdb1.opengl02.w030v.WV030Fragment

class OpenGL02Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_gl02)

        // 初期表示のフラグメントを設定
        changeFragment("wv026")

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_opengl02, menu)
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
            // w030_ブレンドファクター:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv030 -> {
                changeFragment("wv030")
                true
            }
            // w030_ブレンドファクター:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w030 -> {
                changeFragment("w030")
                true
            }
            // w029_アルファブレンディング:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv029 -> {
                changeFragment("wv029")
                true
            }
            // w029_アルファブレンディング:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w029 -> {
                changeFragment("w029")
                true
            }
            // w028_テクスチャパラメータ:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv028 -> {
                changeFragment("wv028")
                true
            }
            // w028_テクスチャパラメータ:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w028 -> {
                changeFragment("w028")
                true
            }
            // w027_マルチテクスチャ:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv027 -> {
                changeFragment("wv027")
                true
            }
            // w027_マルチテクスチャ:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w027 -> {
                changeFragment("w027")
                true
            }
            // w026_テクスチャ:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv026 -> {
                changeFragment("wv026")
                true
            }
            // w026_テクスチャ:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w026 -> {
                changeFragment("w026")
                true
            }
            // w025_点光源:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv025 -> {
                changeFragment("wv025")
                true
            }
            // w025_点光源:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w025 -> {
                changeFragment("w025")
                true
            }
            // w024_フォンシェーディング:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv024 -> {
                changeFragment("wv024")
                true
            }
            // w024_フォンシェーディング:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w024 -> {
                changeFragment("w024")
                true
            }
            // w023_反射光:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv023 -> {
                changeFragment("wv023")
                true
            }
            // w023_反射光:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w023 -> {
                changeFragment("w023")
                true
            }
            // w022_環境光:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv022 -> {
                changeFragment("wv022")
                true
            }
            // w022_環境光:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w022 -> {
                changeFragment("w022")
                true
            }
            // w021_平行光源:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv021 -> {
                changeFragment("wv021")
                true
            }
            // w021_平行光源:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w021 -> {
                changeFragment("w021")
                true
            }
            // w020_トーラス:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv020 -> {
                changeFragment("wv020")
                true
            }
            // w020_トーラス:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w020 -> {
                changeFragment("w020")
                true
            }
            else -> return super.onOptionsItemSelected(item!!)
        }
    }

    // 表示するフラグメントを切り替える
    private fun changeFragment(tag: String) {
        val fragment = when (tag) {
            // w030_ブレンドファクター:VBOあり
            // OpenGL ES 2.0
            "wv030" -> WV030Fragment.newInstance()
            // w030_ブレンドファクター:VBOなし
            // OpenGL ES 2.0
            "w030" -> W030zFragment.newInstance()
            // w029_アルファブレンディング:VBOあり
            // OpenGL ES 2.0
            "wv029" -> WV029Fragment.newInstance()
            // w029_アルファブレンディング:VBOなし
            // OpenGL ES 2.0
            "w029" -> W029Fragment.newInstance()
            // w028_テクスチャパラメータ:VBOあり
            // OpenGL ES 2.0
            "wv028" -> WV028Fragment.newInstance()
            // w028_テクスチャパラメータ:VBOなし
            // OpenGL ES 2.0
            "w028" -> W028Fragment.newInstance()
            // w027_マルチテクスチャ:VBOあり
            // OpenGL ES 2.0
            "wv027" -> WV027Fragment.newInstance()
            // w027_マルチテクスチャ:VBOなし
            // OpenGL ES 2.0
            "w027" -> W027Fragment.newInstance()
            // w026_テクスチャ:VBOあり
            // OpenGL ES 2.0
            "wv026" -> WV026Fragment.newInstance()
            // w026_テクスチャ:VBOなし
            // OpenGL ES 2.0
            "w026" -> W026Fragment.newInstance()
            // w025_点光源:VBOあり
            // OpenGL ES 2.0
            "wv025" -> WV025Fragment.newInstance()
            // w025_点光源:VBOなし
            // OpenGL ES 2.0
            "w025" -> W025Fragment.newInstance()
            // w024_フォンシェーディング:VBOあり
            // OpenGL ES 2.0
            "wv024" -> WV024Fragment.newInstance()
            // w024_フォンシェーディング:VBOなし
            // OpenGL ES 2.0
            "w024" -> W024Fragment.newInstance()
            // w023_反射光:VBOあり
            // OpenGL ES 2.0
            "wv023" -> WV023Fragment.newInstance()
            // w023_反射光:VBOなし
            // OpenGL ES 2.0
            "w023" -> W023Fragment.newInstance()
            // w022_環境光:VBOあり
            // OpenGL ES 2.0
            "wv022" -> WV022Fragment.newInstance()
            // w022_環境光:VBOなし
            // OpenGL ES 2.0
            "w022" -> W022Fragment.newInstance()
            // w021_平行光源:VBOあり
            // OpenGL ES 2.0
            "wv021" -> WV021Fragment.newInstance()
            // w021_平行光源:VBOなし
            // OpenGL ES 2.0
            "w021" -> W021Fragment.newInstance()
            // w020_トーラス:VBOあり
            // OpenGL ES 2.0
            "wv020" -> WV020Fragment.newInstance()
            // w020_トーラス:VBOなし
            // OpenGL ES 2.0
            "w020" -> W020Fragment.newInstance()
            else -> W026Fragment.newInstance()
        }

        // 現在表示しているフラグメントをスタックから外す
        supportFragmentManager.popBackStack()
        // 選択したフラグメントを表示する
        if ( supportFragmentManager.findFragmentByTag(tag) == null ) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.flGL02, fragment, tag)
                    .commit()
        }
    }
}
