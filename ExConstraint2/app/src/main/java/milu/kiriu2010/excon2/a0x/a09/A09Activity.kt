package milu.kiriu2010.excon2.a0x.a09

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import android.view.View
import kotlinx.android.synthetic.main.activity_a09.*
import milu.kiriu2010.excon2.R

// リサイクラービュー
// スワイプでアイテムを削除
// https://www.androidhive.info/2016/01/android-working-with-recycler-view/
class A09Activity : AppCompatActivity() {
    private val movieList:MutableList<Movie> = mutableListOf()
    lateinit private var mAdapter: A09Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a09)

        // 映画リストがある間は、"データがない"旨を表示するビューは非表示とする
        tvA09.visibility = View.GONE

        // 映画リストを表示するアダプタ
        this.mAdapter = A09Adapter(this.movieList, this)
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        rvA09.layoutManager = mLayoutManager
        rvA09.itemAnimator = DefaultItemAnimator()
        rvA09.adapter = this.mAdapter

        // スワイプ時の動作を定義するオブジェクト
        // スワイプするとデータを削除する
        val swipeHandler = object: SwipeToDeleteCallback(this, mAdapter) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rvA09.adapter as A09Adapter
                val pos = viewHolder.adapterPosition
                adapter.removeAt(pos)

                // 表示アイテム数が０になったら、"データがない"旨を表示
                if ( adapter.moviesList.size == 0 ) {
                    tvA09.visibility = View.VISIBLE
                    rvA09.visibility = View.GONE
                }
            }
        }

        // スワイプ時の動作とリサイクラービューを結びつける
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(rvA09)

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
