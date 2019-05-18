package milu.kiriu2010.exdb1.glsl01.g004

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl.MyGL02View

class GLSL04Fragment : Fragment() {

    private lateinit var myGL02View: MyGL02View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_glsl_g04, container, false)

        myGL02View = view.findViewById(R.id.myGL02ViewG04)
        val renderer = GLSL04Renderer(context!!)
        myGL02View.setRenderer(renderer)

        myGL02View.setOnTouchListener { v, event ->
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

        val radioGroupG04A = view.findViewById<RadioGroup>(R.id.radioGroupG04A)
        val radioGroupG04B = view.findViewById<RadioGroup>(R.id.radioGroupG04B)


        val radioButtonG04Ring = view.findViewById<RadioButton>(R.id.radioButtonG04Ring)
        val radioButtonG04RingTime = view.findViewById<RadioButton>(R.id.radioButtonG04RingTime)
        val radioButtonG04Gradation = view.findViewById<RadioButton>(R.id.radioButtonG04Gradation)
        val radioButtonG04Cone = view.findViewById<RadioButton>(R.id.radioButtonG04Cone)
        val radioButtonG04ZoomLine = view.findViewById<RadioButton>(R.id.radioButtonG04ZoomLine)
        val radioButtonG04Flower1 = view.findViewById<RadioButton>(R.id.radioButtonG04Flower1)
        val radioButtonG04Wave = view.findViewById<RadioButton>(R.id.radioButtonG04Wave)
        val radioButtonG04Flower2 = view.findViewById<RadioButton>(R.id.radioButtonG04Flower2)
        val radioButtonG04Fan = view.findViewById<RadioButton>(R.id.radioButtonG04Fan)

        radioGroupG04A.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                radioButtonG04Ring.id      -> {
                    renderer.u_type = 1
                    radioGroupG04B.clearCheck()
                }
                radioButtonG04RingTime.id  -> {
                    renderer.u_type = 2
                    radioGroupG04B.clearCheck()
                }
                radioButtonG04Gradation.id -> {
                    renderer.u_type = 3
                    radioGroupG04B.clearCheck()
                }
                radioButtonG04Cone.id      -> {
                    renderer.u_type = 4
                    radioGroupG04B.clearCheck()
                }
                radioButtonG04ZoomLine.id  -> {
                    renderer.u_type = 5
                    radioGroupG04B.clearCheck()
                }
                else -> {

                }
            }
        }

        radioGroupG04B.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                radioButtonG04Flower1.id    -> {
                    renderer.u_type = 6
                    radioGroupG04A.clearCheck()
                }
                radioButtonG04Wave.id      -> {
                    renderer.u_type = 7
                    radioGroupG04A.clearCheck()
                }
                radioButtonG04Flower2.id    -> {
                    renderer.u_type = 8
                    radioGroupG04A.clearCheck()
                }
                radioButtonG04Fan.id    -> {
                    renderer.u_type = 9
                    radioGroupG04A.clearCheck()
                }
                else -> {

                }
            }
        }


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
                GLSL04Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}