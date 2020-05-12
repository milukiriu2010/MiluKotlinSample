package milu.kiriu2010.exdb1.glsl02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.glsl02.g011.GLSL11Fragment
import milu.kiriu2010.exdb1.glsl02.g011v.GLSLV11Fragment
import milu.kiriu2010.exdb1.glsl02.g012.GLSL12Fragment
import milu.kiriu2010.exdb1.glsl02.g012v.GLSLV12Fragment
import milu.kiriu2010.exdb1.glsl02.g013.GLSL13Fragment
import milu.kiriu2010.exdb1.glsl02.g013v.GLSLV13Fragment
import milu.kiriu2010.exdb1.glsl02.g014.GLSL14Fragment
import milu.kiriu2010.exdb1.glsl02.g014v.GLSLV14Fragment
import milu.kiriu2010.exdb1.glsl02.g015.GLSL15Fragment
import milu.kiriu2010.exdb1.glsl02.g015v.GLSLV15Fragment
import milu.kiriu2010.exdb1.glsl02.g016.GLSL16Fragment
import milu.kiriu2010.exdb1.glsl02.g016v.GLSLV16Fragment
import milu.kiriu2010.exdb1.glsl02.g017.GLSL17Fragment
import milu.kiriu2010.exdb1.glsl02.g017v.GLSLV17Fragment
import milu.kiriu2010.exdb1.glsl02.g018.GLSL18Fragment
import milu.kiriu2010.exdb1.glsl02.g018v.GLSLV18Fragment
import milu.kiriu2010.exdb1.glsl02.g019.GLSL19Fragment
import milu.kiriu2010.exdb1.glsl02.g019v.GLSLV19Fragment
import milu.kiriu2010.exdb1.glsl02.g020.GLSL20Fragment
import milu.kiriu2010.exdb1.glsl02.g020v.GLSLV20Fragment

// -------------------------------------
// ほとんど表示されない
// -------------------------------------
// GLSL
// OpenGL ES 2.0
// -------------------------------------
// https://wgld.org/d/glsl/g011.html
// ～
// https://wgld.org/d/glsl/g020.html
// -------------------------------------
class GLSL02Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glsl02)

        // 初期表示のフラグメントを設定
        changeFragment("gv11")

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_glsl02, menu)
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
            // レイマーチングソフトシャドウ:VBOあり
            // OpenGL ES 2.0
            R.id.glsl_gv20 -> {
                changeFragment("gv20")
                true
            }
            // レイマーチングソフトシャドウ:VBOなし
            // OpenGL ES 2.0
            R.id.glsl_g020 -> {
                changeFragment("g20")
                true
            }
            // オブジェクトを２次元エフェクトやテクスチャを投影:VBOあり
            // OpenGL ES 2.0
            R.id.glsl_gv19 -> {
                changeFragment("gv19")
                true
            }
            // オブジェクトを２次元エフェクトやテクスチャを投影:VBOなし
            // OpenGL ES 2.0
            R.id.glsl_g019 -> {
                changeFragment("g19")
                true
            }
            // オブジェクトを行列でねじる:VBOあり
            // OpenGL ES 2.0
            R.id.glsl_gv18 -> {
                changeFragment("gv18")
                true
            }
            // オブジェクトを行列でねじる:VBOなし
            // OpenGL ES 2.0
            R.id.glsl_g018 -> {
                changeFragment("g18")
                true
            }
            // オブジェクトを行列で回転:VBOあり
            // OpenGL ES 2.0
            R.id.glsl_gv17 -> {
                changeFragment("gv17")
                true
            }
            // オブジェクトを行列で回転:VBOなし
            // OpenGL ES 2.0
            R.id.glsl_g017 -> {
                changeFragment("g17")
                true
            }
            // オブジェクトの重なりをスムースにする:VBOあり
            // OpenGL ES 2.0
            R.id.glsl_gv16 -> {
                changeFragment("gv16")
                true
            }
            // オブジェクトの重なりをスムースにする:VBOなし
            // OpenGL ES 2.0
            R.id.glsl_g016 -> {
                changeFragment("g16")
                true
            }
            // オブジェクトの重なりを考慮してレンダリング:VBOあり
            // OpenGL ES 2.0
            R.id.glsl_gv15 -> {
                changeFragment("gv15")
                true
            }
            // オブジェクトの重なりを考慮してレンダリング:VBOなし
            // OpenGL ES 2.0
            R.id.glsl_g015 -> {
                changeFragment("g15")
                true
            }
            // 異なる形状のオブジェクトをレンダリング:VBOあり
            // OpenGL ES 2.0
            R.id.glsl_gv14 -> {
                changeFragment("gv14")
                true
            }
            // 異なる形状のオブジェクトをレンダリング:VBOなし
            // OpenGL ES 2.0
            R.id.glsl_g014 -> {
                changeFragment("g14")
                true
            }
            // ボックスモデルを複製:VBOあり
            // OpenGL ES 2.0
            R.id.glsl_gv13 -> {
                changeFragment("gv13")
                true
            }
            // ボックスモデルを複製:VBOなし
            // OpenGL ES 2.0
            R.id.glsl_g013 -> {
                changeFragment("g13")
                true
            }
            // オブジェクトを複製:VBOあり
            // OpenGL ES 2.0
            R.id.glsl_gv12 -> {
                changeFragment("gv12")
                true
            }
            // オブジェクトを複製:VBOなし
            // OpenGL ES 2.0
            R.id.glsl_g012 -> {
                changeFragment("g12")
                true
            }
            // レイマーチング(視野角):VBOあり
            // OpenGL ES 2.0
            R.id.glsl_gv11 -> {
                changeFragment("gv11")
                true
            }
            // レイマーチング(視野角):VBOなし
            // OpenGL ES 2.0
            R.id.glsl_g011 -> {
                changeFragment("g11")
                true
            }
            else -> return super.onOptionsItemSelected(item!!)
        }
    }

    // 表示するフラグメントを切り替える
    private fun changeFragment(tag: String) {
        val fragment = when (tag) {
            // レイマーチングソフトシャドウ:VBOあり
            // OpenGL ES 2.0
            "gv20" -> GLSLV20Fragment.newInstance()
            // レイマーチングソフトシャドウ:VBOなし
            // OpenGL ES 2.0
            "g20" -> GLSL20Fragment.newInstance()
            // オブジェクトを２次元エフェクトやテクスチャを投影:VBOなし
            // OpenGL ES 2.0
            "gv19" -> GLSLV19Fragment.newInstance()
            // オブジェクトを２次元エフェクトやテクスチャを投影:VBOなし
            // OpenGL ES 2.0
            "g19" -> GLSL19Fragment.newInstance()
            // オブジェクトを行列でねじる:VBOあり
            // OpenGL ES 2.0
            "gv18" -> GLSLV18Fragment.newInstance()
            // オブジェクトを行列でねじる:VBOなし
            // OpenGL ES 2.0
            "g18" -> GLSL18Fragment.newInstance()
            // オブジェクトを行列で回転:VBOあり
            // OpenGL ES 2.0
            "gv17" -> GLSLV17Fragment.newInstance()
            // オブジェクトを行列で回転:VBOあり
            // OpenGL ES 2.0
            "g17" -> GLSL17Fragment.newInstance()
            // オブジェクトの重なりをスムースにする:VBOなし
            // OpenGL ES 2.0
            "gv16" -> GLSLV16Fragment.newInstance()
            // オブジェクトの重なりをスムースにする:VBOなし
            // OpenGL ES 2.0
            "g16" -> GLSL16Fragment.newInstance()
            // オブジェクトの重なりを考慮してレンダリング:VBOあり
            // OpenGL ES 2.0
            "gv15" -> GLSLV15Fragment.newInstance()
            // オブジェクトの重なりを考慮してレンダリング:VBOなし
            // OpenGL ES 2.0
            "g15" -> GLSL15Fragment.newInstance()
            // 異なる形状のオブジェクトをレンダリング:VBOあり
            // OpenGL ES 2.0
            "gv14" -> GLSLV14Fragment.newInstance()
            // 異なる形状のオブジェクトをレンダリング:VBOあり
            // OpenGL ES 2.0
            "g14" -> GLSL14Fragment.newInstance()
            // ボックスモデルを複製:VBOあり
            // OpenGL ES 2.0
            "gv13" -> GLSLV13Fragment.newInstance()
            // ボックスモデルを複製:VBOなし
            // OpenGL ES 2.0
            "g13" -> GLSL13Fragment.newInstance()
            // オブジェクトを複製:VBOあり
            // OpenGL ES 2.0
            "gv12" -> GLSLV12Fragment.newInstance()
            // オブジェクトを複製:VBOあり
            // OpenGL ES 2.0
            "g12" -> GLSL12Fragment.newInstance()
            // レイマーチング(視野角):VBOあり
            // OpenGL ES 2.0
            "gv11" -> GLSLV11Fragment.newInstance()
            // レイマーチング(視野角):VBOなし
            // OpenGL ES 2.0
            "g11" -> GLSL11Fragment.newInstance()
            // レイマーチング(視野角):VBOなし
            // OpenGL ES 2.0
            else -> GLSL11Fragment.newInstance()
        }

        // 現在表示しているフラグメントをスタックから外す
        supportFragmentManager.popBackStack()
        // 選択したフラグメントを表示する
        if ( supportFragmentManager.findFragmentByTag(tag) == null ) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.flGLSL02, fragment, tag)
                    .commit()
        }
    }
}
