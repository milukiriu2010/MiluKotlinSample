package milu.kiriu2010.exdb1.opengl02.w030z

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import milu.kiriu2010.exdb1.R

class W030zBlendDialog: DialogFragment() {

    // 0.0 - 1.0
    var red = 0f
    var green = 0f
    var blue = 0f
    var alpha = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            red   = it.getFloat("RED")   ?: 0f
            green = it.getFloat("GREEN") ?: 0f
            blue  = it.getFloat("BLUE")  ?: 0f
            alpha = it.getFloat("ALPHA") ?: 0f
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_open_gl02_w030z_blend, container, false)

        val ctx = context ?: return view

        val seekBarW030BlendRed = view.findViewById<SeekBar>(R.id.seekBarW030BlendRed)
        seekBarW030BlendRed.progress = (red * 10f).toInt()
        seekBarW030BlendRed.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                red = seekBar.progress.toFloat()/10f
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                red = seekBar.progress.toFloat()/10f
            }
        })

        val seekBarW030BlendGreen = view.findViewById<SeekBar>(R.id.seekBarW030BlendGreen)
        seekBarW030BlendGreen.progress = (green * 10f).toInt()
        seekBarW030BlendGreen.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                green = seekBar.progress.toFloat()/10f
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                green = seekBar.progress.toFloat()/10f
            }
        })

        val seekBarW030BlendBlue = view.findViewById<SeekBar>(R.id.seekBarW030BlendBlue)
        seekBarW030BlendBlue.progress = (blue * 10f).toInt()
        seekBarW030BlendBlue.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                blue = seekBar.progress.toFloat()/10f
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                blue = seekBar.progress.toFloat()/10f
            }
        })

        val seekBarW030BlendAlpha = view.findViewById<SeekBar>(R.id.seekBarW030BlendAlpha)
        seekBarW030BlendAlpha.progress = (alpha * 10f).toInt()
        seekBarW030BlendAlpha.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                alpha = seekBar.progress.toFloat()/10f
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                alpha = seekBar.progress.toFloat()/10f
            }
        })

        val btnW030CloseBlend = view.findViewById<Button>(R.id.btnW030CloseBlend)
        btnW030CloseBlend.setOnClickListener {
            val intent = Intent().also {
                it.putExtra("RED"  , red)
                it.putExtra("GREEN", green)
                it.putExtra("BLUE" , blue)
                it.putExtra("ALPHA", alpha)
            }
            targetFragment?.onActivityResult(2,0,intent)
            dismiss()
        }


        return view
    }


    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle) =
                W030zBlendDialog().apply {
                    arguments = Bundle().also {
                        it.putFloat("RED"  , bundle.getFloat("RED")   ?: 0f)
                        it.putFloat("GREEN", bundle.getFloat("GREEN") ?: 0f)
                        it.putFloat("BLUE" , bundle.getFloat("BLUE")  ?: 0f)
                        it.putFloat("ALPHA", bundle.getFloat("ALPHA") ?: 0f)
                    }
                }
    }
}