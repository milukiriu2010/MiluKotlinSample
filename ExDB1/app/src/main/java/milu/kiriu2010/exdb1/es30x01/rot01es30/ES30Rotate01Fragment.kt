package milu.kiriu2010.exdb1.es30x01.rot01es30

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.view.MyGLES30View

class ES30Rotate01Fragment : Fragment() {

    private lateinit var myGLES30View: MyGLES30View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_es30_a03, container, false)

        myGLES30View = view.findViewById(R.id.myGLES30ViewA03)
        myGLES30View.setRenderer(ES30Rotate01Renderer(context!!))

        return view
    }

    override fun onResume() {
        super.onResume()
        myGLES30View.onResume()
    }

    override fun onPause() {
        super.onPause()
        myGLES30View.onPause()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                ES30Rotate01Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
