package milu.kiriu2010.exdb1.c0x


import android.graphics.*
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.*

import milu.kiriu2010.exdb1.R

// SurfaceView上で力を表現
class C12Fragment : Fragment()
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
        val view = inflater.inflate(R.layout.fragment_c12, container, false)

        // サーフェースビューを取得
        surfaceViewCanvas = view.findViewById(R.id.svC12)

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

        // 画像リスト作成
        (1..15).reversed().forEach {
            // 質量をランダムに変える
            // 0.2～2.0
            //val mover = Mover( mass = (1..10).shuffled().first()/5f )
            val mover = Mover( mass = it.toFloat()/5f )
            mvLst.add(mover)
        }

        // 力(風)
        val wind = PVector(1f, 0f)
        // 力(重力)
        val gravity = PVector( 0f, 5f )

        // 力を加える
        mvLst.forEach {
            it.applyForce(wind)
            it.applyForce(gravity)
        }

        runnable = Runnable {
            mvLst.forEach {
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

        // 画像を描画
        mvLst.forEach {
            // 元画像の大きさ
            //val src = Rect(it.il.x.toInt(),
            //        it.il.y.toInt(),
            //        it.il.x.toInt()+bmp.width,
            //        it.il.y.toInt()+bmp.height)

            // 元画像を質量によって大きさを変える
            val dst = Rect(it.il.x.toInt(),
                    it.il.y.toInt(),
                    it.il.x.toInt()+(bmp.width*it.mass).toInt(),
                    it.il.y.toInt()+(bmp.height*it.mass).toInt())
            canvas.drawBitmap(bmp, null, dst, paintImage)
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
            //it.il.x = sw/2 - bmp.width/2
            //it.il.y = sh/2 - bmp.height/2
            it.il.x = bmp.width/2f
            it.il.y = bmp.height/2f
            // 画像の移動領域
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
        @JvmStatic
        fun newInstance() =
                C12Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
