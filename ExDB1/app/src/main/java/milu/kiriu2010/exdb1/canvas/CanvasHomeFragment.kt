package milu.kiriu2010.exdb1.canvas


import android.graphics.*
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.ImageButton
import kotlinx.android.synthetic.main.fragment_canvas_home.*

import milu.kiriu2010.exdb1.R
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos

/**
 * A simple [Fragment] subclass.
 * Use the [CanvasHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CanvasHomeFragment : Fragment()
    , SurfaceHolder.Callback {

    // 描画に使うサーフェースビュー
    private lateinit var surfaceViewCanvas: SurfaceView

    // サーフェースビューの幅・高さ
    private var sw: Float = 0f
    private var sh: Float = 0f

    // タッチしたパス
    private val drawPath = Path()
    // タッチしたパスに使うペイント
    private val drawPaint = Paint().apply {
        color = Color.argb(255,0,0,0)
        isAntiAlias = true
        strokeWidth = 50f
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }
    // タッチしたパスを描くビットマップ
    private lateinit var canvasBitmap: Bitmap
    // タッチしたパスを描くキャンバス
    private lateinit var drawCanvas: Canvas

    //
    private val canvasPaint = Paint().apply {
        flags = Paint.DITHER_FLAG
    }


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
        //handler.removeCallbacks(runnable)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_canvas_home, container, false)

        // サーフェースビューを取得
        surfaceViewCanvas = view.findViewById(R.id.surfaceViewCanvas)
        surfaceViewCanvas.setOnTouchListener ret@ { v, event ->

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    drawPath.moveTo(event.x,event.y)
                    drawCanvas.drawPath(drawPath,drawPaint)
                }
                MotionEvent.ACTION_MOVE -> {
                    drawPath.lineTo(event.x,event.y)
                    drawCanvas.drawPath(drawPath,drawPaint)
                }
                MotionEvent.ACTION_UP -> {
                    drawPath.lineTo(event.x,event.y)
                    drawCanvas.drawPath(drawPath,drawPaint)
                    drawPath.reset()
                }
                else ->{
                    return@ret false
                }
            }

            drawCanvas()

            true
        }

        val holder = surfaceViewCanvas.holder
        holder.addCallback(this)

        val imageButtonR = view.findViewById<ImageButton>(R.id.imageButtonR)
        imageButtonR.setOnClickListener { changeDrawPaint( R.drawable.android1_red ) }
        val imageButtonG = view.findViewById<ImageButton>(R.id.imageButtonG)
        imageButtonG.setOnClickListener { changeDrawPaint( R.drawable.android1_green ) }
        val imageButtonB = view.findViewById<ImageButton>(R.id.imageButtonB)
        imageButtonB.setOnClickListener { changeDrawPaint( R.drawable.android1_blue ) }
        val imageButtonC = view.findViewById<ImageButton>(R.id.imageButtonC)
        imageButtonC.setOnClickListener {
            drawPaint.apply {
                color = Color.BLACK
                shader = null
            }
        }

        drawCanvas()

        return view
    }

    // クリックした画像ボタンによって描画内容を変更
    private fun changeDrawPaint( drawableId: Int ) {
        val bmp = BitmapFactory.decodeResource(resources,drawableId)
        val bmpShader = BitmapShader(bmp, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        drawPaint.apply {
            color = Color.argb(255,255,255,255)
            shader = bmpShader
        }
    }

    // 描画
    private fun drawCanvas() {
        val canvas = surfaceViewCanvas.holder.lockCanvas()
        if ( canvas == null ) return
        //Log.d(javaClass.simpleName, "x[$il.x]y[$il.y]")

        // バックグラウンドを描画
        canvas.drawColor(Color.WHITE)

        // "タッチしたパスを描いたビットマップ"をキャンバスに張り付ける
        canvas.drawBitmap(canvasBitmap,0f,0f, canvasPaint)
        // "タッチしたパス"を描画する
        //canvas.drawPath(drawPath,drawPaint)

        surfaceViewCanvas.holder.unlockCanvasAndPost(canvas)
    }

    // SurfaceHolder.Callback
    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        Log.d(javaClass.simpleName, "surfaceChanged")

        // サーフェースビューの幅・高さを取得
        sw = width.toFloat()
        sh = height.toFloat()

        // "タッチしたパスを描くビットマップ"を生成する
        canvasBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
        // "タッチしたパスを描くビットマップ"から"キャンバス"を生成する
        drawCanvas = Canvas(canvasBitmap)
        drawCanvas()
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
                CanvasHomeFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
