package milu.kiriu2010.excon2.b0x.b07

import android.app.ActivityManager
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import milu.kiriu2010.excon2.R

// 起動中アプリ一覧を表示するアダプタ
class B07Adapter(
        context: Context,
        val serviceLst: MutableList<ActivityManager.RunningAppProcessInfo> = mutableListOf() )
    : RecyclerView.Adapter<B07Adapter.ServiceViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = inflater.inflate(R.layout.adapter_b07, parent, false)
        val viewHolder = ServiceViewHolder(view)
        return viewHolder
    }

    override fun getItemCount() = serviceLst.size

    override fun onBindViewHolder(holder: ServiceViewHolder, pos: Int) {
        val service = serviceLst[pos]

        holder.dataProcessName.text = service.processName
    }


    class ServiceViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val dataProcessName = view.findViewById<TextView>(R.id.tvB07)
    }

}