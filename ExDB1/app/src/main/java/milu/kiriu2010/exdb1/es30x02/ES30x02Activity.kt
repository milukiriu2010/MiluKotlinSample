package milu.kiriu2010.exdb1.es30x02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.es30x02.a08.A08Fragment
import milu.kiriu2010.exdb1.es30x02.a09.A09Fragment

class ES30x02Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_es30x02)

        supportFragmentManager.popBackStack()
        if (supportFragmentManager.findFragmentByTag("xyz") == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, A09Fragment.newInstance(), "xyz")
                    .commit()
        }

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
            // UBO
            R.id.es30_a09 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("a09") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, A09Fragment.newInstance(), "a09")
                            .commit()
                }
                true
            }
            // gl_VertexIDとgl_InstanceID
            R.id.es30_a08 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("a08") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, A08Fragment.newInstance(), "a08")
                            .commit()
                }
                true
            }
            else -> return super.onOptionsItemSelected(item!!)
        }
    }
}
