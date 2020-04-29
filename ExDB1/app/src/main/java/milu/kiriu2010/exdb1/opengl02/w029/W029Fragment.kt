package milu.kiriu2010.exdb1.opengl02.w029

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.view.MyGLES20View

class W029Fragment : Fragment() {

    private lateinit var myGLES20View: MyGLES20View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl_w29, container, false)

        myGLES20View = view.findViewById(R.id.myGLES20ViewW29)
        val render = W029Renderer(context!!)
        myGLES20View.setRenderer(render)
        myGLES20View.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                }
                MotionEvent.ACTION_DOWN -> {
                    render.isRunning = !render.isRunning
                }
                MotionEvent.ACTION_MOVE -> {
                }
                else -> {
                }
            }
            true
        }

        // ブレンドタイプ
        val radioGroupW029 = view.findViewById<RadioGroup>(R.id.radioGroupW29)
        val btnW029Normal= view.findViewById<RadioButton>(R.id.btnW029Normal)
        val btnW029Transparency = view.findViewById<RadioButton>(R.id.btnW029TransParency)
        val btnW029Add = view.findViewById<RadioButton>(R.id.btnW029Add)
        val btnW029Reverse = view.findViewById<RadioButton>(R.id.btnW029Reverse)
        val btnW029PhotoShop = view.findViewById<RadioButton>(R.id.btnW029PhotoShop)
        val btnW029Mult = view.findViewById<RadioButton>(R.id.btnW029Mult)
        radioGroupW029.check(btnW029Transparency.id)
        radioGroupW029.setOnCheckedChangeListener { _, checkedId ->
            render.blendType = when (checkedId) {
                // 通常
                btnW029Normal.id -> 0
                // 透過
                btnW029Transparency.id -> 1
                // 加算
                btnW029Add.id -> 2
                // 反転
                btnW029Reverse.id -> 3
                // 加算+アルファ(PhotoShop的スクリーン)
                btnW029PhotoShop.id -> 4
                // 乗算
                btnW029Mult.id -> 5
                else -> 1
            }
        }

        // ブレンディングに使うアルファ成分の割合
        val seekBarW29BlendAlpha = view.findViewById<SeekBar>(R.id.seekBarW29BlendAlpha)
        seekBarW29BlendAlpha.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                render.vertexAplha = seekBar.progress.toFloat()/seekBar.max.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                render.vertexAplha = seekBar.progress.toFloat()/seekBar.max.toFloat()
            }
        })

        // 背景に使う色(赤)
        val seekBarW29BackRed = view.findViewById<SeekBar>(R.id.seekBarW29BackRed)
        seekBarW29BackRed.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                render.colorBack[0] = seekBar.progress.toFloat()/seekBar.max.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                render.colorBack[0] = seekBar.progress.toFloat()/seekBar.max.toFloat()
            }
        })

        // 背景に使う色(緑)
        val seekBarW29BackGreen = view.findViewById<SeekBar>(R.id.seekBarW29BackGreen)
        seekBarW29BackGreen.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                render.colorBack[1] = seekBar.progress.toFloat()/seekBar.max.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                render.colorBack[1] = seekBar.progress.toFloat()/seekBar.max.toFloat()
            }
        })

        // 背景に使う色(青)
        val seekBarW29BackBlue = view.findViewById<SeekBar>(R.id.seekBarW29BackBlue)
        seekBarW29BackBlue.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                render.colorBack[2] = seekBar.progress.toFloat()/seekBar.max.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                render.colorBack[2] = seekBar.progress.toFloat()/seekBar.max.toFloat()
            }
        })

        // 背景に使う色(α)
        val seekBarW29BackAlpha = view.findViewById<SeekBar>(R.id.seekBarW29BackAlpha)
        seekBarW29BackAlpha.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                render.colorBack[3] = seekBar.progress.toFloat()/seekBar.max.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                render.colorBack[3] = seekBar.progress.toFloat()/seekBar.max.toFloat()
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
                W029Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
