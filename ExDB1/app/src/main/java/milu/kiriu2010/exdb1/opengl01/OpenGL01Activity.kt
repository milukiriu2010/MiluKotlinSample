package milu.kiriu2010.exdb1.opengl01

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
import milu.kiriu2010.exdb1.opengl01.w015v.WV015Fragment
import milu.kiriu2010.exdb1.opengl01.w016.W016Fragment
import milu.kiriu2010.exdb1.opengl01.w016v.WV016Fragment
import milu.kiriu2010.exdb1.opengl01.w017.W017Fragment
import milu.kiriu2010.exdb1.opengl01.w017v.WV017Fragment
import milu.kiriu2010.exdb1.opengl01.w018v.WV018Fragment
import milu.kiriu2010.exdb1.opengl01.w019v.WV019Fragment
import milu.kiriu2010.exdb1.opengl01.w020v.WV020Fragment
import milu.kiriu2010.exdb1.opengl01.w021.W021Fragment
import milu.kiriu2010.exdb1.opengl01.w021v.WV021Fragment
import milu.kiriu2010.exdb1.opengl01.w022.W022Fragment
import milu.kiriu2010.exdb1.opengl01.w022v.WV022Fragment
import milu.kiriu2010.exdb1.opengl01.w023v.WV023Fragment
import milu.kiriu2010.exdb1.opengl01.w024v.WV024Fragment
import milu.kiriu2010.exdb1.opengl01.w025.W025Fragment
import milu.kiriu2010.exdb1.opengl01.w025v.WV025Fragment

class OpenGL01Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_gl01)

        // 初期表示のフラグメントを設定
        changeFragment("wv019")

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
            R.id.opengl_wv025 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv25") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL01, WV025Fragment.newInstance(), "wv25")
                            .commit()
                }
                true
            }
            // 点光源
            R.id.opengl_w025 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w25") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL01, W025Fragment.newInstance(), "w25")
                            .commit()
                }
                true
            }
            // フォンシェーディング
            R.id.opengl_wv024 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv24") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL01, WV024Fragment.newInstance(), "wv24")
                            .commit()
                }
                true
            }
            // フォンシェーディング
            R.id.opengl_w024 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w24") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL01, W024Fragment.newInstance(), "w24")
                            .commit()
                }
                true
            }
            // 反射光
            R.id.opengl_wv023 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv23") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL01, WV023Fragment.newInstance(), "wv23")
                            .commit()
                }
                true
            }
            // 反射光
            R.id.opengl_w023 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w23") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL01, W023Fragment.newInstance(), "w23")
                            .commit()
                }
                true
            }
            // 環境光
            R.id.opengl_wv022 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv22") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL01, WV022Fragment.newInstance(), "wv22")
                            .commit()
                }
                true
            }
            // 環境光
            R.id.opengl_w022 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w22") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL01, W022Fragment.newInstance(), "w22")
                            .commit()
                }
                true
            }
            // 平行光源
            R.id.opengl_wv021 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv21") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL01, WV021Fragment.newInstance(), "wv21")
                            .commit()
                }
                true
            }
            // 平行光源
            R.id.opengl_w021 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w21") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL01, W021Fragment.newInstance(), "w21")
                            .commit()
                }
                true
            }
            // wv020_トーラス
            R.id.opengl_wv020 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv20") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL01, WV020Fragment.newInstance(), "wv20")
                            .commit()
                }
                true
            }
            // w020_トーラス
            R.id.opengl_w020 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w20") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL01, W020Fragment.newInstance(), "w20")
                            .commit()
                }
                true
            }
            // w019_カリングと深度テスト:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv019 -> {
                changeFragment("wv019")
                true
            }
            // w019_カリングと深度テスト:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w019 -> {
                changeFragment("w019")
                true
            }
            // w018_インデックスバッファ:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv018 -> {
                changeFragment("wv018")
                true
            }
            // w018_インデックスバッファ:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w018 -> {
                changeFragment("w018")
                true
            }
            // w017_移動・回転・拡大/縮小:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv017 -> {
                changeFragment("wv017")
                true
            }
            // w017_移動・回転・拡大/縮小:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w017 -> {
                changeFragment("w017")
                true
            }
            // w016_複数モデルレンダリング:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv016 -> {
                changeFragment("wv016")
                true
            }
            // w016_複数モデルレンダリング:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w016 -> {
                changeFragment("w016")
                true
            }
            // w015_ポリゴンに色を塗る:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv015 -> {
                changeFragment("wv015")
                true
            }
            // w015_ポリゴンに色を塗る:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w015 -> {
                changeFragment("w015")
                true
            }
            // OpenGL 1.0で描画
            R.id.opengl_begin00 -> {
                changeFragment("OpenGL1.0")
                true
            }
            else -> return super.onOptionsItemSelected(item!!)
        }
    }

    // 表示するフラグメントを切り替える
    private fun changeFragment(tag: String) {
        val fragment = when (tag) {
            // w019_カリングと深度テスト:VBOあり
            // OpenGL ES 2.0
            "wv019" -> WV019Fragment.newInstance()
            // w019_カリングと深度テスト:VBOなし
            // OpenGL ES 2.0
            "w019" -> W019Fragment.newInstance()
            // w018_インデックスバッファ:VBOなし
            // OpenGL ES 2.0
            "wv018" -> WV018Fragment.newInstance()
            // w018_インデックスバッファ:VBOなし
            // OpenGL ES 2.0
            "w018" -> W018Fragment.newInstance()
            // w017_移動・回転・拡大/縮小:VBOあり
            // OpenGL ES 2.0
            "wv017" -> WV017Fragment.newInstance()
            // w017_移動・回転・拡大/縮小:VBOなし
            // OpenGL ES 2.0
            "w017" -> W017Fragment.newInstance()
            // w016_複数モデルレンダリング:VBOあり
            // OpenGL ES 2.0
            "wv016" -> WV016Fragment.newInstance()
            // w016_複数モデルレンダリング:VBOなし
            // OpenGL ES 2.0
            "w016" -> W016Fragment.newInstance()
            // w015_ポリゴンに色を塗る:VBOあり
            // OpenGL ES 2.0
            "wv015" -> WV015Fragment.newInstance()
            // w015_ポリゴンに色を塗る:VBOなし
            // OpenGL ES 2.0
            "w015" -> W015Fragment.newInstance()
            // OpenGL 1.0で描画
            "OpenGL1.0" -> OpenGL10Fragment.newInstance()
            // OpenGL 1.0で描画
            else -> OpenGL10Fragment.newInstance()
        }

        // 現在表示しているフラグメントをスタックから外す
        supportFragmentManager.popBackStack()
        // 選択したフラグメントを表示する
        if ( supportFragmentManager.findFragmentByTag(tag) == null ) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.flGL01, fragment, tag)
                    .commit()
        }
    }
}
