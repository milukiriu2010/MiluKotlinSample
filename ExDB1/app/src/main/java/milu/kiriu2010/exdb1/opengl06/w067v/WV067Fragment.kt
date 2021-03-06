package milu.kiriu2010.exdb1.opengl06.w067v

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

// -----------------------------------
// emuglGLESv2_enc: device/generic/goldfish-opengl/system/GLESv2_enc/GL2Encoder.cpp:s_glVertexAttribPointer:599 GL error 0x501
//    Info: Invalid vertex attribute index. Wanted index: 4294967295. Max index: 16
// WV067ShaderZoomBlur:a_TextureCoord:Board00Model:1281
// -----------------------------------
// ズームブラーフィルタ:VBOあり
// OpenGL ES 2.0
// -----------------------------------
// https://wgld.org/d/webgl/w067.html
// -----------------------------------
class WV067Fragment : Fragment() {

    private lateinit var myGLES20View: MyGLES20View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl_w67, container, false)

        myGLES20View = view.findViewById(R.id.myGLES20ViewW67)
        val renderer = WV067Renderer(context!!)
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

        val radioGroupW67 = view.findViewById<RadioGroup>(R.id.radioGroupW67)
        val radioButtonW67Render = view.findViewById<RadioButton>(R.id.radioButtonW67Render)
        val radioButtonW67Texture1 = view.findViewById<RadioButton>(R.id.radioButtonW67Texture1)
        val radioButtonW67Texture2 = view.findViewById<RadioButton>(R.id.radioButtonW67Texture2)

        radioGroupW67.setOnCheckedChangeListener { _, checkedId ->
            renderer.textureType = when (checkedId) {
                radioButtonW67Render.id -> 0
                radioButtonW67Texture1.id -> 1
                radioButtonW67Texture2.id -> 2
                else -> 0
            }
        }

        val seekBarW67 = view.findViewById<SeekBar>(R.id.seekBarW67)
        seekBarW67.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                renderer.u_strength = seekBar.progress.toFloat() * 3f
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                renderer.u_strength = seekBar.progress.toFloat() * 3f
            }
        })

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
                WV067Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
