package milu.kiriu2010.gui.fractal

import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log
import kotlin.math.*


// ドラゴン曲線
// https://codezine.jp/article/detail/73
// -----------------------------------------
// 1000x1000に一辺500の直線を初期描画
// 真ん中に表示する
// ------------------------------------------
//     (250,500)       (750,500)
//      ***************
// ------------------------------------------
class DragonCurvDrawable: Drawable() {
    // ドラゴン曲線の頂点リスト
    val kochLst: MutableList<Pair<Float,Float>> = mutableListOf()

    // ドラゴン曲線の頂点パス
    val kochPath = Path()

    private val cornerEffect = CornerPathEffect(8f)

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f
        //pathEffect = cornerEffect
    }

    private val dotPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    init {
        // 直線の頂点を初期設定
        kochLst.add(Pair(250f,500f))
        kochLst.add(Pair(750f,500f))

        // コッホ曲線を結ぶ
        createKochPath()
    }

    // コッホ曲線を分割する
    // --------------------------------------------------
    //   A*******B
    // --------------------------------------------------
    //   A*     *B
    //     *   *     AB-ACは45度
    //      * *      AB-BCは45度
    //       *C      Cが直角の直角三角形
    // --------------------------------------------------
    //   A* E****B   AD-DCは90度
    //    *  *       DC-CEは90度
    //    *  *       CE-EBは90度
    //   D****C
    // --------------------------------------------------
    fun divideKochPath() {
        // コッホ曲線のテンポラリ
        val tmpKochLst: MutableList<Pair<Float,Float>> = mutableListOf()

        kochLst.forEachIndexed { index, pair ->
            val a = pair
            val b = when(index) {
                kochLst.size-1 -> return@forEachIndexed
                else -> kochLst[index+1]
            }

            // AB-ACの角度
            // --------------------------------
            // Dを作成するとき、45度回転
            // Eを作成するとき、-45度回転
            val angle = when (index%2) {
                0 -> 45
                else -> -45
            }

            // "A-B"の長さ
            val abLen = sqrt((b.first-a.first)*(b.first-a.first)+(b.second-a.second)*(b.second-a.second))
            // "A-C"の長さ="A-B"の長さ/sqrt(2)
            val acLen = abLen/ sqrt(2f)
            // "A-B"の角度(degree)
            val abAngle = when {
                // BがAより右下
                (b.second >= a.second) and (b.first >= a.first) -> {
                    atan((b.second-a.second)/(b.first-a.first) ) *180.0/ PI
                }
                // BがAより右上
                (b.second < a.second) and (b.first >= a.first) -> {
                    (2.0* PI + atan((b.second-a.second)/(b.first-a.first) ))*180.0/ PI
                }
                // BがAより左下
                (b.second >= a.second) and (b.first < a.first) -> {
                    (PI - atan(-(b.second-a.second)/(b.first-a.first) ))*180.0/ PI
                }
                // BがAより左上
                //(b.second < a.second) and (b.first < a.first) -> {
                else -> {
                    (PI + atan((b.second-a.second)/(b.first-a.first) ))*180.0/ PI
                }
            }
            // "A-C"の角度(degree)
            val acAngle = abAngle + angle
            // Cのx座標
            val cX = a.first + acLen * cos(acAngle*PI/180f)
            // Cのy座標
            val cY = a.second + acLen * sin(acAngle*PI/180f)
            // C
            val c = Pair(cX.toFloat(),cY.toFloat())

            // テンポラリにA,Cを加える
            tmpKochLst.addAll(arrayOf(a,c))
        }
        // テンポラリにBを加える
        tmpKochLst.add(kochLst[kochLst.size-1])

        // ドラゴン曲線の頂点リストを作り直す
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
        //kochPath.close()
    }

    override fun draw(canvas: Canvas) {
        // ドラゴン曲線を描く
        canvas.drawPath(kochPath,linePaint)

        // ドラゴン曲線の両端上に赤点を描画
        val kochFirst = kochLst[0]
        canvas.drawPoint(kochFirst.first,kochFirst.second,dotPaint)
        val kochLast = kochLst[kochLst.size-1]
        canvas.drawPoint(kochLast.first,kochLast.second,dotPaint)
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