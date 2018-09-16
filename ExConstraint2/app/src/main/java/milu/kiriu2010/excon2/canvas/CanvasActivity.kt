package milu.kiriu2010.excon2.canvas

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_canvas.*
import milu.kiriu2010.excon2.R

class CanvasActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas)

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }

        /* 複数SeekBarがあると、うまく動かないかもしれない
        seekBarX.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val pos = ((progress-50)/100).toFloat()
                Log.d(javaClass.simpleName, "progress[$progress]pos[$pos]")
                canvasBasicView.skewX = pos
                canvasBasicView.invalidate()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        seekBarY.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val pos = ((progress-50)/100).toFloat()
                Log.d(javaClass.simpleName, "progress[$progress]pos[$pos]")
                canvasBasicView.skewY = pos
                canvasBasicView.invalidate()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        */

        seekBarX.setOnSeekBarChangeListener(this)
        seekBarY.setOnSeekBarChangeListener(this)
    }

    // アクションバーのアイコンがタップされると呼ばれる
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            // 前画面に戻る
            android.R.id.home -> {
                finish()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        // progress 0 - 100
        // default 50
        val skewX = (seekBarX.progress.toFloat()-50f)/10f
        val skewY = (seekBarY.progress.toFloat()-50f)/10f
        Log.d(javaClass.simpleName, "skewX[$skewX]skewY[$skewY]")
        canvasBasicBiew.skewX = skewX
        canvasBasicBiew.skewY = skewY
        canvasBasicBiew.invalidate()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

}
