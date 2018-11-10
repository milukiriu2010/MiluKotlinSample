package milu.kiriu2010.excon2.screen2.servicelst

import android.app.ActivityManager
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.list_row_service.view.*
import milu.kiriu2010.excon2.R

class ServiceRecyclerViewAdapter(
        context: Context,
        val serviceLst: MutableList<ActivityManager.RunningAppProcessInfo> = mutableListOf() )
    : RecyclerView.Adapter<ServiceRecyclerViewAdapter.ServiceViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = inflater.inflate(R.layout.list_row_service, parent, false)
        val viewHolder = ServiceViewHolder(view)
        return viewHolder
    }

    override fun getItemCount() = serviceLst.size

    override fun onBindViewHolder(holder: ServiceViewHolder, pos: Int) {
        val service = serviceLst[pos]

        holder.dataProcessName.text = service.processName
    }


    class ServiceViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val dataProcessName = view.findViewById<TextView>(R.id.dataProcessName)
    }

}