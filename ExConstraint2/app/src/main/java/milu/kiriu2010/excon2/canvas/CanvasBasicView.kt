package milu.kiriu2010.excon2.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import milu.kiriu2010.excon2.R.mipmap.ic_launcher
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import milu.kiriu2010.excon2.R


class CanvasBasicView: View {
    private val mPaint = Paint()

    constructor(context: Context?): super(context) {
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if ( canvas == null ) return

        // 正方形を、ずらして３つ描画
        /*
        val rect = Rect(100, 100, 200, 200)

        canvas.drawRect(rect, mPaint)
        rect.offset(200, 0)
        canvas.drawRect(rect, mPaint)
        rect.offset(300, 0)
        canvas.drawRect(rect, mPaint)
        */

        // 画像(100x100)を描画
        /*
        val bmp = BitmapFactory.decodeResource(resources, R.drawable.android)

        // 左上1/4
        val srcRect1 = Rect(0, 0, bmp.width / 2, bmp.height / 2)
        // 右下1/4
        val srcRect2 = Rect(bmp.width / 2, bmp.height / 2,
                bmp.width, bmp.height)

        val destRect1 = Rect(0, 0, bmp.width, bmp.height)
        destRect1.offset(100, 500)
        val destRect2 = Rect(0, 0, bmp.width/2, bmp.height/2)
        destRect2.offset(400, 700)

        // 左上1/4を2倍に拡大して表示
        canvas.drawBitmap(bmp, srcRect1, destRect1, mPaint)
        // 右下1/4をそのままの大きさで表示
        canvas.drawBitmap(bmp, srcRect2, destRect2, mPaint)
        */

        // 円を原点をずらして描画
        /*
        for ( i in 0 until 5) {
            canvas.drawCircle(0f, 0f, 50f, mPaint)
            canvas.translate(100f, 100f)
        }
        */

        // 10度ずつ回転して36個の正方形を描画
        /*
        val rect = Rect( 100, 0, 150, 50 )
        for ( i in 0 until 36 ) {
            canvas.rotate(10f)
            canvas.drawRect(rect,mPaint)
        }
        */

        // 画像(100x100)を描画
        // xy方向でゆがみを設定する
        val bmp = BitmapFactory.decodeResource(resources, R.drawable.android)

        canvas.drawBitmap(bmp, 0f, 0f, mPaint)

        canvas.translate(200f, 200f)
        //canvas.skew(0.2f, -0.8f)
        canvas.skew(0.2f, 0f)

        canvas.drawBitmap(bmp, 0f, 0f, mPaint)

}
}