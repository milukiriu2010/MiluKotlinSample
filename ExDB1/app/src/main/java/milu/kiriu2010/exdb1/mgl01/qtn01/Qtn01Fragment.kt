package milu.kiriu2010.exdb1.mgl01.qtn01

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl.MyGL02View

class Qtn01Fragment : Fragment() {

    private lateinit var myGL02View: MyGL02View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mgl01_qtn01, container, false)

        myGL02View = view.findViewById<MyGL02View>(R.id.myGL02View)
        val render = Qtn01Renderer(context!!)
        myGL02View.setRenderer(render)
        myGL02View.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    render.isRunning = false
                }
                MotionEvent.ACTION_DOWN -> {
                    //Log.d(javaClass.simpleName,"ex[${event.x}]ey[${event.y}]")
                    //Log.d(javaClass.simpleName,"vw[${myGL02View.width}]vh[${myGL02View.height}]")
                    render.isRunning = true
                    render.receiveTouch(event,myGL02View.width,myGL02View.height)
                }
                MotionEvent.ACTION_MOVE -> {
                    render.receiveTouch(event,myGL02View.width,myGL02View.height)
                }
                else -> {
                }
            }
            true
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
                Qtn01Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
