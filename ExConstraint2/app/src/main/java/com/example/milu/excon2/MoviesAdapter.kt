package com.example.milu.excon2

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.milu.abc.Movie

// https://www.androidhive.info/2016/01/android-working-with-recycler-view/
// https://android.jlelse.eu/using-recyclerview-in-android-kotlin-722991e86bf3
class MoviesAdapter(val moviesList: MutableList<Movie>, val context: Context) : RecyclerView.Adapter<MoviesAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title) as TextView
        var year: TextView = view.findViewById(R.id.year) as TextView
        var genre: TextView = view.findViewById(R.id.genre) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.movie_list_row, parent, false);

        return MyViewHolder(itemView);
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int): Unit {
        val movie = moviesList.get(position);
        holder.title.setText(movie.title);
        holder.genre.setText(movie.genre);
        holder.year.setText(movie.year);
    }


    override  fun getItemCount(): Int {
        return this.moviesList.size
    }
}