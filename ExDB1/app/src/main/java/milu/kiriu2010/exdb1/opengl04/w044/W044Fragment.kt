package milu.kiriu2010.exdb1.opengl04.w044

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

class W044Fragment : Fragment() {

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
        val bmp0 = BitmapFactory.decodeResource(resources,R.drawable.cube_w44_px)
        val bmp1 = BitmapFactory.decodeResource(resources,R.drawable.cube_w44_py)
        val bmp2 = BitmapFactory.decodeResource(resources,R.drawable.cube_w44_pz)
        val bmp3 = BitmapFactory.decodeResource(resources,R.drawable.cube_w44_nx)
        val bmp4 = BitmapFactory.decodeResource(resources,R.drawable.cube_w44_ny)
        val bmp5 = BitmapFactory.decodeResource(resources,R.drawable.cube_w44_nz)
        val render = W044Renderer(context!!)
        render.bmpArray.add(bmp0)
        render.bmpArray.add(bmp1)
        render.bmpArray.add(bmp2)
        render.bmpArray.add(bmp3)
        render.bmpArray.add(bmp4)
        render.bmpArray.add(bmp5)
        myGLES20View.setRenderer(render)
        myGLES20View.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    render.isRunning = false
                }
                MotionEvent.ACTION_DOWN -> {
                    Log.d(javaClass.simpleName,"ex[${event.x}]ey[${event.y}]")
                    Log.d(javaClass.simpleName,"vw[${myGLES20View.width}]vh[${myGLES20View.height}]")
                    render.isRunning = true
                    render.receiveTouch(event,myGLES20View.width,myGLES20View.height)
                }
                MotionEvent.ACTION_MOVE -> {
                    render.receiveTouch(event,myGLES20View.width,myGLES20View.height)
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
                W044Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
