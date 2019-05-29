package milu.kiriu2010.exdb1.es30x01.a06

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.view.MyGLES30View

class A06Fragment : Fragment() {

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
        val renderer = A06Renderer(context!!)
        myGLES30View.setRenderer(renderer)
        myGLES30View.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                }
                MotionEvent.ACTION_DOWN -> {
                    Log.d(javaClass.simpleName,"ex[${event.x}]ey[${event.y}]")
                    Log.d(javaClass.simpleName,"vw[${myGLES30View.width}]vh[${myGLES30View.height}]")
                    renderer.receiveTouch(event,myGLES30View.width,myGLES30View.height)
                }
                MotionEvent.ACTION_MOVE -> {
                    renderer.receiveTouch(event,myGLES30View.width,myGLES30View.height)
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
        myGLES30View.onResume()
    }

    override fun onPause() {
        super.onPause()
        myGLES30View.onPause()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                A06Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
