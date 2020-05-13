package milu.kiriu2010.exdb1.opengl02.noise01v

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl01.noisev01.NoiseV01Renderer
import milu.kiriu2010.gui.view.MyGLES20View

// -------------------------------------------
// noise01_ノイズテクスチャ:VBOあり
// パーリンノイズで生成した画像をテクスチャとして貼る
// OpenGL ES 2.0
// -------------------------------------------
class NoiseV01Fragment : Fragment() {

    private lateinit var myGLES20View: MyGLES20View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl_noise01, container, false)

        myGLES20View = view.findViewById(R.id.myGLES20ViewNoise01)
        val renderer = NoiseV01Renderer(context!!)
        myGLES20View.setRenderer(renderer)
        myGLES20View.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                }
                MotionEvent.ACTION_DOWN -> {
                    renderer.isRunning = when (renderer.isRunning) {
                        true  -> false
                        false -> true
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                }
                else -> {
                }
            }
            true
        }

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
                NoiseV01Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
