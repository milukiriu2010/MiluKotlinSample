package milu.kiriu2010.exdb1.glsl02

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.glsl02.g011.GLSL11Fragment
import milu.kiriu2010.exdb1.glsl02.g012.GLSL12Fragment
import milu.kiriu2010.exdb1.glsl02.g013.GLSL13Fragment
import milu.kiriu2010.exdb1.glsl02.g014.GLSL14Fragment
import milu.kiriu2010.exdb1.glsl02.g015.GLSL15Fragment
import milu.kiriu2010.exdb1.glsl02.g016.GLSL16Fragment
import milu.kiriu2010.exdb1.glsl02.g017.GLSL17Fragment
import milu.kiriu2010.exdb1.glsl02.g018.GLSL18Fragment
import milu.kiriu2010.exdb1.glsl02.g019.GLSL19Fragment
import milu.kiriu2010.exdb1.glsl02.g020.GLSL20Fragment

class GLSL02Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glsl02)

        supportFragmentManager.popBackStack()
        if (supportFragmentManager.findFragmentByTag("xyz") == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, GLSL20Fragment.newInstance(), "xyz")
                    .commit()
        }

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
            // レイマーチングソフトシャドウ
            R.id.glsl_g020 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("g20") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, GLSL20Fragment.newInstance(), "g20")
                            .commit()
                }
                true
            }
            // オブジェクトを２次元エフェクトやテクスチャを投影
            R.id.glsl_g019 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("g19") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, GLSL19Fragment.newInstance(), "g19")
                            .commit()
                }
                true
            }
            // オブジェクトを行列でねじる
            R.id.glsl_g018 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("g18") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, GLSL18Fragment.newInstance(), "g18")
                            .commit()
                }
                true
            }
            // オブジェクトを行列で回転
            R.id.glsl_g017 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("g17") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, GLSL17Fragment.newInstance(), "g17")
                            .commit()
                }
                true
            }
            // オブジェクトの重なりをスムースにする
            R.id.glsl_g016 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("g16") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, GLSL16Fragment.newInstance(), "g16")
                            .commit()
                }
                true
            }
            // オブジェクトの重なりを考慮してレンダリング
            R.id.glsl_g015 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("g15") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, GLSL15Fragment.newInstance(), "g15")
                            .commit()
                }
                true
            }
            // 異なる形状のオブジェクトをレンダリング
            R.id.glsl_g014 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("g14") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, GLSL14Fragment.newInstance(), "g14")
                            .commit()
                }
                true
            }
            // ボックスモデルを複製
            R.id.glsl_g013 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("g13") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, GLSL13Fragment.newInstance(), "g13")
                            .commit()
                }
                true
            }
            // オブジェクトを複製
            R.id.glsl_g012 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("g12") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, GLSL12Fragment.newInstance(), "g12")
                            .commit()
                }
                true
            }
            // レイマーチング(視野角)
            R.id.glsl_g011 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("g11") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, GLSL11Fragment.newInstance(), "g11")
                            .commit()
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}
