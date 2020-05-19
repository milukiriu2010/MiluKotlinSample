package milu.kiriu2010.excon2.a0x.a25

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import milu.kiriu2010.excon2.R
import kotlinx.android.synthetic.main.activity_a25.*

// シークバー
class A25Activity : AppCompatActivity(), OnSeekBarChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a25)

        // シークバーに変更があったときのリスナーを設定
        sbA25Red.setOnSeekBarChangeListener(this)
        sbA25Green.setOnSeekBarChangeListener(this)
        sbA25Blue.setOnSeekBarChangeListener(this)

        // PorterDuff.ModeのデフォルトをSRCに設定
        spA25.setSelection(1)

        // スピナーの選択に変更があった場合のイベントを設定
        spA25.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // シークバーに沿った色を表示
                setIVA25()
            }
        }

        // シークバーに沿った色を表示
        setIVA25()
    }

    // シークバーに沿った色を表示
    private fun setIVA25() {
        val red   = sbA25Red.progress
        val green = sbA25Green.progress
        val blue  = sbA25Blue.progress

        tvA25Red.text = red.toString()
        tvA25Green.text = green.toString()
        tvA25Blue.text = blue.toString()

        val mode = when (spA25.selectedItem) {
            "CLEAR" -> PorterDuff.Mode.CLEAR
            "SRC" -> PorterDuff.Mode.SRC
            "DST" -> PorterDuff.Mode.DST
            "SRC_OVER" -> PorterDuff.Mode.SRC_OVER
            "DST_OVER" -> PorterDuff.Mode.DST_OVER
            "SRC_IN" -> PorterDuff.Mode.SRC_IN
            "DST_IN" -> PorterDuff.Mode.DST_IN
            "SRC_OUT" -> PorterDuff.Mode.SRC_OUT
            "DST_OUT" -> PorterDuff.Mode.DST_OUT
            "SRC_ATOP" -> PorterDuff.Mode.SRC_ATOP
            "DST_ATOP" -> PorterDuff.Mode.DST_ATOP
            "XOR" -> PorterDuff.Mode.XOR
            "DARKEN" -> PorterDuff.Mode.DARKEN
            "LIGHTEN" -> PorterDuff.Mode.LIGHTEN
            "ADD" -> PorterDuff.Mode.ADD
            "MULTIPLY" -> PorterDuff.Mode.MULTIPLY
            "SCREEN" -> PorterDuff.Mode.SCREEN
            "OVERLAY" -> PorterDuff.Mode.OVERLAY
            else -> PorterDuff.Mode.CLEAR
        }

        // 指定した色と違く見える
        //ivA25.setColorFilter( Color.rgb(red,green,blue), PorterDuff.Mode.LIGHTEN )
        // 期待した通りの色に見える
        //ivA25.setColorFilter( Color.rgb(red,green,blue), PorterDuff.Mode.SRC )
        ivA25.setColorFilter( Color.rgb(red,green,blue), mode )
    }

    // OnSeekBarChangeListener
    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        setIVA25()
    }

    // OnSeekBarChangeListener
    override fun onStartTrackingTouch(p0: SeekBar?) {}

    // OnSeekBarChangeListener
    override fun onStopTrackingTouch(p0: SeekBar?) {}
}
