package milu.kiriu2010.exdb1.b0x


import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.RadioGroup

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.fractal.KochSnowFlakeLapDrawable

// コッホ雪片ラップ
class B05Fragment : Fragment() {

    private lateinit var objectAnimator: ObjectAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_b05, container, false)

        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val kochSnowFlakeLapDrawable = KochSnowFlakeLapDrawable()
        imageView.setImageDrawable(kochSnowFlakeLapDrawable)

        val radioGroup = view.findViewById<RadioGroup>(R.id.rgB05)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if ( this::objectAnimator.isInitialized ) {
                objectAnimator.cancel()
            }

            objectAnimator = when (checkedId) {
                R.id.rbB05Line -> {
                    ObjectAnimator.ofFloat(kochSnowFlakeLapDrawable, KochSnowFlakeLapDrawable.PROGRESS, 0f,1f)
                }
                else -> {
                    // ドットのアニメーション時は、ライン全体が描かれるようにする
                    // と、思ったけど、そうは問屋が卸さないらしい。
                    kochSnowFlakeLapDrawable.progress = 1.0f
                    ObjectAnimator.ofFloat(kochSnowFlakeLapDrawable, KochSnowFlakeLapDrawable.DOT_PROGRESS, 0f,1f)
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
                B05Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
