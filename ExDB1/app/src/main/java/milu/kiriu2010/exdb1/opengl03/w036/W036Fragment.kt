package milu.kiriu2010.exdb1.opengl03.w036

import android.opengl.GLES20
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl.MyGL02View

class W036Fragment : Fragment() {

    private lateinit var myGL02View: MyGL02View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl03_w036, container, false)

        myGL02View = view.findViewById<MyGL02View>(R.id.myGL02ViewA03)
        val render = W036Renderer()
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


        // 点のサイズ
        val seekBarW036 = view.findViewById<SeekBar>(R.id.seekBarW036)
        seekBarW036.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                //render.u_pointSize = seekBarW036.progress.toFloat()/seekBarW036.max.toFloat()
                render.u_pointSize = seekBarW036.progress.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //render.u_pointSize = seekBarW036.progress.toFloat()/seekBarW036.max.toFloat()
                render.u_pointSize = seekBarW036.progress.toFloat()
            }
        })

        // 線のプリミティブタイプを選択
        val radioGroupW036 = view.findViewById<RadioGroup>(R.id.radioGroupW036)
        val rbnW036Lines = view.findViewById<RadioButton>(R.id.rbnW036Lines)
        val rbnW036LineStrip = view.findViewById<RadioButton>(R.id.rbnW036LineStrip)
        val rbnW036LineLoop = view.findViewById<RadioButton>(R.id.rbnW036LineLoop)
        radioGroupW036.setOnCheckedChangeListener { group, checkedId ->
            render.lineType = when (checkedId) {
                rbnW036Lines.id -> GLES20.GL_LINES
                rbnW036LineStrip.id -> GLES20.GL_LINE_STRIP
                rbnW036LineLoop.id -> GLES20.GL_LINE_LOOP
                else -> GLES20.GL_LINES
            }
        }


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
                W036Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
