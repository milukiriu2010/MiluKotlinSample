package milu.kiriu2010.exdb1.opengl03.w035

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.Switch

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl.TextureView


class W035Fragment : Fragment() {

    private lateinit var textureView: TextureView
    private lateinit var switch: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl03_w035, container, false)

        textureView = view.findViewById(R.id.textureViewW035)
        val render = W035Renderer()
        val bmp0 = BitmapFactory.decodeResource(resources,R.drawable.texture_w035_0)
        val bmp1 = BitmapFactory.decodeResource(resources,R.drawable.texture_w035_1)
        render.bmpArray.add(bmp0)
        render.bmpArray.add(bmp1)
        textureView.setRenderer(render)
        textureView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    render.rotateSwitch = false
                }
                MotionEvent.ACTION_DOWN -> {
                    Log.d(javaClass.simpleName,"ex[${event.x}]ey[${event.y}]")
                    Log.d(javaClass.simpleName,"vw[${textureView.width}]vh[${textureView.height}]")
                    render.rotateSwitch = true
                    render.receiveTouch(event,textureView.width,textureView.height)
                }
                MotionEvent.ACTION_MOVE -> {
                    render.receiveTouch(event,textureView.width,textureView.height)
                }
                else -> {
                }
            }
            true
        }

        switch = view.findViewById(R.id.switchW035)
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            render.isBillBoard = isChecked
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
                W035Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
