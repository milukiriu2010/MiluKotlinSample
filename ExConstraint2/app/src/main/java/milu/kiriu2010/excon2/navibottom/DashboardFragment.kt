package milu.kiriu2010.excon2.navibottom


import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import milu.kiriu2010.excon2.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DashboardFragment : Fragment() {

    private lateinit var imageView: ImageView

    private var isCalculated = false

    // 半径
    val radius = 100.0f

    // 中心
    var centerX: Float = 0.0f
    var centerY: Float = 0.0f

    // 回転角度(Z軸)
    var angleZ = 10.0f

    val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)


        // 画像をレイアウトに配置
        imageView = ImageView(context)
        imageView.setImageResource(R.drawable.male)
        imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        (view as ViewGroup)?.addView(imageView)

        // レイアウト・画像サイズ取得
        // エミュレータ(1080x1584) => ButtonNavigationなし
        // エミュレータ(1038x1542) => ButtonNavigationあり
        // 64x64 => 168x168
        view.viewTreeObserver.addOnGlobalLayoutListener {
            if (isCalculated == true) return@addOnGlobalLayoutListener
            Log.d(javaClass.simpleName, "W:w[${view.width}]h[${view.height}]/I:w[${imageView.width}]h[${imageView.height}]")

            // レイアウト幅・高さ
            val lw = view.width.toFloat()
            val lh = view.height.toFloat()
            // 画像幅・高さ
            val iw = imageView.width.toFloat()
            val ih = imageView.height.toFloat()

            // 半径
            val radius = 100.0f

            // 中心
            centerX = lw / 2 - iw / 2
            centerY = lh / 2 - ih / 2

            // 回転角度(Y軸)
            val angleY = 10.0f

            imageView.x = centerX + radius * cos(0.0).toFloat()
            imageView.y = centerY + radius * sin(0.0).toFloat()

            val runnable = DrawRunnable()

            handler.post(runnable)
        }

        return view
    }

    inner class DrawRunnable: Runnable {
        override fun run() {
            imageView.x = centerX + radius*cos(angleZ* PI /180).toFloat()
            imageView.y = centerY + radius*sin(angleZ* PI /180).toFloat()
            Log.d( javaClass.simpleName, "x[${imageView.x}]y[${imageView.y}]angleZ[$angleZ]")
            angleZ += 10
            angleZ %= 360
            handler.postDelayed( this, 1000)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment DashboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                DashboardFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
