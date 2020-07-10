package milu.kiriu2010.excon2.c0x.c05

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import kotlinx.android.synthetic.main.activity_c05.*
import milu.kiriu2010.excon2.R
import milu.kiriu2010.excon2.c0x.c04.C04OCRUtil
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class C05Activity : AppCompatActivity() {
    private val REQUEST = 0
    private lateinit var ocrUtil: C04OCRUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c05)

        ocrUtil = C04OCRUtil(applicationContext)
        btnC05_SCAN.setOnClickListener {
            val intent = Intent()
            intent.setAction("android.media.action.IMAGE_CAPTURE")
            startActivityForResult(intent,REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST -> {
                // シャッターを押したときの画像が渡される
                val bmpOrg = data?.getExtras()?.get("data") as? Bitmap ?: return
                val bos = ByteArrayOutputStream()
                // https://stackoverflow.com/questions/7698409/android-transform-a-bitmap-into-an-input-stream
                bmpOrg.compress(Bitmap.CompressFormat.PNG,0,bos)
                val bitmapOriginData = bos.toByteArray()
                val bis = ByteArrayInputStream(bitmapOriginData)
                val bmp = bis.let {
                    val exifInterface = ExifInterface(it)
                    val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
                    val degrees = when (orientation) {
                        // 正しい方向の場合は回転させない
                        1 -> { 0f }
                        // 逆向きなので180度回転させる
                        3 -> { 180f }
                        // 左向きの画像になってるので90度回転させる
                        6 -> { 90f }
                        // 右向きの画像になってるので270度回転させる
                        8 -> { 270f }
                        else -> { 0f }
                    }
                    val matrix = Matrix()

                    val imageWidth = bmpOrg.getWidth()
                    val imageHeight = bmpOrg.getHeight()
                    matrix.setRotate(degrees, imageWidth.toFloat() / 2, imageHeight.toFloat() / 2)
                    Bitmap.createBitmap(bmpOrg, 0, 0, imageWidth, imageHeight, matrix, true)
                }

                ivC05.setImageBitmap(bmp)
                val langId = rgC05.getCheckedRadioButtonId()
                val langText = findViewById<RadioButton>(langId).text.toString()
                // 画像を文字に変換したものをEditTextに張り付ける
                tvC05_OCR.setText(ocrUtil.getString(applicationContext, bmp, C04OCRUtil.Companion.LangType.getLangType(langText).str))
            }
            else -> {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }
}