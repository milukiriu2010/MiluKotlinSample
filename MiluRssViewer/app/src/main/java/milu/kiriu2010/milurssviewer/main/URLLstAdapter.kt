package milu.kiriu2010.milurssviewer.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import milu.kiriu2010.entity.URLData
import milu.kiriu2010.milurssviewer.R

class URLLstAdapter(
        context: Context,
        // URL一覧
        private val urlDataLst: MutableList<URLData> = mutableListOf<URLData>(),
        // アイテムをクリックすると呼ばれる
        private val onItemClick: (URLData) -> Unit )
    : RecyclerView.Adapter<URLLstAdapter.URLViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): URLViewHolder {
        val view = inflater.inflate(R.layout.list_row_url, parent, false )
        val viewHolder = URLViewHolder(view)

        view.setOnClickListener {
            val pos = viewHolder.adapterPosition
            val urlData = urlDataLst[pos]
            onItemClick(urlData)
        }

        return viewHolder
    }

    override fun getItemCount(): Int = urlDataLst.size

    override fun onBindViewHolder(holder: URLViewHolder, position: Int) {
        val urlData = urlDataLst[position]
        holder.labelTitle.text = urlData.title
        holder.labelURL.text = urlData.url.toString()
    }

    class URLViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val labelTitle = view.findViewById<TextView>(R.id.labelTitle)
        val labelURL  = view.findViewById<TextView>(R.id.labelURL)
    }
}
