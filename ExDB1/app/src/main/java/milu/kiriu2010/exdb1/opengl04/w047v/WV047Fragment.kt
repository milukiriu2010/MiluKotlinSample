package milu.kiriu2010.exdb1.opengl04.w047v

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.view.MyGLES20View

// --------------------------------------
// 動的キューブマッピング:VBOあり
// OpenGL ES 2.0
// --------------------------------------
// https://wgld.org/d/webgl/w047.html
// --------------------------------------
// キューブマップのシェーダで
// UseProgramで0x502が発生する
// トーラスが写らない
// --------------------------------------
class WV047Fragment : Fragment() {

    private lateinit var myGLES20View: MyGLES20View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl_w15, container, false)

        myGLES20View = view.findViewById(R.id.myGLES20ViewW15)
        val renderer = WV047Renderer(context!!)
        myGLES20View.setRenderer(renderer)
        myGLES20View.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    renderer.isRunning = false
                }
                MotionEvent.ACTION_DOWN -> {
                    Log.d(javaClass.simpleName,"ex[${event.x}]ey[${event.y}]")
                    Log.d(javaClass.simpleName,"vw[${myGLES20View.width}]vh[${myGLES20View.height}]")
                    renderer.isRunning = true
                    renderer.receiveTouch(event,myGLES20View.width,myGLES20View.height)
                }
                MotionEvent.ACTION_MOVE -> {
                    renderer.receiveTouch(event,myGLES20View.width,myGLES20View.height)
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
        myGLES20View.onResume()
    }

    override fun onPause() {
        super.onPause()
        myGLES20View.onPause()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                WV047Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
