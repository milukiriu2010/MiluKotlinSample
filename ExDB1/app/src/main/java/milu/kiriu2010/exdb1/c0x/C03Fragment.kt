package milu.kiriu2010.exdb1.c0x


import android.annotation.SuppressLint
import android.graphics.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.*
import android.widget.ImageButton

import milu.kiriu2010.exdb1.R

// SurfaceView上で"画像(シェーダ)"
//  画像をペンとして用いる
class C03Fragment : Fragment()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_c03, container, false)

        // サーフェースビューを取得
        surfaceViewCanvas = view.findViewById(R.id.svC03)
        surfaceViewCanvas.setOnTouchListener ret@ { _, event ->

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

        val imageButtonR = view.findViewById<ImageButton>(R.id.ibC03Red)
        imageButtonR.setOnClickListener { changeDrawPaint( R.drawable.ic_b01e_red ) }
        val imageButtonG = view.findViewById<ImageButton>(R.id.ibC03Green)
        imageButtonG.setOnClickListener { changeDrawPaint( R.drawable.ic_b01e_green ) }
        val imageButtonB = view.findViewById<ImageButton>(R.id.ibC03Blue)
        imageButtonB.setOnClickListener { changeDrawPaint( R.drawable.ic_b01e_blue ) }
        val imageButtonC = view.findViewById<ImageButton>(R.id.ibC03Black)
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
                C03Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
