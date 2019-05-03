package milu.kiriu2010.exdb1.opengl06.w058

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

class W058Fragment : Fragment() {

    private lateinit var myGLView: MyGL02View

    private lateinit var radioGroupW58: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl05_w58, container, false)

        myGLView = view.findViewById(R.id.myGL02ViewW58)
        val render = W058Renderer(context!!)
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

        val checkBoxW58Glare = view.findViewById<CheckBox>(R.id.checkBoxW58Glare)
        checkBoxW58Glare.isChecked = when(render.u_glare) {
            1 -> true
            else -> false
        }
        checkBoxW58Glare.setOnCheckedChangeListener { buttonView, isChecked ->
            render.u_glare = when (isChecked) {
                true -> 1
                else -> 0
            }
        }

        val seekBarW58Dispersion = view.findViewById<SeekBar>(R.id.seekBarW58Dispersion)
        seekBarW58Dispersion.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                render.k_gaussian = if ( seekBarW58Dispersion.progress > 0 ) {
                    seekBarW58Dispersion.progress.toFloat()
                }
                else {
                    1f
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                render.k_gaussian = if ( seekBarW58Dispersion.progress > 0 ) {
                    seekBarW58Dispersion.progress.toFloat()
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
                W058Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
