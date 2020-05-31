package milu.kiriu2010.excon2.a0x.a19

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import milu.kiriu2010.excon2.R

// ドロイド君を表示するカスタマイズビュー
class CanvasBasicView: View {
    private val mPaint = Paint()

    var mode = 0

    var skewX = 0.0f
    var skewY = 0.0f

    var yval = 0

    constructor(context: Context?): super(context) {
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if ( canvas == null ) return

        when ( mode ) {
            0 -> drawMode0(canvas)
            1 -> drawMode1(canvas)
            2 -> drawMode2(canvas)
            3 -> drawMode3(canvas)
            4 -> drawMode4(canvas)
            5 -> drawMode5(canvas)
            6 -> drawMode6(canvas)
            7 -> drawMode7(canvas)
            else -> drawMode0(canvas)
        }

    }

    private fun drawMode0(canvas: Canvas) {
        // 画像(100x100)を描画
        // xy方向でゆがみを設定する
        canvas.save()
        val bmp = BitmapFactory.decodeResource(resources, R.drawable.ic_a19)
        canvas.drawBitmap(bmp, 300f, 0f, mPaint)

        canvas.translate(300f, 500f)
        //canvas.skew(0.2f, -0.8f)
        canvas.skew(skewX, skewY)

        canvas.drawBitmap(bmp, 0f, 0f, mPaint)

        canvas.restore()
    }

    // 正方形を、ずらして３つ描画
    private fun drawMode1(canvas: Canvas) {
        val rect = Rect(100, 100, 200, 200)

        canvas.drawRect(rect, mPaint)
        rect.offset(200, 0)
        canvas.drawRect(rect, mPaint)
        rect.offset(300, 0)
        canvas.drawRect(rect, mPaint)
    }

    // ドロイド君の画像(100x100)を
    // 左上1/4を2倍に拡大して表示
    // 右下1/4をそのままの大きさで表示
    private fun drawMode2(canvas: Canvas) {
        // 画像(100x100)を描画
        val bmp = BitmapFactory.decodeResource(resources, R.drawable.ic_a19)

        // 左上1/4
        val srcRect1 = Rect(0, 0, bmp.width / 2, bmp.height / 2)
        // 右下1/4
        val srcRect2 = Rect(bmp.width / 2, bmp.height / 2,
                bmp.width, bmp.height)

        // (100,500) - (200,600)
        val destRect1 = Rect(0, 0, bmp.width, bmp.height)
        destRect1.offset(100, 500)
        // (400,700) - (450,750)
        val destRect2 = Rect(0, 0, bmp.width/2, bmp.height/2)
        destRect2.offset(400, 700)

        // そのままの大きさを左上に描画
        canvas.drawBitmap(bmp,0f,0f,mPaint)
        // 左上1/4を2倍に拡大して表示
        canvas.drawBitmap(bmp, srcRect1, destRect1, mPaint)
        // 右下1/4をそのままの大きさで表示
        canvas.drawBitmap(bmp, srcRect2, destRect2, mPaint)
    }

    // 円を原点をずらして描画
    private fun drawMode3(canvas: Canvas) {
        for ( i in 0 until 5) {
            canvas.drawCircle(0f, 0f, 50f, mPaint)
            canvas.translate(100f, 100f)
        }
    }

    // 10度ずつ回転して36個の正方形を描画
    private fun drawMode4(canvas: Canvas) {
        canvas.translate(500f, 500f)

        val rect = Rect( 100, 0, 150, 50 )
        for ( i in 0 until 36 ) {
            canvas.rotate(10f)
            canvas.drawRect(rect,mPaint)
        }
    }

    // saveLayerで領域の一部をクリッピング
    // ドロイド君が切れてみえるようになる
    private fun drawMode5(canvas: Canvas) {
        val bmp = BitmapFactory.decodeResource(resources, R.drawable.ic_a19)
        // saveLayerで領域の一部をクリッピング
        val bounds = RectF(0f, 0f, 300f, 300f)
        canvas.saveLayer(bounds, mPaint)

        // バックグラウンドを緑
        canvas.drawColor(Color.GREEN)

        canvas.drawBitmap(bmp, 100f, 200f, mPaint)

        // saveLayer直前に戻る
        canvas.restore()

        canvas.drawBitmap(bmp, 200f, 200f, mPaint)
    }

    // スピナ―で、ドロイド君を拡大・縮小表示
    private fun drawMode6(canvas: Canvas) {
        // 画像(SVG)を描画
        // java.lang.NullPointerException: Attempt to invoke virtual method 'boolean android.graphics.Bitmap.isRecycled()' on a null object reference
        // val bmp = BitmapFactory.decodeResource(resources, R.drawable.xic_a19)
        // 何も表示されない
        //val drawable = ContextCompat.getDrawable(context,R.drawable.xic_a19)
        //val bmp = Bitmap.createBitmap(drawable!!.intrinsicWidth, drawable!!.intrinsicHeight, Bitmap.Config.ARGB_8888)
        //Log.d( javaClass.simpleName, "dw[${drawable.intrinsicWidth}]/dh[${drawable.intrinsicHeight}]")
        //Log.d( javaClass.simpleName, "bw[${bmp.width}]/h[${bmp.height}]")
        //canvas.drawBitmap(bmp, 300f, 300f, mPaint)

        val drawable = ContextCompat.getDrawable(context,R.drawable.ic_a19)
        // (100,100)位置にx倍で表示
        // skewX -5.0 - 5.0
        drawable?.setBounds(100,100,
                100+(drawable.intrinsicWidth*(4+skewX)).toInt(),100+(drawable.intrinsicHeight*(4+skewY)).toInt())
        drawable?.draw(canvas)
    }

    // A19Activityでは、なにもしない
    // A22Activityで、アニメーションする
    private fun drawMode7(canvas: Canvas) {
        canvas.drawRect(400f, (100+yval).toFloat(), 600f, (300+yval).toFloat(), mPaint);
    }
}
