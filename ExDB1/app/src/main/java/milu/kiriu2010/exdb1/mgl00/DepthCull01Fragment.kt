package milu.kiriu2010.exdb1.mgl00

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.mgl00.dodecahedron01.Dodecahedron01Renderer
import milu.kiriu2010.exdb1.mgl00.octahedron01.Octahedron01Renderer
import milu.kiriu2010.exdb1.mgl00.tetrahedron01.Tetrahedron01Renderer
import milu.kiriu2010.exdb1.opengl.MyGL02View

const val ARG_RENDER_ID = "RENDER_ID"

class DepthCull01Fragment : Fragment() {

    private var renderId = 0

    private lateinit var myGL02View: MyGL02View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            renderId = it.getInt(ARG_RENDER_ID) ?: 0
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mgl00_depth_cull_01, container, false)

        myGL02View = view.findViewById<MyGL02View>(R.id.myGL02View)
        val render =
                when (renderId) {
                    0 -> Tetrahedron01Renderer()
                    1 -> Octahedron01Renderer()
                    2 -> Dodecahedron01Renderer()
                    else -> Tetrahedron01Renderer()
                }

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


        // シェーダ選択
        val spinnerShader = view.findViewById<Spinner>(R.id.spinnerShader)
        spinnerShader.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // http://android-note.open-memo.net/sub/spinner--get-resource-id-for-selected-item.html
                val array = resources.obtainTypedArray(R.array.shaderlist)
                val itemId = array.getResourceId(position,R.string.shader_simple)
                render.shaderSwitch = when (itemId) {
                    R.string.shader_simple -> 0
                    R.string.shader_directional_light -> 1
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
        myGL02View.onResume()
    }

    override fun onPause() {
        super.onPause()
        myGL02View.onPause()
    }

    companion object {
        @JvmStatic
        fun newInstance(renderId: Int = 0) =
                DepthCull01Fragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_RENDER_ID,renderId)
                    }
                }
    }
}
