package milu.kiriu2010.exdb1.opengl01

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl.MyGL01View
import milu.kiriu2010.exdb1.opengl01.begin00.MyCube01Renderer

class OpenGL01DashboardFragment : Fragment() {

    private lateinit var myGL01View: MyGL01View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl_dashboard, container, false)

        myGL01View = view.findViewById<MyGL01View>(R.id.myGL01View)
        myGL01View.setRenderer(MyCube01Renderer())

        return view
    }

    override fun onResume() {
        super.onResume()
        myGL01View.onResume()
    }

    override fun onPause() {
        super.onPause()
        myGL01View.onPause()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                OpenGL01DashboardFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}