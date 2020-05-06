package milu.kiriu2010.excon2.a0x.a09

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import milu.kiriu2010.excon2.R

// 各個別の映画情報を表示するアダプタ
// https://www.androidhive.info/2016/01/android-working-with-recycler-view/
// https://android.jlelse.eu/using-recyclerview-in-android-kotlin-722991e86bf3
class A09Adapter(val moviesList: MutableList<Movie>, val context: Context) : RecyclerView.Adapter<A09Adapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvA09_title) as TextView
        var year: TextView = view.findViewById(R.id.tvA09_year) as TextView
        var genre: TextView = view.findViewById(R.id.tvA09_genre) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_a09, parent, false);

        return MyViewHolder(itemView);
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int): Unit {
        val movie = moviesList.get(position);
        holder.title.setText(movie.title);
        holder.genre.setText(movie.genre);
        holder.year.setText(movie.year);
    }


    override  fun getItemCount(): Int = this.moviesList.size

    fun removeAt(pos: Int) {
        this.moviesList.removeAt(pos)
        notifyItemRemoved(pos)
    }
}