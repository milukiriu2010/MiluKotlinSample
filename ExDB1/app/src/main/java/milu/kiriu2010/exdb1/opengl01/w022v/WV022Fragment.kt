package milu.kiriu2010.exdb1.opengl01.w022v

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.view.MyGLES20View

// ---------------------------------------------------
// 環境光によるライティング:VBOあり
// OpenGL ES 2.0
// ---------------------------------------------------
// https://wgld.org/d/webgl/w022.html
// ---------------------------------------------------
class WV022Fragment : Fragment() {

    private lateinit var myGLES20View: MyGLES20View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl_w15, container, false)

        myGLES20View = view.findViewById(R.id.myGLES20ViewW15)
        val renderer = WV022Renderer(context!!)
        myGLES20View.setRenderer(renderer)

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
                WV022Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
