package milu.kiriu2010.exdb1.opengl06.w066v

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

// ---------------------------------------
// emuglGLESv2_enc: device/generic/goldfish-opengl/system/GLESv2_enc/GL2Encoder.cpp:s_glVertexAttribPointer:599 GL error 0x501
// WV066ShaderMosaic:a_TexCoord:Board00Model:1281
// ---------------------------------------
// モザイク:VBOあり
// OpenGL ES 2.0
// ---------------------------------------
// https://wgld.org/d/webgl/w066.html
// ---------------------------------------
class WV066Fragment : Fragment() {

    private lateinit var myGLES20View: MyGLES20View

    private lateinit var radioGroupW66: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl_w66, container, false)

        myGLES20View = view.findViewById(R.id.myGLES20ViewW66)
        val renderer = WV066Renderer(context!!)
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

        radioGroupW66 = view.findViewById(R.id.radioGroupW66)
        val radioButtonW66Render = view.findViewById<RadioButton>(R.id.radioButtonW66Render)
        val radioButtonW66Texture1 = view.findViewById<RadioButton>(R.id.radioButtonW66Texture1)
        val radioButtonW66Texture2 = view.findViewById<RadioButton>(R.id.radioButtonW66Texture2)

        radioGroupW66.setOnCheckedChangeListener { _, checkedId ->
            renderer.textureType = when (checkedId) {
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
        myGLES20View.onResume()
    }

    override fun onPause() {
        super.onPause()
        myGLES20View.onPause()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                WV066Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
