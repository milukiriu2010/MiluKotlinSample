package milu.kiriu2010.exdb1.glsl02.g020

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.view.MyGLES20View

// -------------------------------------------
// レイマーチングソフトシャドウ:VBOなし
// OpenGL ES 2.0
// -------------------------------------------
// https://wgld.org/d/glsl/g020.html
// -------------------------------------------
class GLSL20Fragment : Fragment() {

    private lateinit var myGLES20View: MyGLES20View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_glsl_g10, container, false)

        myGLES20View = view.findViewById(R.id.myGLES20ViewG10)
        val renderer = GLSL20Renderer(context!!)
        myGLES20View.setRenderer(renderer)

        myGLES20View.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    renderer.touchP.x = event.x.toFloat()/renderer.renderW.toFloat()
                    renderer.touchP.y = event.y.toFloat()/renderer.renderH.toFloat()
                }
                MotionEvent.ACTION_DOWN -> {
                    renderer.touchP.x = event.x.toFloat()/renderer.renderW.toFloat()
                    renderer.touchP.y = event.y.toFloat()/renderer.renderH.toFloat()
                }
                MotionEvent.ACTION_MOVE -> {
                }
                else -> {
                }
            }
            true
        }

        val checkBoxG10 = view.findViewById<CheckBox>(R.id.checkBoxG10)
        checkBoxG10.setOnCheckedChangeListener { _, isChecked ->
            renderer.u_showNormal = when (isChecked) {
                true  -> 1
                false -> 0
            }
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
                GLSL20Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
