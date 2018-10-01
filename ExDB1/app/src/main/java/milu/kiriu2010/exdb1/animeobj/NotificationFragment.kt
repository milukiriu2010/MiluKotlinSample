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
 * Use the [NotificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class NotificationFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_notification, container, false)

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
            if ( isCalculated == true ) return@addOnGlobalLayoutListener
            Log.d( javaClass.simpleName, "W:w[${view.width}]h[${view.height}]/I:w[${imageView.width}]h[${imageView.height}]")

            // レイアウト幅・高さ
            val lw = view.width.toFloat()
            val lh = view.height.toFloat()
            // 画像幅・高さ
            val iw = imageView.width.toFloat()
            val ih = imageView.height.toFloat()

            // 半径
            val radius = 100.0f

            // 中心
            val centerX = lw/2 - iw/2
            val centerY = lh/2 - ih/2

            // 縦横真ん中に表示
            /*
            imageView.x = lw/2 - iw/2
            imageView.y = lh/2 - ih/2
            */
            /*
            imageView.translationX = lw/2 - iw/2
            imageView.translationY = lh/2 - ih/2
            */

            // 回転角度(Y軸)
            val angleY = 10.0f
            // 回転角度(Z軸)
            var angleZ = 10.0f

            imageView.x = centerX+(radius* cos(0.0)).toFloat()
            imageView.y = centerY+(radius* sin(0.0)).toFloat()

            // 画像の幅分横に移動
            val duration = 100L
            val animator = imageView.animate()
                    .setDuration(duration)
                    .x(centerX+(radius* cos(angleZ/180* PI)).toFloat())
                    .y(centerY+(radius* sin(angleZ/180* PI)).toFloat())
            /*
            .x(centerX)
            .xBy((radius*cos(angleZ*PI)).toFloat())
            .y(centerY)
            .yBy((radius*sin(angleZ*PI)).toFloat())
            */
            //.rotationBy(angle)
            //.rotationXBy(angle)
            //.rotationYBy(angleY)
            // リピートする
            animator.setListener( object: Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    Log.d( javaClass.simpleName, "onAnimationEnd")
                    angleZ += 10

                    imageView.animate()
                            .setDuration(duration)
                            .x(centerX+(radius* cos(angleZ/180* PI)).toFloat())
                            .y(centerY+(radius* sin(angleZ/180* PI)).toFloat())
                    /*
                    .x(centerX)
                    .xBy((radius*cos(angleZ*PI)).toFloat())
                    .y(centerY)
                    .yBy((radius*sin(angleZ*PI)).toFloat())
                    */
                    //.rotationBy(angle)
                    //.rotationXBy(angle)
                    //.rotationYBy(angleY)
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
         * @return A new instance of fragment NotificationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                NotificationFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
