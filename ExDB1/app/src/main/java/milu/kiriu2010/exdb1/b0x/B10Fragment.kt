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
import milu.kiriu2010.gui.fractal.JuliaSetDrawable
import milu.kiriu2010.gui.fractal.Mandelbrot3Drawable

// ジュリア集合
class B10Fragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_b10, container, false)

        val imageView = view.findViewById<ImageView>(R.id.ivB10)
        val drawable = JuliaSetDrawable()
        imageView.setImageDrawable(drawable)

        val dataRepeat = view.findViewById<TextView>(R.id.tvB10)

        var repeat = 0
        runnable = Runnable {
            drawable.scanImagenary(repeat)
            drawable.invalidateSelf()
            if ( repeat < Mandelbrot3Drawable.n ) {
                repeat++
                dataRepeat.setText(repeat.toString())
                handler.postDelayed(runnable,10)
            }
        }
        handler.postDelayed(runnable,10)

        return view
    }


    companion object {
        @JvmStatic
        fun newInstance() =
                B10Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
