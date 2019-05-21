package milu.kiriu2010.exdb1.opengl01

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl01.begin00.OpenGL10Fragment
import milu.kiriu2010.exdb1.opengl01.w018.W018Fragment
import milu.kiriu2010.exdb1.opengl01.w019.W019Fragment
import milu.kiriu2010.exdb1.opengl01.w020.W020Fragment
import milu.kiriu2010.exdb1.opengl01.w023.W023Fragment
import milu.kiriu2010.exdb1.opengl01.w024.W024Fragment
import milu.kiriu2010.exdb1.opengl01.w015.W015Fragment
import milu.kiriu2010.exdb1.opengl01.w016.W016Fragment
import milu.kiriu2010.exdb1.opengl01.w017.W017Fragment
import milu.kiriu2010.exdb1.opengl01.w021.W021Fragment
import milu.kiriu2010.exdb1.opengl01.w022.W022Fragment
import milu.kiriu2010.exdb1.opengl01.w025.W025Fragment

class OpenGL01Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_gl01)

        supportFragmentManager.popBackStack()
        if (supportFragmentManager.findFragmentByTag("xyz") == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, W025Fragment.newInstance(), "xyz")
                    .commit()
        }

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_opengl01, menu)
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
            // 点光源
            R.id.opengl_w025 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w25") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W025Fragment.newInstance(), "w25")
                            .commit()
                }
                true
            }
            // フォンシェーディング
            R.id.opengl_w024 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w24") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W024Fragment.newInstance(), "w24")
                            .commit()
                }
                true
            }
            // 反射光
            R.id.opengl_w023 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w23") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W023Fragment.newInstance(), "w23")
                            .commit()
                }
                true
            }
            // 環境光
            R.id.opengl_w022 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w22") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W022Fragment.newInstance(), "w22")
                            .commit()
                }
                true
            }
            // 平行光源
            R.id.opengl_w021 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w21") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W021Fragment.newInstance(), "w21")
                            .commit()
                }
                true
            }
            // w020_トーラス
            R.id.opengl_w020 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w20") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W020Fragment.newInstance(), "w20")
                            .commit()
                }
                true
            }
            // w019_カリングと深度テスト
            R.id.opengl_w019 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w19") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W019Fragment.newInstance(), "w19")
                            .commit()
                }
                true
            }
            // w018_インデックスバッファ
            R.id.opengl_w018 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w18") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W018Fragment.newInstance(), "w18")
                            .commit()
                }
                true
            }
            // w17_移動・回転・拡大/縮小
            R.id.opengl_w017 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w17") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W017Fragment.newInstance(), "w17")
                            .commit()
                }
                true
            }
            // w016_複数モデルレンダリング
            R.id.opengl_w016 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w16") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W016Fragment.newInstance(), "w16")
                            .commit()
                }
                true
            }
            // w015_ポリゴンに色を塗る
            R.id.opengl_w015 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w15") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W015Fragment.newInstance(), "w15")
                            .commit()
                }
                true
            }
            // OpenGL 1.0
            R.id.opengl_begin00 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("OpenGL1.0") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, OpenGL10Fragment.newInstance(), "OpenGL1.0")
                            .commit()
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
