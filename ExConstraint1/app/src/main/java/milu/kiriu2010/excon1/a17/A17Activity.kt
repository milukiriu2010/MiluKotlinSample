package milu.kiriu2010.excon1.a17

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import milu.kiriu2010.excon1.R
import kotlinx.android.synthetic.main.activity_a17.*
import java.util.*

// リサイクラービューの練習
class A17Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a17)

        val adapter = A17Adapter(this) { timeZone ->
            Toast.makeText(this, timeZone.displayName, Toast.LENGTH_LONG).show()
        }

        rvA17.adapter = adapter
        rvA17.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    class A17Adapter(context: Context,
                     private val onItemClicked: (TimeZone)->Unit )
        : RecyclerView.Adapter<A17Adapter.A17ViewHolder>(){

        private val inflater = LayoutInflater.from(context)
        private val timeZones = TimeZone.getAvailableIDs().map{
            id -> TimeZone.getTimeZone(id)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): A17ViewHolder {
            val view = inflater.inflate(R.layout.adapter_a17, parent, false)
            val viewHolder = A17ViewHolder(view)

            view.setOnClickListener{
                val position = viewHolder.adapterPosition
                val timeZone = timeZones[position]
                onItemClicked(timeZone)
            }

            return viewHolder
        }

        override fun getItemCount() = timeZones.size

        override fun onBindViewHolder(holder: A17ViewHolder, position: Int) {
            val timeZone = timeZones[position]
            holder.timeZone.text = timeZone.id
        }

        class A17ViewHolder(view: View): RecyclerView.ViewHolder(view){
            val timeZone = view.findViewById<TextView>(R.id.tvA17)
        }
    }
}
