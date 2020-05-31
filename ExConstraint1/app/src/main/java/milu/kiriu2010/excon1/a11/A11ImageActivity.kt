package milu.kiriu2010.excon1.a11

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_a11_audio.*
import kotlinx.android.synthetic.main.activity_a11_image.*
import milu.kiriu2010.excon1.R
import java.io.File

// タップされたファイルが画像の場合、表示されるアクティビティ
// http://tomooomin.hatenablog.com/entry/2015/11/15/232441
class A11ImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a11_image)

        // 前の画面から渡されたデータを読み込む
        val file = intent.getSerializableExtra("file") as? File ?: return

        // ファイルパス
        tvA11PathImg.text = file.absolutePath

        // ファイルサイズ
        tvA11SizeImg.text = file.length().toString()

        // 読み込んだ画像を表示する
        val bmp = BitmapFactory.decodeFile(file.absolutePath)
        ivA11.setImageBitmap(bmp)
    }
}