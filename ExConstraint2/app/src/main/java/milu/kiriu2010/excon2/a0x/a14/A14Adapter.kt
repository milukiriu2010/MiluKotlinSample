package milu.kiriu2010.excon2.a0x.a14

import android.content.Context
import android.hardware.Sensor
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import milu.kiriu2010.excon2.R

// 利用できるセンサ一覧を表示するアダプタ
class A14Adapter(
        context: Context,
        private val sensorLst: MutableList<Sensor> = mutableListOf() )
    : RecyclerView.Adapter<A14Adapter.SensorViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorViewHolder {
        val view = inflater.inflate(R.layout.adapter_a14, parent, false)
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
        val dataType = view.findViewById<TextView>(R.id.tvA14_DataType)
        val dataName = view.findViewById<TextView>(R.id.tvA14_DataName)
    }
}