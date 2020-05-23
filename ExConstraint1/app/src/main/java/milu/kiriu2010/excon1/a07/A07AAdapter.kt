package milu.kiriu2010.excon1.a07

import android.annotation.SuppressLint
import android.content.Context
import android.icu.util.TimeZone
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextClock
import android.widget.TextView
import milu.kiriu2010.excon1.R

// アダプタにタイムゾーン情報を表示する
// TimeZone.getAvailableIDs() => 24
class A07AAdapter(context: Context,
                  private val timeZones: MutableList<String> = mutableListOf() ) : BaseAdapter() {

    private val inflater = LayoutInflater.from(context)

    private fun createView( parent: ViewGroup? ): View {
        val view = inflater.inflate(R.layout.adapter_a07a, parent, false )
        view.tag = ViewHolder(view)
        return view
    }

    // アダプタ位置に対応するタイムゾーン情報を表示
    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: createView(parent)

        val timeZoneId = getItem(position) as String
        val timeZone = TimeZone.getTimeZone(timeZoneId)

        val viewHolder = view.tag as ViewHolder

        viewHolder.name.text = "${timeZone.displayName}(${timeZone.id})"
        viewHolder.clock.timeZone = timeZone.id

        return view
    }

    override fun getItem(position: Int): Any = timeZones[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = timeZones.size

    private class ViewHolder( view: View ) {
        val name = view.findViewById<TextView>(R.id.tvA07)
        val clock = view.findViewById<TextClock>(R.id.clA07)
    }
}