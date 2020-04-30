package milu.kiriu2010.excon1.a04

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.widget.DatePicker
import android.widget.TimePicker
import java.util.*

// ------------------------------------
// アラーム時刻が来ると表示されるダイアログ
// ------------------------------------
class A04AlertDialog: DialogFragment() {

    interface OnClickListener {
        fun onPositiveClick()
        fun onNegativeClick()
    }

    private lateinit var listener: OnClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if ( context is OnClickListener ) {
            // A04Activityをlistenerにセットしている
            listener = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if ( context == null )
            return super.onCreateDialog(savedInstanceState)
        val builder = AlertDialog.Builder(context).apply {
            setMessage("時間になりました！")
            setPositiveButton("起きる") { _, _ ->
                //context.toast("起きるがクリックされました")
                listener.onPositiveClick()
            }
            setNegativeButton("あと５分") { _, _ ->
                //context.toast("あと５分がクリックされました")
                listener.onNegativeClick()
            }
        }
        return builder.create()
    }
}

