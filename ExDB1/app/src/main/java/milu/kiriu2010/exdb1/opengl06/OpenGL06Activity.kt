package milu.kiriu2010.exdb1.opengl06

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl06.w058.W058Fragment
import milu.kiriu2010.exdb1.opengl06.w058v.WV058Fragment
import milu.kiriu2010.exdb1.opengl06.w059.W059Fragment
import milu.kiriu2010.exdb1.opengl06.w059v.WV059Fragment
import milu.kiriu2010.exdb1.opengl06.w060.W060Fragment
import milu.kiriu2010.exdb1.opengl06.w060v.WV060Fragment
import milu.kiriu2010.exdb1.opengl06.w061.W061Fragment
import milu.kiriu2010.exdb1.opengl06.w061v.WV061Fragment
import milu.kiriu2010.exdb1.opengl06.w062.W062Fragment
import milu.kiriu2010.exdb1.opengl06.w062v.WV062Fragment
import milu.kiriu2010.exdb1.opengl06.w063.W063Fragment
import milu.kiriu2010.exdb1.opengl06.w063v.WV063Fragment
import milu.kiriu2010.exdb1.opengl06.w064.W064Fragment
import milu.kiriu2010.exdb1.opengl06.w064v.WV064Fragment
import milu.kiriu2010.exdb1.opengl06.w065.W065Fragment
import milu.kiriu2010.exdb1.opengl06.w065v.WV065Fragment
import milu.kiriu2010.exdb1.opengl06.w066.W066Fragment
import milu.kiriu2010.exdb1.opengl06.w066v.WV066Fragment
import milu.kiriu2010.exdb1.opengl06.w067.W067Fragment
import milu.kiriu2010.exdb1.opengl06.w067v.WV067Fragment
import milu.kiriu2010.exdb1.opengl06.w068.W068Fragment
import milu.kiriu2010.exdb1.opengl06.w068v.WV068Fragment

class OpenGL06Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_gl06)

        supportFragmentManager.popBackStack()
        if (supportFragmentManager.findFragmentByTag("xyz") == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, WV068Fragment.newInstance(), "xyz")
                    .commit()
        }

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_opengl06, menu)
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
            // ゴッドレイ
            R.id.opengl_wv68 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv68") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, WV068Fragment.newInstance(), "wv68")
                            .commit()
                }
                true
            }
            // ゴッドレイ
            R.id.opengl_w068 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w068") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W068Fragment.newInstance(), "w068")
                            .commit()
                }
                true
            }
            // ズームブラー
            R.id.opengl_wv67 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv67") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, WV067Fragment.newInstance(), "wv67")
                            .commit()
                }
                true
            }
            // ズームブラー
            R.id.opengl_w067 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w067") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W067Fragment.newInstance(), "w067")
                            .commit()
                }
                true
            }
            // モザイクフィルタ
            R.id.opengl_wv66 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv66") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, WV066Fragment.newInstance(), "wv66")
                            .commit()
                }
                true
            }
            // モザイクフィルタ
            R.id.opengl_w066 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w066") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W066Fragment.newInstance(), "w066")
                            .commit()
                }
                true
            }
            // 後光 表面化散乱
            R.id.opengl_wv65 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv65") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, WV065Fragment.newInstance(), "wv65")
                            .commit()
                }
                true
            }
            // 後光 表面化散乱
            R.id.opengl_w065 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w065") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W065Fragment.newInstance(), "w065")
                            .commit()
                }
                true
            }
            // リムライティング
            R.id.opengl_wv64 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv64") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, WV064Fragment.newInstance(), "wv64")
                            .commit()
                }
                true
            }
            // リムライティング
            R.id.opengl_w064 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w064") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W064Fragment.newInstance(), "w064")
                            .commit()
                }
                true
            }
            // 半球ライティング
            R.id.opengl_wv63 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv63") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, WV063Fragment.newInstance(), "wv63")
                            .commit()
                }
                true
            }
            // 半球ライティング
            R.id.opengl_w063 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w063") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W063Fragment.newInstance(), "w063")
                            .commit()
                }
                true
            }
            // ステンシル鏡面反射
            R.id.opengl_wv62 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv62") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, WV062Fragment.newInstance(), "wv62")
                            .commit()
                }
                true
            }
            // ステンシル鏡面反射
            R.id.opengl_w062 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w062") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W062Fragment.newInstance(), "w062")
                            .commit()
                }
                true
            }
            // パーティクルフォグ
            R.id.opengl_wv61 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv61") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, WV061Fragment.newInstance(), "wv61")
                            .commit()
                }
                true
            }
            // パーティクルフォグ
            R.id.opengl_w061 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w061") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W061Fragment.newInstance(), "w061")
                            .commit()
                }
                true
            }
            // フォグ距離
            R.id.opengl_wv60 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv60") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, WV060Fragment.newInstance(), "wv60")
                            .commit()
                }
                true
            }
            // フォグ距離
            R.id.opengl_w060 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w060") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W060Fragment.newInstance(), "w060")
                            .commit()
                }
                true
            }
            // 被写界深度
            R.id.opengl_wv59 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv59") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, WV059Fragment.newInstance(), "wv59")
                            .commit()
                }
                true
            }
            // 被写界深度
            R.id.opengl_w059 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w059") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W059Fragment.newInstance(), "w059")
                            .commit()
                }
                true
            }
            // グレアフィルタ
            R.id.opengl_wv58 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("wv58") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, WV058Fragment.newInstance(), "wv58")
                            .commit()
                }
                true
            }
            // グレアフィルタ
            R.id.opengl_w058 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("w058") == null) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, W058Fragment.newInstance(), "w058")
                            .commit()
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}
