package milu.kiriu2010.milurssviewer

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import milu.kiriu2010.entity.URLData

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = URLAdapter(this ) { urlData ->

        }

        rvURL.adapter = adapter

        rvURL.layoutManager = LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false )
    }

    class URLAdapter(context: Context,
                     private val urlLst: MutableList<URLData> = mutableListOf<URLData>(),
                     private val onItemClicked: (URLData) -> Unit )
        : RecyclerView.Adapter<URLAdapter.URLViewHolder>() {

        private val inflater = LayoutInflater.from(context)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): URLViewHolder {
            val view = inflater.inflate(R.layout.list_row_url, parent, false )
            val viewHolder = URLViewHolder(view)

            view.setOnClickListener {
                val pos = viewHolder.adapterPosition
                val urlData = urlLst[pos]
                onItemClicked(urlData)
            }

            return viewHolder
        }

        override fun getItemCount(): Int = urlLst.size

        override fun onBindViewHolder(holder: URLViewHolder, position: Int) {
            val urlData = urlLst[position]
            holder.labelTitle.text = urlData.title
            holder.labelURL.text   = urlData.url.path
        }

        class URLViewHolder(view: View): RecyclerView.ViewHolder(view) {
            val labelTitle = view.findViewById<TextView>(R.id.labelTitle)
            val labelURL   = view.findViewById<TextView>(R.id.labelURL)
        }
    }
}
