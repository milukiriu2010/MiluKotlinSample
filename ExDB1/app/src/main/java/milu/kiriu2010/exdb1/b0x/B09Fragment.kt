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
import milu.kiriu2010.gui.fractal.DragonCurvDrawable

// ドラゴン曲線
class B09Fragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_b09, container, false)

        val imageView = view.findViewById<ImageView>(R.id.ivB09)
        val drawable = DragonCurvDrawable()
        imageView.setImageDrawable(drawable)

        val dataRepeat = view.findViewById<TextView>(R.id.tvB09)

        var repeat = 0
        runnable = Runnable {

            drawable.divideKochPath()
            drawable.invalidateSelf()
            if ( repeat < 10 ) {
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
                B09Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
