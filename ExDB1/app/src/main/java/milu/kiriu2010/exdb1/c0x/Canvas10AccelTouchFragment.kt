package milu.kiriu2010.exdb1.c0x


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.*

import milu.kiriu2010.exdb1.R

/**
 * A simple [Fragment] subclass.
 * Use the [Canvas10AccelTouchFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Canvas10AccelTouchFragment : Fragment()
        , SurfaceHolder.Callback {

    // 描画に使うサーフェースビュー
    private lateinit var surfaceViewCanvas: SurfaceView

    // サーフェースビューの幅・高さ
    private var sw: Float = 0f
    private var sh: Float = 0f

    // 描画する画像
    private lateinit var bmp: Bitmap

    // 画像を描画する位置
    private val il = PVector()

    // 画像の移動速度
    //private val iv = PVector(10f,1f)
    private val iv = PVector()

    // 画像の移動加速度
    //private val ia = PVector(0.3f,0.1f)
    private val ia = PVector()

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
        val view = inflater.inflate(R.layout.fragment_canvas10_accel_touch, container, false)


        // サーフェースビューを取得
        surfaceViewCanvas = view.findViewById(R.id.surfaceViewCanvas)

        surfaceViewCanvas.setOnTouchListener { _, event ->
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

        runnable = Runnable {
            // 画面タッチされていないときは
            // 加速度をランダムに決定
            if ( touched == false ) {
                val ta = PVector().random2D()
                // random2Dは単位ベクトル化されているので倍数計算する
                ta.mult((0..10).shuffled().first().toFloat() )
                ia.set(ta)
            }
            // 画面タッチしているときは
            // 現在位置とタッチ位置を元に加速度を決定
            else {
                // 画面タッチ位置
                val tl = tlLst[tlLst.size-1]
                // 次の加速度
                val dir = PVector().set(tl)
                // "画面タッチ位置"－"現在位置"
                dir.sub(il)
                // 単位ベクトル化
                dir.normalize()
                // スケール
                //dir.mult((0..10).shuffled().first().toFloat())
                dir.mult(5f)
                ia.set(dir)
            }

            // 速度に加速度を加算する
            // 速度にリミットを設けている
            iv.add(ia,20f)
            // 移動
            il.add(iv)
            // 右端調整
            il.checkEdge()
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
        canvas.drawBitmap(bmp, il.x, il.y, paintImage)

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
        il.x = sw/2 - bmp.width/2
        il.y = sh/2 - bmp.height/2
        // 画像の移動領域
        il.x1 = -bmp.width.toFloat()
        il.x2 = sw
        il.y1 = -bmp.height.toFloat()
        il.y2 = sh
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
         * @return A new instance of fragment Canvas10AccelTouchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                Canvas10AccelTouchFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
