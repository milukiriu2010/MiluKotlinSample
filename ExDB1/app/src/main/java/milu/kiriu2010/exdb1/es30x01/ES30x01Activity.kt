package milu.kiriu2010.exdb1.es30x01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.es30x01.rot01es30.ES30Rotate01Fragment
import milu.kiriu2010.exdb1.es30x01.a03.A03Fragment
import milu.kiriu2010.exdb1.es30x01.a03v.AV03Fragment
import milu.kiriu2010.exdb1.es30x01.a04.A04Fragment
import milu.kiriu2010.exdb1.es30x01.a04v.AV04Fragment
import milu.kiriu2010.exdb1.es30x01.a05.A05Fragment
import milu.kiriu2010.exdb1.es30x01.a05v.AV05Fragment
import milu.kiriu2010.exdb1.es30x01.a06.A06Fragment
import milu.kiriu2010.exdb1.es30x01.a07.A07Fragment

// -------------------------------------
// OpenGL ES 3.0サンプル
// -------------------------------------
// https://wgld.org/d/webgl2/w003.html
// ～
// https://wgld.org/d/webgl2/w007.html
// -------------------------------------
class ES30x01Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_es30x01)

        // 初期表示のフラグメントを設定
        changeFragment("av03")

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
            // インスタンシング
            // OpenGL ES 3.0
            R.id.es30_a07 -> {
                changeFragment("a07")
                true
            }
            // VAO
            // OpenGL ES 3.0
            R.id.es30_a06 -> {
                changeFragment("a06")
                true
            }
            // flat補間:VBOあり
            // OpenGL ES 3.0
            R.id.es30_av05 -> {
                changeFragment("av05")
                true
            }
            // flat補間:VBOなし
            // OpenGL ES 3.0
            R.id.es30_a05 -> {
                changeFragment("a05")
                true
            }
            // GLSL ES 3.0 layout:VBOあり
            // OpenGL ES 3.0
            R.id.es30_av04 -> {
                changeFragment("av04")
                true
            }
            // GLSL ES 3.0 layout:VBOなし
            // OpenGL ES 3.0
            R.id.es30_a04 -> {
                changeFragment("a04")
                true
            }
            // GLSL ES 3.0:VBOあり
            // OpenGL ES 3.0
            R.id.es30_av03 -> {
                changeFragment("av03")
                true
            }
            // GLSL ES 3.0:VBOなし
            // OpenGL ES 3.0
            R.id.es30_a03 -> {
                changeFragment("a03")
                true
            }
            // 回転(立方体)01_ES30:VBOなし
            // OpenGL ES 3.0
            R.id.es30_cube_rotate01 -> {
                changeFragment("rot01es30")
                true
            }
            else -> return super.onOptionsItemSelected(item!!)
        }
    }

    // 表示するフラグメントを切り替える
    private fun changeFragment(tag: String) {
        val fragment = when (tag) {
            // インスタンシング
            // OpenGL ES 3.0
            "a07" -> A07Fragment.newInstance()
            // VAO
            // OpenGL ES 3.0
            "a06" -> A06Fragment.newInstance()
            // flat補間:VBOあり
            // OpenGL ES 3.0
            "av05" -> AV05Fragment.newInstance()
            // flat補間:VBOなし
            // OpenGL ES 3.0
            "a05" -> A05Fragment.newInstance()
            // GLSL ES 3.0 layout:VBOあり
            // OpenGL ES 3.0
            "av04" -> AV04Fragment.newInstance()
            // GLSL ES 3.0 layout:VBOなし
            // OpenGL ES 3.0
            "a04" -> A04Fragment.newInstance()
            // GLSL ES 3.0:VBOあり
            // OpenGL ES 3.0
            "av03" -> AV03Fragment.newInstance()
            // GLSL ES 3.0:VBOなし
            // OpenGL ES 3.0
            "a03" -> A03Fragment.newInstance()
            // 回転(立方体)01_ES30:VBOなし
            // OpenGL ES 3.0
            "rot01es30" -> ES30Rotate01Fragment.newInstance()
            // GLSL ES 3.0:VBOなし
            // OpenGL ES 3.0
            else -> A03Fragment.newInstance()
        }

        // 現在表示しているフラグメントをスタックから外す
        supportFragmentManager.popBackStack()
        // 選択したフラグメントを表示する
        if ( supportFragmentManager.findFragmentByTag(tag) == null ) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.flES30x01, fragment, tag)
                    .commit()
        }
    }
}
