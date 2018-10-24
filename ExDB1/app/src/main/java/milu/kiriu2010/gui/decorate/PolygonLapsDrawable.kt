package milu.kiriu2010.gui.decorate

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.ComposePathEffect
import android.graphics.CornerPathEffect
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Paint.Style.FILL
import android.graphics.Paint.Style.STROKE
import android.graphics.Path
import android.graphics.PathDashPathEffect
import android.graphics.PathDashPathEffect.Style.TRANSLATE
import android.graphics.PathMeasure
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.FloatProperty
import android.util.Log
import java.lang.Math.PI

class PolygonLapsDrawable : Drawable() {

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

    private val polygons = listOf(
            Polygon(15, 0xffe84c65.toInt(), 362f, 2),
            Polygon(14, 0xffe84c65.toInt(), 338f, 3),
            Polygon(13, 0xffd554d9.toInt(), 314f, 4),
            Polygon(12, 0xffaf6eee.toInt(), 292f, 5),
            Polygon(11, 0xff4a4ae6.toInt(), 268f, 6),
            Polygon(10, 0xff4294e7.toInt(), 244f, 7),
            Polygon(9, 0xff6beeee.toInt(), 220f, 8),
            Polygon(8, 0xff42e794.toInt(), 196f, 9),
            Polygon(7, 0xff5ae75a.toInt(), 172f, 10),
            Polygon(6, 0xffade76b.toInt(), 148f, 11),
            Polygon(5, 0xffefefbb.toInt(), 128f, 12),
            Polygon(4, 0xffe79442.toInt(), 106f, 13),
            Polygon(3, 0xffe84c65.toInt(), 90f, 14)
    )

    private val cornerEffect = CornerPathEffect(8f)

    private val linePaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = STROKE
        strokeWidth = 4f
        pathEffect = cornerEffect
    }

    private val dotPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = 0xff0e0d0e.toInt()
        style = FILL
    }

    // 黒ペンに小さい円のパス
    private val pathDot = Path().apply {
        // CW:時計回り
        addCircle(0f, 0f, 8f, Path.Direction.CW)
    }

    override fun draw(canvas: Canvas) {

        // 多角形を描く
        polygons.forEach { polygon ->
            linePaint.color = polygon.color
            if (progress < 1f) {
                // 第１引数 => OFF
                // 第２引数 => ON
                // 第３引数 => OFF
                // --------------------------------
                // 三角形
                // 0.0 => 0.0, 467.65,    0.0, 389.7
                // 0.5 => 0.0,       ,       , 389.7
                // 1.0 => 0.0,   0.0 , 467.65, 389.7
                // --------------------------------
                // 六角形
                // 0.0 => 0.0, 888.0,   0.0, 814.0
                // 0.5 => 0.0,      ,      , 814.0
                // 1.0 => 0.0,   0.0, 888.0, 814.0
                // --------------------------------
                val progressEffect = DashPathEffect(
                        floatArrayOf(0f,
                                // 後半(これによりPathとは逆回り(右回り)で描いてるようにみえる)
                                (1f - progress) * polygon.length,
                                // 前半
                                progress * polygon.length,
                                0f),
                        polygon.initialPhase)
                linePaint.pathEffect = ComposePathEffect(progressEffect, cornerEffect)
            }
            canvas.drawPath(polygon.path, linePaint)
        }

        // ドットを描く
        // loop separately to ensure the dots are on top
        polygons.forEach { polygon ->
            // --------------------------------------
            // 下辺ど真ん中を
            // "初期の描画オフセット位置"とするっぽい
            // polygon.length * polygon.lapsは
            // ドットの移動距離
            // --------------------------------------
            // 三角形
            //   0.0 => 389.71+0.0*467.65*14
            //   0.5 => 389.71+0.5*467.65*14
            //   1.0 => 389.71+1.0*467.65*14
            // --------------------------------------
            // 六角形
            //   0.0 => 814.0+0.0*888.0*11
            //   0.5 => 814.0+0.5*888.0*11
            //   1.0 => 814.0+1.0*888.0*11
            val phase = polygon.initialPhase + dotProgress * polygon.length * polygon.laps
            // ---------------------------------------------
            // スタンプを押す
            // ---------------------------------------------
            // pathDot: スタンプに使う黒ペンに小さい円
            // polygon.length: スタンプ間のスペース？
            // phase: 最初のスタンプ位置のオフセット？
            // TRANSLATE: 平行移動？
            // ---------------------------------------------
            dotPaint.pathEffect = PathDashPathEffect(pathDot, polygon.length, phase, TRANSLATE)
            canvas.drawPath(polygon.path, dotPaint)
        }
    }

    override fun setAlpha(alpha: Int) {
        linePaint.alpha = alpha
        dotPaint.alpha = alpha
    }

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter) {
        linePaint.colorFilter = colorFilter
        dotPaint.colorFilter = colorFilter
    }

    override fun getIntrinsicWidth() = width

    override fun getIntrinsicHeight() = height

    private class Polygon(val sides: Int, val color: Int, radius: Float, val laps: Int) {
        val path = createPath(sides, radius)
        // 多角形で使われている全体のパスの長さ？
        //   三角形 => 467.65(=90*sqrt(3)*3)
        //   四角形 => 599.62(=106*sqrt(2)*4)
        //   五角形 => 752.36
        //   六角形 => 888.00(=148*6)
        val length by lazy(LazyThreadSafetyMode.NONE) {
            pathMeasure.setPath(path, false)
            Log.d( javaClass.simpleName, "sides[$sides]length[${pathMeasure.length}]")
            pathMeasure.length
        }
        // 三角形 => (1-(1/(2*3)))*467.65 = 389.71
        // 四角形 => (1-(1/(2*4)))*599.62 = 524.67
        // 六角形 => (1-(1/(2*6)))*888    = 814
        val initialPhase by lazy(LazyThreadSafetyMode.NONE) {
            Log.d( javaClass.simpleName, "sides[$sides]initialPhase[${(1f - (1f / (2 * sides))) * length}]")

            (1f - (1f / (2 * sides))) * length
        }

        private fun createPath(sides: Int, radius: Float): Path {
            val path = Path()
            // 三角形 120
            // 四角形  90
            // 五角形  72
            // 六角形  60
            val angle = 2.0 * PI / sides
            // 三角形 90+60
            // 四角形 90+45
            // 五角形 90+36
            // 六角形 90+30
            val startAngle = PI / 2.0 + Math.toRadians(360.0 / (2 * sides))
            // 下ちょい左を描画スタート地点とするっぽい
            path.moveTo(
                    cx + (radius * Math.cos(startAngle)).toFloat(),
                    cy + (radius * Math.sin(startAngle)).toFloat())
            // 左回りに描いていく
            //for (i in 1 until 6) {
            for (i in 1 until sides) {
                path.lineTo(
                        cx + (radius * Math.cos(startAngle - angle * i)).toFloat(),
                        cy + (radius * Math.sin(startAngle - angle * i)).toFloat())
            }
            path.close()
            return path
        }
    }

    companion object {
        private const val width = 1080
        private const val height = 1080
        private const val cx = (width / 2).toFloat()
        private const val cy = (height / 2).toFloat()
        private val pathMeasure = PathMeasure()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    object PROGRESS : FloatProperty<PolygonLapsDrawable>("progress") {
        override fun setValue(drawable: PolygonLapsDrawable, progress: Float) {
            //Log.d( javaClass.simpleName, "progress[$progress]")
            drawable.progress = progress
        }

        override fun get(drawable: PolygonLapsDrawable) = drawable.progress
    }

    @RequiresApi(Build.VERSION_CODES.N)
    object DOT_PROGRESS : FloatProperty<PolygonLapsDrawable>("dotProgress") {
        override fun setValue(drawable: PolygonLapsDrawable, dotProgress: Float) {
            drawable.dotProgress = dotProgress
        }

        override fun get(drawable: PolygonLapsDrawable) = drawable.dotProgress
    }

}
