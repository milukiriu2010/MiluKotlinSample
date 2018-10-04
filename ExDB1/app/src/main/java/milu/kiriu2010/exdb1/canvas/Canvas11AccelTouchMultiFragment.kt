package milu.kiriu2010.exdb1.canvas


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*

import milu.kiriu2010.exdb1.R

/**
 * A simple [Fragment] subclass.
 * Use the [Canvas11AccelTouchMultiFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Canvas11AccelTouchMultiFragment : Fragment()
        , SurfaceHolder.Callback {

    // 描画に使うサーフェースビュー
    private lateinit var surfaceViewCanvas: SurfaceView

    // サーフェースビューの幅・高さ
    private var sw: Float = 0f
    private var sh: Float = 0f

    // 描画する画像
    private lateinit var bmp: Bitmap
    // 画像リスト
    private val mvLst = mutableListOf<Mover>()

    // タッチ中かどうか
    private var touched = false

    // タッチ位置のリスト
    //private val tl = PVector()
    private var tlLst = mutableListOf<PVector>()

    // 画像に使うペイント
    private val paintImage = Paint().apply {
        color = Color.BLACK
        //style = Paint.Style.STROKE
    }
    // タッチに使うペイント
    private val paintTouch = Paint().apply {
        color = Color.BLACK
        strokeWidth = 50f
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
        val view = inflater.inflate(R.layout.fragment_canvas11_accel_touch_multi, container, false)


        // サーフェースビューを取得
        surfaceViewCanvas = view.findViewById(R.id.surfaceViewCanvas)

        surfaceViewCanvas.setOnTouchListener { v, event ->
            Log.d(javaClass.simpleName, "touch.x[${event.x}]touch.y[${event.y}]")

            // タッチしているかどうかを取得
            touched = when (event.action) {
                MotionEvent.ACTION_DOWN -> true
                MotionEvent.ACTION_MOVE -> true
                MotionEvent.ACTION_UP -> false
                MotionEvent.ACTION_CANCEL -> false
                MotionEvent.ACTION_OUTSIDE -> false
                else -> false
            }

            // タッチ位置を保存
            if ( touched ) {
                val tl = PVector()
                tl.x = event.x
                tl.y = event.y
                tlLst.add(tl)
                if (tlLst.size > 30) {
                    tlLst.removeAt(0)
                }
            }

            true
        }

        val holder = surfaceViewCanvas.holder
        holder.addCallback(this)

        // 描画する画像
        bmp = BitmapFactory.decodeResource(resources,R.drawable.a_male)

        // 画像リスト作成
        (0..20).forEach { mvLst.add(Mover()) }

        runnable = Runnable {
            mvLst.forEach {
                // 画面タッチ位置
                val tl = if (tlLst.size > 0 ) {
                    tlLst[tlLst.size-1]
                }
                else {
                    PVector()
                }
                it.moveBallon( touched, tl)
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
        mvLst.forEach {
            canvas.drawBitmap(bmp, it.il.x, it.il.y, paintImage)
        }

        // タッチ箇所を描画
        tlLst.forEach {
            canvas.drawPoint(it.x,it.y,paintTouch)
        }

        surfaceViewCanvas.holder.unlockCanvasAndPost(canvas)
    }

    // SurfaceHolder.Callback
    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        Log.d(javaClass.simpleName, "surfaceChanged")

        // サーフェースビューの幅・高さを取得
        sw = width.toFloat()
        sh = height.toFloat()

        // 画像を描画する位置の初期値
        // 横：左端　縦：中央(画像の高さ分引き算)
        mvLst.forEach {
            it.il.x = sw/2 - bmp.width/2
            it.il.y = sh/2 - bmp.height/2
            // 画像の移動領域
            it.il.x1 = -bmp.width.toFloat()
            it.il.x2 = sw
            it.il.y1 = -bmp.height.toFloat()
            it.il.y2 = sh
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
         * @return A new instance of fragment Canvas11AccelTouchMultiFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                Canvas11AccelTouchMultiFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
