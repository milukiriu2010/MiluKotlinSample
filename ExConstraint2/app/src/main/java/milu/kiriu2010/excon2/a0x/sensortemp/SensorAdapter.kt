package milu.kiriu2010.excon2.a0x.sensortemp

import android.content.Context
import android.hardware.Sensor
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import milu.kiriu2010.excon2.R

class SensorAdapter(
        context: Context,
        private val sensorLst: MutableList<Sensor> = mutableListOf() )
    : RecyclerView.Adapter<SensorAdapter.SensorViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorViewHolder {
        val view = inflater.inflate(R.layout.list_row_sensor, parent, false)
        val viewHolder = SensorViewHolder(view)

        return viewHolder
    }

    override fun getItemCount(): Int = sensorLst.size

    override fun onBindViewHolder(holder: SensorViewHolder, pos: Int) {
        val sensor = sensorLst[pos]

        holder.dataType.text = sensor.stringType
        holder.dataName.text = sensor.name
    }


    class SensorViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val dataType = view.findViewById<TextView>(R.id.dataType)
        val dataName = view.findViewById<TextView>(R.id.dataName)
    }
}