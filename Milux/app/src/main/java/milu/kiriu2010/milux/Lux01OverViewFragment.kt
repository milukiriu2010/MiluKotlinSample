package milu.kiriu2010.milux


import android.content.pm.ActivityInfo
import android.graphics.*
import android.hardware.SensorManager.LIGHT_NO_MOON
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.TextView
import kotlin.math.log

/**
 * A simple [Fragment] subclass.
 * Use the [Lux01OverViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Lux01OverViewFragment : Fragment()
        , SurfaceHolder.Callback
        , NewValListener
        , OrientationListener{

    // 照度
    private var lux: Float = 0f

    // 照度の数値を表示するビュー
    private lateinit var dataLux: TextView
    // 照度の強さを表すビュー
    private lateinit var overView: SurfaceView

    // 照度の強さを表示するビューの幅・高さ
    private var ow = 0f
    private var oh = 0f

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            /*
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            */
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        Log.d( javaClass.simpleName, "onCreateView" )
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_lux01_over_view, container, false)

        // 照度の強さを表示するサーフェスビュー
        overView = view.findViewById(R.id.overView)
        overView.holder.addCallback(this)

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

    // NewValListener
    // 新しい照度を設定
    override fun onUpdate(lux: Float) {
        Log.d( javaClass.simpleName, "lux[$lux]")

        this.lux = lux

        // 照度の数値を表示
        if (this::dataLux.isInitialized) {
            Log.d( javaClass.simpleName, "dataLux already initialized")
            dataLux.text = this.lux.toString()
        }

        // 照度の強さを表示
        if (this::overView.isInitialized) {
            drawCanvas()
        }

    }

    // 照度の強さを描画
    private fun drawCanvas() {
        val canvas = overView.holder.lockCanvas()
        if (canvas == null) {
            Log.d( javaClass.simpleName, "canvas is null")
            overView.holder.unlockCanvasAndPost(canvas)
            return
        }

        Log.d( javaClass.simpleName, "drawCanvas:w[$ow]h[$oh]")

        // 座標移動するため、初期位置を保存する
        canvas.save()

        // バックグラウンドを塗りつぶす
        val frame = Rect( 0, 0, ow.toInt(), oh.toInt())
        canvas.drawRect(frame, paintBackground)

        // 枠を描画
        canvas.drawRect(frame, paintFrame)

        // 各フレームの高さ
        val fh = oh/8
        // 各フレームを描画
        val baseLine = Path()
        baseLine.moveTo(0f, 0f)
        baseLine.lineTo( ow, 0f)
        // 閉じると点線じゃなくなる？
        //baseLine.close()
        for ( i in 1 until 8 ) {
            canvas.translate( 0f, fh.toFloat())
            canvas.drawPath(baseLine,paintLineBase)
        }

        // 座標位置を初期値に戻す
        canvas.restore()

        // ---------------------------------------------------------------------------
        // 太陽を描画
        //   120000.0:5.079
        // https://illustimage.com/?id=1743
        // ---------------------------------------------------------------------------
        val bmpSun = BitmapFactory.decodeResource(resources,R.drawable.a_sun)
        // 描画元の矩形イメージ
        val srcSun = Rect(0,0,bmpSun.width,bmpSun.height)
        // 描画先の矩形イメージ
        val dstSun = Rect( (ow-fh).toInt(), 0, ow.toInt(), fh.toInt())
        canvas.drawBitmap(bmpSun, srcSun, dstSun, paintBackground)

        // ---------------------------------------------------------------------------
        // 日の出を描画
        //   400.0:2.602
        // https://icon-icons.com/ja/%E3%82%A2%E3%82%A4%E3%82%B3%E3%83%B3/%E3%82%B5%E3%83%B3%E3%83%A9%E3%82%A4%E3%82%BA/98976
        // ---------------------------------------------------------------------------
        val bmpSunRise = BitmapFactory.decodeResource(resources,R.drawable.a_sunrise)
        // 描画元の矩形イメージ
        val srcSunrise = Rect(0,0,bmpSunRise.width,bmpSunRise.height)
        // 描画先の矩形イメージ
        val dstSunrise = Rect( (ow-fh).toInt(), 2*fh.toInt(), ow.toInt(), 3*fh.toInt())
        canvas.drawBitmap(bmpSunRise, srcSunrise, dstSunrise, paintBackground)

        // ---------------------------------------------------------------------------
        // 月を描画
        //   0.25:-0.602
        // https://illustrain.com/?p=21515
        // ---------------------------------------------------------------------------
        val bmpMoon = BitmapFactory.decodeResource(resources,R.drawable.a_moon)
        // 描画元の矩形イメージ
        val srcMoon = Rect(0,0,bmpMoon.width,bmpMoon.height)
        // 描画先の矩形イメージ
        val dstMoon = Rect( (ow-fh).toInt(), 5*fh.toInt(), ow.toInt(), 6*fh.toInt())
        canvas.drawBitmap(bmpMoon, srcMoon, dstMoon, paintBackground)

        // http://seesaawiki.jp/w/moonlight_aska/d/%be%c8%c5%d9%a5%bb%a5%f3%a5%b5%a1%bc%a4%ce%c3%cd%a4%f2%bc%e8%c6%c0%a4%b9%a4%eb
        // ----------------------------------------------------------------------
        // 明るさの目安
        // ----------------------------------------------------------------------
        //   LIGHT_SUNLIGHT_MAX 120000.0       5.079
        //   LIGHT_SUNLLIGHT    110000.0       5.041
        //   LIGHT_SHADE         20000.0       4.301
        //   LIGHT_OVERCAST      10000.0       4
        //   LIGHT_SUNRISE         400.0       2.602
        //   LIGHT_CLOUDY          100.0       2
        //   LIGHT_FULLMOON          0.25     -0.602
        //   LIGHT_NO_MOON           0.0010   -3
        // ----------------------------------------------------------------------
        // 照度をlog対数表示するため、補正する
        val luxC = when {
            ( lux < LIGHT_NO_MOON ) -> 0f
            ( log(lux,10f) > 5 ) -> 8f
            else -> log(lux,10f)+3f
        }

        // 照度の位置を描画
        val luxH = oh * (8-luxC)/8
        canvas.drawLine(0f, luxH, ow, luxH, paintLineLux )

        overView.holder.unlockCanvasAndPost(canvas)
    }

    // OrientationListener
    override fun onActivityOrientation(): Int {
        return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment Lux01OverViewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                Lux01OverViewFragment().apply {
                    arguments = Bundle().apply {
                        /*
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                        */
                    }
                }
    }
}
