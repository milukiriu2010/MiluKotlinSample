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
import milu.kiriu2010.gui.fractal.KochTreeDrawable

// コッホツリー
class B06Fragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_b06, container, false)


        val imageView = view.findViewById<ImageView>(R.id.ivB06)
        val kochTreeDrawable = KochTreeDrawable()
        imageView.setImageDrawable(kochTreeDrawable)

        val dataRepeat = view.findViewById<TextView>(R.id.tvB06)

        var repeat = 0
        runnable = Runnable {

            kochTreeDrawable.divideKochPath()
            kochTreeDrawable.invalidateSelf()
            if ( repeat < 8 ) {
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
                B06Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
