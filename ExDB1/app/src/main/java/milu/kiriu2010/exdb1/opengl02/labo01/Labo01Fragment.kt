package milu.kiriu2010.exdb1.opengl02.labo01

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import milu.kiriu2010.exdb1.R

// ----------------------------------------------
// テクスチャ用画像をプログラムで生成しOpenGLを使って描画
// OpenGL ES 2.0
// ----------------------------------------------
class Labo01Fragment : Fragment() {

    private lateinit var labo01View: Labo01View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl_labo01, container, false)

        labo01View = view.findViewById<Labo01View>(R.id.labo01View)
        /*
        val bmp = BitmapFactory.decodeResource(resources,R.drawable.texture)
        testGLView.bmp = bmp
        */
        /*
        val drawable = ResourcesCompat.getDrawable(resources,R.drawable.texture,null)
        // drawableからbitmapへ変換
        val bmp = (drawable as BitmapDrawable).bitmap
        val render = W026Renderer()
        render.bmp = bmp
        w026View.setRenderer(render)
        */

        return view
    }

    override fun onResume() {
        super.onResume()
        labo01View.onResume()
    }

    override fun onPause() {
        super.onPause()
        labo01View.onPause()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                Labo01Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
