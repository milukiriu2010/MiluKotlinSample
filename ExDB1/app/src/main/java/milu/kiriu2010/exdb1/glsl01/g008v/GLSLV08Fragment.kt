package milu.kiriu2010.exdb1.glsl01.g008v

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.view.MyGLES20View

// -----------------------------------------------------
// シェーダ内でレイを定義して着色したサンプル:VBOあり
// OpenGL ES 2.0
// -----------------------------------------------------
// レイトレーシング
//   視線を追跡してレンダリングする技法
//   レイを追跡して得られた結果をもとに
//   スクリーンに何かを描き出す
//   レイとオブジェクトの衝突判定の結果をもとに
//   衝突しているならオブジェクトをレンダリングする
// レイマーチング
//   レイトレーシングの技法の１つ
// -----------------------------------------------------
// https://wgld.org/d/glsl/g008.html
// -----------------------------------------------------
class GLSLV08Fragment : Fragment() {

    private lateinit var myGLES20View: MyGLES20View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl_w15, container, false)

        myGLES20View = view.findViewById(R.id.myGLES20ViewW15)
        val renderer = GLSLV08Renderer(context!!)
        myGLES20View.setRenderer(renderer)

        myGLES20View.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    renderer.touchP.x = event.x.toFloat()/renderer.renderW.toFloat()
                    renderer.touchP.y = event.y.toFloat()/renderer.renderH.toFloat()
                }
                MotionEvent.ACTION_DOWN -> {
                    renderer.touchP.x = event.x.toFloat()/renderer.renderW.toFloat()
                    renderer.touchP.y = event.y.toFloat()/renderer.renderH.toFloat()
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
                GLSLV08Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
