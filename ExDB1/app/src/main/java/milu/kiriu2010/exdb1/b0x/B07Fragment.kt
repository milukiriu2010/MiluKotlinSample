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
import milu.kiriu2010.gui.fractal.KochTreeLapDrawable

// コッホツリーラップ
class B07Fragment : Fragment() {

    private lateinit var objectAnimator: ObjectAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_b07, container, false)


        val imageView = view.findViewById<ImageView>(R.id.ivB07)
        val kochTreeLapDrawable = KochTreeLapDrawable()
        imageView.setImageDrawable(kochTreeLapDrawable)

        val radioGroup = view.findViewById<RadioGroup>(R.id.rgB07)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (this::objectAnimator.isInitialized) {
                objectAnimator.cancel()
            }

            objectAnimator = when (checkedId) {
                R.id.rbB07Line -> {
                    ObjectAnimator.ofFloat(kochTreeLapDrawable, KochTreeLapDrawable.PROGRESS,0f,1f)
                }
                else -> {
                    // ドットのアニメーション時は、ライン全体が描かれるようにする
                    // と、思ったけど、そうは問屋が卸さないらしい。
                    kochTreeLapDrawable.progress = 1.0f
                    ObjectAnimator.ofFloat(kochTreeLapDrawable, KochTreeLapDrawable.DOT_PROGRESS,1f,0f)
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
                B07Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
