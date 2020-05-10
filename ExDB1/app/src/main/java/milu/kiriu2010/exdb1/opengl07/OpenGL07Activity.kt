package milu.kiriu2010.exdb1.opengl07

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl07.w069.W069Fragment
import milu.kiriu2010.exdb1.opengl07.w069v.WV069Fragment
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
            // ハーフトーンシェーディング
            R.id.opengl_wv76 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv76") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL07, WV076Fragment.newInstance(), "wv76")
                            .commit()
                }
                true
            }
            // ハーフトーンシェーディング
            R.id.opengl_w076 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w076") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL07, W076Fragment.newInstance(), "w076")
                            .commit()
                }
                true
            }
            // 浮動小数点数VTF
            R.id.opengl_wv72 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv72") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL07, WV072Fragment.newInstance(), "wv72")
                            .commit()
                }
                true
            }
            // 浮動小数点数VTF
            R.id.opengl_w072 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w072") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL07, W072Fragment.newInstance(), "w072")
                            .commit()
                }
                true
            }
            // 頂点テクスチャフェッチ
            R.id.opengl_wv71 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv71") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL07, WV071Fragment.newInstance(), "wv71")
                            .commit()
                }
                true
            }
            // 頂点テクスチャフェッチ
            R.id.opengl_w071 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w071") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL07, W071Fragment.newInstance(), "w071")
                            .commit()
                }
                true
            }
            // 浮動小数点数テクスチャ
            R.id.opengl_wv70 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv70") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL07, WV070Fragment.newInstance(), "wv70")
                            .commit()
                }
                true
            }
            // 浮動小数点数テクスチャ
            R.id.opengl_w070 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w070") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL07, W070Fragment.newInstance(), "w070")
                            .commit()
                }
                true
            }
            // 正しい深度値を適用したシャドウマッピング
            R.id.opengl_wv69 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv69") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL07, WV069Fragment.newInstance(), "wv69")
                            .commit()
                }
                true
            }
            // 正しい深度値を適用したシャドウマッピング
            R.id.opengl_w069 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w069") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL07, W069Fragment.newInstance(), "w069")
                            .commit()
                }
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
            // w077_ラインシェード:VBOなし
            // OpenGL ES 2.0
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
