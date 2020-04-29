package milu.kiriu2010.exdb1.opengl06.w058v

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.view.MyGLES20View

class WV058Fragment : Fragment() {

    private lateinit var myGLES20View: MyGLES20View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl_w58, container, false)

        myGLES20View = view.findViewById(R.id.myGLES20ViewW58)
        val renderer = WV058Renderer(context!!)
        myGLES20View.setRenderer(renderer)
        myGLES20View.setOnTouchListener { _, event ->
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

        val checkBoxW58Glare = view.findViewById<CheckBox>(R.id.checkBoxW58Glare)
        checkBoxW58Glare.isChecked = when(renderer.u_glare) {
            1 -> true
            else -> false
        }
        checkBoxW58Glare.setOnCheckedChangeListener { _, isChecked ->
            renderer.u_glare = when (isChecked) {
                true -> 1
                else -> 0
            }
        }

        val seekBarW58Dispersion = view.findViewById<SeekBar>(R.id.seekBarW58Dispersion)
        seekBarW58Dispersion.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener{
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
                WV058Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
