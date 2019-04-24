package milu.kiriu2010.exdb1.opengl03.w037

import android.graphics.BitmapFactory
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

class W037Fragment : Fragment() {

    private lateinit var myGL02View: MyGL02View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl03_w037, container, false)

        myGL02View = view.findViewById<MyGL02View>(R.id.myGL02ViewA03)
        val bmp0 = BitmapFactory.decodeResource(resources,R.drawable.texture_w037)
        val render = W037Renderer(context!!)
        render.bmpArray.add(bmp0)
        myGL02View.setRenderer(render)
        myGL02View.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    render.isRunning = false
                }
                MotionEvent.ACTION_DOWN -> {
                    Log.d(javaClass.simpleName,"ex[${event.x}]ey[${event.y}]")
                    Log.d(javaClass.simpleName,"vw[${myGL02View.width}]vh[${myGL02View.height}]")
                    render.isRunning = true
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
        val seekBarW037 = view.findViewById<SeekBar>(R.id.seekBarW037)
        seekBarW037.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                render.u_pointSize = seekBarW037.progress.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                render.u_pointSize = seekBarW037.progress.toFloat()
            }
        })

        // 線のプリミティブタイプを選択
        val radioGroupW037 = view.findViewById<RadioGroup>(R.id.radioGroupW037)
        val rbnW037Lines = view.findViewById<RadioButton>(R.id.rbnW037Lines)
        val rbnW037LineStrip = view.findViewById<RadioButton>(R.id.rbnW037LineStrip)
        val rbnW037LineLoop = view.findViewById<RadioButton>(R.id.rbnW037LineLoop)
        radioGroupW037.setOnCheckedChangeListener { group, checkedId ->
            render.lineType = when (checkedId) {
                rbnW037Lines.id -> GLES20.GL_LINES
                rbnW037LineStrip.id -> GLES20.GL_LINE_STRIP
                rbnW037LineLoop.id -> GLES20.GL_LINE_LOOP
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
                W037Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
