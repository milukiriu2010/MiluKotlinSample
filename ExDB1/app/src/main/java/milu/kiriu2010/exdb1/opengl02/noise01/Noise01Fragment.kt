package milu.kiriu2010.exdb1.opengl02.noise01

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl01.w019.Noise01Renderer
import milu.kiriu2010.gui.view.MyGLES20View

class Noise01Fragment : Fragment() {

    private lateinit var myGLES20View: MyGLES20View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl_noise01, container, false)

        myGLES20View = view.findViewById(R.id.myGLES20ViewNoise01)
        val bmp = BitmapFactory.decodeResource(resources,R.drawable.texture_w026)
        val render = Noise01Renderer(context!!)
        render.bmpArray.add(bmp)
        myGLES20View.setRenderer(render)

        return view
    }

    override fun onResume() {
        super.onResume()
        myGLES20View.onResume()
    }

    override fun onPause() {
        super.onPause()
        myGLES20View.onPause()
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
