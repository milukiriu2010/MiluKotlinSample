package milu.kiriu2010.exdb1.opengl06.w068

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

class W068Fragment : Fragment() {

    private lateinit var myGLView: MyGL02View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl06_w68, container, false)

        myGLView = view.findViewById(R.id.myGL02ViewW68)
        val bmp0 = BitmapFactory.decodeResource(resources,R.drawable.texture_w68)
        val render = W068Renderer(context!!)
        render.bmpArray.add(bmp0)
        myGLView.setRenderer(render)
        myGLView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                }
                MotionEvent.ACTION_DOWN -> {
                    Log.d(javaClass.simpleName,"ex[${event.x}]ey[${event.y}]")
                    Log.d(javaClass.simpleName,"vw[${myGLView.width}]vh[${myGLView.height}]")
                    //render.receiveTouch(event,myGLView.width,myGLView.height)
                    render.mouseP[0] = event.x
                    render.mouseP[1] = event.y
                }
                MotionEvent.ACTION_MOVE -> {
                    //render.receiveTouch(event,myGLView.width,myGLView.height)
                    render.mouseP[0] = event.x
                    render.mouseP[1] = event.y
                }
                else -> {
                }
            }
            true
        }

        val seekBarW68 = view.findViewById<SeekBar>(R.id.seekBarW68)
        seekBarW68.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                render.u_strength = seekBar.progress.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                render.u_strength = seekBar.progress.toFloat()
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
                W068Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
