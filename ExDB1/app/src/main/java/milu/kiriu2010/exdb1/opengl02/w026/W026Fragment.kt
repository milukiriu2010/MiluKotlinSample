package milu.kiriu2010.exdb1.opengl02.w026

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_open_gl02_home.view.*

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl02.pyramid01.MyPyramid01Renderer
import milu.kiriu2010.exdb1.opengl.W026View
import milu.kiriu2010.exdb1.opengl01.w019.W026Renderer

class W026Fragment : Fragment() {

    private lateinit var w026View: W026View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl02_home, container, false)

        w026View = view.findViewById<W026View>(R.id.w026View)
        /*
        val drawable = ResourcesCompat.getDrawable(resources,R.drawable.texture,null)
        // drawableからbitmapへ変換
        val bmp = (drawable as BitmapDrawable).bitmap
        */
        val bmp = BitmapFactory.decodeResource(resources,R.drawable.texture)
        val render = W026Renderer()
        render.bmp = bmp
        w026View.setRenderer(render)

        return view
    }

    override fun onResume() {
        super.onResume()
        w026View.onResume()
    }

    override fun onPause() {
        super.onPause()
        w026View.onPause()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                W026Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
