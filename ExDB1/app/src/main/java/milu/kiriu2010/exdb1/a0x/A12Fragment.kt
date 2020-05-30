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

// XY平面:インボリュート
// Y軸を上から見て左回りに回転
class A12Fragment : Fragment() {

    private lateinit var imageView: ImageView

    //private var isCalculated = false

    // 半径
    private val radius = 10.0f

    // 中心
    private var centerX = 0.0f
    private var centerY = 0.0f

    // 回転角度(Y軸)
    private val angleY = 10.0f
    // 回転角度(XY平面の媒介変数)
    private var angleZ = 0.0f
    // 回転角度(XY平面の媒介変数)差分
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

            // 中心
            centerX = lw/2 - iw/2
            centerY = lh / 2 - ih / 2

            // 縦横真ん中を初期表示位置とする
            imageView.x = centerX + radius * ( cos(angleZ/180* PI) + (angleZ/180* PI) * sin(angleZ/180* PI) ).toFloat()
            imageView.y = centerY + radius * ( sin(angleZ/180* PI) - (angleZ/180* PI) * cos(angleZ/180* PI) ).toFloat()

            // インボリュート曲線
            // x = r * ( cos(t) + t*sin(t) )
            // y = r * ( sin(t) - t*cos(t) )

            // 画像の幅分横に移動
            //val duration = 100L
            val animator = imageView.animate()
                    .setDuration(duration)
                    .x(centerX + radius * ( cos(angleZ/180* PI) + (angleZ/180* PI) * sin(angleZ/180* PI) ).toFloat())
                    .y(centerY + radius * ( sin(angleZ/180* PI) - (angleZ/180* PI) * cos(angleZ/180* PI) ).toFloat())
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
        //angleZ %= 360

        Log.d(javaClass.simpleName, "moveNext:x[${imageView.x}]y[${imageView.y}]angleZ[$angleZ]angleZd[$angleZd]")

        imageView.animate()
                .setDuration(duration)
                .x(centerX + radius * ( cos(angleZ/180*PI) + (angleZ/180*PI) * sin(angleZ/180*PI) ).toFloat())
                .y(centerY + radius * ( sin(angleZ/180*PI) - (angleZ/180*PI) * cos(angleZ/180*PI) ).toFloat())
                .rotationYBy(angleY)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                A12Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
