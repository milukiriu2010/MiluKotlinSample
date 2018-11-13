package milu.kiriu2010.exdb1.draw


import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.RadioGroup

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.decorate.*

class DrawHomeFragment : Fragment() {

    val handler = Handler()

    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_draw_home, container, false)

        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val kochTreeDrawable = KochTreeDrawable()
        imageView.setImageDrawable(kochTreeDrawable)

        var repeat = 0
        runnable = Runnable {

            kochTreeDrawable.divideKochPath()
            kochTreeDrawable.invalidateSelf()
            if ( repeat < 1 ) {
                repeat++
                handler.postDelayed(runnable,1000)
            }
        }
        handler.postDelayed(runnable,1000)


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
