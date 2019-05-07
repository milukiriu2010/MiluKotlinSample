package milu.kiriu2010.exdb1.opengl02.noise01

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl.MyGL02View
import milu.kiriu2010.exdb1.opengl01.w019.Noise01Renderer

class Noise01Fragment : Fragment() {

    private lateinit var myGL02View: MyGL02View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl02_noise01, container, false)

        myGL02View = view.findViewById<MyGL02View>(R.id.myGL02ViewNoise01)
        val bmp = BitmapFactory.decodeResource(resources,R.drawable.texture_w026)
        val render = Noise01Renderer(context!!)
        render.bmpArray.add(bmp)
        myGL02View.setRenderer(render)

        return view
    }

    override fun onResume() {
        super.onResume()
        myGL02View.onResume()
    }

    override fun onPause() {
        super.onPause()
        myGL02View.onPause()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                Noise01Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
