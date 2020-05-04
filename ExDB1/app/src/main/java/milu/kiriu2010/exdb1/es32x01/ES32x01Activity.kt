package milu.kiriu2010.exdb1.es32x01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.es32x01.rot01es32.ES32Rotate01Fragment

class ES32x01Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_es32x01)

        supportFragmentManager.popBackStack()
        if (supportFragmentManager.findFragmentByTag("xyz") == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.flES32x01, ES32Rotate01Fragment.newInstance(), "xyz")
                    .commit()
        }

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_es30x01, menu)
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
            // 回転(立方体)01_ES32
            R.id.es32_cube_rotate01 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("rot01es32") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flES32x01, ES32Rotate01Fragment.newInstance(), "rot01es32")
                            .commit()
                }
                true
            }
            else -> return super.onOptionsItemSelected(item!!)
        }
    }
}
