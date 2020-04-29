package milu.kiriu2010.exdb1.canvas


import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.*
import android.widget.SeekBar

import milu.kiriu2010.exdb1.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * A simple [Fragment] subclass.
 * Use the [Canvas04PolygonFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Canvas04PolygonFragment : Fragment()
        , SurfaceHolder.Callback {

    // 描画に使うサーフェースビュー
    private lateinit var surfaceViewCanvas: SurfaceView

    // サーフェースビューの幅・高さ
    private var sw: Float = 0f
    private var sh: Float = 0f

    // 多角形の描画に使う半径
    private val radius = 400.0

    // 多角形の角の数を決定するシークバー
    private lateinit var seekBar: SeekBar

    // 多角形のパス
    private val drawPath = Path()
    // 多角形のパスに使うペイント
    private val drawPaint = Paint().apply {
        color = Color.argb(255,0,0,0)
        isAntiAlias = true
        strokeWidth = 10f
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_canvas04_polygon, container, false)

        // サーフェースビューを取得
        surfaceViewCanvas = view.findViewById(R.id.surfaceViewCanvas)

        val holder = surfaceViewCanvas.holder
        holder.addCallback(this)

        seekBar = view.findViewById<SeekBar>(R.id.seekBar)
        seekBar.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                changeSides(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // 多角形の初期位置
        changeSides(seekBar.progress)

        return view
    }

    private fun changeSides( sides: Int ) {
        var sides2 = sides + 3
        Log.d( javaClass.simpleName, "sides[$sides2]")
        drawPath.reset()
        val angle = 2.0 * PI / sides2
        drawPath.moveTo(
                sw/2 + (radius * cos(0.0)).toFloat(),
                sh/2 + (radius * sin(0.0)).toFloat() )
        for ( i in 1 until sides2 ) {
            drawPath.lineTo(
                    sw/2 + (radius * cos(angle*i)).toFloat(),
                    sh/2 + (radius * sin(angle*i)).toFloat() )
        }
        drawPath.close()
        drawCanvas()
    }

    // 描画
    private fun drawCanvas() {
        val canvas = surfaceViewCanvas.holder.lockCanvas()
        if ( canvas == null ) return
        //Log.d(javaClass.simpleName, "x[$il.x]y[$il.y]")

        // バックグラウンドを描画
        canvas.drawColor(Color.WHITE)

        // 多角形を描画する
        canvas.drawPath(drawPath, drawPaint)

        surfaceViewCanvas.holder.unlockCanvasAndPost(canvas)
    }

    // SurfaceHolder.Callback
    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        Log.d(javaClass.simpleName, "surfaceChanged")

        // サーフェースビューの幅・高さを取得
        sw = width.toFloat()
        sh = height.toFloat()
        // 多角形の初期位置
        changeSides(seekBar.progress)
        // 描画
        drawCanvas()
    }

    // SurfaceHolder.Callback
    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    // SurfaceHolder.Callback
    override fun surfaceCreated(holder: SurfaceHolder?) {
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                Canvas04PolygonFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
