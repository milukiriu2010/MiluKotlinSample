package com.example.milu.excon1.alarm

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.DatePicker
import android.widget.TimePicker
import java.util.*

class SimpleAlertDialog: DialogFragment() {

    interface OnClickListener {
        fun onPositiveClick()
        fun onNegativeClick()
    }

    private lateinit var listener: OnClickListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if ( context is OnClickListener) {
            // AlaramClockActivityをlistenerにセットしている
            listener = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = context
        if ( context == null )
            return super.onCreateDialog(savedInstanceState)
        val builder = AlertDialog.Builder(context).apply {
            setMessage("時間になりました！")
            setPositiveButton("起きる") { dialog, which ->
                //context.toast("起きるがクリックされました")
                listener.onPositiveClick()
            }
            setNegativeButton("あと５分") { dialog, which ->
                //context.toast("あと５分がクリックされました")
                listener.onNegativeClick()
            }
        }
        return builder.create()
    }
}

class DatePickerFragment: DialogFragment(),
        DatePickerDialog.OnDateSetListener {

    interface OnDateSelectedListener {
        fun onSelected(year: Int, month: Int, date: Int)
    }

    private lateinit var listener: OnDateSelectedListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if ( context is OnDateSelectedListener) {
            listener = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //return super.onCreateDialog(savedInstanceState)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val date = c.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(context,this, year, month, date)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        listener.onSelected(year,month,dayOfMonth)
    }

}

class TimePickerFragment: DialogFragment(),
        TimePickerDialog.OnTimeSetListener {

    interface OnTimeSelectedListener {
        fun onSelected(hourOfDay: Int, minute: Int)
    }

    private lateinit var listener: OnTimeSelectedListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if ( context is OnTimeSelectedListener) {
            listener = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //return super.onCreateDialog(savedInstanceState)
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        return TimePickerDialog(context,this,hour,minute,true)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        listener.onSelected(hourOfDay,minute)
    }

}