package milu.kiriu2010.exdb1.opengl06.w067

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

class W067Fragment : Fragment() {

    private lateinit var myGLView: MyGL02View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl06_w67, container, false)

        myGLView = view.findViewById(R.id.myGL02ViewW67)
        val bmp0 = BitmapFactory.decodeResource(resources,R.drawable.texture_w56_01)
        val bmp1 = BitmapFactory.decodeResource(resources,R.drawable.texture_w56_02)
        val render = W067Renderer(context!!)
        render.bmpArray.add(bmp0)
        render.bmpArray.add(bmp1)
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

        val radioGroupW67 = view.findViewById<RadioGroup>(R.id.radioGroupW67)
        val radioButtonW67Render = view.findViewById<RadioButton>(R.id.radioButtonW67Render)
        val radioButtonW67Texture1 = view.findViewById<RadioButton>(R.id.radioButtonW67Texture1)
        val radioButtonW67Texture2 = view.findViewById<RadioButton>(R.id.radioButtonW67Texture2)

        radioGroupW67.setOnCheckedChangeListener { group, checkedId ->
            render.textureType = when (checkedId) {
                radioButtonW67Render.id -> 0
                radioButtonW67Texture1.id -> 1
                radioButtonW67Texture2.id -> 2
                else -> 0
            }
        }

        val seekBarW67 = view.findViewById<SeekBar>(R.id.seekBarW67)
        seekBarW67.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                render.u_strength = seekBar.progress.toFloat() * 0.3f
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                render.u_strength = seekBar.progress.toFloat() * 0.3f
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
                W067Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
