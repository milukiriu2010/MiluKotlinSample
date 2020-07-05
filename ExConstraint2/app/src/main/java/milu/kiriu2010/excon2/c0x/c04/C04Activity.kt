package milu.kiriu2010.excon2.c0x.c04

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import kotlinx.android.synthetic.main.activity_c04.*
import milu.kiriu2010.excon2.R
import java.io.IOException

// 文字認識
// https://qiita.com/tarumzu/items/9eaa9c13041a4f8ed63c
// 言語データ
// https://github.com/tesseract-ocr/tessdata
class C04Activity : AppCompatActivity() {

    private val REQUEST = 0
    private lateinit var ocrUtil: C04OCRUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c04)

        ocrUtil = C04OCRUtil(applicationContext)
        btnC04_SCAN.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent,REQUEST)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ( requestCode == REQUEST ) {
            if ( resultCode == Activity.RESULT_OK && data != null ) {
                var bitmapOrigin: Bitmap? = null
                var bitmap: Bitmap? = null

                val uri = data.data
                try {
                    // tess-twoで画像を読み込む場合、
                    // 画像の向きが正しくないと当然読み込めません。
                    // ギャラリーから読み込む場合、
                    // 画像が回転してしまっている場合があるので
                    // Exifを見て正しい方向に直す必要があります。
                    uri?.let { it ->
                        contentResolver.openFileDescriptor(it, "r").use { parcelFileDescriptorNullable ->
                            parcelFileDescriptorNullable?.let { parcelFileDescriptor ->
                                val fileDescriptor = parcelFileDescriptor.fileDescriptor
                                bitmapOrigin = BitmapFactory.decodeFileDescriptor(fileDescriptor)
                                contentResolver.openInputStream(it).use { inputStream ->
                                    inputStream?.let {
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

                                        val imageWidth = bitmapOrigin?.getWidth() ?: 0
                                        val imageHeight = bitmapOrigin?.getHeight() ?: 0
                                        matrix.setRotate(degrees, imageWidth.toFloat() / 2, imageHeight.toFloat() / 2)
                                        bitmap = Bitmap.createBitmap(bitmapOrigin!!, 0, 0, imageWidth, imageHeight, matrix, true)
                                    }
                                }
                            }
                        }
                    }

                    bitmap?.let {
                        ivC04.setImageBitmap(it)
                        val langId = rgC04.getCheckedRadioButtonId()
                        val langText = findViewById<RadioButton>(langId).text.toString()
                        // 画像を文字に変換したものをEditTextに張り付ける
                        tvC04_OCR.setText(ocrUtil.getString(applicationContext, it, C04OCRUtil.Companion.LangType.getLangType(langText).str))
                    } ?: run {
                        tvC04_OCR.setText("bitmap is null")
                    }
                }
                catch ( e: IOException ) {
                    e.printStackTrace()
                }
            }
        }
        else {
            tvC04_OCR.setText("system error")
        }
    }
}