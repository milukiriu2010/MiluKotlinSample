package milu.kiriu2010.exdb1.a0x

import android.animation.Animator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import milu.kiriu2010.exdb1.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// 回転するアニメーション
// XY平面をZ軸中心で右回りに回転
// Y軸を上から見て左回りに回転
class A04Fragment : Fragment() {

    private lateinit var imageView: ImageView

    //private var isCalculated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_a0x, container, false)

        // 画像をレイアウトに配置
        imageView = ImageView(context)
        imageView.setImageResource(R.drawable.a_male)
        imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        (view as ViewGroup).addView(imageView)

        // レイアウト・画像サイズ取得
        // エミュレータ(1080x1584) => ButtonNavigationなし
        // エミュレータ(1038x1542) => ButtonNavigationあり
        // 64x64 => 168x168
        view.viewTreeObserver.addOnGlobalLayoutListener {
            //if (isCalculated == true) return@addOnGlobalLayoutListener
            Log.d(javaClass.simpleName, "W:w[${view.width}]h[${view.height}]/I:w[${imageView.width}]h[${imageView.height}]")

            // レイアウト幅・高さ
            val lw = view.width.toFloat()
            val lh = view.height.toFloat()
            // 画像幅・高さ
            val iw = imageView.width.toFloat()
            val ih = imageView.height.toFloat()

            // 半径
            val radius = 200.0f

            // 中心
            val centerX = lw / 2 - iw / 2
            val centerY = lh / 2 - ih / 2

            // 回転角度(Y軸)
            val angleY = 10.0f
            // 回転角度(Z軸)
            var angleZ = 10.0f
            // 回転角度(Z軸)差分
            val angleZd = 10.0f

            // 縦横真ん中から半径分右にずらして表示
            imageView.x = centerX + (radius * cos(0.0)).toFloat()
            imageView.y = centerY + (radius * sin(0.0)).toFloat()

            // 画像の幅分横に移動
            val duration = 100L
            val animator = imageView.animate()
                    .setDuration(duration)
                    .x(centerX + (radius * cos(angleZ / 180 * PI)).toFloat())
                    .y(centerY + (radius * sin(angleZ / 180 * PI)).toFloat())
                    //.rotationBy(angle)
                    //.rotationXBy(angle)
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
                            .y(centerY + (radius * sin(angleZ / 180 * PI)).toFloat())
                            //.rotationBy(angle)
                            //.rotationXBy(angle)
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
        @JvmStatic
        fun newInstance() =
                A04Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
