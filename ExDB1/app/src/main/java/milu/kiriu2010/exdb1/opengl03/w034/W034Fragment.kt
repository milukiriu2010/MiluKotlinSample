package milu.kiriu2010.exdb1.opengl03.w034

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import kotlinx.android.synthetic.main.fragment_open_gl03_w034.view.*

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl.MyGL02View

class W034Fragment : Fragment() {

    private lateinit var myGL02View: MyGL02View

    private lateinit var seekBarW034: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl03_w034, container, false)

        myGL02View = view.findViewById<MyGL02View>(R.id.myGL02ViewW34)
        val render = W034Renderer()
        myGL02View.setRenderer(render)
        myGL02View.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    render.rotateSwitch = false
                }
                MotionEvent.ACTION_DOWN -> {
                    Log.d(javaClass.simpleName,"ex[${event.x}]ey[${event.y}]")
                    Log.d(javaClass.simpleName,"vw[${myGL02View.width}]vh[${myGL02View.height}]")
                    render.rotateSwitch = true
                    render.receiveTouch(event,myGL02View.width,myGL02View.height)
                }
                MotionEvent.ACTION_MOVE -> {
                    render.receiveTouch(event,myGL02View.width,myGL02View.height)
                }
                else -> {
                }
            }
            true
        }

        seekBarW034 = view.findViewById<SeekBar>(R.id.seekBarW034)
        seekBarW034.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                render.ktimeNow = seekBarW034.progress.toFloat()
                render.ktimeMax = seekBarW034.max.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                render.ktimeNow = seekBarW034.progress.toFloat()
                render.ktimeMax = seekBarW034.max.toFloat()
            }

        });


        return view
    }

    override fun onResume() {
        super.onResume()
        myGL02View.onResume()
    }

    override fun onPause() {
        super.onPause()
        myGL02View.onPause()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                W034Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
