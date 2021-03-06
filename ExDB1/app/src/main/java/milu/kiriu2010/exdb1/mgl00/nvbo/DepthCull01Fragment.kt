package milu.kiriu2010.exdb1.mgl00.nvbo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.*
import android.widget.*

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.view.MyGLES20View

const val ARG_RENDER_ID = "RENDER_ID"

class DepthCull01Fragment : Fragment() {

    private var renderId = 0

    private lateinit var myGLES20View: MyGLES20View
    private lateinit var scaleDetector: ScaleGestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            renderId = it.getInt(ARG_RENDER_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mgl00_depth_cull_01, container, false)

        myGLES20View = view.findViewById(R.id.myGLES20ViewMGL00)
        val renderer = DepthCull01Renderer(renderId, context!!)
        myGLES20View.setRenderer(renderer)
        myGLES20View.setOnTouchListener { _, event ->
            // scaleDetectorは、認識されない
            scaleDetector.onTouchEvent(event)
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

        // ピンチイン・アウト
        scaleDetector = ScaleGestureDetector(context,object: ScaleGestureDetector.OnScaleGestureListener{
            override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                Log.d(javaClass.simpleName,"ScaleBegin")

                return true
            }

            override fun onScaleEnd(detector: ScaleGestureDetector?) {
            }

            override fun onScale(detector: ScaleGestureDetector): Boolean {
                Log.d(javaClass.simpleName,"scale[${detector.scaleFactor}]")

                return true
            }
        })


        // 深度テスト
        val checkBoxDepth = view.findViewById<CheckBox>(R.id.cbDepthMGL00)
        checkBoxDepth.isChecked = renderer.isDepth
        checkBoxDepth.setOnCheckedChangeListener { _, isChecked ->
            renderer.isDepth = isChecked
        }
        // カリング
        val checkBoxCull = view.findViewById<CheckBox>(R.id.cbCullMGL00)
        checkBoxCull.isChecked = renderer.isCull
        checkBoxCull.setOnCheckedChangeListener { _, isChecked ->
            renderer.isCull = isChecked
        }
        // 回転
        val checkBoxRotate = view.findViewById<CheckBox>(R.id.cbRotateMGL00)
        checkBoxRotate.isChecked = false
        checkBoxRotate.setOnCheckedChangeListener { _, isChecked ->
            renderer.isRunning = isChecked
        }

        // シェーダ選択
        val spinnerShader = view.findViewById<Spinner>(R.id.spShaderMGL00)
        spinnerShader.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // http://android-note.open-memo.net/sub/spinner--get-resource-id-for-selected-item.html
                val array = resources.obtainTypedArray(R.array.MGL00_LIST_SHADER)
                val itemId = array.getResourceId(position,R.string.MGL00_SHADER_SIMPLE)
                renderer.shaderSwitch = when (itemId) {
                    R.string.MGL00_SHADER_SIMPLE -> 0
                    R.string.MGL00_SHADER_DIRECTIONAL_LIGHT -> 1
                    R.string.MGL00_SHADER_TEXTURE -> 2
                    R.string.MGL00_SHADER_LINES -> 3
                    else -> 0
                }
                // 使わなくなったら解放
                array.recycle()
            }

        }

        // Perspective/Frustum
        val radioGroupPersFrus = view.findViewById<RadioGroup>(R.id.rgPersFrusMGL00)
        val radioButtonPers = view.findViewById<RadioButton>(R.id.rbPersMGL00)
        val radioButtonFrus = view.findViewById<RadioButton>(R.id.rbFrusMGL00)
        val radioButtonOrth = view.findViewById<RadioButton>(R.id.rbOrthMGL00)
        when (renderer.flgPersFrus) {
            1 -> {
                radioButtonPers.isChecked = true
                radioButtonFrus.isChecked = false
                radioButtonOrth.isChecked = false
            }
            2 -> {
                radioButtonPers.isChecked = false
                radioButtonFrus.isChecked = true
                radioButtonOrth.isChecked = false
            }
            3 -> {
                radioButtonPers.isChecked = false
                radioButtonFrus.isChecked = false
                radioButtonOrth.isChecked = true
            }
        }
        radioGroupPersFrus.setOnCheckedChangeListener { _, checkedId ->
            renderer.flgPersFrus = when (checkedId) {
                radioButtonPers.id -> 1
                radioButtonFrus.id -> 2
                radioButtonOrth.id -> 3
                else -> 1
            }
        }

        // fov
        val seekBarFov = view.findViewById<SeekBar>(R.id.sbFovMGL00)
        seekBarFov.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                renderer.fov = seekBar.progress.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                renderer.fov = seekBar.progress.toFloat()
            }

        })

        // near
        val seekBarNear = view.findViewById<SeekBar>(R.id.sbNearMGL00)
        seekBarNear.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                renderer.near = seekBar.progress.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                renderer.near = seekBar.progress.toFloat()
            }

        })

        // far
        val seekBarFar = view.findViewById<SeekBar>(R.id.sbFarMGL00)
        seekBarFar.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                renderer.far = seekBar.progress.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                renderer.far = seekBar.progress.toFloat()
            }

        })

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
        fun newInstance(renderId: Int = 0) =
                DepthCull01Fragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_RENDER_ID,renderId)
                    }
                }
    }
}
