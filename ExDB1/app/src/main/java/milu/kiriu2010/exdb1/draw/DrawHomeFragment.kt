package milu.kiriu2010.exdb1.draw


import android.graphics.drawable.Animatable
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.decorate.Deco03ConstraintLayout
import milu.kiriu2010.gui.decorate.DecorateTextView
import milu.kiriu2010.gui.decorate.DecorateView
import milu.kiriu2010.gui.decorate.PolygonLapsDrawable

/**
 * A simple [Fragment] subclass.
 * Use the [DrawHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DrawHomeFragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_draw_home, container, false)

        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val polygonLapsDrawable = PolygonLapsDrawable()
        imageView.setImageDrawable(polygonLapsDrawable)

        val dv = 0.01f
        /*
        runnable = Runnable {
            if ( polygonLapsDrawable.dotProgress >= 1.0f ) {
                polygonLapsDrawable.dotProgress = 0f
            }
            else {
                polygonLapsDrawable.dotProgress += dv
            }
            handler.postDelayed(runnable, 100)
        }
        */
        runnable = Runnable {
            if ( polygonLapsDrawable.progress >= 1.0f ) {
                polygonLapsDrawable.progress = 0f
            }
            else {
                polygonLapsDrawable.progress += dv
            }
            handler.postDelayed(runnable, 100)
        }
        handler.post(runnable)

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
