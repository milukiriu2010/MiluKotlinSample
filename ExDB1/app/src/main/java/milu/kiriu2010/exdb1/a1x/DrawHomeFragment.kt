package milu.kiriu2010.exdb1.a1x


import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.fractal.TakagiCurve01Drawable

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_draw_home, container, false)

        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val drawable = TakagiCurve01Drawable()
        imageView.setImageDrawable(drawable)

        val dataRepeat = view.findViewById<TextView>(R.id.dataRepeat)

        drawable.cal(0)
        drawable.invalidateSelf()

        var n = 0
        runnable = Runnable {
            n = when {
                n < 10 -> n+1
                else -> 0
            }
            drawable.cal(n)
            drawable.invalidateSelf()
            dataRepeat.setText(n.toString())
            handler.postDelayed(runnable,1000)
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
