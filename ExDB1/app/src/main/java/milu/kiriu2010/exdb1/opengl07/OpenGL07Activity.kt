package milu.kiriu2010.exdb1.opengl07

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl06.w069.W069Fragment
import milu.kiriu2010.exdb1.opengl06.w069v.WV069Fragment
import milu.kiriu2010.exdb1.opengl07.w070.W070Fragment
import milu.kiriu2010.exdb1.opengl07.w070v.WV070Fragment
import milu.kiriu2010.exdb1.opengl07.w071.W071Fragment
import milu.kiriu2010.exdb1.opengl07.w071v.WV071Fragment
import milu.kiriu2010.exdb1.opengl07.w072.W072Fragment
import milu.kiriu2010.exdb1.opengl07.w072v.WV072Fragment
import milu.kiriu2010.exdb1.opengl07.w076.W076Fragment
import milu.kiriu2010.exdb1.opengl07.w076v.WV076Fragment
import milu.kiriu2010.exdb1.opengl07.w077.W077Fragment
import milu.kiriu2010.exdb1.opengl07.w077v.WV077Fragment

// -------------------------------------
// OpenGL ES 2.0サンプル
// -------------------------------------
// https://wgld.org/d/webgl/w070.html
// ～
// https://wgld.org/d/webgl/w079.html
// -------------------------------------
class OpenGL07Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_gl07)

        // 初期表示のフラグメントを設定
        changeFragment("wv077")

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_opengl07, menu)
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
            // w077_ラインシェード:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv77 -> {
                changeFragment("wv077")
                true
            }
            // w077_ラインシェード:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w077 -> {
                changeFragment("w077")
                true
            }
            // ハーフトーンシェーディング:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv76 -> {
                changeFragment("wv076")
                true
            }
            // ハーフトーンシェーディング:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w076 -> {
                changeFragment("w076")
                true
            }
            // 浮動小数点数VTF:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv72 -> {
                changeFragment("wv072")
                true
            }
            // 浮動小数点数VTF:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w072 -> {
                changeFragment("w072")
                true
            }
            // 頂点テクスチャフェッチ:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv71 -> {
                changeFragment("wv071")
                true
            }
            // 頂点テクスチャフェッチ:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w071 -> {
                changeFragment("w071")
                true
            }
            // 浮動小数点数テクスチャ:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv70 -> {
                changeFragment("wv070")
                true
            }
            // 浮動小数点数テクスチャ:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w070 -> {
                changeFragment("w070")
                true
            }
            else -> return super.onOptionsItemSelected(item!!)
        }
    }

    // 表示するフラグメントを切り替える
    private fun changeFragment(tag: String) {
        val fragment = when (tag) {
            // w077_ラインシェード:VBOあり
            // OpenGL ES 2.0
            "wv077" -> WV077Fragment.newInstance()
            // w077_ラインシェード:VBOなし
            // OpenGL ES 2.0
            "w077" -> W077Fragment.newInstance()
            // ハーフトーンシェーディング:VBOあり
            // OpenGL ES 2.0
            "wv076" -> WV076Fragment.newInstance()
            // ハーフトーンシェーディング:VBOなし
            // OpenGL ES 2.0
            "w076" -> W076Fragment.newInstance()
            // 浮動小数点数VTF:VBOあり
            // OpenGL ES 2.0
            "wv072" -> WV072Fragment.newInstance()
            // 浮動小数点数VTF:VBOなし
            // OpenGL ES 2.0
            "w072" -> W072Fragment.newInstance()
            // 頂点テクスチャフェッチ:VBOあり
            // OpenGL ES 2.0
            "wv071" -> WV071Fragment.newInstance()
            // 頂点テクスチャフェッチ:VBOなし
            // OpenGL ES 2.0
            "w071" -> W071Fragment.newInstance()
            // 浮動小数点数テクスチャ:VBOあり
            // OpenGL ES 2.0
            "wv070" -> WV070Fragment.newInstance()
            // 浮動小数点数テクスチャ:VBOなし
            // OpenGL ES 2.0
            "w070" -> W070Fragment.newInstance()
            else -> W077Fragment.newInstance()
        }

        // 現在表示しているフラグメントをスタックから外す
        supportFragmentManager.popBackStack()
        // 選択したフラグメントを表示する
        if ( supportFragmentManager.findFragmentByTag(tag) == null ) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.flGL07, fragment, tag)
                    .commit()
        }
    }
}
