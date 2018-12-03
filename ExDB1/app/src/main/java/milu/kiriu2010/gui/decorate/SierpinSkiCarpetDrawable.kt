package milu.kiriu2010.gui.decorate

import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log
import java.io.ByteArrayOutputStream

// シェルピンスキー・カーペット
// https://imagenavi.jp/search/detail.asp?id=64002846
// ------------------------------------------------------
//   1 x  1
//   3 x  3
//   9 x  9
//  27 x 27
//  81 x 81
// ------------------------------------------------------
//   ***                          ***
//   * * => 画像を１／９にする =>  * * で並べる
//   ***                          ***
// ------------------------------------------------------
class SierpinSkiCarpetDrawable: Drawable() {

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 1f
    }

    private val linePaintW = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 1f
    }

    private val linePaintB = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 1f
    }

    // シェルピンスキー・カーペットをビットマップに描画
    private val imageBitmap = Bitmap.createBitmap(size, size,Bitmap.Config.ARGB_8888)

    private val plotLst: MutableList<MutableList<Int>> = mutableListOf(mutableListOf(1))

    init {
        val canvas = Canvas(imageBitmap)
        // 一面黒く塗りつぶす
        canvas.drawRect(Rect(0,0,size,size),linePaintB)
        // 真ん中を白く塗りつぶす
        canvas.drawRect(Rect(size/3,size/3,size*2/3, size*2/3),linePaintW)
    }

    override fun draw(canvas: Canvas) {
        /*
        // 処理回数(=配列のサイズ)
        val procNum = plotLst.size
        // 描画する図形のサイズ
        val figSize = size/procNum

        Log.d(javaClass.simpleName, "procNum=[${procNum}]figSize[${figSize}]")

        plotLst.forEachIndexed { y, xLst ->
            // --------------------
            // v
            // --------------------
            //   0=>白
            //   1=>黒
            xLst.forEachIndexed { x, v ->
                linePaint.color = if (v==0) Color.WHITE else Color.BLACK
                canvas.drawRect(RectF(x.toFloat()*figSize.toFloat(),
                        y.toFloat()*figSize.toFloat(),
                        (x+1).toFloat()*figSize.toFloat(),
                        (y+1).toFloat()*figSize.toFloat()),
                        linePaint)
            }
        }
        */
        canvas.drawBitmap(imageBitmap,Rect(0,0,size,size),
                Rect(margin,margin,margin+size,margin+size),
                linePaint)
    }

    override fun setAlpha(alpha: Int) {
        linePaint.alpha = alpha
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        linePaint.colorFilter = colorFilter
    }

    override fun getIntrinsicWidth() = size+margin*2

    override fun getIntrinsicHeight() = size+margin*2

    // 処理
    // ------------------------------------
    //   *
    // ------------------------------------
    //   ***
    //   * *
    //   ***
    // ------------------------------------
    //   *******
    //   * * * *
    //   *******
    //   * * * *
    //   *******
    //   * * * *
    //   *******
    // ------------------------------------
    public fun proc() {
        /*
        // 処理回数(=配列のサイズ)
        val procNum = plotLst.size+1

        val tmpPlotLst: MutableList<MutableList<Int>> = mutableListOf(mutableListOf())

        plotLst.forEachIndexed { y, xLst ->
            // --------------------
            // v
            // --------------------
            //   0=>白
            //   1=>黒
            // --------------------
            xLst.forEachIndexed { x, v ->

            }
        }
        */

        /*
        val stream = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
        val byteArray = stream.toByteArray()
        imageBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
        */

        val stream = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
        val byteArray = stream.toByteArray()
        val tmpBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)

        val canvas = Canvas(imageBitmap)
        // 左上
        canvas.drawBitmap(tmpBitmap,Rect(0,0,size,size),
                Rect(0,0,size/3,size/3),linePaint)
        // 真ん中上
        canvas.drawBitmap(tmpBitmap,Rect(0,0,size,size),
                Rect(size/3,0,size*2/3,size/3),linePaint)
        // 右上
        canvas.drawBitmap(tmpBitmap,Rect(0,0,size,size),
                Rect(size*2/3,0,size,size/3),linePaint)
        // 左中
        canvas.drawBitmap(tmpBitmap,Rect(0,0,size,size),
                Rect(0,size/3,size/3,size*2/3),linePaint)
        // 右中
        canvas.drawBitmap(tmpBitmap,Rect(0,0,size,size),
                Rect(size*2/3,size/3,size,size*2/3),linePaint)
        // 左下
        canvas.drawBitmap(tmpBitmap,Rect(0,0,size,size),
                Rect(0,size*2/3,size/3,size),linePaint)
        // 真ん中下
        canvas.drawBitmap(tmpBitmap,Rect(0,0,size,size),
                Rect(size/3,size*2/3,size*2/3,size),linePaint)
        // 右下
        canvas.drawBitmap(tmpBitmap,Rect(0,0,size,size),
                Rect(size*2/3,size*2/3,size,size),linePaint)
    }

    companion object {
        private const val size: Int = 810
        private const val margin: Int = 50
    }
}
