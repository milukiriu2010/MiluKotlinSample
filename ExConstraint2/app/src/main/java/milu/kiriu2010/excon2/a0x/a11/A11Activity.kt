package milu.kiriu2010.excon2.a0x.a11

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import milu.kiriu2010.excon2.R
import kotlinx.android.synthetic.main.activity_a11.*

// 大きな画像をロードする(5684x4223=8.4MB)
// ivA11のlayout_width,layout_height=0dpにするとロードできない
// http://tekeye.uk/android/examples/android-bitmap-loading
class A11Activity : AppCompatActivity() {

    //stores ImageView dimensions
    var xDim: Int = -1
    var yDim: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a11)

        Log.d(this.javaClass.toString(), "=== onCreate ===" )

        // 画像を読み込む
        btnA11A.setOnClickListener {
            Log.d(this.javaClass.toString(), "=== btnLargeBmp.setOnClickListener ===" )
            // First decode with inJustDecodeBounds=true to check dimensions
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            val resId = R.drawable.ic_a11
            // optionsに変更がかかる？
            BitmapFactory.decodeResource( resources, resId, options )
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, xDim, yDim )
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false
            val bmpRes = BitmapFactory.decodeResource( resources, resId, options )
            ivA11.setImageBitmap(bmpRes)
        }
    }

    // Get the size of the Image view after the
    // Activity has completely loaded
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        Log.d(this.javaClass.toString(), "=== onWindowFocusChanged ===")
        super.onWindowFocusChanged(hasFocus)
        xDim=ivA11.width
        yDim=ivA11.height
    }

    // Given the bitmap size and View size calculate a subsampling size (powers of 2)
    private fun calculateInSampleSize( options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int ): Int {
        Log.d(this.javaClass.toString(), "=== calculateInSampleSize:reqWidth(${reqWidth}) ===" )
        Log.d(this.javaClass.toString(), "=== calculateInSampleSize:reqHeight(${reqHeight}) ===" )
        //Default subsampling size
        var inSampleSize = 1
        // See if image raw height and width is bigger than that of required view
        if (options.outHeight > reqHeight || options.outWidth > reqWidth) {
            // bigger
            val halfHeight = options.outHeight/2
            val halfWidth  = options.outWidth/2
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ( ((halfHeight/inSampleSize) > reqHeight) and ((halfWidth/inSampleSize) > reqWidth ) ) {
                inSampleSize *= 2
                Log.d(this.javaClass.toString(), "=== inSampleSize(${inSampleSize}) ===" )
            }
            Log.d(this.javaClass.toString(), "=== done ===" )

        }

        return inSampleSize
    }
}
