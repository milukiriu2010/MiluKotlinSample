package milu.kiriu2010.exdb1.draw


import android.animation.ObjectAnimator
import android.animation.ValueAnimator.INFINITE
import android.animation.ValueAnimator.RESTART
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import kotlinx.android.synthetic.main.fragment_draw_home.*

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.decorate.Deco03ConstraintLayout
import milu.kiriu2010.gui.decorate.DecorateTextView
import milu.kiriu2010.gui.decorate.DecorateView
import milu.kiriu2010.gui.decorate.PolygonLapsDrawable

/**
 * A simple [Fragment] subclass.
 * Use the [DrawHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DrawHomeFragment : Fragment() {

    val handler = Handler()

    private lateinit var runnable: Runnable

    private lateinit var objectAnimator: ObjectAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_draw_home, container, false)

        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val polygonLapsDrawable = PolygonLapsDrawable()
        imageView.setImageDrawable(polygonLapsDrawable)

        val dv = 0.01f
        /* ドットを動かす
        runnable = Runnable {
            if ( polygonLapsDrawable.dotProgress >= 1.0f ) {
                polygonLapsDrawable.dotProgress = 0f
            }
            else {
                polygonLapsDrawable.dotProgress += dv
            }
            handler.postDelayed(runnable, 100)
        }
        handler.post(runnable)
        */
        /* 多角形を一辺ずつ描く
        runnable = Runnable {
            if ( polygonLapsDrawable.progress >= 1.0f ) {
                polygonLapsDrawable.progress = 0f
            }
            else {
                polygonLapsDrawable.progress += dv
            }
            handler.postDelayed(runnable, 100)
        }
        handler.post(runnable)
        */


        /* ドットを動かす
        ObjectAnimator.ofFloat(polygonLapsDrawable, PolygonLapsDrawable.DOT_PROGRESS, 0f,1f).apply {
            duration = 6000L
            interpolator = LinearInterpolator()
            repeatCount = INFINITE
            repeatMode = RESTART
        }.start()
        */
        /* 多角形を一辺ずつ描く
        ObjectAnimator.ofFloat(polygonLapsDrawable, PolygonLapsDrawable.PROGRESS, 0f,1f).apply {
            duration = 4000L
            interpolator = LinearInterpolator()
            repeatCount = INFINITE
            repeatMode = RESTART
        }.start()
        */

        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if ( this::objectAnimator.isInitialized ) {
                objectAnimator.cancel()
            }

            objectAnimator = when (checkedId) {
                R.id.rbtnLine -> {
                    ObjectAnimator.ofFloat(polygonLapsDrawable, PolygonLapsDrawable.PROGRESS, 0f,1f)
                }
                else -> {
                    // ドットのアニメーション時は、ライン全体が描かれるようにする
                    // と、思ったけど、そうは問屋が卸さないらしい。
                    polygonLapsDrawable.progress = 1.0f
                    ObjectAnimator.ofFloat(polygonLapsDrawable, PolygonLapsDrawable.DOT_PROGRESS, 0f,1f)
                }
            }

            objectAnimator.apply {
                duration = 4000L
                interpolator = LinearInterpolator()
                repeatCount = INFINITE
                repeatMode = RESTART
            }.start()
        }

        return view
    }


    companion object {
        @JvmStatic
        fun newInstance() =
                DrawHomeFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
