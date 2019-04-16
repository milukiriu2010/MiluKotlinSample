package milu.kiriu2010.exdb1.mgl00.icosahedron01

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Switch

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl.MyGL02View

class Icosahedron01Fragment : Fragment() {

    private lateinit var myGL02View: MyGL02View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mgl00_depth_cull, container, false)

        myGL02View = view.findViewById<MyGL02View>(R.id.myGL02View)
        val render = Icosahedron01Renderer()
        myGL02View.setRenderer(render)
        myGL02View.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    render.rotateSwitch = false
                }
                MotionEvent.ACTION_DOWN -> {
                    Log.d(javaClass.simpleName,"ex[${event.x}]ey[${event.y}]")
                    Log.d(javaClass.simpleName,"vw[${myGL02View.width}]vh[${myGL02View.height}]")
                    render.rotateSwitch = true
                }
                MotionEvent.ACTION_MOVE -> {
                }
                else -> {
                }
            }
            true
        }
        // 深度テスト
        val switchDepth = view.findViewById<Switch>(R.id.switchDepth)
        switchDepth.setOnCheckedChangeListener { buttonView, isChecked ->
            render.isDepth = isChecked
        }
        // カリング
        val switchCull = view.findViewById<Switch>(R.id.switchCull)
        switchCull.setOnCheckedChangeListener { buttonView, isChecked ->
            render.isCull = isChecked
        }
        // 回転
        val switchRotate = view.findViewById<Switch>(R.id.switchRotate)
        switchRotate.setOnCheckedChangeListener { buttonView, isChecked ->
            render.rotateSwitch = isChecked
        }

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
                Icosahedron01Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
