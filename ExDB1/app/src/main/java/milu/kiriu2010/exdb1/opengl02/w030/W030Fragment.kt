package milu.kiriu2010.exdb1.opengl02.w030

import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import kotlinx.android.synthetic.main.fragment_open_gl02_w030.*

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl.TextureView
import milu.kiriu2010.exdb1.opengl01.w019.W030Renderer

class W030Fragment : Fragment() {

    private lateinit var textureView: TextureView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl02_w030_x, container, false)

        textureView = view.findViewById<TextureView>(R.id.textureView)
        val bmp0 = BitmapFactory.decodeResource(resources,R.drawable.texture_w029)
        val render = W030Renderer()
        render.bmpArray.add(bmp0)
        textureView.setRenderer(render)
        textureView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    render.rotateSwitch = false
                }
                MotionEvent.ACTION_DOWN -> {
                    Log.d(javaClass.simpleName,"ex[${event.x}]ey[${event.y}]")
                    Log.d(javaClass.simpleName,"vw[${textureView.width}]vh[${textureView.height}]")
                    render.rotateSwitch = true
                    render.receiveTouch(event,textureView.width,textureView.height)
                }
                MotionEvent.ACTION_MOVE -> {
                    render.receiveTouch(event,textureView.width,textureView.height)
                }
                else -> {
                }
            }
            true
        }

        // ブレンド有効
        val checkBoxW030 = view.findViewById<CheckBox>(R.id.checkBoxW030)
        checkBoxW030.setOnCheckedChangeListener { buttonView, isChecked ->
            render.blend = isChecked
        }

        // アルファ成分
        val seekBarW030 = view.findViewById<SeekBar>(R.id.seekBarW030)
        seekBarW030.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                render.vertexAplha = seekBarW030.progress.toFloat()/seekBarW030.max.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                render.vertexAplha = seekBarW030.progress.toFloat()/seekBarW030.max.toFloat()
            }
        })

        // Color Equation
        val spinnerW30CE = view.findViewById<Spinner>(R.id.spinnerW30CE)
        spinnerW30CE.onItemSelectedListener = object: OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                render.equationColor = when (spinnerW30CE.selectedItem) {
                    "FUNC_ADD" -> GLES20.GL_FUNC_ADD
                    "FUNC_SUBTRACT" -> GLES20.GL_FUNC_SUBTRACT
                    "FUNC_REVERSE_SUBTRACT" -> GLES20.GL_FUNC_REVERSE_SUBTRACT
                    else -> GLES20.GL_FUNC_ADD
                }
            }
        }

        // Alpha Equation
        val spinnerW30AE = view.findViewById<Spinner>(R.id.spinnerW30AE)
        spinnerW30AE.onItemSelectedListener = object: OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                render.equationAlpha = when (spinnerW30CE.selectedItem) {
                    "FUNC_ADD" -> GLES20.GL_FUNC_ADD
                    "FUNC_SUBTRACT" -> GLES20.GL_FUNC_SUBTRACT
                    "FUNC_REVERSE_SUBTRACT" -> GLES20.GL_FUNC_REVERSE_SUBTRACT
                    else -> GLES20.GL_FUNC_ADD
                }
            }
        }

        // ブレンドファクター(カラー元)
        val spinnerW30SCBF = view.findViewById<Spinner>(R.id.spinnerW30SCBF)
        spinnerW30SCBF.onItemSelectedListener = object: OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                render.blendFctSCBF = when (spinnerW30SCBF.selectedItem) {
                    "ZERO" -> GLES20.GL_ZERO
                    "ONE" -> GLES20.GL_ONE
                    "SRC_COLOR" -> GLES20.GL_SRC_COLOR
                    "DST_COLOR" -> GLES20.GL_DST_COLOR
                    "ONE_MINUS_SRC_COLOR" -> GLES20.GL_ONE_MINUS_SRC_COLOR
                    "ONE_MINUS_DST_COLOR" -> GLES20.GL_ONE_MINUS_DST_COLOR
                    "SRC_ALPHA" -> GLES20.GL_SRC_ALPHA
                    "DST_ALPHA" -> GLES20.GL_DST_ALPHA
                    "ONE_MINUS_SRC_ALPHA" -> GLES20.GL_ONE_MINUS_SRC_ALPHA
                    "ONE_MINUS_DST_ALPHA" -> GLES20.GL_ONE_MINUS_DST_ALPHA
                    "CONSTANT_COLOR" -> GLES20.GL_CONSTANT_COLOR
                    "ONE_MINUS_CONSTANT_COLOR" -> GLES20.GL_ONE_MINUS_CONSTANT_COLOR
                    "CONSTANT_ALPHA" -> GLES20.GL_CONSTANT_ALPHA
                    "ONE_MINUS_CONSTANT_ALPHA" -> GLES20.GL_ONE_MINUS_CONSTANT_ALPHA
                    "SRC_ALPHA_SATURATE" -> GLES20.GL_SRC_ALPHA_SATURATE
                    else -> GLES20.GL_ONE
                }
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        textureView.onResume()
    }

    override fun onPause() {
        super.onPause()
        textureView.onPause()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                W030Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
