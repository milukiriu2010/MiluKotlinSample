package milu.kiriu2010.exdb1.opengl01.begin00

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.view.MyGLES10View

class OpenGL10Fragment : Fragment() {

    private lateinit var myGLES10View: MyGLES10View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl_10, container, false)

        myGLES10View = view.findViewById(R.id.myGLES10View)
        myGLES10View.setRenderer(MyCube01Renderer())

        return view
    }

    override fun onResume() {
        super.onResume()
        myGLES10View.onResume()
    }

    override fun onPause() {
        super.onPause()
        myGLES10View.onPause()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                OpenGL10Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
