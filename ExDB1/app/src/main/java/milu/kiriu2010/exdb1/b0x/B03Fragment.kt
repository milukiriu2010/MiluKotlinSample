package milu.kiriu2010.exdb1.b0x


import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.RadioGroup

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.fractal.PolygonLapsDrawable

// ポリゴンラップ
class B03Fragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_b03, container, false)

        val imageView = view.findViewById<ImageView>(R.id.ivB03)
        val polygonLapsDrawable = PolygonLapsDrawable()
        imageView.setImageDrawable(polygonLapsDrawable)

        //val dv = 0.01f
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

        val radioGroup = view.findViewById<RadioGroup>(R.id.rgB03)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if ( this::objectAnimator.isInitialized ) {
                objectAnimator.cancel()
            }

            objectAnimator = when (checkedId) {
                // 線アニメーション
                R.id.rbB03A -> {
                    ObjectAnimator.ofFloat(polygonLapsDrawable, PolygonLapsDrawable.PROGRESS, 0f,1f)
                }
                // ドットアニメーション
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
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.RESTART
            }.start()
        }

        return view
    }


    companion object {
        @JvmStatic
        fun newInstance() =
                B03Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
