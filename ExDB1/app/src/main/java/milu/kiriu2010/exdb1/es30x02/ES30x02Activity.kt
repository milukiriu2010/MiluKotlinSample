package milu.kiriu2010.exdb1.es30x02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.es30x02.a08.A08Fragment
import milu.kiriu2010.exdb1.es30x02.a09.A09Fragment

// -------------------------------------
// OpenGL ES 3.0サンプル
// -------------------------------------
// https://wgld.org/d/webgl2/w008.html
// ～
// https://wgld.org/d/webgl2/w009.html
// -------------------------------------
class ES30x02Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_es30x02)

        // 初期表示のフラグメントを設定
        changeFragment("a08")

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_es30x02, menu)
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
            // できてない
            // UBO
            // OpenGL ES 3.0
            R.id.es30_a09 -> {
                changeFragment("a09")
                true
            }
            // gl_VertexIDとgl_InstanceID
            // OpenGL ES 3.0
            R.id.es30_a08 -> {
                changeFragment("a08")
                true
            }
            else -> return super.onOptionsItemSelected(item!!)
        }
    }

    // 表示するフラグメントを切り替える
    private fun changeFragment(tag: String) {
        val fragment = when (tag) {
            // UBO
            // OpenGL ES 3.0
            "a09" -> A09Fragment.newInstance()
            // gl_VertexIDとgl_InstanceID
            // OpenGL ES 3.0
            "a08" -> A08Fragment.newInstance()
            // gl_VertexIDとgl_InstanceID
            // OpenGL ES 3.0
            else -> A08Fragment.newInstance()
        }

        // 現在表示しているフラグメントをスタックから外す
        supportFragmentManager.popBackStack()
        // 選択したフラグメントを表示する
        if ( supportFragmentManager.findFragmentByTag(tag) == null ) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.flES30x02, fragment, tag)
                    .commit()
        }
    }
}
