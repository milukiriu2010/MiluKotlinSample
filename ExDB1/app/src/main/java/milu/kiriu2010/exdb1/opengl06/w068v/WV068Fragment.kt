package milu.kiriu2010.exdb1.opengl06.w068v

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

// -----------------------------------------
// emuglGLESv2_enc: device/generic/goldfish-opengl/system/GLESv2_enc/GL2Encoder.cpp:s_glVertexAttribPointer:599 GL error 0x501
//    Info: Invalid vertex attribute index. Wanted index: 4294967295. Max index: 16
// WV068ShaderZoomBlur:a_TextureCoord:Board00Model:1281
// -----------------------------------------
// ゴッドレイフィルタ:VBOあり
// OpenGL ES 2.0
// -----------------------------------------
// https://wgld.org/d/webgl/w068.html
// -----------------------------------------
class WV068Fragment : Fragment() {

    private lateinit var myGLES20View: MyGLES20View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl_w68, container, false)

        myGLES20View = view.findViewById(R.id.myGLES20ViewW68)
        val renderer = WV068Renderer(context!!)
        myGLES20View.setRenderer(renderer)
        myGLES20View.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                }
                MotionEvent.ACTION_DOWN -> {
                    Log.d(javaClass.simpleName,"ex[${event.x}]ey[${event.y}]")
                    Log.d(javaClass.simpleName,"vw[${myGLES20View.width}]vh[${myGLES20View.height}]")
                    //render.receiveTouch(event,myGLES20View.width,myGLES20View.height)
                    renderer.mouseP[0] = event.x
                    renderer.mouseP[1] = event.y
                }
                MotionEvent.ACTION_MOVE -> {
                    //render.receiveTouch(event,myGLES20View.width,myGLES20View.height)
                    renderer.mouseP[0] = event.x
                    renderer.mouseP[1] = event.y
                }
                else -> {
                }
            }
            true
        }

        val seekBarW68 = view.findViewById<SeekBar>(R.id.seekBarW68)
        seekBarW68.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                renderer.u_strength = seekBar.progress.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                renderer.u_strength = seekBar.progress.toFloat()
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
                WV068Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
