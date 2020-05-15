package milu.kiriu2010.exdb1.opengl03

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl03.w030.W030zFragment
import milu.kiriu2010.exdb1.opengl03.w030v.WV030Fragment
import milu.kiriu2010.exdb1.opengl03.w032.W032Fragment
import milu.kiriu2010.exdb1.opengl03.w032v.WV032Fragment
import milu.kiriu2010.exdb1.opengl03.w033.W033Fragment
import milu.kiriu2010.exdb1.opengl03.w033v.WV033Fragment
import milu.kiriu2010.exdb1.opengl03.w034.W034Fragment
import milu.kiriu2010.exdb1.opengl03.w034v.WV034Fragment
import milu.kiriu2010.exdb1.opengl03.w035.W035Fragment
import milu.kiriu2010.exdb1.opengl03.w035v.WV035Fragment
import milu.kiriu2010.exdb1.opengl03.w036.W036Fragment
import milu.kiriu2010.exdb1.opengl03.w036v.WV036Fragment
import milu.kiriu2010.exdb1.opengl03.w037.W037Fragment
import milu.kiriu2010.exdb1.opengl03.w037v.WV037Fragment
import milu.kiriu2010.exdb1.opengl03.w038.W038Fragment
import milu.kiriu2010.exdb1.opengl03.w038v.WV038Fragment
import milu.kiriu2010.exdb1.opengl03.w039.W039Fragment
import milu.kiriu2010.exdb1.opengl03.w039v.WV039Fragment
import milu.kiriu2010.exdb1.opengl04.w040.W040Fragment
import milu.kiriu2010.exdb1.opengl04.w040v.WV040Fragment
import milu.kiriu2010.exdb1.opengl04.w041.W041Fragment
import milu.kiriu2010.exdb1.opengl04.w041v.WV041Fragment

// -------------------------------------
// OpenGL ES 2.0サンプル
// -------------------------------------
// https://wgld.org/d/webgl/w030.html
// ～
// https://wgld.org/d/webgl/w039.html
// -------------------------------------
class OpenGL03Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_gl03)

        // 初期表示のフラグメントを設定
        changeFragment("wv030")

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_opengl03, menu)
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
            // ステンシルバッファでアウトライン:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv39 -> {
                changeFragment("wv039")
                true
            }
            // ステンシルバッファでアウトライン:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w039 -> {
                changeFragment("w039")
                true
            }
            // ステンシルバッファ:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv38 -> {
                changeFragment("wv038")
                true
            }
            // ステンシルバッファ:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w038 -> {
                changeFragment("w038")
                true
            }
            // ポイントスプライト:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv37 -> {
                changeFragment("wv037")
                true
            }
            // ポイントスプライト:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w037 -> {
                changeFragment("w037")
                true
            }
            // 点や線のレンダリング:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv36 -> {
                changeFragment("wv036")
                true
            }
            // 点や線のレンダリング:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w036 -> {
                changeFragment("w036")
                true
            }
            // クォータニオン(ビルボード):VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv35 -> {
                changeFragment("wv035")
                true
            }
            // クォータニオン(ビルボード):VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w035 -> {
                changeFragment("w035")
                true
            }
            // クォータニオン(球面線形補間):VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv34 -> {
                changeFragment("wv034")
                true
            }
            // クォータニオン(球面線形補間):VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w034 -> {
                changeFragment("w034")
                true
            }
            // クォータニオン(タッチで回転):VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv33 -> {
                changeFragment("wv033")
                true
            }
            // クォータニオン(タッチで回転):VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w033 -> {
                changeFragment("w033")
                true
            }
            // w032_クォータニオン:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv32 -> {
                changeFragment("wv032")
                true
            }
            // w032_クォータニオン:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w032 -> {
                changeFragment("w032")
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
            else -> return super.onOptionsItemSelected(item!!)
        }
    }

    // 表示するフラグメントを切り替える
    private fun changeFragment(tag: String) {
        val fragment = when (tag) {
            // ステンシルバッファでアウトライン:VBOあり
            // OpenGL ES 2.0
            "wv039" -> WV039Fragment.newInstance()
            // ステンシルバッファでアウトライン:VBOなし
            // OpenGL ES 2.0
            "w039" -> W039Fragment.newInstance()
            // ステンシルバッファ:VBOなし
            // OpenGL ES 2.0
            "wv038" -> WV038Fragment.newInstance()
            // ステンシルバッファ:VBOなし
            // OpenGL ES 2.0
            "w038" -> W038Fragment.newInstance()
            // ポイントスプライト:VBOあり
            // OpenGL ES 2.0
            "wv037" -> WV037Fragment.newInstance()
            // ポイントスプライト:VBOなし
            // OpenGL ES 2.0
            "w037" -> W037Fragment.newInstance()
            // 点や線のレンダリング:VBOあり
            // OpenGL ES 2.0
            "wv036" -> WV036Fragment.newInstance()
            // 点や線のレンダリング:VBOなし
            // OpenGL ES 2.0
            "w036" -> W036Fragment.newInstance()
            // クォータニオン(ビルボード):VBOなし
            // OpenGL ES 2.0
            "wv035" -> WV035Fragment.newInstance()
            // クォータニオン(ビルボード):VBOなし
            // OpenGL ES 2.0
            "w035" -> W035Fragment.newInstance()
            // クォータニオン(球面線形補間):VBOなし
            // OpenGL ES 2.0
            "wv034" -> WV034Fragment.newInstance()
            // クォータニオン(球面線形補間):VBOあり
            // OpenGL ES 2.0
            "w034" -> W034Fragment.newInstance()
            // クォータニオン(タッチで回転):VBOあり
            // OpenGL ES 2.0
            "wv033" -> WV033Fragment.newInstance()
            // クォータニオン(タッチで回転):VBOなし
            // OpenGL ES 2.0
            "w033" -> W033Fragment.newInstance()
            // w032_クォータニオン:VBOあり
            // OpenGL ES 2.0
            "wv032" -> WV032Fragment.newInstance()
            // w032_クォータニオン:VBOなし
            // OpenGL ES 2.0
            "w032" -> W032Fragment.newInstance()
            // w030_ブレンドファクター:VBOあり
            // OpenGL ES 2.0
            "wv030" -> WV030Fragment.newInstance()
            // w030_ブレンドファクター:VBOなし
            // OpenGL ES 2.0
            "w030" -> W030zFragment.newInstance()
            else -> W030zFragment.newInstance()
        }

        // 現在表示しているフラグメントをスタックから外す
        supportFragmentManager.popBackStack()
        // 選択したフラグメントを表示する
        if ( supportFragmentManager.findFragmentByTag(tag) == null ) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.flGL03, fragment, tag)
                    .commit()
        }
    }
}
