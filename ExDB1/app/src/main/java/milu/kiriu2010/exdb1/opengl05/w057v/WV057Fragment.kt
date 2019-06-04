package milu.kiriu2010.exdb1.opengl05.w057v

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

class WV057Fragment : Fragment() {

    private lateinit var myGLES20View: MyGLES20View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl_w57, container, false)

        myGLES20View = view.findViewById(R.id.myGLES20ViewW57)
        val renderer = WV057Renderer(context!!)
        myGLES20View.setRenderer(renderer)
        myGLES20View.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                }
                MotionEvent.ACTION_DOWN -> {
                    Log.d(javaClass.simpleName,"ex[${event.x}]ey[${event.y}]")
                    Log.d(javaClass.simpleName,"vw[${myGLES20View.width}]vh[${myGLES20View.height}]")
                    renderer.receiveTouch(event,myGLES20View.width,myGLES20View.height)
                }
                MotionEvent.ACTION_MOVE -> {
                    renderer.receiveTouch(event,myGLES20View.width,myGLES20View.height)
                }
                else -> {
                }
            }
            true
        }

        val radioGroupW57 = view.findViewById<RadioGroup>(R.id.radioGroupW57)
        val radioButtonW57Render = view.findViewById<RadioButton>(R.id.radioButtonW57Render)
        val radioButtonW57Texture1 = view.findViewById<RadioButton>(R.id.radioButtonW57Texture1)
        val radioButtonW57Texture2 = view.findViewById<RadioButton>(R.id.radioButtonW57Texture2)
        radioGroupW57.setOnCheckedChangeListener { group, checkedId ->
            renderer.textureType = when (checkedId) {
                radioButtonW57Render.id -> 0
                radioButtonW57Texture1.id -> 1
                radioButtonW57Texture2.id -> 2
                else -> 0
            }
        }

        val checkBoxW57Gaussian = view.findViewById<CheckBox>(R.id.checkBoxW57Gaussian)
        checkBoxW57Gaussian.isChecked = when(renderer.u_gaussian) {
            1 -> true
            else -> false
        }
        checkBoxW57Gaussian.setOnCheckedChangeListener { buttonView, isChecked ->
            renderer.u_gaussian = when (isChecked) {
                true -> 1
                else -> 0
            }
        }

        val seekBarW57Dispersion = view.findViewById<SeekBar>(R.id.seekBarW57Dispersion)
        seekBarW57Dispersion.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                renderer.k_gaussian = if ( seekBar.progress > 0 ) {
                    (seekBar.progress+1).toFloat()
                }
                else {
                    1f
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                renderer.k_gaussian = if ( seekBar.progress > 0 ) {
                    (seekBar.progress+1).toFloat()
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
        myGLES20View.onResume()
    }

    override fun onPause() {
        super.onPause()
        myGLES20View.onPause()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                WV057Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
