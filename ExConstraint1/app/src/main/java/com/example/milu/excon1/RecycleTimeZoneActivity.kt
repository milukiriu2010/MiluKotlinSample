package com.example.milu.excon1

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.milu.intent2.R
import kotlinx.android.synthetic.main.activity_recycle_time_zone.*
import java.util.*

class RecycleTimeZoneActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle_time_zone)

        val adapter = SampleAdapter(this) { timeZone ->
            Toast.makeText(this, timeZone.displayName, Toast.LENGTH_LONG).show()
        }

        rvTimeZone.adapter = adapter
        rvTimeZone.layoutManager = LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false )
    }

    class SampleAdapter(context: Context,
                        private val onItemClicked: (TimeZone)->Unit )
        : RecyclerView.Adapter<SampleAdapter.SampleViewHolder>(){

        private val inflater = LayoutInflater.from(context)
        private val timeZones = TimeZone.getAvailableIDs().map{
            id -> TimeZone.getTimeZone(id)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampleViewHolder {
            val view = inflater.inflate(R.layout.list_time_zone_row, parent, false)
            val viewHolder = SampleViewHolder(view)

            view.setOnClickListener{
                val position = viewHolder.adapterPosition
                val timeZone = timeZones[position]
                onItemClicked(timeZone)
            }

            return viewHolder
        }

        override fun getItemCount() = timeZones.size

        override fun onBindViewHolder(holder: SampleViewHolder, position: Int) {
            val timeZone = timeZones[position]
            holder.timeZone.text = timeZone.id
        }

        class SampleViewHolder(view: View): RecyclerView.ViewHolder(view){
            val timeZone = view.findViewById<TextView>(R.id.timeZone)
        }
    }
}
