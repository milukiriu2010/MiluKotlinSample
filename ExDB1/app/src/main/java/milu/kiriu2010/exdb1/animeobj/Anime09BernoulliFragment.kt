package milu.kiriu2010.exdb1.animeobj


import android.animation.Animator
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import milu.kiriu2010.exdb1.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.sin

/**
 * A simple [Fragment] subclass.
 * Use the [Anime09BernoulliFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Anime09BernoulliFragment : Fragment() {

    private lateinit var imageView: ImageView

    private var isCalculated = false

    // 半径
    private val radius = 2.0f
    private var b = 0.2f

    // 中心
    private var centerX = 0.0f
    private var centerY = 0.0f

    // 回転角度(Y軸)
    private val angleY = 10.0f
    // 回転角度(Z軸)
    private var angleZ = 0.0f
    // 回転角度(Z軸)差分
    private var angleZd = 10.0f

    // アニメーションする時間
    val duration = 100L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_anime09_bernoulli, container, false)

        // 画像をレイアウトに配置
        imageView = ImageView(context)
        imageView.setImageResource(R.drawable.a_male)
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
            //val radius = 2.0f
            //var b = 0.2f

            // 中心
            centerX = lw/2 - iw/2
            centerY = lh / 2 - ih / 2

            // 回転角度(Y軸)
            //val angleY = 10.0f
            // 回転角度(Z軸)
            //var angleZ = 0.0f
            // 回転角度(Z軸)差分
            //var angleZd = 10.0f

            // 縦横真ん中を初期表示位置とする
            imageView.x = centerX + (radius * exp(0.0) * cos(0.0)).toFloat()
            imageView.y = centerY + (radius * exp(0.0) * sin(0.0)).toFloat()

            // ベルヌーイ螺旋曲線
            // x = a * exp(b) * cos(b)
            // y = a * exp(b) * sin(b)

            // 画像の幅分横に移動
            //val duration = 100L
            val animator = imageView.animate()
                    .setDuration(duration)
                    .x(centerX + (radius * exp(b*angleZ/180* PI) * cos(angleZ/180* PI)).toFloat())
                    .y(centerY + (radius * exp(b*angleZ/180* PI) * sin(angleZ/180* PI)).toFloat())
                    .rotationYBy(angleY)
            // リピートする
            animator.setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    moveNext()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }
            })
        }

        return view
    }

    private fun moveNext() {
        angleZ += angleZd
        // 5回転したら逆回し
        if ( angleZ.toInt()%1800 == 0 ) {
            angleZd = -1 * angleZd
        }

        Log.d(javaClass.simpleName, "moveNext:x[${imageView.x}]y[${imageView.y}]angleZ[$angleZ]angleZd[$angleZd]")

        imageView.animate()
                .setDuration(duration)
                .x(centerX + (radius * exp(b*angleZ/180*PI) * cos(angleZ/180*PI)).toFloat())
                .y(centerY + (radius * exp(b*angleZ/180*PI) * sin(angleZ/180*PI)).toFloat())
                .rotationYBy(angleY)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment Anime09BernoulliFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                Anime09BernoulliFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
