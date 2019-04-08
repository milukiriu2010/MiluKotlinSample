package milu.kiriu2010.exdb1.opengl02.jayce07

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl.TextureView
import milu.kiriu2010.exdb1.opengl01.w019.W026Renderer
import milu.kiriu2010.exdb1.opengl01.w019.W027Renderer
import milu.kiriu2010.exdb1.opengl01.w019.W028Renderer

class Jayce07Fragment : Fragment() {

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
        val render = Test7Renderer(context!!)
        textureView.setRenderer(render)
        textureView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    //render.rotateSwitch = false
                }
                MotionEvent.ACTION_DOWN -> {
                    Log.d(javaClass.simpleName,"ex[${event.x}]ey[${event.y}]")
                    Log.d(javaClass.simpleName,"vw[${textureView.width}]vh[${textureView.height}]")
                    //render.rotateSwitch = true
                    //render.receiveTouch(event,textureView.width,textureView.height)
                }
                MotionEvent.ACTION_MOVE -> {
                    //render.receiveTouch(event,textureView.width,textureView.height)
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
        textureView.onResume()
    }

    override fun onPause() {
        super.onPause()
        textureView.onPause()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                Jayce07Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}