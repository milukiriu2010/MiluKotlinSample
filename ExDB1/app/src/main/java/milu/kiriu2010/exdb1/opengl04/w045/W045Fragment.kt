package milu.kiriu2010.exdb1.opengl04.w045

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl.MyGL02View
import milu.kiriu2010.exdb1.opengl.TextureView
import milu.kiriu2010.exdb1.opengl04.w042.W042Renderer

class W045Fragment : Fragment() {

    private lateinit var myGLView: MyGL02View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl04_home, container, false)

        myGLView = view.findViewById(R.id.myGL02ViewA04)
        val bmp0 = BitmapFactory.decodeResource(resources,R.drawable.cube_w45_px)
        val bmp1 = BitmapFactory.decodeResource(resources,R.drawable.cube_w45_py)
        val bmp2 = BitmapFactory.decodeResource(resources,R.drawable.cube_w45_pz)
        val bmp3 = BitmapFactory.decodeResource(resources,R.drawable.cube_w45_nx)
        val bmp4 = BitmapFactory.decodeResource(resources,R.drawable.cube_w45_ny)
        val bmp5 = BitmapFactory.decodeResource(resources,R.drawable.cube_w45_nz)
        val bmp6 = BitmapFactory.decodeResource(resources,R.drawable.texture_w45)
        val render = W045Renderer()
        render.bmpArray.add(bmp0)
        render.bmpArray.add(bmp1)
        render.bmpArray.add(bmp2)
        render.bmpArray.add(bmp3)
        render.bmpArray.add(bmp4)
        render.bmpArray.add(bmp5)
        render.bmpArray.add(bmp6)
        myGLView.setRenderer(render)
        myGLView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    render.rotateSwitch = false
                }
                MotionEvent.ACTION_DOWN -> {
                    Log.d(javaClass.simpleName,"ex[${event.x}]ey[${event.y}]")
                    Log.d(javaClass.simpleName,"vw[${myGLView.width}]vh[${myGLView.height}]")
                    render.rotateSwitch = true
                    render.receiveTouch(event,myGLView.width,myGLView.height)
                }
                MotionEvent.ACTION_MOVE -> {
                    render.receiveTouch(event,myGLView.width,myGLView.height)
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
        myGLView.onResume()
    }

    override fun onPause() {
        super.onPause()
        myGLView.onPause()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                W045Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
