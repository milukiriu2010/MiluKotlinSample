package milu.kiriu2010.exdb1.mgl01

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.mgl01.qtn01.Qtn01Fragment
import milu.kiriu2010.exdb1.mgl01.rot01.Rotate01Fragment
import milu.kiriu2010.exdb1.mgl01.rot02.CubeRotate02Fragment

class Mgl01Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mgl01)

        supportFragmentManager.popBackStack()
        if (supportFragmentManager.findFragmentByTag("rot02") == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, CubeRotate02Fragment.newInstance(), "rot02")
                    .commit()
        }

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_mgl01, menu)
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
            // 回転(立方体)02
            R.id.mgl01_cube_rotate02 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("rot02") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, CubeRotate02Fragment.newInstance(), "rot02")
                            .commit()
                }
                true
            }
            // 回転(立方体)01
            R.id.mgl01_cube_rotate01 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("rot01") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Rotate01Fragment.newInstance(), "rot01")
                            .commit()
                }
                true
            }
            // クォータニオン
            R.id.mgl01_qtn01 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("qtn01") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Qtn01Fragment.newInstance(), "qtn01")
                            .commit()
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
