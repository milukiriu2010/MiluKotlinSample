package milu.kiriu2010.exdb1.opengl07

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl07.w069.W069Fragment
import milu.kiriu2010.exdb1.opengl07.w070.W070Fragment
import milu.kiriu2010.exdb1.opengl07.w071.W071Fragment
import milu.kiriu2010.exdb1.opengl07.w072.W072Fragment
import milu.kiriu2010.exdb1.opengl07.w076.W076Fragment
import milu.kiriu2010.exdb1.opengl07.w077.W077Fragment

class OpenGL07Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_gl07)

        supportFragmentManager.popBackStack()
        if (supportFragmentManager.findFragmentByTag("xyz") == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, W077Fragment.newInstance(), "xyz")
                    .commit()
        }

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
            // ラインシェード
            R.id.opengl_w077 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w077") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W077Fragment.newInstance(), "w077")
                            .commit()
                }
                true
            }
            // ハーフトーンシェーディング
            R.id.opengl_w076 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w076") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W076Fragment.newInstance(), "w076")
                            .commit()
                }
                true
            }
            // 浮動小数点数VTF
            R.id.opengl_w072 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w072") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W072Fragment.newInstance(), "w072")
                            .commit()
                }
                true
            }
            // 頂点テクスチャフェッチ
            R.id.opengl_w071 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w071") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W071Fragment.newInstance(), "w071")
                            .commit()
                }
                true
            }
            // 浮動小数点数テクスチャ
            R.id.opengl_w070 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w070") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W070Fragment.newInstance(), "w070")
                            .commit()
                }
                true
            }
            // 正しい深度値を適用したシャドウマッピング
            R.id.opengl_w069 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w069") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W069Fragment.newInstance(), "w069")
                            .commit()
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
