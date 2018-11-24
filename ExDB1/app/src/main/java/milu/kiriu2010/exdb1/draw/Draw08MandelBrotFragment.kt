package milu.kiriu2010.exdb1.draw


import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.decorate.MandelbrotDrawable

class Draw08MandelBrotFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_draw08_mandel_brot, container, false)

        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val drawable = MandelbrotDrawable()
        imageView.setImageDrawable(drawable)

        return view
    }


    companion object {
        @JvmStatic
        fun newInstance() =
                Draw08MandelBrotFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
