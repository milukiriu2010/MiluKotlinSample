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
        val render = DepthCull01Renderer(renderId,context!!)
        myGL02View.setRenderer(render)
        myGL02View.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                }
                MotionEvent.ACTION_DOWN -> {
                    Log.d(javaClass.simpleName,"ex[${event.x}]ey[${event.y}]")
                    Log.d(javaClass.simpleName,"vw[${myGL02View.width}]vh[${myGL02View.height}]")
                    render.receiveTouch(event,myGL02View.width,myGL02View.height)
                }
                MotionEvent.ACTION_MOVE -> {
                    render.receiveTouch(event,myGL02View.width,myGL02View.height)
                }
                else -> {
                }
            }
            true
        }
        // 深度テスト
        val checkBoxDepth = view.findViewById<CheckBox>(R.id.checkBoxDepth)
        checkBoxDepth.isChecked = render.isDepth
        checkBoxDepth.setOnCheckedChangeListener { buttonView, isChecked ->
            render.isDepth = isChecked
        }
        // カリング
        val checkBoxCull = view.findViewById<CheckBox>(R.id.checkBoxCull)
        checkBoxCull.isChecked = render.isCull
        checkBoxCull.setOnCheckedChangeListener { buttonView, isChecked ->
            render.isCull = isChecked
        }
        // 回転
        val checkBoxRotate = view.findViewById<CheckBox>(R.id.checkBoxRotate)
        checkBoxRotate.isChecked = false
        checkBoxRotate.setOnCheckedChangeListener { buttonView, isChecked ->
            render.isRunning = isChecked
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
                    R.string.shader_texture -> 2
                    R.string.shader_lines -> 3
                    else -> 0
                }
                // 使わなくなったら解放
                array.recycle()
            }

        }

        // Perspective/Frustum
        val radioGroupPersFrus = view.findViewById<RadioGroup>(R.id.radioGroupPersFrus)
        val radioButtonPers = view.findViewById<RadioButton>(R.id.radioButtonPers)
        val radioButtonFrus = view.findViewById<RadioButton>(R.id.radioButtonFrus)
        when (render.flgPersFrus) {
            1 -> {
                radioButtonPers.isChecked = true
                radioButtonFrus.isChecked = false
            }
            2 -> {
                radioButtonPers.isChecked = false
                radioButtonFrus.isChecked = true
            }
        }
        radioGroupPersFrus.setOnCheckedChangeListener { group, checkedId ->
            render.flgPersFrus = when (checkedId) {
                radioButtonPers.id -> 1
                radioButtonFrus.id -> 2
                else -> 1
            }
        }

        // fov
        val seekBarFov = view.findViewById<SeekBar>(R.id.seekBarFov)
        seekBarFov.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                render.fov = seekBar.progress.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                render.fov = seekBar.progress.toFloat()
            }

        })

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
