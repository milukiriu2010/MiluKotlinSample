package milu.kiriu2010.excon1.a03

import android.content.Context
import android.icu.util.TimeZone
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextClock
import android.widget.TextView
import milu.kiriu2010.excon1.R

// TimeZone.getAvailableIDs() => 24
class TimeZoneAdapter(private val context: Context,
                       private val timeZones: Array<String> = TimeZone.getAvailableIDs() ) : BaseAdapter() {
    private val inflater = LayoutInflater.from(context)

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

    private fun createView( parent: ViewGroup? ): View {
        val view = inflater.inflate(R.layout.list_time_zone_row_for_worldclock, parent, false )
        view.tag = ViewHolder(view)
        return view
    }

    private class ViewHolder( view: View ) {
        val name = view.findViewById<TextView>(R.id.txtTimeZone)
        val clock = view.findViewById<TextClock>(R.id.txtClock)
    }
}