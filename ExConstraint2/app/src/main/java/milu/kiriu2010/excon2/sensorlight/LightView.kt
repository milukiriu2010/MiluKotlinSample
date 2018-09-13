package milu.kiriu2010.excon2.sensorlight

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.log10

// http://ojed.hatenablog.com/entry/2015/12/05/161013
// カスタムビューでは
//   onSizeChanged
//   onDraw
// を継承する必要がある
class LightView: View {
    // 照度
    var lux: Float = 0f

    // スケール
    private val scale = Rect()
    // スケールのマージン(Top)
    private val scaleMarginT = 50
    // スケールのマージン(Left)
    private val scaleMarginL = 50
    // スケールの幅
    private val scaleWidth = 20
    // スケールのペイント
    private val scalePaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    // スケール位置
    private val pos = Path()
    // スケール位置のマージン(Left)
    private val posMarginL = 100f
    // スケール位置の幅
    private val posWidth = 30f
    // スケール位置の高さ
    private val posHeight = 15f
    // スケール位置のペイント
    private val posPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    constructor(context: Context?): super(context) {
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
    }

    init {
        // スケール
        scale.top = scaleMarginT
        scale.bottom = width - scaleMarginT
        scale.left = scaleMarginL
        scale.right = scaleMarginL + scaleWidth

        // スケール位置
        // 左
        pos.moveTo( posMarginL, scaleMarginT.toFloat())
        // 右上
        pos.lineTo( (posMarginL+posWidth), (scaleMarginT-posHeight) )
        // 右下
        pos.lineTo( (posMarginL+posWidth), (scaleMarginT+posHeight) )
        pos.close()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        scale.top = scaleMarginT
        scale.bottom = w - scaleMarginT
    }

    override fun onDraw(canvas: Canvas?) {
        //super.onDraw(canvas)
        // 描画クリア
        //canvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        // バックグラウンドを白く塗りつぶす
        canvas?.drawColor(Color.WHITE)

        // スケールを塗りつぶす
        canvas?.drawRect( scale, scalePaint )

        canvas?.drawPath( pos, posPaint )

    }

    fun movePos() {
        // 照度を対数に変換
        val luxNow = log10(lux)
        // スケールの高さ
        val scaleHeight = scale.height()
        // 一番強い照度が対数で5.079
        // 一番弱い照度が対数で-3
        val luxPos =  ( 5 - luxNow )/8 * scaleHeight + scaleMarginT

        Log.d( javaClass.simpleName, "luxNow={$luxNow}luxPos=${luxPos}" )

        // スケール位置
        // 1回リセットしないと、
        // すべてのパスが保たれて、複数の三角形が表示されてしまう
        pos.reset()
        // 左
        pos.moveTo( posMarginL, luxPos)
        // 右上
        pos.lineTo( (posMarginL+posWidth), (luxPos-posHeight) )
        // 右下
        pos.lineTo( (posMarginL+posWidth), (luxPos+posHeight) )
        pos.close()
    }
}