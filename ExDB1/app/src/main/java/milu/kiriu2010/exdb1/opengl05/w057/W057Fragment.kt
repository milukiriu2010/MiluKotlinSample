package milu.kiriu2010.exdb1.opengl05.w057

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

class W057Fragment : Fragment() {

    private lateinit var myGLView: MyGL02View

    private lateinit var radioGroupW57: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl05_w57, container, false)

        myGLView = view.findViewById(R.id.myGL02ViewA05)
        val bmp0 = BitmapFactory.decodeResource(resources,R.drawable.texture_w56_01)
        val bmp1 = BitmapFactory.decodeResource(resources,R.drawable.texture_w56_02)
        val render = W057Renderer()
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

        radioGroupW57 = view.findViewById(R.id.radioGroupW57)
        val radioButtonW57Render = view.findViewById<RadioButton>(R.id.radioButtonW57Render)
        val radioButtonW57Texture1 = view.findViewById<RadioButton>(R.id.radioButtonW57Texture1)
        val radioButtonW57Texture2 = view.findViewById<RadioButton>(R.id.radioButtonW57Texture2)

        radioGroupW57.setOnCheckedChangeListener { group, checkedId ->
            render.textureType = when (checkedId) {
                radioButtonW57Render.id -> 0
                radioButtonW57Texture1.id -> 1
                radioButtonW57Texture2.id -> 2
                else -> 0
            }
        }

        val checkBoxW57Gaussian = view.findViewById<CheckBox>(R.id.checkBoxW57Gaussian)
        checkBoxW57Gaussian.setOnCheckedChangeListener { buttonView, isChecked ->
            render.u_gaussian = when (isChecked) {
                true -> 1
                else -> 0
            }
        }

        val seekBarW57Dispersion = view.findViewById<SeekBar>(R.id.seekBarW57Dispersion)
        seekBarW57Dispersion.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                render.k_gaussian = if ( seekBarW57Dispersion.progress > 0 ) {
                    seekBarW57Dispersion.progress.toFloat()
                }
                else {
                    1f
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                render.k_gaussian = if ( seekBarW57Dispersion.progress > 0 ) {
                    seekBarW57Dispersion.progress.toFloat()
                }
                else {
                    1f
                }
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
                W057Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
