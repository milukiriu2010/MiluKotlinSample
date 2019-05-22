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
import milu.kiriu2010.gui.view.MyGLES20View

class W067Fragment : Fragment() {

    private lateinit var myGLES20View: MyGLES20View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl06_w67, container, false)

        myGLES20View = view.findViewById(R.id.myGLES20ViewW67)
        val bmp0 = BitmapFactory.decodeResource(resources,R.drawable.texture_w55_01)
        val bmp1 = BitmapFactory.decodeResource(resources,R.drawable.texture_w55_02)
        val render = W067Renderer(context!!)
        render.bmpArray.add(bmp0)
        render.bmpArray.add(bmp1)
        myGLES20View.setRenderer(render)
        myGLES20View.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                }
                MotionEvent.ACTION_DOWN -> {
                    Log.d(javaClass.simpleName,"ex[${event.x}]ey[${event.y}]")
                    Log.d(javaClass.simpleName,"vw[${myGLES20View.width}]vh[${myGLES20View.height}]")
                    render.receiveTouch(event,myGLES20View.width,myGLES20View.height)
                }
                MotionEvent.ACTION_MOVE -> {
                    render.receiveTouch(event,myGLES20View.width,myGLES20View.height)
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
        myGLES20View.onResume()
    }

    override fun onPause() {
        super.onPause()
        myGLES20View.onPause()
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
