package milu.kiriu2010.exdb1.opengl05

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl05.w050.W050Fragment
import milu.kiriu2010.exdb1.opengl05.w050v.WV050Fragment
import milu.kiriu2010.exdb1.opengl05.w051.W051Fragment
import milu.kiriu2010.exdb1.opengl05.w051v.WV051Fragment
import milu.kiriu2010.exdb1.opengl05.w052.W052Fragment
import milu.kiriu2010.exdb1.opengl05.w052v.WV052Fragment
import milu.kiriu2010.exdb1.opengl05.w053.W053Fragment
import milu.kiriu2010.exdb1.opengl05.w053v.WV053Fragment
import milu.kiriu2010.exdb1.opengl05.w054.W054Fragment
import milu.kiriu2010.exdb1.opengl05.w054v.WV054Fragment
import milu.kiriu2010.exdb1.opengl05.w055.W055Fragment
import milu.kiriu2010.exdb1.opengl05.w055v.WV055Fragment
import milu.kiriu2010.exdb1.opengl05.w056.W056Fragment
import milu.kiriu2010.exdb1.opengl05.w056v.WV056Fragment
import milu.kiriu2010.exdb1.opengl05.w057.W057Fragment
import milu.kiriu2010.exdb1.opengl05.w057v.WV057Fragment
import milu.kiriu2010.exdb1.opengl05.w058.W058Fragment
import milu.kiriu2010.exdb1.opengl05.w058v.WV058Fragment
import milu.kiriu2010.exdb1.opengl05.w059.W059Fragment
import milu.kiriu2010.exdb1.opengl05.w059v.WV059Fragment

// -------------------------------------
// OpenGL ES 2.0サンプル
// -------------------------------------
// https://wgld.org/d/webgl/w050.html
// ～
// https://wgld.org/d/webgl/w059.html
// -------------------------------------
class OpenGL05Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_gl05)

        // 初期表示のフラグメントを設定
        changeFragment("wv050")

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_opengl05, menu)
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
            // 被写界深度:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv59 -> {
                changeFragment("wv059")
                true
            }
            // 被写界深度:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w059 -> {
                changeFragment("w059")
                true
            }
            // グレアフィルタ:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv58 -> {
                changeFragment("wv058")
                true
            }
            // グレアフィルタ:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w058 -> {
                changeFragment("w058")
                true
            }
            // gaussianフィルタ:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv57 -> {
                changeFragment("wv057")
                true
            }
            // gaussianフィルタ:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w057 -> {
                changeFragment("w057")
                true
            }
            // laplacianフィルタ:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_wv56 -> {
                changeFragment("wv056")
                true
            }
            // laplacianフィルタ:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w056 -> {
                changeFragment("w056")
                true
            }
            // sobelフィルタ:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv55 -> {
                changeFragment("wv055")
                true
            }
            // sobelフィルタ:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w055 -> {
                changeFragment("w055")
                true
            }
            // セピア調:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv54 -> {
                changeFragment("wv054")
                true
            }
            // セピア調:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w054 -> {
                changeFragment("w054")
                true
            }
            // グレースケール:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv53 -> {
                changeFragment("wv053")
                true
            }
            // グレースケール:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w053 -> {
                changeFragment("w053")
                true
            }
            // 高解像度車道マップ:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv52 -> {
                changeFragment("wv052")
                true
            }
            // 高解像度車道マップ:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w052 -> {
                changeFragment("w052")
                true
            }
            // シャドウマッピング:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv51 -> {
                changeFragment("wv051")
                true
            }
            // シャドウマッピング:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w051 -> {
                changeFragment("w051")
                true
            }
            // w050_光学迷彩:VBOあり
            // OpenGL ES 2.0
            R.id.opengl_wv50 -> {
                changeFragment("wv050")
                true
            }
            // w050_光学迷彩:VBOなし
            // OpenGL ES 2.0
            R.id.opengl_w050 -> {
                changeFragment("w050")
                true
            }
            else -> return super.onOptionsItemSelected(item!!)
        }
    }

    // 表示するフラグメントを切り替える
    private fun changeFragment(tag: String) {
        val fragment = when (tag) {
            // 被写界深度:VBOなし
            // OpenGL ES 2.0
            "wv059" -> WV059Fragment.newInstance()
            // 被写界深度:VBOなし
            // OpenGL ES 2.0
            "w059" -> W059Fragment.newInstance()
            // グレアフィルタ:VBOなし
            // OpenGL ES 2.0
            "wv058" -> WV058Fragment.newInstance()
            // グレアフィルタ:VBOなし
            // OpenGL ES 2.0
            "w058" -> W058Fragment.newInstance()
            // gaussianフィルタ:VBOあり
            // OpenGL ES 2.0
            "wv057" -> WV057Fragment.newInstance()
            // gaussianフィルタ:VBOなし
            // OpenGL ES 2.0
            "w057" -> W057Fragment.newInstance()
            // laplacianフィルタ:VBOなし
            // OpenGL ES 2.0
            "wv056" -> WV056Fragment.newInstance()
            // laplacianフィルタ:VBOなし
            // OpenGL ES 2.0
            "w056" -> W056Fragment.newInstance()
            // sobelフィルタ:VBOあり
            // OpenGL ES 2.0
            "wv055" -> WV055Fragment.newInstance()
            // sobelフィルタ:VBOなし
            // OpenGL ES 2.0
            "w055" -> W055Fragment.newInstance()
            // セピア調:VBOあり
            // OpenGL ES 2.0
            "wv054" -> WV054Fragment.newInstance()
            // セピア調:VBOあり
            // OpenGL ES 2.0
            "w054" -> W054Fragment.newInstance()
            // グレースケール:VBOなし
            // OpenGL ES 2.0
            "wv053" -> WV053Fragment.newInstance()
            // グレースケール:VBOなし
            // OpenGL ES 2.0
            "w053" -> W053Fragment.newInstance()
            // 高解像度車道マップ:VBOあり
            // OpenGL ES 2.0
            "wv052" -> WV052Fragment.newInstance()
            // 高解像度車道マップ:VBOなし
            // OpenGL ES 2.0
            "w052" -> W052Fragment.newInstance()
            // シャドウマッピング:VBOなし
            // OpenGL ES 2.0
            "wv051" -> WV051Fragment.newInstance()
            // シャドウマッピング:VBOなし
            // OpenGL ES 2.0
            "w051" -> W051Fragment.newInstance()
            // w050_光学迷彩:VBOあり
            // OpenGL ES 2.0
            "wv050" -> WV050Fragment.newInstance()
            // w050_光学迷彩:VBOなし
            // OpenGL ES 2.0
            "w050" -> W050Fragment.newInstance()
            else -> W050Fragment.newInstance()
        }

        // 現在表示しているフラグメントをスタックから外す
        supportFragmentManager.popBackStack()
        // 選択したフラグメントを表示する
        if ( supportFragmentManager.findFragmentByTag(tag) == null ) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.flGL05, fragment, tag)
                    .commit()
        }
    }
}
