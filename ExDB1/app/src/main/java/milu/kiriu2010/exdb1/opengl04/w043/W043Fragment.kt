package milu.kiriu2010.exdb1.opengl04.w043

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
import milu.kiriu2010.exdb1.opengl.TextureView
import milu.kiriu2010.exdb1.opengl04.w042.W042Renderer

class W043Fragment : Fragment() {

    private lateinit var myGLView: MyGL02View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl04_w43, container, false)

        myGLView = view.findViewById(R.id.myGL02ViewA04)
        val bmp0 = BitmapFactory.decodeResource(resources,R.drawable.texture_w43_0)
        val bmp1 = BitmapFactory.decodeResource(resources,R.drawable.texture_w43_1)
        val render = W043Renderer()
        render.bmpArray.add(bmp0)
        render.bmpArray.add(bmp1)
        myGLView.setRenderer(render)
        myGLView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    render.rotateSwitch = false
                }
                MotionEvent.ACTION_DOWN -> {
                    Log.d(javaClass.simpleName,"ex[${event.x}]ey[${event.y}]")
                    Log.d(javaClass.simpleName,"vw[${myGLView.width}]vh[${myGLView.height}]")
                    render.rotateSwitch = true
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

        val seekBarW043 = view.findViewById<SeekBar>(R.id.seekBarW043)
        seekBarW043.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                render.hScale = seekBarW043.progress.toFloat()/(seekBarW043.max.toFloat()*100f)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                render.hScale = seekBarW043.progress.toFloat()/(seekBarW043.max.toFloat()*100f)
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
                W043Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
