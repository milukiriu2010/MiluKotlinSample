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

// https://wgld.org/sitemap.html
// w80以降
class OpenGL08Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_gl08)

        // 初期表示のフラグメントを設定
        changeFragment("wv81")

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
            // スフィア環境マッピング:VBO未使用
            R.id.opengl_wv89 -> {
                changeFragment("wv89")
                true
            }
            // スフィア環境マッピング:VBO未使用
            R.id.opengl_w089 -> {
                changeFragment("w089")
                true
            }
            // フラットシェーディング:VBO使用
            R.id.opengl_wv87 -> {
                changeFragment("wv87")
                true
            }
            // フラットシェーディング:VBO未使用
            R.id.opengl_w087 -> {
                changeFragment("w087")
                true
            }
            // 描画結果から色を取得:VBO使用
            R.id.opengl_wv86 -> {
                changeFragment("wv86")
                true
            }
            // 描画結果から色を取得:VBO未使用
            R.id.opengl_w086 -> {
                changeFragment("w086")
                true
            }
            // VBOを逐次更新:VBO使用
            R.id.opengl_wv81 -> {
                changeFragment("wv81")
                true
            }
            // VBOを逐次更新:VBO未使用
            R.id.opengl_w081 -> {
                changeFragment("w081")
                true
            }
            else -> return super.onOptionsItemSelected(item!!)
        }
    }

    // 表示するフラグメントを切り替える
    private fun changeFragment(tag: String) {
        val fragment = when (tag) {
            // VBOを逐次更新:VBO未使用
            "w081" -> W081Fragment.newInstance()
            // VBOを逐次更新:VBO使用
            "wv81" -> WV081Fragment.newInstance()
            // 描画結果から色を取得:VBO未使用
            "w086" -> W086Fragment.newInstance()
            // 描画結果から色を取得:VBO使用
            "wv86" -> WV086Fragment.newInstance()
            // フラットシェーディング:VBO未使用
            "w087" -> W087Fragment.newInstance()
            // フラットシェーディング:VBO使用
            "wv87" -> WV087Fragment.newInstance()
            // スフィア環境マッピング:VBO未使用
            "w089" -> W089Fragment.newInstance()
            // スフィア環境マッピング:VBO使用
            "wv89" -> WV089Fragment.newInstance()
            // VBOを逐次更新:VBO使用
            else -> WV081Fragment.newInstance()
        }

        // 現在表示しているフラグメントをスタックから外す
        supportFragmentManager.popBackStack()
        // 選択したフラグメントを表示する
        if ( supportFragmentManager.findFragmentByTag(tag) == null ) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.flGL08, fragment, tag)
                    .commit()
        }
    }
}
