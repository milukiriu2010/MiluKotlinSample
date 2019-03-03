package milu.kiriu2010.exdb1.opengl

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl.triangle01.MyTriangle01Renderer
import milu.kiriu2010.exdb1.opengl.triangle02.MyTriangle02Renderer
import milu.kiriu2010.exdb1.opengl.triangle03.MyTriangle03Renderer
import milu.kiriu2010.exdb1.opengl.triangle04.MyTriangle04Renderer
import milu.kiriu2010.exdb1.opengl.triangle05.MyTriangle05Renderer

class OpenGLHomeFragment : Fragment() {

    private lateinit var myGL02View: MyGL02View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl_home, container, false)

        myGL02View = view.findViewById<MyGL02View>(R.id.myGL02View)
        //myGL02View.setRenderer(MyTriangle01Renderer())
        //myGL02View.setRenderer(MyTriangle02Renderer())
        //myGL02View.setRenderer(MyTriangle03Renderer())
        //myGL02View.setRenderer(MyTriangle04Renderer())
        myGL02View.setRenderer(MyTriangle05Renderer())

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
                OpenGLHomeFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}