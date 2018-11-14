package milu.kiriu2010.gui.decorate

import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log
import kotlin.math.*

// 1000x1000に長さ100の木を初期描画
// -----------------------------------------
//   *   *
//    * *
//     * => (500,800)
//     *
//     * => (500,900)
// -----------------------------------------
class KochTreeDrawable : Drawable() {
    // コッホツリーの頂点リスト
    val kochTreeLst: MutableList<MutableList<Pair<Float,Float>>> = mutableListOf()

    // コッホツリーの頂点パス
    val kochPathLst: MutableList<Path> = mutableListOf()

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
        // 長さ100の木の頂点を初期設定
        val kochTree: MutableList<Pair<Float,Float>> = mutableListOf()
        kochTree.add(Pair(500f,900f))
        kochTree.add(Pair(500f,750f))

        // コッホツリーの頂点リストに初期線を格納
        kochTreeLst.add(kochTree)

        // コッホ曲線を結ぶ
        createKochPath()
    }

    // コッホ曲線を結ぶ
    private fun createKochPath() {
        Log.d(javaClass.simpleName,"----------------------------")
        kochPathLst.clear()
        kochTreeLst.forEachIndexed { id1, kochTree ->
            // コッホツリー1本
            val kochPath = Path()
            kochTree.forEachIndexed { id2, pair ->
                Log.d( javaClass.simpleName, "i[$id2]x[${pair.first}]y[${pair.second}]")
                if ( id2 == 0 ) {
                    kochPath.moveTo(pair.first,pair.second)
                }
                else {
                    kochPath.lineTo(pair.first,pair.second)
                }
            }
            //kochPath.close()

            // コッホツリーリストにコッホツリー１本を格納
            kochPathLst.add(kochPath)
        }
    }

    // コッホ曲線を分割する
    fun divideKochPath() {
        // -------------------------------------------
        // C *   * D
        //    * *
        //     * B
        //     *
        //     * A
        // -------------------------------------------
        // (B-C)の長さ=(B-D)の長さ=(A-B)の長さ x ratio
        // "B-C"と"A-B"の角度は30度
        // "B-D"と"A-B"の角度は30度
        // -------------------------------------------
        // A,Bの頂点リスト
        // =>
        // A,B,Cの頂点リスト
        // A,B,Dの頂点リスト
        // と頂点リストが倍になる
        // -------------------------------------------

        // コッホツリーの頂点リスト(テンポラリ)
        val tmpKochTreeLst: MutableList<MutableList<Pair<Float,Float>>> = mutableListOf()

        // コッホツリーの頂点リストを倍にして
        // コッホツリーの頂点リスト(テンポラリ)へ格納
        kochTreeLst.forEachIndexed { id1, kochTree ->
            tmpKochTreeLst.add(kochTree.toMutableList())
            tmpKochTreeLst.add(kochTree.toMutableList())
        }

        tmpKochTreeLst.forEachIndexed { id1, tmpKochTree ->
            // コッホツリーの最後から２番目の頂点を取得
            val tmpKoch1 = tmpKochTree[tmpKochTree.size-2]
            // コッホツリーの最後の頂点を取得
            val tmpKoch2 = tmpKochTree[tmpKochTree.size-1]

            // 新しく生成する頂点が左右どちらに分かれるか
            //   -1=>左側に分かれる頂点/1=>右側に分かれる頂点
            val sign = if (id1%2 == 0) -1 else 1
            // 新しく生成する頂点の角度
            val angle = calNextAngle(tmpKoch1,tmpKoch2,sign)
            Log.d(javaClass.simpleName, "id1[$id1]angle[$angle]sign[$sign]")

            // "コッホツリーの最後の頂点"～"新しい頂点"の長さ
            val len = ratio * sqrt((tmpKoch2.first-tmpKoch1.first)*(tmpKoch2.first-tmpKoch1.first)+(tmpKoch2.second-tmpKoch1.second)*(tmpKoch2.second-tmpKoch1.second))
            // 新しい頂点のX座標
            val x = len * cos(angle*PI/180.0) + tmpKoch2.first
            // 新しい頂点のY座標
            val y = len * sin(angle*PI/180.0) + tmpKoch2.second
            // 新しい頂点
            val newKoch = Pair<Float,Float>(x.toFloat(),y.toFloat())
            // 新しい頂点をコッホツリーに加える
            tmpKochTree.add(newKoch)
        }


        // コッホツリーの頂点リストを作り直す
        kochTreeLst.clear()
        tmpKochTreeLst.forEach {
            kochTreeLst.add(it)
        }

        // コッホ曲線を結ぶ
        createKochPath()
    }

    // 新しく生成する頂点の角度を求める
    //   tmpKoch1:頂点1
    //   tmpKoch2:頂点2
    //   sign: -1=>左側に分かれる頂点/1=>右側に分かれる頂点
    private fun calNextAngle(tmpKoch1: Pair<Float,Float>, tmpKoch2: Pair<Float,Float>,sign: Int): Float {
        var angle: Float =
            // 頂点1と2のX座標が同じ場合
            if (tmpKoch1.first == tmpKoch2.first) {
                // 頂点2が頂点1より上
                if (tmpKoch1.second > tmpKoch2.second) {
                    270.0f
                }
                // 頂点2が頂点1より下
                else {
                    90.0f
                }
            }
            // 頂点2が頂点1より左の場合
            else if (tmpKoch1.first > tmpKoch2.first){
                // 頂点2が頂点1より上
                if (tmpKoch1.second > tmpKoch2.second) {
                    (atan((tmpKoch2.second-tmpKoch1.second)/(tmpKoch2.first-tmpKoch1.first))*180.0/ PI).toFloat()+180.0f
                }
                // 頂点2が頂点1より下
                else {
                    (atan((tmpKoch2.second-tmpKoch1.second)/(tmpKoch2.first-tmpKoch1.first))*180.0/ PI).toFloat()+180.0f
                }
            }
            // 頂点2が頂点1より右の場合
            else {
                // 頂点2が頂点1より上
                if (tmpKoch1.second > tmpKoch2.second) {
                    360.0f-(atan(-1.0*(tmpKoch2.second-tmpKoch1.second)/(tmpKoch2.first-tmpKoch1.first))*180.0/ PI).toFloat()
                }
                // 頂点2が頂点1より下
                else {
                    (atan((tmpKoch2.second-tmpKoch1.second)/(tmpKoch2.first-tmpKoch1.first))*180.0/ PI).toFloat()
                }
            }
        return angle + sign * baseAngle
    }

     override fun draw(canvas: Canvas) {
        // コッホツリーを描画
        kochPathLst.forEachIndexed { index, path ->
            canvas.drawPath(path,linePaint)
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
        private const val ratio = 0.8
        private const val baseAngle = 30.0f
    }
}
