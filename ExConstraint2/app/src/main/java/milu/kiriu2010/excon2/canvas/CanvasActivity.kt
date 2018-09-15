package milu.kiriu2010.excon2.canvas

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_canvas.*
import milu.kiriu2010.excon2.R

class CanvasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas)

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }


    }

    // アクションバーのアイコンがタップされると呼ばれる
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            // 前画面に戻る
            android.R.id.home -> {
                finish()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
