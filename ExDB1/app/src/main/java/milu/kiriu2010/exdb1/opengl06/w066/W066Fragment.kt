package milu.kiriu2010.exdb1.opengl06.w066

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

class W066Fragment : Fragment() {

    private lateinit var myGLView: MyGL02View

    private lateinit var radioGroupW66: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl06_w66, container, false)

        myGLView = view.findViewById(R.id.myGL02ViewW66)
        val bmp0 = BitmapFactory.decodeResource(resources,R.drawable.texture_w55_01)
        val bmp1 = BitmapFactory.decodeResource(resources,R.drawable.texture_w55_02)
        val render = W066Renderer(context!!)
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

        radioGroupW66 = view.findViewById(R.id.radioGroupW66)
        val radioButtonW66Render = view.findViewById<RadioButton>(R.id.radioButtonW66Render)
        val radioButtonW66Texture1 = view.findViewById<RadioButton>(R.id.radioButtonW66Texture1)
        val radioButtonW66Texture2 = view.findViewById<RadioButton>(R.id.radioButtonW66Texture2)

        radioGroupW66.setOnCheckedChangeListener { group, checkedId ->
            render.textureType = when (checkedId) {
                radioButtonW66Render.id -> 0
                radioButtonW66Texture1.id -> 1
                radioButtonW66Texture2.id -> 2
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
                W066Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
