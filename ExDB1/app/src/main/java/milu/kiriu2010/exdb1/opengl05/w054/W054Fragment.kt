package milu.kiriu2010.exdb1.opengl05.w054

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl.MyGL02View

class W054Fragment : Fragment() {

    private lateinit var myGLView: MyGL02View

    private lateinit var radioGroupW54: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl05_w54, container, false)

        myGLView = view.findViewById(R.id.myGL02ViewA05)
        val render = W054Renderer(context!!)
        myGLView.setRenderer(render)
        myGLView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                }
                MotionEvent.ACTION_DOWN -> {
                    Log.d(javaClass.simpleName,"ex[${event.x}]ey[${event.y}]")
                    Log.d(javaClass.simpleName,"vw[${myGLView.width}]vh[${myGLView.height}]")
                    render.receiveTouch(event,myGLView.width,myGLView.height)
                }
                MotionEvent.ACTION_MOVE -> {
                    render.receiveTouch(event,myGLView.width,myGLView.height)
                }
                else -> {
                }
            }
            true
        }

        radioGroupW54 = view.findViewById(R.id.radioGroupW54)
        val radioButtonW54Color = view.findViewById<RadioButton>(R.id.radioButtonW54Color)
        val radioButtonW54Gray = view.findViewById<RadioButton>(R.id.radioButtonW54Gray)
        val radioButtonW54Sepia = view.findViewById<RadioButton>(R.id.radioButtonW54Sepia)

        radioGroupW54.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                radioButtonW54Color.id -> {
                    render.u_grayScale = 0
                    render.u_sepiaScale = 0
                }
                radioButtonW54Gray.id -> {
                    render.u_grayScale = 1
                    render.u_sepiaScale = 0
                }
                radioButtonW54Sepia.id -> {
                    render.u_grayScale = 0
                    render.u_sepiaScale = 1
                }
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        myGLView.onResume()
    }

    override fun onPause() {
        super.onPause()
        myGLView.onPause()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                W054Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
