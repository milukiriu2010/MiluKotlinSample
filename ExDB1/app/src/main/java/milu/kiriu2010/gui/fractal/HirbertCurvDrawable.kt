package milu.kiriu2010.gui.fractal

import android.graphics.*
import android.graphics.drawable.Drawable
import java.io.ByteArrayOutputStream

class HirbertCurvDrawable: Drawable() {

    // 枠
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        //style = Paint.Style.FILL_AND_STROKE
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    // 白部分
    private val linePaintW = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 1f
    }

    // 黒部分
    private val linePaintB = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    // ヒルベルト曲線をビットマップに描画
    private val imageBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)

    init {
        /*
        val canvas = Canvas(imageBitmap)
        // 一面白く塗りつぶす
        canvas.drawRect(Rect(0,0, size, size),linePaintW)
        // 左・下・右に線を描く
        val path = Path()
        path.moveTo(shift.toFloat(),shift.toFloat())
        path.lineTo(shift.toFloat(), (size-shift).toFloat())
        path.lineTo((size-shift).toFloat(),(size-shift).toFloat())
        path.lineTo((size-shift).toFloat(),shift.toFloat())
        canvas.drawPath(path,linePaintB)
        */
        val canvas = Canvas(imageBitmap)
        // 一面白く塗りつぶす
        canvas.drawRect(Rect(0,0, size, size),linePaintW)
        // 左・下・右に線を描く
        val path = Path()
        path.moveTo((size /4).toFloat(),(size /4).toFloat())
        path.lineTo((size /4).toFloat(), (size *3/4).toFloat())
        path.lineTo((size *3/4).toFloat(),(size *3/4).toFloat())
        path.lineTo((size *3/4).toFloat(),(size /4).toFloat())
        canvas.drawPath(path,linePaintB)
    }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(imageBitmap,Rect(0,0, size, size),
                Rect(margin, margin, margin + size, margin + size),
                linePaint)    }

    override fun setAlpha(alpha: Int) {
        linePaint.alpha = alpha
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        linePaint.colorFilter = colorFilter
    }

    override fun getIntrinsicWidth() = size + margin * 2

    override fun getIntrinsicHeight() = size + margin * 2

    // 処理
    // ------------------------------------
    public fun proc() {
        val stream = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
        val byteArray = stream.toByteArray()
        val tmpBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)

        val canvas = Canvas(imageBitmap)
        // 左上
        val matLU = Matrix()
        matLU.postRotate(270f)
        val tmpBitmapLU = Bitmap.createBitmap(tmpBitmap,0,0, size, size,matLU,true)
        canvas.drawBitmap(tmpBitmapLU,Rect(0,0, size, size),
                Rect(0,0, size /2, size /2),linePaint)
        // 左下
        canvas.drawBitmap(tmpBitmap,Rect(0,0, size, size),
                Rect(0, size /2, size /2, size),linePaint)
        // 右下
        canvas.drawBitmap(tmpBitmap,Rect(0,0, size, size),
                Rect(size /2, size /2, size, size),linePaint)
        // 右上
        val matRU = Matrix()
        matRU.postRotate(90f)
        val tmpBitmapRU = Bitmap.createBitmap(tmpBitmap,0,0, size, size,matRU,true)
        canvas.drawBitmap(tmpBitmapRU,Rect(0,0, size, size),
                Rect(size /2,0, size, size /2),linePaint)
    }


    companion object {
        private const val size: Int = 800
        private const val margin: Int = 50
        private const val shift: Int = 20
    }
}