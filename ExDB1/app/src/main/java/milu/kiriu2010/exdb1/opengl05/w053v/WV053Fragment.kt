package milu.kiriu2010.exdb1.opengl05.w053v

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Switch

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.view.MyGLES20View

// -------------------------------------
// シャドウマッピング:VBOあり
// OpenGL ES 2.0
// -------------------------------------
// https://wgld.org/d/webgl/w053.html
// -------------------------------------
class WV053Fragment : Fragment() {

    private lateinit var myGLES20View: MyGLES20View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl_w53, container, false)

        myGLES20View = view.findViewById(R.id.myGLES20ViewW53)
        val render = WV053Renderer(context!!)
        myGLES20View.setRenderer(render)
        myGLES20View.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                }
                MotionEvent.ACTION_DOWN -> {
                    Log.d(javaClass.simpleName,"ex[${event.x}]ey[${event.y}]")
                    Log.d(javaClass.simpleName,"vw[${myGLES20View.width}]vh[${myGLES20View.height}]")
                    render.receiveTouch(event,myGLES20View.width,myGLES20View.height)
                }
                MotionEvent.ACTION_MOVE -> {
                    render.receiveTouch(event,myGLES20View.width,myGLES20View.height)
                }
                else -> {
                }
            }
            true
        }

        val switchW53 = view.findViewById<Switch>(R.id.switchW53)
        switchW53.setOnCheckedChangeListener { _, isChecked ->
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
        myGLES20View.onResume()
    }

    override fun onPause() {
        super.onPause()
        myGLES20View.onPause()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                WV053Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
