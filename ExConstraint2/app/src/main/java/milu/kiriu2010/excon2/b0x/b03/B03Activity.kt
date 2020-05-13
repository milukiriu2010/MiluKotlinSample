package milu.kiriu2010.excon2.b0x.b03

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_b03.*
import milu.kiriu2010.excon2.R

// ギャラリー表示２
// カメラで撮影した画像をピックアップし、ビューに表示する
class B03Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b03)

        // ギャラリー呼び出し
        btnB03A.setOnClickListener {
            val intent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            }
            startActivityForResult(intent,0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            val inputStream = contentResolver.openInputStream(data?.data!!)
            val img = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            ivB03.setImageBitmap(img)
        }
        catch ( ex: Exception ) {
            ex.printStackTrace()
        }
    }
}
