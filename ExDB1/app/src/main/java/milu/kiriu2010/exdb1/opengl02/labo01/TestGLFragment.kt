package milu.kiriu2010.exdb1.opengl02.labo01

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import milu.kiriu2010.exdb1.R

class TestGLFragment : Fragment() {

    private lateinit var testGLView: TestGLView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl02_testgl, container, false)

        testGLView = view.findViewById<TestGLView>(R.id.testGLView)
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
        testGLView.onResume()
    }

    override fun onPause() {
        super.onPause()
        testGLView.onPause()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                TestGLFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
