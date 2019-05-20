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
import milu.kiriu2010.gui.view.MyGLES20View

class W045Fragment : Fragment() {

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
        val bmp0 = BitmapFactory.decodeResource(resources,R.drawable.cube_w45_px)
        val bmp1 = BitmapFactory.decodeResource(resources,R.drawable.cube_w45_py)
        val bmp2 = BitmapFactory.decodeResource(resources,R.drawable.cube_w45_pz)
        val bmp3 = BitmapFactory.decodeResource(resources,R.drawable.cube_w45_nx)
        val bmp4 = BitmapFactory.decodeResource(resources,R.drawable.cube_w45_ny)
        val bmp5 = BitmapFactory.decodeResource(resources,R.drawable.cube_w45_nz)
        val bmp6 = BitmapFactory.decodeResource(resources,R.drawable.texture_w45)
        val renderer = W045Renderer(context!!)
        renderer.bmpArray.add(bmp0)
        renderer.bmpArray.add(bmp1)
        renderer.bmpArray.add(bmp2)
        renderer.bmpArray.add(bmp3)
        renderer.bmpArray.add(bmp4)
        renderer.bmpArray.add(bmp5)
        renderer.bmpArray.add(bmp6)
        myGLES20View.setRenderer(renderer)
        myGLES20View.setOnTouchListener { v, event ->
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
                W045Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
