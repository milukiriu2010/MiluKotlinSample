package milu.kiriu2010.excon1.a13

import android.app.Activity
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import milu.kiriu2010.excon1.R
import kotlinx.android.synthetic.main.activity_a13.*

// ラジオボタンで選択した画像リソースを読み込む
class A13Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a13)

        // ラジオボタンの選択で、表示する画像を切り替える
        // rbA13Blue => 青い三角形
        // rbA13Red  => 赤い丸
        rgA13.setOnCheckedChangeListener { _, checkedId ->
            val rbImage = findViewById<RadioButton>(checkedId)

            val bmpID = if ( rbA13Blue.equals(rbImage) ) R.drawable.ic_a13_blue else R.drawable.ic_a13_red
            val bmp = BitmapFactory.decodeResource( resources, bmpID )
            ivA13.setImageBitmap(bmp)
        }

    }
}
