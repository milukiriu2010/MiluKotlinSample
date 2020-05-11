package milu.kiriu2010.exdb1.es32x01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.es32x01.rot01es32.ES32Rotate01Fragment

// -------------------------------------
// OpenGL ES 3.2サンプル
// -------------------------------------
class ES32x01Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_es32x01)

        // 初期表示のフラグメントを設定
        changeFragment("rot01es32")

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_es32x01, menu)
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
            // OpenGL ES 3.2
            R.id.es32_cube_rotate01 -> {
                changeFragment("rot01es32")
                true
            }
            else -> return super.onOptionsItemSelected(item!!)
        }
    }

    // 表示するフラグメントを切り替える
    private fun changeFragment(tag: String) {
        val fragment = when (tag) {
            // 回転(立方体)01_ES32
            // OpenGL ES 3.2
            "rot01es32" -> ES32Rotate01Fragment.newInstance()
            // 回転(立方体)01_ES32
            // OpenGL ES 3.2
            else -> ES32Rotate01Fragment.newInstance()
        }

        // 現在表示しているフラグメントをスタックから外す
        supportFragmentManager.popBackStack()
        // 選択したフラグメントを表示する
        if ( supportFragmentManager.findFragmentByTag(tag) == null ) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.flES32x01, fragment, tag)
                    .commit()
        }
    }
}
