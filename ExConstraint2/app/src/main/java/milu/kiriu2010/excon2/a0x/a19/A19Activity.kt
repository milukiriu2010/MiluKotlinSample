package milu.kiriu2010.excon2.a0x.a19

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_a19.*
import milu.kiriu2010.excon2.R

// キャンバス(SKEW)
class A19Activity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a19)

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

        // スピナ―に描画する内容の種類を設定する
        spA19.adapter = ArrayAdapter.createFromResource(this,
                R.array.a19_mode,
                android.R.layout.simple_spinner_item )

        // スピナ―の選択を変更すると、描画する内容が変更される
        spA19.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val mode = spA19.selectedItem.toString().toInt()
                cbView.mode = mode
                cbView.invalidate()

                // シークバーを使う描画だけ、シークバーを表示するようにしている
                when (mode) {
                    0 -> {
                        sbA19X.visibility = View.VISIBLE
                        sbA19Y.visibility = View.VISIBLE
                    }
                    6 -> {
                        sbA19X.visibility = View.VISIBLE
                        sbA19Y.visibility = View.VISIBLE
                    }
                    else -> {
                        sbA19X.visibility = View.INVISIBLE
                        sbA19Y.visibility = View.INVISIBLE
                    }
                }
            }
        }

        sbA19X.setOnSeekBarChangeListener(this)
        sbA19Y.setOnSeekBarChangeListener(this)
    }

    // アクションバーのアイコンがタップされると呼ばれる
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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
        val skewX = (sbA19X.progress.toFloat()-50f)/10f
        val skewY = (sbA19Y.progress.toFloat()-50f)/10f
        Log.d(javaClass.simpleName, "skewX[$skewX]skewY[$skewY]")
        cbView.skewX = skewX
        cbView.skewY = skewY
        cbView.invalidate()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

}
