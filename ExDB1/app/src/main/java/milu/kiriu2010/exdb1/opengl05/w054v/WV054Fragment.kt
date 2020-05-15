package milu.kiriu2010.exdb1.opengl05.w054v

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.view.MyGLES20View

// -------------------------------------------
// セピア調変換:VBOあり
// OpenGL ES 2.0
// -------------------------------------------
// https://wgld.org/d/webgl/w054.html
// -------------------------------------------
class WV054Fragment : Fragment() {

    private lateinit var myGLES20View: MyGLES20View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl_w54, container, false)

        myGLES20View = view.findViewById(R.id.myGLES20ViewW54)
        val renderer = WV054Renderer(context!!)
        myGLES20View.setRenderer(renderer)
        myGLES20View.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                }
                MotionEvent.ACTION_DOWN -> {
                    Log.d(javaClass.simpleName,"ex[${event.x}]ey[${event.y}]")
                    Log.d(javaClass.simpleName,"vw[${myGLES20View.width}]vh[${myGLES20View.height}]")
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

        val radioGroupW54 = view.findViewById<RadioGroup>(R.id.radioGroupW54)
        val radioButtonW54Color = view.findViewById<RadioButton>(R.id.radioButtonW54Color)
        val radioButtonW54Gray = view.findViewById<RadioButton>(R.id.radioButtonW54Gray)
        val radioButtonW54Sepia = view.findViewById<RadioButton>(R.id.radioButtonW54Sepia)

        radioGroupW54.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                radioButtonW54Color.id -> {
                    renderer.u_grayScale = 0
                    renderer.u_sepiaScale = 0
                }
                radioButtonW54Gray.id -> {
                    renderer.u_grayScale = 1
                    renderer.u_sepiaScale = 0
                }
                radioButtonW54Sepia.id -> {
                    renderer.u_grayScale = 0
                    renderer.u_sepiaScale = 1
                }
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
                WV054Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
