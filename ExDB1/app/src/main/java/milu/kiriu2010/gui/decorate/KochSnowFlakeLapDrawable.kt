package milu.kiriu2010.gui.decorate

import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.FloatProperty
import android.util.Log
import kotlin.math.*

// 1000x1000に一辺500の三角形を初期描画
// ------------------------------------------
//   高さ=500/2*sqrt(3)=433.0127
//   真ん中に表示したいので
//   y1 = y2 = 500 - 250*1/sqrt(3) = 355.662
//   y3      = 500 + 250*2/sqrt(3) = 788.675
// ------------------------------------------
//     (250,y1)       (750,y2)
//      ***************
//       *           *
//        *         *
//         *       *
//          *     *
//           *   *
//            * *
//             *
//            (500,y3)
// ------------------------------------------
class KochSnowFlakeLapDrawable: Drawable() {
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

    private val snowflakes = listOf(
            SnowFlake(0xffe84c65.toInt(), 80f,5),
            SnowFlake(0xffd554d9.toInt(), 160f,4),
            SnowFlake(0xffaf6eee.toInt(), 240f,3),
            SnowFlake(0xff4a4ae6.toInt(), 320f,2),
            SnowFlake(0xff4294e7.toInt(), 400f,1)
    )

    override fun draw(canvas: Canvas) {
        // 雪片を描画
        snowflakes.forEach { snowflake ->
            linePaint.color = snowflake.color
            if (progress <1f) {
                val progressEffect = DashPathEffect(
                        floatArrayOf(0f,
                                // 後半(これによりPathとは逆回り(左回り)で描いてるようにみえる)
                                (1f-progress) * snowflake.length,
                                // 前半
                                progress * snowflake.length,
                                0f),
                        snowflake.initialPhase)
                linePaint.pathEffect = ComposePathEffect(progressEffect,cornerEffect)
            }
            canvas.drawPath(snowflake.kochPath,linePaint)
        }

        // ドットを描く
        snowflakes.forEach { snowflake ->
            // ドットを描く位置
            val phase = snowflake.initialPhase + dotProgress * snowflake.length * snowflake.laps
            // ---------------------------------------------
            // スタンプを押す
            // ---------------------------------------------
            // pathDot: スタンプに使う黒ペンに小さい円
            // polygon.length: スタンプ間のスペース？
            // phase: 最初のスタンプ位置のオフセット？
            // TRANSLATE: 平行移動？
            // ---------------------------------------------
            dotPaint.pathEffect = PathDashPathEffect(pathDot, snowflake.length, phase, PathDashPathEffect.Style.TRANSLATE)
            canvas.drawPath(snowflake.kochPath, dotPaint)
        }
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

    override fun getIntrinsicWidth() = width

    override fun getIntrinsicHeight() = height

    private class SnowFlake(val color: Int, val radius: Float, val laps: Int) {
        // コッホ雪印の頂点リスト
        val kochLst: MutableList<Pair<Float,Float>> = mutableListOf()

        // コッホ雪印の頂点パス
        val kochPath = Path()

        // パス全体の長さ
        val length by lazy(LazyThreadSafetyMode.NONE) {
            pathMeasure.setPath(kochPath,false)
            pathMeasure.length
        }

        // ドット初期位置
        // 全体のパスの5/6にすることにより、
        // 初期位置を頂点にもっていってる
        val initialPhase by lazy(LazyThreadSafetyMode.NONE) {
            length*5/6
        }

        init {
            val y1 = cy - radius/sqrt(3f)
            val y3 = cy + radius*2/sqrt(3f)

            // 三角形の頂点を初期設定
            kochLst.add( Pair(cy-radius,y1) )
            kochLst.add( Pair(cy+radius,y1) )
            kochLst.add( Pair(cy,y3) )

            // コッホ曲線を分割する(3回実施)
            (0..2).forEach { divideKochPath() }

            // コッホ曲線を結ぶ
            createKochPath()
        }

        // コッホ曲線を分割する
        fun divideKochPath() {
            // コッホ曲線のテンポラリ
            val tmpKochLst: MutableList<Pair<Float,Float>> = mutableListOf()

            // --------------------------------------------------
            //       E
            //       *
            //      * *
            //   ***   ***
            //   A C   D B
            // --------------------------------------------------
            //   C = A+(B-A)*1/3
            //   D = A+(B-A)*2/3
            //   "A-E"の長さは(B-A)/2*2/sqrt(3)
            //   "A-E"と"A-B"の角度は30度
            // --------------------------------------------------
            kochLst.forEachIndexed { index, pair ->
                val a = pair
                val b = when (index) {
                    kochLst.size-1 -> kochLst[0]
                    else -> kochLst[index+1]
                }
                val c = Pair(a.first + (b.first-a.first)/3f,a.second + (b.second-a.second)/3f)
                val d = Pair(a.first + (b.first-a.first)*2f/3f,a.second + (b.second-a.second)*2f/3f)

                // "A-E"の長さ
                //   = (B-A)/2*2/sqrt(3)
                val aeLen = sqrt((b.first-a.first)*(b.first-a.first)+(b.second-a.second)*(b.second-a.second))/sqrt(3f)
                // "A-B"の角度(degree)
                //val abAngle = atan(-(b.second-a.second)/(b.first-a.first) )*180f/ PI
                val abAngle = when {
                // BがAより右下
                    (b.second >= a.second) and (b.first >= a.first) -> {
                        atan((b.second-a.second)/(b.first-a.first) )*180.0/ PI
                    }
                // BがAより右上
                    (b.second < a.second) and (b.first >= a.first) -> {
                        (2.0*PI+atan((b.second-a.second)/(b.first-a.first) ))*180.0/ PI
                    }
                // BがAより左下
                    (b.second >= a.second) and (b.first < a.first) -> {
                        (PI-atan(-(b.second-a.second)/(b.first-a.first) ))*180.0/ PI
                    }
                // BがAより左上
                //(b.second < a.second) and (b.first < a.first) -> {
                    else -> {
                        (PI+atan((b.second-a.second)/(b.first-a.first) ))*180.0/ PI
                    }
                }
                // "A-E"の角度(degree)
                //   = "A-B"の角度+30度
                val aeAngle = abAngle - 30f
                // Eのx座標
                val eX = a.first + aeLen * cos(aeAngle*PI/180f)
                // Eのy座標
                val eY = a.second + aeLen * sin(aeAngle*PI/180f)
                // E
                val e = Pair(eX.toFloat(),eY.toFloat())

                // テンポラリにA,C,E,Dを加える
                tmpKochLst.addAll(arrayOf<Pair<Float,Float>>(a,c,e,d))
            }

            // コッホ雪印の頂点リストを作り直す
            kochLst.clear()
            tmpKochLst.forEach {
                kochLst.add(it)
            }

        }

        // コッホ曲線を結ぶ
        private fun createKochPath() {
            kochPath.reset()
            kochLst.forEachIndexed { index, pair ->
                Log.d( javaClass.simpleName, "i[$index]x[${pair.first}]y[${pair.second}]")
                if ( index == 0 ) {
                    kochPath.moveTo(pair.first,pair.second)
                }
                else {
                    kochPath.lineTo(pair.first,pair.second)
                }
            }
            kochPath.close()
        }
    }

    companion object {
        private const val width = 1000
        private const val height = 1000
        private const val cx = (width / 2).toFloat()
        private const val cy = (height / 2).toFloat()
        private val pathMeasure = PathMeasure()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    object PROGRESS: FloatProperty<KochSnowFlakeLapDrawable>("progress") {
        override fun setValue(drawable: KochSnowFlakeLapDrawable, progress: Float) {
            drawable.progress = progress
        }

        override fun get(drawable: KochSnowFlakeLapDrawable): Float = drawable.progress

    }

    @RequiresApi(Build.VERSION_CODES.N)
    object DOT_PROGRESS: FloatProperty<KochSnowFlakeLapDrawable>("dotProgress") {
        override fun setValue(drawable: KochSnowFlakeLapDrawable, dotProgress: Float) {
            drawable.dotProgress = dotProgress
        }

        override fun get(drawable: KochSnowFlakeLapDrawable): Float = drawable.dotProgress
    }
}