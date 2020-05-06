package milu.kiriu2010.exdb1.c0x


import android.graphics.*
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.*

import milu.kiriu2010.exdb1.R

/**
 * A simple [Fragment] subclass.
 * Use the [Canvas02RotateYFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Canvas02RotateYFragment : Fragment()
        , SurfaceHolder.Callback {
    // 描画に使うサーフェースビュー
    private lateinit var surfaceViewCanvas: SurfaceView

    // サーフェースビューの幅・高さ
    private var sw: Float = 0f
    private var sh: Float = 0f

    // 描画する画像
    private lateinit var bmp: Bitmap

    // 物体リスト
    private val mvLst = mutableListOf<Mover>()

    // 画像に使うペイント
    private val paintImage = Paint().apply {
        color = Color.BLACK
        //style = Paint.Style.STROKE
    }

    // 描画に使うハンドラ
    val handler = Handler()
    // 描画に使うスレッド
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_canvas02_rotate_y, container, false)

        // サーフェースビューを取得
        surfaceViewCanvas = view.findViewById(R.id.surfaceViewCanvas)

        val holder = surfaceViewCanvas.holder
        holder.addCallback(this)

        // 描画する画像
        bmp = BitmapFactory.decodeResource(resources,R.drawable.a_male)

        // 物体リスト作成
        (3..7).reversed().forEach {
            val mover = Mover( mass = it.toFloat()/5f )

            // 回転角度(位置)
            mover.al = PAngle( x= 180.0f, y = 180.0f )
            // 回転角度(刻み)
            mover.av = PAngle( y = 10.0f )

            mvLst.add(mover)
        }

        runnable = Runnable {
            mvLst.forEach {
                // 回転する
                it.rotateByCenter()
            }

            drawCanvas()

            handler.postDelayed( runnable, 50)
        }
        handler.post(runnable)

        return view
    }


    // 描画
    private fun drawCanvas() {
        val canvas = surfaceViewCanvas.holder.lockCanvas()
        if ( canvas == null ) return
        //Log.d(javaClass.simpleName, "x[$il.x]y[$il.y]")

        // バックグラウンドを描画
        canvas.drawColor(Color.WHITE)

        // 画像を描画
        mvLst.forEachIndexed { _, mover ->
            val tmpX = mover.il.x
            val tmpY = mover.il.y
            val tmpW = mover.widthByRotate()
            val tmpH = mover.heightByRotate()

            val matrix = Matrix()
            // 左右反転
            if ( tmpW < 0 ) {
                matrix.preScale(-1f, 1f)
                //if ( index == 0 ) Log.d(javaClass.simpleName, "[{$index}]L<=>R")
            }
            // 上下反転
            if ( tmpH < 0 ) {
                matrix.preScale( 1f, -1f )
                //if ( index == 0 ) Log.d(javaClass.simpleName, "T<=>B")
            }
            val tmpB = Bitmap.createBitmap(bmp, 0,0, bmp.width, bmp.height, matrix, false)
            val dst = if ( (tmpW >= 0) and (tmpH >= 0) ) {
                RectF(tmpX,tmpY,tmpX+tmpW,tmpY+tmpH)
            }
            else if ( (tmpW < 0) and (tmpH >= 0) ) {
                RectF(tmpX+tmpW,tmpY,tmpX,tmpY+tmpH)
            }
            else if ( (tmpW >= 0) and (tmpH < 0) ) {
                RectF(tmpX,tmpY+tmpH,tmpX+tmpW,tmpY)
            }
            else {
                RectF(tmpX+tmpW,tmpY+tmpH,tmpX,tmpY)
            }
            //if ( index == 0 ) Log.d(javaClass.simpleName, "[{$index}][${dst}]")
            canvas.drawBitmap(tmpB, null, dst, paintImage)
        }

        surfaceViewCanvas.holder.unlockCanvasAndPost(canvas)
    }

    // SurfaceHolder.Callback
    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        Log.d(javaClass.simpleName, "surfaceChanged")

        // サーフェースビューの幅・高さを取得
        sw = width.toFloat()
        sh = height.toFloat()

        // 物体を描画する位置の初期値
        // 横：左端　縦：中央(画像の高さ分引き算)
        //var i = 0f
        mvLst.forEach {
            // 物体の位置
            it.il.x = bmp.width/2f + (1..5).shuffled().first().toFloat()*150f
            it.il.y = bmp.height/2f+ (1..5).shuffled().first().toFloat()*150f
            // 物体の幅・高さ
            it.w = bmp.width.toFloat()*it.mass
            it.h = bmp.height.toFloat()*it.mass
            // 物体の移動領域
            it.il.x1 = -bmp.width.toFloat()*it.mass
            it.il.x2 = sw-bmp.width.toFloat()*it.mass
            it.il.y1 = -bmp.height.toFloat()*it.mass
            it.il.y2 = sh-bmp.height.toFloat()*it.mass
        }

    }

    // SurfaceHolder.Callback
    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    // SurfaceHolder.Callback
    override fun surfaceCreated(holder: SurfaceHolder?) {
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment Canvas02RotateYFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                Canvas02RotateYFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
