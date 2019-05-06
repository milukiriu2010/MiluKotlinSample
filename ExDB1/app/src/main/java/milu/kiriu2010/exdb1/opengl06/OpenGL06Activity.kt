package milu.kiriu2010.exdb1.opengl06

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl06.w058.W058Fragment
import milu.kiriu2010.exdb1.opengl06.w059.W059Fragment
import milu.kiriu2010.exdb1.opengl06.w060.W060Fragment
import milu.kiriu2010.exdb1.opengl06.w061.W061Fragment

class OpenGL06Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_gl06)

        supportFragmentManager.popBackStack()
        if (supportFragmentManager.findFragmentByTag("w061") == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, W061Fragment.newInstance(), "w061")
                    .commit()
        }

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
            // パーティクルフォグ
            R.id.opengl_w061 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w061") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W061Fragment.newInstance(), "w061")
                            .commit()
                }
                true
            }
            // フォグ距離
            R.id.opengl_w060 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w060") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W060Fragment.newInstance(), "w060")
                            .commit()
                }
                true
            }
            // 被写界深度
            R.id.opengl_w059 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w059") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W059Fragment.newInstance(), "w059")
                            .commit()
                }
                true
            }
            // グレアフィルタ
            R.id.opengl_w058 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w058") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W058Fragment.newInstance(), "w058")
                            .commit()
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}
