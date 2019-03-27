package milu.kiriu2010.exdb1.opengl02.w026

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl.TextureView
import milu.kiriu2010.exdb1.opengl01.w019.W026Renderer

class W026Fragment : Fragment() {

    private lateinit var textureView: TextureView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl02_home, container, false)

        textureView = view.findViewById<TextureView>(R.id.textureView)
        val bmp = BitmapFactory.decodeResource(resources,R.drawable.texture_w026)
        val render = W026Renderer()
        render.bmpArray.add(bmp)
        textureView.setRenderer(render)

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
                W026Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
