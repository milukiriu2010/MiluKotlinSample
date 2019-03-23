package milu.kiriu2010.exdb1.mgl01.cube04

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.mgl01.cube01.Cube01Renderer
import milu.kiriu2010.exdb1.mgl01.cube02.Cube02Renderer
import milu.kiriu2010.exdb1.mgl01.cube03.Cube03Renderer
import milu.kiriu2010.exdb1.mgl01.cube04.Cube04Renderer
import milu.kiriu2010.exdb1.opengl.MyGL02View


class Cube04Fragment : Fragment() {

    private lateinit var myGL02View: MyGL02View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mgl_home, container, false)

        myGL02View = view.findViewById<MyGL02View>(R.id.myGL02View)
        //myGL02View.setRenderer(Cube01Renderer())
        //myGL02View.setRenderer(Cube02Renderer())
        //myGL02View.setRenderer(Cube03Renderer())
        myGL02View.setRenderer(Cube04Renderer())

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
                Cube04Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}