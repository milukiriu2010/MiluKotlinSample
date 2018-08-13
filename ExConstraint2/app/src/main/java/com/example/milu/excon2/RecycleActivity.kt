package com.example.milu.excon2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.milu.abc.Movie
import kotlinx.android.synthetic.main.activity_recycle.*

// https://www.androidhive.info/2016/01/android-working-with-recycler-view/
class RecycleActivity : AppCompatActivity() {
    private val movieList:MutableList<Movie> = mutableListOf()
    //lateinit private var recyclerView: RecyclerView
    lateinit private var mAdapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle)

        //recyclerView = findViewById(R.id.recycler_view) As RecyclerView

        this.mAdapter = MoviesAdapter(this.movieList,this)
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = mLayoutManager
        recycler_view.itemAnimator = DefaultItemAnimator()
        recycler_view.adapter = this.mAdapter

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

        this.mAdapter.notifyDataSetChanged()
    }
}
