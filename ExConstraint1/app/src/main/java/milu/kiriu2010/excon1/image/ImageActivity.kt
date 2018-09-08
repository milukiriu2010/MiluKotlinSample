package milu.kiriu2010.excon1.image

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import milu.kiriu2010.excon1.R
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        rgImage.setOnCheckedChangeListener { _, checkedId ->
            val rbImage = findViewById<RadioButton>(checkedId) as RadioButton

            val bmpID = if ( rbBlue.equals(rbImage) ) R.drawable.blue else R.drawable.red
            val bmp = BitmapFactory.decodeResource( resources, bmpID )
            imgColor.setImageBitmap(bmp)
        }


        btnBack.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
