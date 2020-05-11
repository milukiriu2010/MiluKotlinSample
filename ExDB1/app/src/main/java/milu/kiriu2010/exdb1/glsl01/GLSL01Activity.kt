package milu.kiriu2010.exdb1.glsl01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.glsl01.g001.GLSL01Fragment
import milu.kiriu2010.exdb1.glsl01.g001v.GLSLV01Fragment
import milu.kiriu2010.exdb1.glsl01.g002.GLSL02Fragment
import milu.kiriu2010.exdb1.glsl01.g002v.GLSLV02Fragment
import milu.kiriu2010.exdb1.glsl01.g003.GLSL03Fragment
import milu.kiriu2010.exdb1.glsl01.g003v.GLSLV03Fragment
import milu.kiriu2010.exdb1.glsl01.g004.GLSL04Fragment
import milu.kiriu2010.exdb1.glsl01.g004v.GLSLV04Fragment
import milu.kiriu2010.exdb1.glsl01.g005.GLSL05Fragment
import milu.kiriu2010.exdb1.glsl01.g005v.GLSLV05Fragment
import milu.kiriu2010.exdb1.glsl01.g006.GLSL06Fragment
import milu.kiriu2010.exdb1.glsl01.g006v.GLSLV06Fragment
import milu.kiriu2010.exdb1.glsl01.g007.GLSL07Fragment
import milu.kiriu2010.exdb1.glsl01.g007v.GLSLV07Fragment
import milu.kiriu2010.exdb1.glsl01.g008.GLSL08Fragment
import milu.kiriu2010.exdb1.glsl01.g008v.GLSLV08Fragment
import milu.kiriu2010.exdb1.glsl01.g009.GLSL09Fragment
import milu.kiriu2010.exdb1.glsl01.g009v.GLSLV09Fragment
import milu.kiriu2010.exdb1.glsl01.g010.GLSL10Fragment
import milu.kiriu2010.exdb1.glsl01.g010v.GLSLV10Fragment

// -------------------------------------
// GLSL
// OpenGL ES 2.0
// -------------------------------------
// https://wgld.org/d/glsl/g001.html
// ～
// https://wgld.org/d/glsl/g010.html
// -------------------------------------
class GLSL01Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glsl01)

        // 初期表示のフラグメントを設定
        changeFragment("gv01")

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_glsl01, menu)
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
            // 球体をライティング:VBOあり
            // OpenGL ES 2.0
            R.id.glsl_gv10 -> {
                changeFragment("gv10")
                true
            }
            // 球体をライティング:VBOなし
            // OpenGL ES 2.0
            R.id.glsl_g010 -> {
                changeFragment("g10")
                true
            }
            // レイマーチングで球体:VBOあり
            // OpenGL ES 2.0
            R.id.glsl_gv09 -> {
                changeFragment("gv09")
                true
            }
            // レイマーチングで球体:VBOなし
            // OpenGL ES 2.0
            R.id.glsl_g009 -> {
                changeFragment("g09")
                true
            }
            // レイ:VBOあり
            // OpenGL ES 2.0
            R.id.glsl_gv08 -> {
                changeFragment("gv08")
                true
            }
            // レイ:VBOなし
            // OpenGL ES 2.0
            R.id.glsl_g008 -> {
                changeFragment("g08")
                true
            }
            // ノイズ:VBOあり
            // OpenGL ES 2.0
            R.id.glsl_gv07 -> {
                changeFragment("gv07")
                true
            }
            // ノイズ:VBOなし
            // OpenGL ES 2.0
            R.id.glsl_g007 -> {
                changeFragment("g07")
                true
            }
            // ジュリア集合:VBOあり
            // OpenGL ES 2.0
            R.id.glsl_gv06 -> {
                changeFragment("gv06")
                true
            }
            // ジュリア集合:VBOなし
            // OpenGL ES 2.0
            R.id.glsl_g006 -> {
                changeFragment("g06")
                true
            }
            // マンデルブロ集合:VBOあり
            // OpenGL ES 2.0
            R.id.glsl_gv05 -> {
                changeFragment("gv05")
                true
            }
            // マンデルブロ集合:VBOなし
            // OpenGL ES 2.0
            R.id.glsl_g005 -> {
                changeFragment("g05")
                true
            }
            // 様々な図形:VBOあり
            // OpenGL ES 2.0
            R.id.glsl_gv04 -> {
                changeFragment("gv04")
                true
            }
            // 様々な図形:VBOなし
            // OpenGL ES 2.0
            R.id.glsl_g004 -> {
                changeFragment("g04")
                true
            }
            // 光の玉:VBOあり
            // OpenGL ES 2.0
            R.id.glsl_gv03 -> {
                changeFragment("gv03")
                true
            }
            // 光の玉:VBOなし
            // OpenGL ES 2.0
            R.id.glsl_g003 -> {
                changeFragment("g03")
                true
            }
            // 同心円:VBOあり
            // OpenGL ES 2.0
            R.id.glsl_gv02 -> {
                changeFragment("gv02")
                true
            }
            // 同心円:VBOなし
            // OpenGL ES 2.0
            R.id.glsl_g002 -> {
                changeFragment("g02")
                true
            }
            // GLSLだけでレンダリング:VBOあり
            // OpenGL ES 2.0
            R.id.glsl_gv01 -> {
                changeFragment("gv01")
                true
            }
            // GLSLだけでレンダリング:VBOなし
            // OpenGL ES 2.0
            R.id.glsl_g001 -> {
                changeFragment("g01")
                true
            }
            else -> return super.onOptionsItemSelected(item!!)
        }
    }

    // 表示するフラグメントを切り替える
    private fun changeFragment(tag: String) {
        val fragment = when (tag) {
            // 球体をライティング:VBOあり
            // OpenGL ES 2.0
            "gv10" -> GLSLV10Fragment.newInstance()
            // 球体をライティング:VBOなし
            // OpenGL ES 2.0
            "g10" -> GLSL10Fragment.newInstance()
            // レイマーチングで球体:VBOなし
            // OpenGL ES 2.0
            "gv09" -> GLSLV09Fragment.newInstance()
            // レイマーチングで球体:VBOなし
            // OpenGL ES 2.0
            "g09" -> GLSL09Fragment.newInstance()
            // レイ:VBOあり
            // OpenGL ES 2.0
            "gv08" -> GLSLV08Fragment.newInstance()
            // レイ:VBOなし
            // OpenGL ES 2.0
            "g08" -> GLSL08Fragment.newInstance()
            // ノイズ:VBOあり
            // OpenGL ES 2.0
            "gv07" -> GLSLV07Fragment.newInstance()
            // ノイズ:VBOなし
            // OpenGL ES 2.0
            "g07" -> GLSL07Fragment.newInstance()
            // ジュリア集合:VBOあり
            // OpenGL ES 2.0
            "gv06" -> GLSLV06Fragment.newInstance()
            // ジュリア集合:VBOなし
            // OpenGL ES 2.0
            "g06" -> GLSL06Fragment.newInstance()
            // マンデルブロ集合:VBOあり
            // OpenGL ES 2.0
            "gv05" -> GLSLV05Fragment.newInstance()
            // マンデルブロ集合:VBOなし
            // OpenGL ES 2.0
            "g05" -> GLSL05Fragment.newInstance()
            // 様々な図形:VBOあり
            // OpenGL ES 2.0
            "gv04" -> GLSLV04Fragment.newInstance()
            // 様々な図形:VBOなし
            // OpenGL ES 2.0
            "g04" -> GLSL04Fragment.newInstance()
            // 光の玉:VBOあり
            // OpenGL ES 2.0
            "gv03" -> GLSLV03Fragment.newInstance()
            // 光の玉:VBOなし
            // OpenGL ES 2.0
            "g03" -> GLSL03Fragment.newInstance()
            // 同心円:VBOあり
            // OpenGL ES 2.0
            "gv02" -> GLSLV02Fragment.newInstance()
            // 同心円:VBOなし
            // OpenGL ES 2.0
            "g02" -> GLSL02Fragment.newInstance()
            // GLSLのみでレンダリング:VBOあり
            // OpenGL ES 2.0
            "gv01" -> GLSLV01Fragment.newInstance()
            // GLSLのみでレンダリング:VBOなし
            // OpenGL ES 2.0
            "g01" -> GLSL01Fragment.newInstance()
            // GLSLのみでレンダリング:VBOなし
            // OpenGL ES 2.0
            else -> GLSL01Fragment.newInstance()
        }

        // 現在表示しているフラグメントをスタックから外す
        supportFragmentManager.popBackStack()
        // 選択したフラグメントを表示する
        if ( supportFragmentManager.findFragmentByTag(tag) == null ) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.flGLSL01, fragment, tag)
                    .commit()
        }
    }
}
