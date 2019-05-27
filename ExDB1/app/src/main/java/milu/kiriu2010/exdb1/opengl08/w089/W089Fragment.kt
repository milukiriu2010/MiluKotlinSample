package milu.kiriu2010.exdb1.opengl08.w089

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.view.MyGLES20View

class W089Fragment : Fragment() {

    private lateinit var myGLES20View: MyGLES20View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl_w89, container, false)

        myGLES20View = view.findViewById(R.id.myGLES20ViewW89)
        val renderer = W089Renderer(context!!)
        myGLES20View.setRenderer(renderer)
        myGLES20View.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                }
                MotionEvent.ACTION_DOWN -> {
                    Log.d(javaClass.simpleName,"ex[${event.x}]ey[${event.y}]")
                    Log.d(javaClass.simpleName,"vw[${myGLES20View.width}]vh[${myGLES20View.height}]")
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

        val spinnerW89 = view.findViewById<Spinner>(R.id.spinnerW89)
        spinnerW89.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // http://android-note.open-memo.net/sub/spinner--get-resource-id-for-selected-item.html
                val array = resources.obtainTypedArray(R.array.w89list)
                val itemId = array.getResourceId(position,R.string.w59_depth_of_field)
                renderer.textureID = when (itemId) {
                    R.string.w89_texture0 -> 0
                    R.string.w89_texture1 -> 1
                    R.string.w89_texture2 -> 2
                    R.string.w89_texture3 -> 3
                    R.string.w89_texture4 -> 4
                    R.string.w89_texture5 -> 5
                    R.string.w89_texture6 -> 6
                    R.string.w89_texture7 -> 7
                    else -> 0
                }
                // 使わなくなったら解放
                array.recycle()
            }

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
                W089Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
