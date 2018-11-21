package milu.kiriu2010.gui.decorate

import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.FloatProperty

// 1000x1000に長さ100の木を初期描画
// -----------------------------------------
//   *   *
//    * *
//     * => (500,800)
//     *
//     * => (500,900)
// -----------------------------------------
class KochTreeLapDrawable : Drawable() {
    var progress = 1f
        set(value) {
            // 0 <= value <= 1は、その値
            // 0より小さいと0
            // 1より大きいと1
            // を返す
            field = value.coerceIn(0f, 1f)
            callback.invalidateDrawable(this)
        }

    var dotProgress = 0f
        set(value) {
            field = value.coerceIn(0f, 1f)
            callback.invalidateDrawable(this)
        }

    private val cornerEffect = CornerPathEffect(8f)

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f
        //pathEffect = cornerEffect
    }

    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xff0e0d0e.toInt()
        style = Paint.Style.FILL
    }

    // 黒ペンに小さい円のパス
    private val pathDot = Path().apply {
        // CW:時計回り
        addCircle(0f, 0f, 16f, Path.Direction.CW)
    }



    override fun draw(canvas: Canvas) {
    }

    override fun setAlpha(alpha: Int) {
        linePaint.alpha = alpha
        dotPaint.alpha = alpha
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        linePaint.colorFilter = colorFilter
        dotPaint.colorFilter = colorFilter
    }

    override fun getIntrinsicWidth() = KochTreeLapDrawable.width

    override fun getIntrinsicHeight() = KochTreeLapDrawable.height

    companion object {
        private const val width = 1000
        private const val height = 1000
        private const val cx = (width / 2).toFloat()
        private const val cy = (height / 2).toFloat()
        private val pathMeasure = PathMeasure()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    object PROGRESS: FloatProperty<KochTreeLapDrawable>("progress") {
        override fun setValue(drawable: KochTreeLapDrawable, progress: Float) {
            drawable.progress = progress
        }

        override fun get(drawable: KochTreeLapDrawable): Float = drawable.progress

    }

    @RequiresApi(Build.VERSION_CODES.N)
    object DOT_PROGRESS: FloatProperty<KochTreeLapDrawable>("dotProgress") {
        override fun setValue(drawable: KochTreeLapDrawable, dotProgress: Float) {
            drawable.dotProgress = dotProgress
        }

        override fun get(drawable: KochTreeLapDrawable): Float = drawable.dotProgress
    }
}
