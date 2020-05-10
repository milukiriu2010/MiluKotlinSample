package milu.kiriu2010.exdb1.opengl05

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_open_gl05.*
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl05.w048.W048Fragment
import milu.kiriu2010.exdb1.opengl05.w048v.WV048Fragment
import milu.kiriu2010.exdb1.opengl05.w049.W049Fragment
import milu.kiriu2010.exdb1.opengl05.w049v.WV049Fragment
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
            // gaussianフィルタ
            R.id.opengl_wv57 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv57") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL05, WV057Fragment.newInstance(), "wv57")
                            .commit()
                }
                true
            }
            // gaussianフィルタ
            R.id.opengl_w057 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w057") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL05, W057Fragment.newInstance(), "w057")
                            .commit()
                }
                true
            }
            // laplacianフィルタ
            R.id.opengl_wv56 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv56") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL05, WV056Fragment.newInstance(), "wv56")
                            .commit()
                }
                true
            }
            // laplacianフィルタ
            R.id.opengl_w056 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w056") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL05, W056Fragment.newInstance(), "w056")
                            .commit()
                }
                true
            }
            // sobelフィルタ
            R.id.opengl_wv55 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv55") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL05, WV055Fragment.newInstance(), "wv55")
                            .commit()
                }
                true
            }
            // sobelフィルタ
            R.id.opengl_w055 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w055") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL05, W055Fragment.newInstance(), "w055")
                            .commit()
                }
                true
            }
            // セピア調
            R.id.opengl_wv54 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv54") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL05, WV054Fragment.newInstance(), "wv54")
                            .commit()
                }
                true
            }
            // セピア調
            R.id.opengl_w054 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w054") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL05, W054Fragment.newInstance(), "w054")
                            .commit()
                }
                true
            }
            // グレースケール
            R.id.opengl_wv53 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv53") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL05, WV053Fragment.newInstance(), "wv53")
                            .commit()
                }
                true
            }
            // グレースケール
            R.id.opengl_w053 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w053") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL05, W053Fragment.newInstance(), "w053")
                            .commit()
                }
                true
            }
            // 高解像度車道マップ
            R.id.opengl_wv52 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv52") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL05, WV052Fragment.newInstance(), "wv52")
                            .commit()
                }
                true
            }
            // 高解像度車道マップ
            R.id.opengl_w052 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w052") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL05, W052Fragment.newInstance(), "w052")
                            .commit()
                }
                true
            }
            // シャドウマッピング
            R.id.opengl_wv51 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv51") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL05, WV051Fragment.newInstance(), "wv51")
                            .commit()
                }
                true
            }
            // シャドウマッピング
            R.id.opengl_w051 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w051") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL05, W051Fragment.newInstance(), "w051")
                            .commit()
                }
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
            // 射影テクスチャマッピング
            R.id.opengl_wv49 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv49") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL05, WV049Fragment.newInstance(), "wv49")
                            .commit()
                }
                true
            }
            // 射影テクスチャマッピング
            R.id.opengl_w049 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w049") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL05, W049Fragment.newInstance(), "w049")
                            .commit()
                }
                true
            }
            // トゥーンレンダリング
            R.id.opengl_wv48 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv48") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL05, WV048Fragment.newInstance(), "wv48")
                            .commit()
                }
                true
            }
            // トゥーンレンダリング
            R.id.opengl_w048 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w048") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.flGL05, W048Fragment.newInstance(), "w048")
                            .commit()
                }
                true
            }
            else -> return super.onOptionsItemSelected(item!!)
        }
    }

    // 表示するフラグメントを切り替える
    private fun changeFragment(tag: String) {
        val fragment = when (tag) {
            // w050_光学迷彩:VBOあり
            // OpenGL ES 2.0
            "wv050" -> WV050Fragment.newInstance()
            // w050_光学迷彩:VBOなし
            // OpenGL ES 2.0
            "w050" -> W050Fragment.newInstance()
            // w050_光学迷彩:VBOなし
            // OpenGL ES 2.0
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
