package milu.kiriu2010.exdb1.opengl08

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl08.w081.W081Fragment
import milu.kiriu2010.exdb1.opengl08.w081v.WV081Fragment
import milu.kiriu2010.exdb1.opengl08.w086.W086Fragment
import milu.kiriu2010.exdb1.opengl08.w086v.WV086Fragment
import milu.kiriu2010.exdb1.opengl08.w087.W087Fragment
import milu.kiriu2010.exdb1.opengl08.w087v.WV087Fragment
import milu.kiriu2010.exdb1.opengl08.w089.W089Fragment
import milu.kiriu2010.exdb1.opengl08.w089v.WV089Fragment

class OpenGL08Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_gl08)

        supportFragmentManager.popBackStack()
        if (supportFragmentManager.findFragmentByTag("xyz") == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.flGL08, WV089Fragment.newInstance(), "xyz")
                    .commit()
        }

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_opengl08, menu)
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
            // スフィア環境マッピング
            R.id.opengl_wv89 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv89") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL08, WV089Fragment.newInstance(), "wv89")
                            .commit()
                }
                true
            }
            // スフィア環境マッピング
            R.id.opengl_w089 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w089") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL08, W089Fragment.newInstance(), "w089")
                            .commit()
                }
                true
            }
            // フラットシェーディング
            R.id.opengl_wv87 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv87") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL08, WV087Fragment.newInstance(), "wv87")
                            .commit()
                }
                true
            }
            // フラットシェーディング
            R.id.opengl_w087 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w087") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL08, W087Fragment.newInstance(), "w087")
                            .commit()
                }
                true
            }
            // 描画結果から色を取得
            R.id.opengl_wv86 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv86") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL08, WV086Fragment.newInstance(), "wv86")
                            .commit()
                }
                true
            }
            // 描画結果から色を取得
            R.id.opengl_w086 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w086") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL08, W086Fragment.newInstance(), "w086")
                            .commit()
                }
                true
            }
            // VBOを逐次更新
            R.id.opengl_wv81 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv81") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL08, WV081Fragment.newInstance(), "wv81")
                            .commit()
                }
                true
            }
            // VBOを逐次更新
            R.id.opengl_w081 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w081") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL08, W081Fragment.newInstance(), "w081")
                            .commit()
                }
                true
            }
            else -> return super.onOptionsItemSelected(item!!)
        }
    }
}
