package milu.kiriu2010.exdb1.opengl

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import milu.kiriu2010.exdb1.R

class OpenGLHomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl_home, container, false)

        val myGLView = view.findViewById<MyGLView>(R.id.myGLView)

        return view
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
