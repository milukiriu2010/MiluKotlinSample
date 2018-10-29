package milu.kiriu2010.gui.decorate

import android.graphics.*
import android.graphics.drawable.Drawable
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
class KochSnowFlakeDrawable: Drawable() {
    // コッホ雪印の頂点リスト
    val kochLst: MutableList<Pair<Float,Float>> = mutableListOf()

    // コッホ雪印の頂点パス
    val kochPath = Path()

    private val cornerEffect = CornerPathEffect(8f)

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f
        //pathEffect = cornerEffect
    }

    private val dotPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    init {
        val y1 = 500 - 250/sqrt(3f)
        val y3 = 500 + 250*2/sqrt(3f)

        // 三角形の頂点を初期設定
        kochLst.add( Pair(250f,y1) )
        kochLst.add( Pair(750f,y1) )
        kochLst.add( Pair(500f,y3) )

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

        // コッホ曲線を結ぶ
        createKochPath()
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

    override fun draw(canvas: Canvas) {
        canvas.drawPath(kochPath,linePaint)

        canvas.drawPoint( 500f, 500f, dotPaint )
        (0..2).forEach {
            canvas.drawPoint(
                    500f+100f*cos(it.toFloat()*30f*PI/180f).toFloat(),
                    500f+100f*sin(it.toFloat()*30f*PI/180f).toFloat(),
                    dotPaint )
        }
    }

    override fun setAlpha(alpha: Int) {
        linePaint.alpha = alpha
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        linePaint.colorFilter = colorFilter
    }

    override fun getIntrinsicWidth() = width

    override fun getIntrinsicHeight() = height

    companion object {
        private const val width = 1000
        private const val height = 1000
    }
}