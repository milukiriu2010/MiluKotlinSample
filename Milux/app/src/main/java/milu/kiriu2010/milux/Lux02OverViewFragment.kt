package milu.kiriu2010.milux


import android.content.pm.ActivityInfo
import android.graphics.*
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.TextView
import kotlin.concurrent.timer

/**
 * A simple [Fragment] subclass.
 * Use the [Lux02OverViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Lux02OverViewFragment : Fragment()
        , SurfaceHolder.Callback
        , NewValListener
        , OrientationListener {
    // 照度
    private var lux: Float = 0f

    // 照度の数値を表示するビュー
    private lateinit var dataLux: TextView
    // 照度の強さを表すビュー
    private lateinit var overView: SurfaceView

    // バックグラウンドに使うペイント
    private val paintBackground = Paint().apply {
        color = Color.WHITE
    }
    // 枠に使うペイント
    private val paintFrame = Paint(0).apply {
        color = Color.BLACK
        strokeWidth = 20f
        // これがないと枠でなく、塗りつぶされてしまう
        style = Paint.Style.STROKE
    }
    // 基準に使うペイント
    private val paintLineBase = Paint(0).apply {
        color = Color.BLACK
        strokeWidth = 2f
        style = Paint.Style.STROKE
        // ダッシュ線
        pathEffect = DashPathEffect( floatArrayOf(25f,15f), 0f)
    }
    // 照度の位置を示すペイント
    private  val paintLineLux = Paint().apply {
        color = Color.RED
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }

    // 照度の強さを表示するビューの幅・高さ
    private var ow = 0f
    private var oh = 0f

    // ビューとグラフの枠とのマージン
    private val mw = 100f
    private val mh = 100f

    // タイマーで呼び出されるハンドラー
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_lux02_over_view, container, false)

        // 照度の強さを表示するサーフェスビュー
        overView = view.findViewById(R.id.overView)
        overView.holder.addCallback(this)

        timer( period = 1000) {
            handler.post {
                drawCanvas()
            }
        }

        // 照度の数値を表示するビュー
        dataLux = view.findViewById(R.id.dataLux)

        return view
    }

    // SurfaceHolder.Callback
    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

        // 1080 x 1457(24sp) emulator
        // 1080 x 1769(24sp) huawei p20 lite

        Log.d( javaClass.simpleName, "surfaceChanged:w[$width]h[$height]")
        ow = width.toFloat()
        oh = height.toFloat()
    }

    // SurfaceHolder.Callback
    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    // SurfaceHolder.Callback
    override fun surfaceCreated(holder: SurfaceHolder?) {
    }

    override fun onUpdate(lux: Float) {
        Log.d( javaClass.simpleName, "lux[$lux]")

        this.lux = lux

        // 照度の数値を表示
        if (this::dataLux.isInitialized) {
            Log.d( javaClass.simpleName, "dataLux already initialized")
            dataLux.text = this.lux.toString()
        }
    }

    private fun drawCanvas() {
        Log.d( javaClass.simpleName, "drawCanvas:w[$ow]h[$oh]")
        val canvas = overView.holder.lockCanvas()
        if (canvas == null) {
            Log.d( javaClass.simpleName, "canvas is null")
            overView.holder.unlockCanvasAndPost(canvas)
            return
        }

        // バックグラウンドを塗りつぶす
        val background = Rect( 0, 0, ow.toInt(), oh.toInt())
        canvas.drawRect(background, paintBackground)

        // 枠を描画
        val frame = Rect( mw.toInt(), 0, ow.toInt(), (oh-mh).toInt())
        canvas.drawRect(frame, paintFrame)

        // 座標移動するため、初期位置を保存する
        canvas.save()

        // 時間枠を表示
        // 全体で60秒表示するため10秒ごとに描画)
        val timeLine = Path()
        timeLine.moveTo( 0f, 0f )
        timeLine.lineTo( 0f, oh-mh)
        // X軸を右マージン分移動
        canvas.translate( mw, 0f)
        for ( i in 1 until 6 ) {
            // X軸を10秒単位で移動
            canvas.translate( (frame.width()/6).toFloat(), 0f)
            canvas.drawPath(timeLine,paintLineBase)
        }


        // 座標位置を初期値に戻す
        canvas.restore()

        overView.holder.unlockCanvasAndPost(canvas)
    }

    // OrientationListener
    override fun onActivityOrientation(): Int {
        return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment Lux02OverViewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                Lux02OverViewFragment().apply {
                    arguments = Bundle().apply {
                        /*
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                        */
                    }
                }
    }
}
