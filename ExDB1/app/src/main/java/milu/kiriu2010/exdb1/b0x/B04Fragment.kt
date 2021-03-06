package milu.kiriu2010.exdb1.b0x


import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.fractal.KochSnowFlakeDrawable

// コッホ雪片
class B04Fragment : Fragment() {

    val handler = Handler()

    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_b04, container, false)

        val imageView = view.findViewById<ImageView>(R.id.ivB04)
        val kochSnowFlakeDrawable = KochSnowFlakeDrawable()
        imageView.setImageDrawable(kochSnowFlakeDrawable)

        val dataRepeat = view.findViewById<TextView>(R.id.tvB04)

        var repeat = 0
        runnable = Runnable {

            kochSnowFlakeDrawable.divideKochPath()
            kochSnowFlakeDrawable.invalidateSelf()
            if ( repeat < 3 ) {
                repeat++
                dataRepeat.setText(repeat.toString())
                handler.postDelayed(runnable,1000)
            }
        }
        handler.postDelayed(runnable,1000)

        return view
    }


    companion object {
        @JvmStatic
        fun newInstance() =
                B04Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
