package milu.kiriu2010.exdb1.draw


import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.decorate.DragonCurvDrawable
import milu.kiriu2010.gui.decorate.Mandelbrot2Drawable
import milu.kiriu2010.gui.decorate.MandelbrotDrawable

class DrawNaviFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_draw_navi, container, false)

        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val drawable = MandelbrotDrawable()
        imageView.setImageDrawable(drawable)

        // フラグメントの大きさを図る
        view.viewTreeObserver.addOnGlobalLayoutListener {
            Log.d(javaClass.simpleName, "w[${view.measuredWidth}]h[${view.measuredHeight}]" )
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                DrawNaviFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
