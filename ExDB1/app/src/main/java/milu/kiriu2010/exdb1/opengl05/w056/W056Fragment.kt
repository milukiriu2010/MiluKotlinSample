package milu.kiriu2010.exdb1.opengl05.w056

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

class W056Fragment : Fragment() {

    private lateinit var myGLView: MyGL02View

    private lateinit var radioGroupW56: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl05_w56, container, false)

        myGLView = view.findViewById(R.id.myGL02ViewA05)
        val bmp0 = BitmapFactory.decodeResource(resources,R.drawable.texture_w55_01)
        val bmp1 = BitmapFactory.decodeResource(resources,R.drawable.texture_w55_02)
        val render = W056Renderer(context!!)
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

        radioGroupW56 = view.findViewById(R.id.radioGroupW56)
        val radioButtonW56Render = view.findViewById<RadioButton>(R.id.radioButtonW56Render)
        val radioButtonW56Texture1 = view.findViewById<RadioButton>(R.id.radioButtonW56Texture1)
        val radioButtonW56Texture2 = view.findViewById<RadioButton>(R.id.radioButtonW56Texture2)

        radioGroupW56.setOnCheckedChangeListener { group, checkedId ->
            render.textureType = when (checkedId) {
                radioButtonW56Render.id -> 0
                radioButtonW56Texture1.id -> 1
                radioButtonW56Texture2.id -> 2
                else -> 0
            }
        }

        val checkBoxW56Laplacian = view.findViewById<CheckBox>(R.id.checkBoxW57Laplacian)
        checkBoxW56Laplacian.setOnCheckedChangeListener { buttonView, isChecked ->
            render.u_laplacian = when (isChecked) {
                true -> 1
                else -> 0
            }
        }

        val checkBoxW56Gray = view.findViewById<CheckBox>(R.id.checkBoxW56Gray)
        checkBoxW56Gray.setOnCheckedChangeListener { buttonView, isChecked ->
            render.u_laplacianGray = when (isChecked) {
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
                W056Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
