package milu.kiriu2010.exdb1.opengl05.w055

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.view.MyGLES20View

class W055Fragment : Fragment() {

    private lateinit var myGLES20View: MyGLES20View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl_w55, container, false)

        myGLES20View = view.findViewById(R.id.myGLES20ViewW55)
        val renderer = W055Renderer(context!!)

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

        val radioGroupW55 = view.findViewById<RadioGroup>(R.id.radioGroupW55)
        val radioButtonW55Render = view.findViewById<RadioButton>(R.id.radioButtonW55Render)
        val radioButtonW55Texture1 = view.findViewById<RadioButton>(R.id.radioButtonW55Texture1)
        val radioButtonW55Texture2 = view.findViewById<RadioButton>(R.id.radioButtonW55Texture2)

        radioGroupW55.setOnCheckedChangeListener { _, checkedId ->
            renderer.textureType = when (checkedId) {
                radioButtonW55Render.id -> 0
                radioButtonW55Texture1.id -> 1
                radioButtonW55Texture2.id -> 2
                else -> 0
            }
        }

        val checkBoxW55Sobel = view.findViewById<CheckBox>(R.id.checkBoxW55Sobel)
        checkBoxW55Sobel.setOnCheckedChangeListener { _, isChecked ->
            renderer.u_sobel = when (isChecked) {
                true -> 1
                else -> 0
            }
        }

        val checkBoxW55Gray = view.findViewById<CheckBox>(R.id.checkBoxW55Gray)
        checkBoxW55Gray.setOnCheckedChangeListener { _, isChecked ->
            renderer.u_sobelGray = when (isChecked) {
                true -> 1
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
                W055Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
