package milu.kiriu2010.exdb1.opengl08.w086v

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.view.MyGLES20View

// ------------------------------------
// 描画結果から色を取得:VBO使用
// ------------------------------------
// https://wgld.org/d/webgl/w086.html
// ------------------------------------
class WV086Fragment : Fragment() {

    private lateinit var myGLES20View: MyGLES20View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl_w86, container, false)

        val textViewW86 = view.findViewById<TextView>(R.id.textViewW86)

        myGLES20View = view.findViewById(R.id.myGLES20ViewW86)
        val renderer = WV086Renderer(context!!)
        myGLES20View.setRenderer(renderer)
        myGLES20View.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                }
                MotionEvent.ACTION_DOWN -> {
                    renderer.touchP.x = event.x
                    renderer.touchP.y = event.y

                    textViewW86.text = "R[${renderer.colorBuf.get(0)}]G[${renderer.colorBuf.get(1)}]B[${renderer.colorBuf.get(2)}]A[${renderer.colorBuf.get(3)}]"
                }
                MotionEvent.ACTION_MOVE -> {
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
                WV086Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
