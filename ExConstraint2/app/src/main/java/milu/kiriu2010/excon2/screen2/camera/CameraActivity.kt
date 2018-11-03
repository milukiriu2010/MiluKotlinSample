package milu.kiriu2010.excon2.screen2.camera

import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_camera.*
import milu.kiriu2010.excon2.R

class CameraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        // カメラ起動
        btnCamera.setOnClickListener {
            val intent = Intent()
            intent.setAction("android.media.action.IMAGE_CAPTURE")
            startActivityForResult(intent,0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            0 -> {
                val bmp = data?.getExtras()?.get("data") as? Bitmap ?: return
                imageView.setImageBitmap(bmp)
            }
            else -> {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }
}
