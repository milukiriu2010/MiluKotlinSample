package milu.kiriu2010.excon2.a0x.a22


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import milu.kiriu2010.excon2.R
import milu.kiriu2010.excon2.a0x.a19.CanvasBasicView

// カスタマイズビュー(CanvasBasicView)にページ番号(mode)を渡して動作を変える
class A22CanvasFragment1 : Fragment()
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
        val view = inflater.inflate(R.layout.fragment_a22_canvas, container, false)

        canvasBasicView1 = view.findViewById(R.id.cvA22)
        canvasBasicView1.mode = mode

        seekBarX = view.findViewById(R.id.sbA22X)
        seekBarY = view.findViewById(R.id.sbA22Y)

        seekBarX.setOnSeekBarChangeListener(this)
        seekBarY.setOnSeekBarChangeListener(this)

        // シークバーを使う描画だけ、シークバーを表示するようにしている
        when (mode) {
            0 -> seekOnOff(View.VISIBLE)
            6 -> seekOnOff(View.VISIBLE)
            else -> seekOnOff(View.INVISIBLE)
        }

        return view
    }

    // シークバーの表示・非表示を制御する
    private fun seekOnOff(visibility: Int) {
        seekBarX.visibility = visibility
        seekBarY.visibility = visibility
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
        @JvmStatic
        fun newInstance(mode: Int = 1) =
                A22CanvasFragment1().apply {
                    arguments = Bundle().apply {
                        putInt( "mode", mode )
                    }
                }
    }
}
