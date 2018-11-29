package milu.kiriu2010.exdb1.basic


import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.*

import milu.kiriu2010.exdb1.R

class BasicHomeFragment : Fragment()
        , SurfaceHolder.Callback {

    // 描画に使うサーフェースビュー
    private lateinit var surfaceViewCanvas: SurfaceView

    // サーフェースビューの幅・高さ
    private var sw: Float = 0f
    private var sh: Float = 0f

    private val marginW = 50f
    private val marginH = 50f

    // 描画に使うハンドラ
    val handler = Handler()
    // 描画に使うスレッド
    private lateinit var runnable: Runnable

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 4f
        //pathEffect = cornerEffect
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_basic_home, container, false)

        // サーフェースビューを取得
        surfaceViewCanvas = view.findViewById(R.id.surfaceViewCanvas)

        val holder = surfaceViewCanvas.holder
        holder.addCallback(this)

        runnable = Runnable {
            drawCanvas()
        }
        handler.post(runnable)


        return view
    }

    // 描画
    private fun drawCanvas() {
        val canvas = surfaceViewCanvas.holder.lockCanvas()
        if (canvas == null) return

        // バックグラウンドを描画
        canvas.drawColor(Color.WHITE)

        var n = 1f

        // 実線
        canvas.drawLine(marginW,n*marginH,sw-marginW,n*marginH,linePaint)

        // 100,50,0点線
        val dashPath1 = DashPathEffect(floatArrayOf(100f,50f),0f)
        linePaint.setPathEffect(dashPath1)
        n = 2f
        canvas.drawLine(marginW,n*marginH,sw-marginW,n*marginH,linePaint)

        // 100,50,20点線(左に20ずれる)
        val dashPath2 = DashPathEffect(floatArrayOf(100f,50f),20f)
        linePaint.setPathEffect(dashPath2)
        n = 3f
        canvas.drawLine(marginW,n*marginH,sw-marginW,n*marginH,linePaint)

        // 100,50,-20点線(右に20ずれる)
        val dashPath3 = DashPathEffect(floatArrayOf(100f,50f),-20f)
        linePaint.setPathEffect(dashPath3)
        n = 4f
        canvas.drawLine(marginW,n*marginH,sw-marginW,n*marginH,linePaint)

        // 100,50,150点線(左に150ずれる)
        val dashPath4 = DashPathEffect(floatArrayOf(100f,50f),150f)
        linePaint.setPathEffect(dashPath4)
        n = 5f
        canvas.drawLine(marginW,n*marginH,sw-marginW,n*marginH,linePaint)

        surfaceViewCanvas.holder.unlockCanvasAndPost(canvas)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        // サーフェースビューの幅・高さを取得
        sw = width.toFloat()
        sh = height.toFloat()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                BasicHomeFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
