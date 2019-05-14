package milu.kiriu2010.exdb1.opengl02.w029

import android.graphics.BitmapFactory
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
import kotlinx.android.synthetic.main.fragment_open_gl02_w029.*

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl.TextureView
import milu.kiriu2010.exdb1.opengl01.w019.W027Renderer
import milu.kiriu2010.exdb1.opengl01.w019.W028Renderer
import milu.kiriu2010.exdb1.opengl01.w019.W029Renderer

class W029Fragment : Fragment() {

    private lateinit var textureView: TextureView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl02_w029, container, false)

        textureView = view.findViewById<TextureView>(R.id.textureView)
        val bmp0 = BitmapFactory.decodeResource(resources,R.drawable.texture_w029)
        val render = W029Renderer()
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


        // ブレンドタイプ
        val radioGroupW029 = view.findViewById<RadioGroup>(R.id.radioGroupW029)
        val btnW029Transparency = view.findViewById<RadioButton>(R.id.btnW029TransParency)
        val btnW029Add = view.findViewById<RadioButton>(R.id.btnW029Add)
        radioGroupW029.check(btnW029Transparency.id)
        render.blendType = 0
        radioGroupW029.setOnCheckedChangeListener { group, checkedId ->
            render.blendType = when (checkedId) {
                btnW029Transparency.id -> 0
                btnW029Add.id -> 1
                else -> -1
            }
        }

        // アルファ成分
        val seekBarW029 = view.findViewById<SeekBar>(R.id.seekBarW029)
        seekBarW029.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                render.vertexAplha = seekBarW029.progress.toFloat()/seekBarW029.max.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                render.vertexAplha = seekBarW029.progress.toFloat()/seekBarW029.max.toFloat()
            }
        })

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
                W029Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
