package milu.kiriu2010.gui.fractal

import android.graphics.*
import android.graphics.drawable.Drawable
import milu.kiriu2010.gui.basic.MyPointF

// ---------------------------------------------------------------------
// Takagi Curve
// https://ja.wikipedia.org/wiki/%E9%AB%98%E6%9C%A8%E6%9B%B2%E7%B7%9A
// ---------------------------------------------------------------------
class TakagiCurve01Drawable: Drawable() {

    // ------------------------------------------------
    // Drawing Area
    // ------------------------------------------------
    private val side = 800f
    private val margin = 50f

    // ------------------------------------------------
    // Sum points for Takagi Curve
    // ------------------------------------------------
    private val sumPointMap = mutableMapOf<Float,Float>()

    private val imageBitmap = Bitmap.createBitmap(intrinsicWidth,intrinsicHeight, Bitmap.Config.ARGB_8888)

    // ------------------------------------------------
    // Paint for frame
    // ------------------------------------------------
    private val framePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    // ------------------------------------------------
    // Paint for background
    // ------------------------------------------------
    private val backPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    // ------------------------------------------------
    // Paint for Takagi Curve
    // ------------------------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    fun cal(n: Int){
        sumPointMap.clear()

        // ---------------------------------
        // Initial Points
        // ---------------------------------
        val a = MyPointF(0f,0f)
        val b = MyPointF(side,0f)
        val c = MyPointF(side/2,side/2)

        // -----------------------------------
        // Add initial points to Takagi Curve
        // -----------------------------------
        sumPointMap.put(a.x,a.y)
        sumPointMap.put(b.x,b.y)
        sumPointMap.put(c.x,c.y)

        // ---------------------------------
        // add next level wave
        // ---------------------------------
        addWave(a,c,n)
        addWave(c,b,n)


        val canvas = Canvas(imageBitmap)
        // draw background
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),backPaint)

        // draw frame
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),framePaint)

        // move base point (0,0) => (margin,margin)
        canvas.translate(margin,margin)

        // draw Takagi Curve
        val path = Path()
        sumPointMap.keys.sorted().forEachIndexed { index, x ->
            when (index) {
                0 -> path.moveTo(x,sumPointMap.get(x) ?: 0f)
                else -> path.lineTo(x,sumPointMap.get(x) ?: 0f)
            }
        }
        canvas.drawPath(path,linePaint)
    }

    // ---------------------------------
    // add next level wave
    // ---------------------------------
    private fun addWave(a: MyPointF, b: MyPointF, n: Int) {
        if ( n == 0 ) {
            return
        }

        // ---------------------------------
        // middle point for a,b
        // ---------------------------------
        val x1 = (a.x + b.x)/2f
        val y1 = (a.y + b.y)/2f

        // ---------------------------------
        // get "height of summation" at x1
        // ---------------------------------
        val y2 = getCorrectY(x1)

        // ---------------------------------
        // sum
        // ---------------------------------
        sumPointMap.put(x1,y1+y2)

        // ---------------------------------
        // calculation next level
        // ---------------------------------
        val c = MyPointF(x1,y1)
        val d = MyPointF(a.x,0f)
        val e = MyPointF(b.x,0f)
        addWave(d,c,n-1)
        addWave(c,e,n-1)
    }

    // ---------------------------------
    // get "height of summation" at x0
    // ---------------------------------
    private fun getCorrectY(x0: Float): Float {
        val x1 = sumPointMap.keys.filter { it < x0 }.sorted().last()
        val x2 = sumPointMap.keys.filter { it > x0 }.sorted().first()

        val y1 = sumPointMap.get(x1) ?: 0f
        val y2 = sumPointMap.get(x2) ?: 0f

        return (y1+y2)/2f
    }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(imageBitmap,0f,0f,framePaint)

        /*
        // draw frame
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),framePaint)

        // move base point (0,0) => (margin,margin)
        canvas.translate(margin,margin)

        // draw Takagi Curve
        val path = Path()
        sumPointMap.keys.sorted().forEachIndexed { index, x ->
            when (index) {
                0 -> path.moveTo(x,sumPointMap.get(x) ?: 0f)
                else -> path.lineTo(x,sumPointMap.get(x) ?: 0f)
            }
        }
        canvas.drawPath(path,linePaint)
        */
    }

    override fun setAlpha(alpha: Int) {
        linePaint.alpha = alpha
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        linePaint.colorFilter = colorFilter
    }

    override fun getIntrinsicWidth(): Int = (side + margin*2).toInt()

    override fun getIntrinsicHeight(): Int = (side + margin*2).toInt()
}