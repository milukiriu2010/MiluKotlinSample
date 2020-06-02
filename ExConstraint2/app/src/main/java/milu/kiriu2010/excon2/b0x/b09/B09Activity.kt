package milu.kiriu2010.excon2.b0x.b09

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_b09.*
import milu.kiriu2010.excon2.R

// カメラ起動＆撮影し、ビューに張り付ける
class B09Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b09)

        // カメラ起動
        btnB09A.setOnClickListener {
            val intent = Intent()
            intent.setAction("android.media.action.IMAGE_CAPTURE")
            startActivityForResult(intent,0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            0 -> {
                // シャッターを押したときの画像が渡される
                val bmp = data?.getExtras()?.get("data") as? Bitmap ?: return
                ivB09.setImageBitmap(bmp)
            }
            else -> {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }
}
