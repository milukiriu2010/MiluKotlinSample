package milu.kiriu2010.gui.decorate

import android.graphics.*
import android.graphics.drawable.Drawable
import java.io.ByteArrayOutputStream

class SierpinSkiTriangleDrawable: Drawable() {

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
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 1f
    }

    // シェルピンスキー・カーペットをビットマップに描画
    private val imageBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)

    init {
        val canvas = Canvas(imageBitmap)
        // 一面黒く塗りつぶす
        canvas.drawRect(Rect(0,0, size, size),linePaintB)
        // 右下を白く塗りつぶす
        val path = Path()
        path.moveTo(size.toFloat(),0f)
        path.lineTo(0f, size.toFloat())
        path.lineTo(size.toFloat(),size.toFloat())
        path.close()
        canvas.drawPath(path,linePaintW)
        // 黒枠
        canvas.drawRect(Rect(0,0, size, size),linePaint)
    }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(imageBitmap,Rect(0,0, size, size),
                Rect(margin, margin, margin + size, margin + size),
                linePaint)
    }

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
        canvas.drawBitmap(tmpBitmap,Rect(0,0,size,size),
                Rect(0,0,size/2,size/2),linePaint)
        // 右上
        canvas.drawBitmap(tmpBitmap,Rect(0,0,size,size),
                Rect(size/2,0,size,size/2),linePaint)
        // 左下
        canvas.drawBitmap(tmpBitmap,Rect(0,0,size,size),
                Rect(0,size/2,size/2,size),linePaint)
    }

    companion object {
        private const val size: Int = 800
        private const val margin: Int = 50
    }
}
