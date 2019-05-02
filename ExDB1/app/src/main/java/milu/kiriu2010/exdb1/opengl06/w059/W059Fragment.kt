package milu.kiriu2010.exdb1.opengl06.w059

import android.graphics.BitmapFactory
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

class W059Fragment : Fragment() {

    private lateinit var myGLView: MyGL02View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl06_w59, container, false)

        myGLView = view.findViewById(R.id.myGL02ViewW59)
        val bmp0 = BitmapFactory.decodeResource(resources,R.drawable.texture_w59)
        val render = W059Renderer(context!!)
        render.bmpArray.add(bmp0)
        myGLView.setRenderer(render)
        myGLView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                }
                MotionEvent.ACTION_DOWN -> {
                    Log.d(javaClass.simpleName,"ex[${event.x}]ey[${event.y}]")
                    Log.d(javaClass.simpleName,"vw[${myGLView.width}]vh[${myGLView.height}]")
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

        val spinnerW59 = view.findViewById<Spinner>(R.id.spinnerW59)
        spinnerW59.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // http://android-note.open-memo.net/sub/spinner--get-resource-id-for-selected-item.html
                val array = resources.obtainTypedArray(R.array.w59list)
                val itemId = array.getResourceId(position,R.string.w59_depth_of_field)
                render.u_result = when (itemId) {
                    R.string.w59_depth_of_field -> 0
                    R.string.w59_depth -> 1
                    R.string.w59_scene -> 2
                    R.string.w59_blur1 -> 3
                    R.string.w59_blur2 -> 4
                    else -> 0
                }
                // 使わなくなったら解放
                array.recycle()
            }

        }

        val seekBarW59Depth = view.findViewById<SeekBar>(R.id.seekBarW59Depth)
        seekBarW59Depth.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                render.u_depthOffset = (seekBar.progress-5).toFloat()/10f * 0.85f
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                render.u_depthOffset = (seekBar.progress-5).toFloat()/10f * 0.85f
            }

        })

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
                W059Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
