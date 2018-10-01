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

/**
 * A simple [Fragment] subclass.
 * Use the [Anime3BounceFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Anime03BounceFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_anime03_bounce, container, false)

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

            /*
            // 座標を真ん中に移す
            imageView.translationX = lw/2
            imageView.translationY = lh/2

            // 画像を真ん中に配置
            // と思ったが、左上端に1/4だけ表示された
            imageView.x = -iw/2
            imageView.y = -ih/2
            */

            // スピード
            var vx = iw
            var vy = ih

            // 縦は真ん中に表示
            imageView.y = lh/2 - ih/2

            // 画像の幅分横に移動
            val duration = 100L
            val animator = imageView.animate()
                    .setDuration(duration)
                    .xBy(vx)
                    .yBy(vy)
            // リピートする
            animator.setListener( object: Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    Log.d( javaClass.simpleName, "onAnimationEnd")

                    // ------------------------------------------
                    // 壁で反射する
                    // ------------------------------------------
                    // 次の移動で右の壁にヒット
                    // ------------------------------------------
                    if ( (imageView.x+iw+vx) >= lw ) {
                        vx = -vx
                    }
                    // ------------------------------------------
                    // 次の移動で左の壁にヒット
                    // ------------------------------------------
                    else if ( (imageView.x+vx) < 0 ) {
                        vx = -vx
                    }

                    // ------------------------------------------
                    // 次の移動で下の壁にヒット
                    // ------------------------------------------
                    if ( (imageView.y+iw+vy) >= lh ) {
                        vy = -vy
                    }
                    // ------------------------------------------
                    // 次の移動で上の壁にヒット
                    // ------------------------------------------
                    else if ( (imageView.y+vy) < 0 ) {
                        vy = -vy
                    }

                    imageView.animate()
                            .setDuration(duration)
                            .xBy(vx)
                            .yBy(vy)
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
         * @return A new instance of fragment Anime3BounceFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                Anime03BounceFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
