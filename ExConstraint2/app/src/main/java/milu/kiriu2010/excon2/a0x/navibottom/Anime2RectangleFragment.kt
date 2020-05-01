package milu.kiriu2010.excon2.a0x.navibottom

import android.animation.Animator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import milu.kiriu2010.excon2.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Anime2RectangleFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Anime2RectangleFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Anime2RectangleFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_anime2_rectangle, container, false)

        // 画像をレイアウトに配置
        imageView = ImageView(context)
        imageView.setImageResource(R.drawable.male)
        imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        (view as ViewGroup).addView(imageView)

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

            // 縦は真ん中に表示
            imageView.y = lh/2 - ih/2

            // 画像の幅分横に移動
            val duration = 100L
            val animator = imageView.animate()
                    .setDuration(duration)
                    .xBy(iw)
            // 移動モード
            // 1: 左=>右
            // 2: 下=>上
            // 3: 右=>左
            // 4: 上=>下
            var mode = 1

            // リピートする
            animator.setListener( object: Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    Log.d( javaClass.simpleName, "onAnimationEnd")

                    // ------------------------------------------
                    // 長方形を描くようにアニメーションする
                    // "1:左=>右"は、１つずつ移動
                    // 上記以外は、一瞬で移動
                    // ------------------------------------------
                    // 次の位置がレイアウトを超える場合は
                    // (1) 1行上に移動
                    // (2) 左端に戻す
                    // ------------------------------------------
                    if ( (imageView.x+imageView.width*2) >= lw ) {
                        when (mode) {
                            // "1:左=>右" => "2:下=>上"
                            1 -> {
                                mode = 2
                                imageView.animate()
                                        .setDuration(duration)
                                        .y( lh/2 - ih*3/2 )
                            }
                            // "2:下=>上" => "3:右=>左"
                            2 -> {
                                mode = 3
                                imageView.animate()
                                        .setDuration(duration)
                                        .x( 0.0f )
                                        .y( lh/2 - ih*3/2 )
                            }
                        }
                    }
                    // ------------------------------------------
                    // (1) 次の位置を横にずらす
                    // (3) "3:右=>左" => "4:上=>下"
                    // (4) "4:上=>下" => "1:左=>右"
                    // ------------------------------------------
                    else {
                        when (mode) {
                            // "1:左=>右"
                            1 -> {
                                imageView.animate()
                                        .setDuration(duration)
                                        .xBy(iw)
                                        .y(lh/2 - ih/2)
                            }
                            // "3:右=>左" => "4:上=>下"
                            3 -> {
                                mode = 4
                                imageView.animate()
                                        .setDuration(duration)
                                        .x( 0.0f)
                                        .y(lh/2 - ih/2)
                            }
                            // "4:上=>下" => "1:左=>右"
                            4 -> {
                                mode = 1
                                imageView.animate()
                                        .setDuration(duration)
                                        .xBy(iw)
                                        .y(lh/2 - ih/2)
                            }
                        }
                    }
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
         * @return A new instance of fragment Anime2RectangleFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                Anime2RectangleFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
