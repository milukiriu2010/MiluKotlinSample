package milu.kiriu2010.exdb1.opengl05.w053

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Switch
import kotlinx.android.synthetic.main.fragment_open_gl05_w51.*

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl.MyGL02View

class W053Fragment : Fragment() {

    private lateinit var myGLView: MyGL02View

    private lateinit var switch: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl05_w53, container, false)

        myGLView = view.findViewById(R.id.myGL02ViewA05)
        val render = W053Renderer(context!!)
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
        switch = view.findViewById(R.id.switchW53)
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.d(javaClass.simpleName,"W53:switch:${isChecked}")
            when (isChecked) {
                true -> render.u_grayScale = 1
                false -> render.u_grayScale = 0
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
                W053Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
