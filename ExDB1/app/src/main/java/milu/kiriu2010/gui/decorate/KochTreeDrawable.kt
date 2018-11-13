package milu.kiriu2010.gui.decorate

import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log

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
        kochTree.add(Pair(500f,800f))

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
            kochPath.close()

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


        }


        // コッホツリーの頂点リストを作り直す
        kochTreeLst.clear()
        tmpKochTreeLst.forEach {
            kochTreeLst.add(it)
        }

        // コッホ曲線を結ぶ
        createKochPath()
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
    }
}
