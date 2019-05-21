package milu.kiriu2010.exdb1.opengl05.w055

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl.MyGL02View

class W055Fragment : Fragment() {

    private lateinit var myGLView: MyGL02View

    private lateinit var radioGroupW55: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl05_w55, container, false)

        myGLView = view.findViewById(R.id.myGL02ViewA05)
        val bmp0 = BitmapFactory.decodeResource(resources,R.drawable.texture_w56_01)
        val bmp1 = BitmapFactory.decodeResource(resources,R.drawable.texture_w56_02)
        val render = W055Renderer(context!!)
        render.bmpArray.add(bmp0)
        render.bmpArray.add(bmp1)
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

        radioGroupW55 = view.findViewById(R.id.radioGroupW55)
        val radioButtonW55Render = view.findViewById<RadioButton>(R.id.radioButtonW55Render)
        val radioButtonW55Texture1 = view.findViewById<RadioButton>(R.id.radioButtonW55Texture1)
        val radioButtonW55Texture2 = view.findViewById<RadioButton>(R.id.radioButtonW55Texture2)

        radioGroupW55.setOnCheckedChangeListener { group, checkedId ->
            render.textureType = when (checkedId) {
                radioButtonW55Render.id -> 0
                radioButtonW55Texture1.id -> 1
                radioButtonW55Texture2.id -> 2
                else -> 0
            }
        }

        val checkBoxW55Sobel = view.findViewById<CheckBox>(R.id.checkBoxW55Sobel)
        checkBoxW55Sobel.setOnCheckedChangeListener { buttonView, isChecked ->
            render.u_sobel = when (isChecked) {
                true -> 1
                else -> 0
            }
        }

        val checkBoxW55Gray = view.findViewById<CheckBox>(R.id.checkBoxW55Gray)
        checkBoxW55Gray.setOnCheckedChangeListener { buttonView, isChecked ->
            render.u_sobelGray = when (isChecked) {
                true -> 1
                else -> 0
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
                W055Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
