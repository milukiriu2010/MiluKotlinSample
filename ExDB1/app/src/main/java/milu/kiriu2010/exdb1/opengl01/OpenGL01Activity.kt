package milu.kiriu2010.exdb1.opengl01

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl01.begin00.OpenGL10Fragment
import milu.kiriu2010.exdb1.opengl01.jayce07.Jayce07Fragment
import milu.kiriu2010.exdb1.opengl01.labo01.Labo01Fragment
import milu.kiriu2010.exdb1.opengl01.noise01.Noise01Fragment
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
import milu.kiriu2010.exdb1.opengl01.noise01v.NoiseV01Fragment

// -------------------------------------
// OpenGL ES 2.0サンプル
// -------------------------------------
// https://wgld.org/d/webgl/w015.html
// ～
// https://wgld.org/d/webgl/w019.html
// -------------------------------------
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
            // w025_点光源:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv025 -> {
                changeFragment("wv025")
                true
            }
            // w025_点光源:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w025 -> {
                changeFragment("w025")
                true
            }
            // w024_フォンシェーディング:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv024 -> {
                changeFragment("wv024")
                true
            }
            // w024_フォンシェーディング:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w024 -> {
                changeFragment("w024")
                true
            }
            // w023_反射光:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv023 -> {
                changeFragment("wv023")
                true
            }
            // w023_反射光:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w023 -> {
                changeFragment("w023")
                true
            }
            // w022_環境光:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv022 -> {
                changeFragment("wv022")
                true
            }
            // w022_環境光:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w022 -> {
                changeFragment("w022")
                true
            }
            // w021_平行光源:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv021 -> {
                changeFragment("wv021")
                true
            }
            // w021_平行光源:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w021 -> {
                changeFragment("w021")
                true
            }
            // w020_トーラス:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv020 -> {
                changeFragment("wv020")
                true
            }
            // w020_トーラス:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w020 -> {
                changeFragment("w020")
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
            // noise01_ノイズテクスチャ:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_noisev01 -> {
                changeFragment("noisev01")
                true
            }
            // noise01_ノイズテクスチャ:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_noise01 -> {
                changeFragment("noise01")
                true
            }
            // labo01_テクスチャ
            // OpenGL ES 2.0
            R.id.opengl_labo01 -> {
                changeFragment("labo01")
                true
            }
            // jayce07_フレームバッファ
            R.id.opengl_jayce07 -> {
                changeFragment("jayce07")
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
            // w025_点光源:VBOあり
            // OpenGL ES 2.0
            "wv025" -> WV025Fragment.newInstance()
            // w025_点光源:VBOなし
            // OpenGL ES 2.0
            "w025" -> W025Fragment.newInstance()
            // w024_フォンシェーディング:VBOあり
            // OpenGL ES 2.0
            "wv024" -> WV024Fragment.newInstance()
            // w024_フォンシェーディング:VBOなし
            // OpenGL ES 2.0
            "w024" -> W024Fragment.newInstance()
            // w023_反射光:VBOあり
            // OpenGL ES 2.0
            "wv023" -> WV023Fragment.newInstance()
            // w023_反射光:VBOなし
            // OpenGL ES 2.0
            "w023" -> W023Fragment.newInstance()
            // w022_環境光:VBOあり
            // OpenGL ES 2.0
            "wv022" -> WV022Fragment.newInstance()
            // w022_環境光:VBOなし
            // OpenGL ES 2.0
            "w022" -> W022Fragment.newInstance()
            // w021_平行光源:VBOあり
            // OpenGL ES 2.0
            "wv021" -> WV021Fragment.newInstance()
            // w021_平行光源:VBOなし
            // OpenGL ES 2.0
            "w021" -> W021Fragment.newInstance()
            // w020_トーラス:VBOあり
            // OpenGL ES 2.0
            "wv020" -> WV020Fragment.newInstance()
            // w020_トーラス:VBOなし
            // OpenGL ES 2.0
            "w020" -> W020Fragment.newInstance()
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
            // noise01_ノイズテクスチャ:VBOあり
            // OpenGL ES 2.0
            "noisev01" -> NoiseV01Fragment.newInstance()
            // noise01_ノイズテクスチャ:VBOなし
            // OpenGL ES 2.0
            "noise01" -> Noise01Fragment.newInstance()
            // labo01_テクスチャ
            // OpenGL ES 2.0
            "labo01" -> Labo01Fragment.newInstance()
            // jayce07_フレームバッファ
            // OpenGL ES 2.0
            "jayce07" -> Jayce07Fragment.newInstance()
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
