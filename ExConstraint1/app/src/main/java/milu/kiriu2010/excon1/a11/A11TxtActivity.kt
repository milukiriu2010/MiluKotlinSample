package milu.kiriu2010.excon1.a11

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_a11_image.*
import kotlinx.android.synthetic.main.activity_a11_text.*
import milu.kiriu2010.excon1.R
import java.io.File

class A11TxtActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a11_text)

        // 前の画面から渡されたデータを読み込む
        val file = intent.getSerializableExtra("file") as? File ?: return

        // ファイルパス
        tvA11PathTxt.text = file.absolutePath

        // ファイルサイズ
        tvA11SizeTxt.text = file.length().toString()

        // ファイルの中身をロード
        val contents = file.inputStream().readBytes().toString(Charsets.UTF_8)
        tvA11Txt.text = contents
    }
}