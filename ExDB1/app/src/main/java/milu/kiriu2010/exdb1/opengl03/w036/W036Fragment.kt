package milu.kiriu2010.exdb1.opengl03.w036

import android.opengl.GLES20
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.view.MyGLES20View

// --------------------------------------
// 点や線のレンダリング:VBOなし
// OpenGL ES 2.0
// --------------------------------------
// https://wgld.org/d/webgl/w036.html
// --------------------------------------
class W036Fragment : Fragment() {

    private lateinit var myGLES20View: MyGLES20View

    private lateinit var textViewW36MinSizeVal: TextView

    private lateinit var textViewW36MaxSizeVal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl_w36, container, false)

        myGLES20View = view.findViewById(R.id.myGLES20ViewW36)
        val renderer = W036Renderer(context!!)
        myGLES20View.setRenderer(renderer)
        myGLES20View.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    renderer.isRunning = false
                }
                MotionEvent.ACTION_DOWN -> {
                    Log.d(javaClass.simpleName,"ex[${event.x}]ey[${event.y}]")
                    Log.d(javaClass.simpleName,"vw[${myGLES20View.width}]vh[${myGLES20View.height}]")
                    renderer.isRunning = true
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
        
        // 点のサイズ
        val seekBarW36 = view.findViewById<SeekBar>(R.id.seekBarW36)
        seekBarW36.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                renderer.u_pointSize = (seekBar.progress+20).toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                renderer.u_pointSize = (seekBar.progress+20).toFloat()
            }
        })

        // 線のプリミティブタイプを選択
        val radioGroupW36 = view.findViewById<RadioGroup>(R.id.radioGroupW36)
        val rbnW36Lines = view.findViewById<RadioButton>(R.id.rbnW36Lines)
        val rbnW36LineStrip = view.findViewById<RadioButton>(R.id.rbnW36LineStrip)
        val rbnW36LineLoop = view.findViewById<RadioButton>(R.id.rbnW36LineLoop)
        radioGroupW36.setOnCheckedChangeListener { _, checkedId ->
            renderer.lineType = when (checkedId) {
                rbnW36Lines.id     -> GLES20.GL_LINES
                rbnW36LineStrip.id -> GLES20.GL_LINE_STRIP
                rbnW36LineLoop.id  -> GLES20.GL_LINE_LOOP
                else               -> GLES20.GL_LINES
            }
        }

        // 点のサイズ(最小)
        textViewW36MinSizeVal = view.findViewById(R.id.textViewW36MinSizeVal)
        textViewW36MinSizeVal.text = renderer.pointSizeRange[0].toString()

        // 点のサイズ(最大)
        textViewW36MaxSizeVal = view.findViewById(R.id.textViewW36MaxSizeVal)
        textViewW36MaxSizeVal.text = renderer.pointSizeRange[1].toString()

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
                W036Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
