package milu.kiriu2010.excon2.b0x.b01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager

// 設定
class B01Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 画面つけっぱなし
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        supportFragmentManager.beginTransaction()
                .replace(android.R.id.content,B01Fragment.newInstance())
                .commit()
    }
}
