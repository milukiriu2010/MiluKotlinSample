package milu.kiriu2010.excon2.a0x.a03

import android.annotation.SuppressLint
import android.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import milu.kiriu2010.excon2.R
import milu.kiriu2010.excon2.R.layout.layout_a03
import kotlinx.android.synthetic.main.layout_a03.*

// アクションバーをカスタマイズ
// https://www.journaldev.com/9952/android-custom-action-bar-example-tutorial
class A03Activity : AppCompatActivity() {

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a03)

        // カスタマイズされたアクションバーを表示するためのおまじない
        // アクションバーの部分のレイアウトをアクティビティと違うファイルで実現している
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(layout_a03)
        //val view: View? = supportActionBar?.customView

        // 戻るボタンクリック
        ivA03A.setOnClickListener { finish() }

        // 進ボタンクリック
        ivA03B.setOnClickListener {
            Toast.makeText( this, "Forward Button is clicked.", Toast.LENGTH_LONG).show()
        }
    }
}
