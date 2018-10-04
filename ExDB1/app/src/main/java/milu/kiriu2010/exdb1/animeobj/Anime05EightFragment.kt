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
import kotlin.math.sin

/**
 * A simple [Fragment] subclass.
 * Use the [Anime5EightFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Anime05EightFragment : Fragment() {

    private lateinit var imageView: ImageView

    private var isCalculated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_anime05_eight, container, false)

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
            val radius = 300.0f

            // 中心
            val centerX = lw / 2 - iw / 2
            val centerY = lh / 2 - ih / 2

            // 回転角度(Y軸)
            val angleY = 10.0f
            // 回転角度(Z軸)
            var angleZ = 10.0f
            // 回転角度(Z軸)差分
            var angleZd = 10.0f

            // 縦横真ん中から半径分右にずらして表示
            imageView.x = centerX + (radius * cos(0.0)).toFloat()
            imageView.y = centerY + (radius * sin(0.0)).toFloat()

            // 8の字
            // x = cos(a)
            // y = sin(2a)

            // 画像の幅分横に移動
            val duration = 100L
            val animator = imageView.animate()
                    .setDuration(duration)
                    .x(centerX + (radius * cos(angleZ / 180 * PI)).toFloat())
                    .y(centerY + (radius * sin(2*angleZ / 180 * PI)).toFloat())
                    .rotationYBy(angleY)
            // リピートする
            animator.setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    Log.d(javaClass.simpleName, "onAnimationEnd")
                    angleZ += angleZd

                    imageView.animate()
                            .setDuration(duration)
                            .x(centerX + (radius * cos(angleZ / 180 * PI)).toFloat())
                            .y(centerY + (radius * sin(2*angleZ / 180 * PI)).toFloat())
                            .rotationYBy(angleY)
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }
            })
        }

        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment Anime5EightFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                Anime05EightFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
