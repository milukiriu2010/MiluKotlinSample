package milu.kiriu2010.excon2.recycler

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import kotlinx.android.synthetic.main.activity_recycle.*
import milu.kiriu2010.abc.Movie
import milu.kiriu2010.excon2.R

// https://www.androidhive.info/2016/01/android-working-with-recycler-view/
class RecycleActivity : AppCompatActivity() {
    private val movieList:MutableList<Movie> = mutableListOf()
    //lateinit private var recyclerView: RecyclerView
    lateinit private var mAdapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle)

        textViewMovie.visibility = View.GONE

        this.mAdapter = MoviesAdapter(this.movieList, this)
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerViewMovie.layoutManager = mLayoutManager
        recyclerViewMovie.itemAnimator = DefaultItemAnimator()
        recyclerViewMovie.adapter = this.mAdapter

        val swipeHandler = object: SwipeToDeleteCallback(this, mAdapter) {
            /*
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val fromPos = viewHolder.adapterPosition
                val toPos = target.adapterPosition
                mAdapter.notifyItemMoved(fromPos,toPos)
                return true
            }
            */

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerViewMovie.adapter as MoviesAdapter
                val pos = viewHolder.adapterPosition
                adapter.removeAt(pos)

                // 表示アイテム数が０になったら、"データがない"旨を表示
                if ( adapter.moviesList.size == 0 ) {
                    textViewMovie.visibility = View.VISIBLE
                    recyclerViewMovie.visibility = View.GONE
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerViewMovie)

        this.prepareMovieData()
    }

    private fun prepareMovieData() {
        movieList.add(Movie("Mad Max: Fury Road", "Action & Adventure", "2015"))
        movieList.add(Movie("Inside Out", "Animation, Kids & Family", "2015"))
        movieList.add(Movie("Star Wars: Episode VII - The Force Awakens", "Action", "2015"))
        movieList.add(Movie("Shaun the Sheep", "Animation", "2015"))
        movieList.add(Movie("The Martian", "Science Fiction & Fantasy", "2015"))
        movieList.add(Movie("Mission: Impossible Rogue Nation", "Action", "2015"))
        movieList.add(Movie("Up", "Animation", "2009"))
        movieList.add(Movie("Star Trek", "Science Fiction", "2009"))
        movieList.add(Movie("The LEGO Movie", "Animation", "2014"))
        movieList.add(Movie("Iron Man", "Action & Adventure", "2008"))
        movieList.add(Movie("Aliens", "Science Fiction", "1986"))
        movieList.add(Movie("Chicken Run", "Animation", "2000"))
        movieList.add(Movie("Back to the Future", "Science Fiction", "1985"))
        movieList.add(Movie("Raiders of the Lost Ark", "Action & Adventure", "1981"))
        movieList.add(Movie("Goldfinger", "Action & Adventure", "1965"))
        movieList.add(Movie("Guardians of the Galaxy", "Science Fiction & Fantasy", "2014"))

        // リストに変更があったことをアダプタに知らせる
        this.mAdapter.notifyDataSetChanged()
    }
}
