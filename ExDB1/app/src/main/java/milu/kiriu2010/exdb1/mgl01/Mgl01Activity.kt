package milu.kiriu2010.exdb1.mgl01

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.mgl01.rot01.Rotate01Fragment
import milu.kiriu2010.exdb1.mgl01.rot02.CubeRotate02Fragment
import milu.kiriu2010.exdb1.mgl01.vbo01.ES20VBO01Fragment
import milu.kiriu2010.exdb1.mgl01.vbo02.ES20VBO02Fragment

// -------------------------------------------
// OpenGL いろいろ実験
// -------------------------------------------
class Mgl01Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mgl01)

        // 初期表示のフラグメントを設定
        changeFragment("vbo01")

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
            // VBO(三角形(白))
            // OpenGL ES 2.0
            R.id.mgl01_vbo02 -> {
                changeFragment("vbo02")
                true
            }
            // VBO(立方体)
            // OpenGL ES 2.0
            R.id.mgl01_vbo01 -> {
                changeFragment("vbo01")
                true
            }
            // 回転(立方体)02:VBOなし
            // OpenGL ES 2.0
            R.id.mgl01_cube_rotate02 -> {
                changeFragment("rot02")
                true
            }
            // 回転(立方体)01:VBOなし
            // OpenGL ES 2.0
            R.id.mgl01_cube_rotate01 -> {
                changeFragment("rot01")
                true
            }
            else -> return super.onOptionsItemSelected(item!!)
        }
    }

    // 表示するフラグメントを切り替える
    private fun changeFragment(tag: String) {
        val fragment = when (tag) {
            // VBO(三角形(白))
            // OpenGL ES 2.0
            "vbo02" -> ES20VBO02Fragment.newInstance()
            // VBO(立方体)
            // OpenGL ES 2.0
            "vbo01" -> ES20VBO01Fragment.newInstance()
            // 回転(立方体)02:VBOなし
            // OpenGL ES 2.0
            "rot02" -> CubeRotate02Fragment.newInstance()
            // 回転(立方体)01:VBOなし
            // OpenGL ES 2.0
            "rot01" -> Rotate01Fragment.newInstance()
            // 回転(立方体)01:VBOなし
            // OpenGL ES 2.0
            else -> Rotate01Fragment.newInstance()
        }

        // 現在表示しているフラグメントをスタックから外す
        supportFragmentManager.popBackStack()
        // 選択したフラグメントを表示する
        if ( supportFragmentManager.findFragmentByTag(tag) == null ) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.flMGL01, fragment, tag)
                    .commit()
        }
    }
}
