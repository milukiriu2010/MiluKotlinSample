package milu.kiriu2010.excon2.b0x.b04


import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_b04.*
import milu.kiriu2010.excon2.R
import android.graphics.BitmapFactory
import android.media.ImageReader
import androidx.core.content.ContextCompat
import android.widget.Toast

// -----------------------------------------------------------------
// カメラの画像をキャプチャー
// -----------------------------------------------------------------
// レンズで映る画像をリアルタイムにビューに表示する
// タップすると、下にキャプチャーした画像が表示される
// http://blog.kotemaru.org/2015/05/23/android-camera2-sample.html
// -----------------------------------------------------------------
class B04Activity : AppCompatActivity() {
    private val mCamera2 = Camera2StateMachine()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b04)
    }

    // カメラのパーミッションをチェックする
    private fun checkPermission() {
        // パーミッションを確認し,
        // 許可されている場合
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            mCamera2.open(this, aftvB04 )
        }
        // 許可されていない場合、
        // パーミッションを要求
        else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA),1)
        }
    }

    // エラーを表示
    private fun showErrorMessage() {
        Toast.makeText(this,"カメラ起動できません", Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // 許可
        if ( permissions.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
            // パーミッションが付与されたらカメラを起動
            checkPermission()
        }
        // 不許可
        else {

            showErrorMessage()
        }
    }

    override fun onResume() {
        super.onResume()
        //mCamera2.open(this, textureView )
        checkPermission()
    }

    override fun onPause() {
        super.onPause()
        mCamera2.close()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if ( keyCode == KeyEvent.KEYCODE_BACK && ivB04.visibility == View.VISIBLE ) {
            aftvB04.visibility = View.VISIBLE
            ivB04.visibility = View.INVISIBLE
        }
        return super.onKeyDown(keyCode, event)
    }

    fun onClickShutter(@Suppress("UNUSED_PARAMETER")view: View) {
        mCamera2.takePicture(object : ImageReader.OnImageAvailableListener {
            override fun onImageAvailable(reader: ImageReader) {
                // 撮れた画像をImageViewに貼り付けて表示。
                val image = reader.acquireLatestImage()
                val buffer = image.getPlanes()[0].getBuffer()
                val bytes = ByteArray(buffer.remaining())
                buffer.get(bytes)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                image.close()

                ivB04.setImageBitmap(bitmap)
                //imageView.setVisibility(View.VISIBLE)
                //textureView.setVisibility(View.INVISIBLE)
            }
        })
    }
}
