package milu.kiriu2010.excon2.screen1.tabbed


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import milu.kiriu2010.excon2.R
import milu.kiriu2010.excon2.screen1.canvas.CanvasBasicView

/**
 * A simple [Fragment] subclass.
 * Use the [CanvasFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CanvasFragment1 : Fragment()
    , SeekBar.OnSeekBarChangeListener {

    private lateinit var canvasBasicView1: CanvasBasicView
    private lateinit var seekBarX: SeekBar
    private lateinit var seekBarY: SeekBar

    var mode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mode = it.getInt("mode")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_canvas1, container, false)

        canvasBasicView1 = view.findViewById(R.id.canvasBasicView1)
        canvasBasicView1.mode = mode

        seekBarX = view.findViewById(R.id.seekBarX)
        /*
        seekBarX.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val pos = ((progress-50)/100).toFloat()
                Log.d(javaClass.simpleName, "progress[$progress]pos[$pos]")
                canvasBasicView1.skewX = pos
                canvasBasicView1.invalidate()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        */

        seekBarY = view.findViewById(R.id.seekBarY)
        /*
        seekBarY.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val pos = ((progress-50)/100).toFloat()
                Log.d(javaClass.simpleName, "progress[$progress]pos[$pos]")
                canvasBasicView1.skewY = pos
                canvasBasicView1.invalidate()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        */

        seekBarX.setOnSeekBarChangeListener(this)
        seekBarY.setOnSeekBarChangeListener(this)

        return view
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        // progress 0 - 100
        // default 50
        val skewX = (seekBarX.progress.toFloat()-50f)/10f
        val skewY = (seekBarY.progress.toFloat()-50f)/10f
        Log.d(javaClass.simpleName, "skewX[$skewX]skewY[$skewY]")
        canvasBasicView1.skewX = skewX
        canvasBasicView1.skewY = skewY
        canvasBasicView1.invalidate()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CanvasFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(mode: Int = 1) =
                CanvasFragment1().apply {
                    arguments = Bundle().apply {
                        putInt( "mode", mode )
                    }
                }
    }
}
