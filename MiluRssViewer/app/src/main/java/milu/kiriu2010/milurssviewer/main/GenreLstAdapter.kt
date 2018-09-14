package milu.kiriu2010.milurssviewer.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import milu.kiriu2010.entity.GenreData
import milu.kiriu2010.milurssviewer.R

class GenreLstAdapter(
        context: Context,
        // ジャンル一覧
        private val genreDataLst: MutableList<GenreData> = mutableListOf<GenreData>(),
        // アイテムをクリックすると呼ばれる
        private val onItemClick: (GenreData) -> Unit )
    : RecyclerView.Adapter<GenreLstAdapter.GenreViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val view = inflater.inflate(R.layout.list_row_genre, parent, false )
        val viewHolder = GenreViewHolder(view)

        view.setOnClickListener {
            val pos = viewHolder.adapterPosition
            val genreData = genreDataLst[pos]
            onItemClick(genreData)
        }

        return viewHolder
    }

    override fun getItemCount(): Int = genreDataLst.size

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genreData = genreDataLst[position]
        holder.labelGenre.text = genreData.genre
    }

    class GenreViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val labelGenre = view.findViewById<TextView>(R.id.labelGenre)
    }
}
