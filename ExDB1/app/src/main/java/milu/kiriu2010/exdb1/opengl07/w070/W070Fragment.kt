package milu.kiriu2010.exdb1.opengl07.w070

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.view.MyGLES20View

// -------------------------------------------
// 正しい深度値を適用したシャドウマッピング:VBOなし
// OpenGL ES 2.0
// -------------------------------------------
// w69に対して
// 浮動小数点数テクスチャに対応
// -------------------------------------------
// https://wgld.org/d/webgl/w070.html
// -------------------------------------------
class W070Fragment : Fragment() {

    private lateinit var myGLES20View: MyGLES20View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl_w69, container, false)

        myGLES20View = view.findViewById(R.id.myGLES20ViewW69)
        val renderer = W070Renderer(context!!)
        myGLES20View.setRenderer(renderer)
        myGLES20View.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    renderer.isRunning = false
                }
                MotionEvent.ACTION_DOWN -> {
                    Log.d(javaClass.simpleName,"ex[${event.x}]ey[${event.y}]")
                    Log.d(javaClass.simpleName,"vw[${myGLES20View.width}]vh[${myGLES20View.height}]")
                    renderer.isRunning = true
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
        val seekBarW69 = view.findViewById<SeekBar>(R.id.seekBarW69)
        seekBarW69.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                renderer.k = (seekBar.progress+20).toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                renderer.k = (seekBar.progress+20).toFloat()
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
                W070Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
