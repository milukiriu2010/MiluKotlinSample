package milu.kiriu2010.exdb1.canvas


import android.graphics.*
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*

import milu.kiriu2010.exdb1.R

/**
 * A simple [Fragment] subclass.
 * Use the [Canvas14LiquidFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Canvas14LiquidFragment : Fragment()
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

    // 液体
    private val liquid = Liquid( c = 0.01f )

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
    // 液体に使うペイント
    private val paintLiquid = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.FILL_AND_STROKE
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
        val view = inflater.inflate(R.layout.fragment_canvas14_liquid, container, false)


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
        (1..10).reversed().forEach {
            // 質量をランダムに変える
            // 0.2～2.0
            //val mover = Mover( mass = (1..10).shuffled().first()/5f )
            val mover = Mover( mass = it.toFloat()/5f )
            mvLst.add(mover)
        }

        // 力(重力)
        val gravity = PVector( 0f, 5f )

        // 力を加える
        mvLst.forEach {
            it.applyForce(gravity)
        }

        runnable = Runnable {
            mvLst.forEach {
                // 液体の中にいた場合、
                // 物体に加わる力を
                // "drag+重力"を合わせて再計算
                it.ia.set( PVector() )
                if (it.isInside(liquid)) {
                    //Log.d(javaClass.simpleName, "mass[{${it.mass}}]inside")
                    it.drag(liquid)
                }
                it.applyForce(gravity)

                // 移動
                it.moveReflect()
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

        // 液体を描画
        canvas.drawRect(liquid.x, liquid.y, liquid.x+liquid.w, liquid.y+liquid.h, paintLiquid)

        // 画像を描画
        mvLst.forEach {
            // 元画像を質量によって大きさを変える
            val dst = Rect(it.il.x.toInt(),
                    it.il.y.toInt(),
                    it.il.x.toInt()+(bmp.width*it.mass).toInt(),
                    it.il.y.toInt()+(bmp.height*it.mass).toInt())
            canvas.drawBitmap(bmp, null, dst, paintImage)
        }

        // タッチ箇所を描画
        //tlLst.forEach {
        //    canvas.drawPoint(it.x,it.y,paintTouch)
        //}

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
        var i = 0f
        mvLst.forEach {
            // 横の初期位置をずらす
            it.il.x = bmp.width/2f + (i++)*100f
            it.il.y = bmp.height/2f
            // 画像の移動領域
            it.il.x1 = -bmp.width.toFloat()*it.mass
            it.il.x2 = sw-bmp.width.toFloat()*it.mass
            it.il.y1 = -bmp.height.toFloat()*it.mass
            it.il.y2 = sh-bmp.height.toFloat()*it.mass
        }

        // 液体の大きさを設定する
        // 画面下半分を液体とする
        liquid.x = 0f
        liquid.y = (height/2).toFloat()
        liquid.w = width.toFloat()
        liquid.h = (height/2).toFloat()
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
         * @return A new instance of fragment Canvas14LiquidFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                Canvas14LiquidFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
