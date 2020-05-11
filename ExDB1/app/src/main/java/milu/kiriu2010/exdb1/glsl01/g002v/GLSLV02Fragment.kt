package milu.kiriu2010.exdb1.glsl01.g002v

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.view.MyGLES20View

// ----------------------------------------------
// 同心円を描く:VBOあり
// 時間経過とマウスカーソル座標を利用したサンプル
// OpenGL ES 2.0
// ----------------------------------------------
// https://wgld.org/d/glsl/g002.html
// ----------------------------------------------
class GLSLV02Fragment : Fragment() {

    private lateinit var myGLES20View: MyGLES20View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_glsl_g02, container, false)

        myGLES20View = view.findViewById(R.id.myGLES20ViewG02)
        val renderer = GLSLV02Renderer(context!!)
        myGLES20View.setRenderer(renderer)

        myGLES20View.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    renderer.touchP.x = event.x.toFloat()/renderer.renderW.toFloat()
                    renderer.touchP.y = event.y.toFloat()/renderer.renderH.toFloat()
                }
                MotionEvent.ACTION_DOWN -> {
                    renderer.touchP.x = event.x.toFloat()/renderer.renderW.toFloat()
                    renderer.touchP.y = event.y.toFloat()/renderer.renderH.toFloat()
                }
                MotionEvent.ACTION_MOVE -> {
                }
                else -> {
                }
            }
            true
        }

        val seekBarG02Speed = view.findViewById<SeekBar>(R.id.seekBarG02Speed)
        seekBarG02Speed.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                renderer.u_speed = seekBar.progress.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                renderer.u_speed = seekBar.progress.toFloat()
            }
        })

        val seekBarG02Gap = view.findViewById<SeekBar>(R.id.seekBarG02Gap)
        seekBarG02Gap.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                renderer.u_gap = seekBar.progress.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                renderer.u_gap = seekBar.progress.toFloat()
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
                GLSLV02Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
