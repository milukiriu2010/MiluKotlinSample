package milu.kiriu2010.exdb1.opengl01.w019

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl.MyGL02View
import milu.kiriu2010.gui.view.MyGLES20View

class W019Fragment : Fragment() {

    private lateinit var myGLES20View: MyGLES20View

    private lateinit var switchCulling: Switch

    private lateinit var switchFront: Switch

    private lateinit var switchDepth: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl_w19, container, false)

        myGLES20View = view.findViewById(R.id.myGLES20ViewW19)
        var renderer = W019Renderer(context!!)
        myGLES20View.setRenderer(renderer)

        switchCulling = view.findViewById(R.id.switchCullingW19)
        switchCulling.setOnCheckedChangeListener { buttonView, isChecked ->
            renderer.culling = isChecked
        }

        switchFront = view.findViewById(R.id.switchFrontW19)
        switchFront.setOnCheckedChangeListener { buttonView, isChecked ->
            renderer.frontFace = isChecked
        }

        switchDepth = view.findViewById(R.id.switchDepthW19)
        switchDepth.setOnCheckedChangeListener { buttonView, isChecked ->
            renderer.depthTest = isChecked
        }

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
                W019Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
