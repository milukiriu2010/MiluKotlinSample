package milu.kiriu2010.exdb1.opengl02.w027

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl.MyGL02View
import milu.kiriu2010.exdb1.opengl01.w019.W027Renderer

class W027Fragment : Fragment() {

    private lateinit var myGL02View: MyGL02View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl_w15, container, false)

        myGL02View = view.findViewById(R.id.myGL02ViewW15)
        val bmp0 = BitmapFactory.decodeResource(resources,R.drawable.texture_w027_0)
        val bmp1 = BitmapFactory.decodeResource(resources,R.drawable.texture_w027_1)
        val render = W027Renderer()
        render.bmpArray.add(bmp0)
        render.bmpArray.add(bmp1)
        myGL02View.setRenderer(render)

        return view
    }

    override fun onResume() {
        super.onResume()
        myGL02View.onResume()
    }

    override fun onPause() {
        super.onPause()
        myGL02View.onPause()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                W027Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
