package milu.kiriu2010.exdb1.opengl05.w052

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

class W052Fragment : Fragment() {

    private lateinit var myGLView: MyGL02View

    private lateinit var seekBarW052: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl05_w52, container, false)

        myGLView = view.findViewById(R.id.myGL02ViewA05)
        val render = W052Renderer(context!!)
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
        seekBarW052 = view.findViewById(R.id.seekBarW052)
        seekBarW052.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.d(javaClass.simpleName,"seekBarW052:${seekBarW052.progress}")
                render.k = (seekBarW052.progress+30).toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Log.d(javaClass.simpleName,"seekBarW052:${seekBarW052.progress}")
                render.k = (seekBarW052.progress+30).toFloat()
            }

        })
        /*
        val radioGroupW051 = view.findViewById<RadioGroup>(R.id.radioGroupW051)
        radioGroupW051.setOnCheckedChangeListener { group, checkedId ->
            Log.d(javaClass.simpleName,"radioGroupW051:${checkedId}")
            render.u_depthBuffer = when (checkedId) {
                radioButtonW051Frag.id -> 1
                else -> 0
            }
        }
        */

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
                W052Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
