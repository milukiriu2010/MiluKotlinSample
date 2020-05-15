package milu.kiriu2010.exdb1.opengl04.w043v

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.view.MyGLES20View

// -----------------------------------------------------
// 視差マッピング:VBOあり
// OpenGL ES 2.0
// -----------------------------------------------------
// 視線や高さを考慮したバンプマッピング
// -----------------------------------------------------
// 視差マッピングを行うためには、
// 法線マップと高さマップの２つのテクスチャが必要
// 高さマップは、画像データに高さデータを格納したもので
// 通常モノクロで扱う
// 黒⇒0(最も低い),白⇒1(最も高い)
// -----------------------------------------------------
// https://wgld.org/d/webgl/w043.html
// -----------------------------------------------------
class WV043Fragment : Fragment() {

    private lateinit var myGLES20View: MyGLES20View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl_w43, container, false)

        myGLES20View = view.findViewById(R.id.myGLES20ViewW43)
        val renderer = WV043Renderer(context!!)
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

        val seekBarW043 = view.findViewById<SeekBar>(R.id.seekBarW43)
        seekBarW043.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                renderer.hScale = seekBar.progress.toFloat()*0.1f
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                renderer.hScale = seekBar.progress.toFloat()*0.1f
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
                WV043Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
