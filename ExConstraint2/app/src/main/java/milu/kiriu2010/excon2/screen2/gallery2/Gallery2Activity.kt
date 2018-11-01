package milu.kiriu2010.excon2.screen2.gallery2

import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_gallery2.*
import milu.kiriu2010.excon2.R

class Gallery2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery2)

        // ギャラリー呼び出し
        btnGallery2.setOnClickListener {
            val intent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            }
            startActivityForResult(intent,0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        try {
            val inputStream = contentResolver.openInputStream(data?.data)
            val img = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            imageView.setImageBitmap(img)
        }
        catch ( ex: Exception ) {
            ex.printStackTrace()
        }
    }
}
