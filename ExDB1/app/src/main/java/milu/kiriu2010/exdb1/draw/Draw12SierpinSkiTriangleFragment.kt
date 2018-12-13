package milu.kiriu2010.exdb1.draw


import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.fractal.SierpinSkiTriangleDrawable

class Draw12SierpinSkiTriangleFragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_draw12_sierpin_ski_triangle, container, false)

        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val drawable = SierpinSkiTriangleDrawable()
        imageView.setImageDrawable(drawable)

        val dataRepeat = view.findViewById<TextView>(R.id.dataRepeat)

        var repeat = 0
        runnable = Runnable {
            drawable.proc()
            drawable.invalidateSelf()
            if ( repeat < 6 ) {
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
                Draw12SierpinSkiTriangleFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
