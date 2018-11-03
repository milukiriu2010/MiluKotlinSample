package milu.kiriu2010.excon2.screen2.cameraparam


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_camera_param.*
import milu.kiriu2010.excon2.R
import android.graphics.BitmapFactory
import android.media.ImageReader
import android.media.ImageReader.OnImageAvailableListener



// http://blog.kotemaru.org/2015/05/23/android-camera2-sample.html
class CameraParamActivity : AppCompatActivity() {
    private val mCamera2 = Camera2StateMachine()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_param)
    }

    override fun onResume() {
        super.onResume()
        mCamera2.open(this, textureView )
    }

    override fun onPause() {
        super.onPause()
        mCamera2.close()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if ( keyCode == KeyEvent.KEYCODE_BACK && imageView.visibility == View.VISIBLE ) {
            textureView.visibility = View.VISIBLE
            imageView.visibility = View.INVISIBLE
        }
        return super.onKeyDown(keyCode, event)
    }

    fun onClickShutter(view: View) {
        mCamera2.takePicture(object : ImageReader.OnImageAvailableListener {
            override fun onImageAvailable(reader: ImageReader) {
                // 撮れた画像をImageViewに貼り付けて表示。
                val image = reader.acquireLatestImage()
                val buffer = image.getPlanes()[0].getBuffer()
                val bytes = ByteArray(buffer.remaining())
                buffer.get(bytes)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                image.close()

                imageView.setImageBitmap(bitmap)
                imageView.setVisibility(View.VISIBLE)
                textureView.setVisibility(View.INVISIBLE)
            }
        })
    }
}
