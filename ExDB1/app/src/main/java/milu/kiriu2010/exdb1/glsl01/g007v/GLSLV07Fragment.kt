package milu.kiriu2010.exdb1.glsl01.g007v

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.view.MyGLES20View

class GLSLV07Fragment : Fragment() {

    private lateinit var myGLES20View: MyGLES20View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_glsl_g07, container, false)

        myGLES20View = view.findViewById(R.id.myGLES20ViewG07)
        val renderer = GLSLV07Renderer(context!!)
        myGLES20View.setRenderer(renderer)

        myGLES20View.setOnTouchListener { v, event ->
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

        val radioGroupG07 = view.findViewById<RadioGroup>(R.id.radioGroupG07)
        val radioButtonG07Noise = view.findViewById<RadioButton>(R.id.radioButtonG07Noise)
        val radioButtonG07Seamless = view.findViewById<RadioButton>(R.id.radioButtonG07Seamless)
        radioGroupG07.setOnCheckedChangeListener { group, checkedId ->
            renderer.u_noiseType = when (checkedId) {
                radioButtonG07Noise.id    ->  0
                radioButtonG07Seamless.id ->  1
                else -> 0
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
                GLSLV07Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
