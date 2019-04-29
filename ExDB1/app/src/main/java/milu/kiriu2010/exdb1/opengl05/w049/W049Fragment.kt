package milu.kiriu2010.exdb1.opengl05.w049

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl.MyGL02View
import milu.kiriu2010.exdb1.opengl05.w048.W048Renderer

class W049Fragment : Fragment() {

    private lateinit var myGLView: MyGL02View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl05_w49, container, false)

        myGLView = view.findViewById(R.id.myGL02ViewA05)
        val bmp0 = BitmapFactory.decodeResource(resources,R.drawable.texture_w49)
        val render = W049Renderer(context!!)
        render.bmpArray.add(bmp0)
        myGLView.setRenderer(render)
        myGLView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    render.isRunning = false
                }
                MotionEvent.ACTION_DOWN -> {
                    Log.d(javaClass.simpleName,"ex[${event.x}]ey[${event.y}]")
                    Log.d(javaClass.simpleName,"vw[${myGLView.width}]vh[${myGLView.height}]")
                    render.isRunning = true
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
        val seekBarW049 = view.findViewById<SeekBar>(R.id.seekBarW049)
        seekBarW049.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                render.k = seekBarW049.progress.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                render.k = seekBarW049.progress.toFloat()
            }

        })

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
                W049Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
