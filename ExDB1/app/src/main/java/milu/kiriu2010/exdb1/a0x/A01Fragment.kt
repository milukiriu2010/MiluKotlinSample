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

// 左⇒右へアニメーション
class A01Fragment : Fragment() {
    // アニメーションする画像
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

        // アニメーションする画像をレイアウトに配置
        imageView = ImageView(context)
        imageView.setImageResource(R.drawable.a_male)
        imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        // FrameLayoutはViewGroupを継承している
        // ViewGroupはViewを継承している
        (view as ViewGroup).addView(imageView)

        // レイアウト・画像サイズ取得
        // エミュレータ(1080x1584) => ButtonNavigationなし
        // エミュレータ(1038x1542) => ButtonNavigationあり
        // 64x64 => 168x168
        view.viewTreeObserver.addOnGlobalLayoutListener {
            //if ( isCalculated == true ) return@addOnGlobalLayoutListener
            Log.d( javaClass.simpleName, "W:w[${view.width}]h[${view.height}]/I:w[${imageView.width}]h[${imageView.height}]")

            // レイアウト幅・高さ
            val lw = view.width.toFloat()
            val lh = view.height.toFloat()
            // 画像幅・高さ
            val iw = imageView.width.toFloat()
            val ih = imageView.height.toFloat()

            // 初期表示
            // 縦:中央
            // 横:左端
            imageView.x = 0.0f
            imageView.y = lh/2 - ih/2

            // 100msごとに画像の幅分横に移動するアニメーションオブジェクトを生成
            val duration = 100L
            val animator = imageView.animate()
                    .setDuration(duration)
                    .xBy(iw)

            // リピートする
            animator.setListener( object: Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    Log.d( javaClass.simpleName, "onAnimationEnd")

                    // 次の位置がレイアウトを超える場合は
                    // 初期値に戻す
                    if ( (imageView.x+imageView.width) >= lw ) {
                        /* ここでanimateすると、右=>左にアニメーションしてしまう
                        imageView.animate()
                                .setDuration(duration)
                                .x(0.0f)
                                */
                        imageView.x = 0.0f
                    }

                    // 次の位置を横にずらす
                    imageView.animate()
                            .setDuration(duration)
                            .xBy(iw)
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
                A01Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
