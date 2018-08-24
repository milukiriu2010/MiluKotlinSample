package milu.kiriu2010.gui.main

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import milu.kiriu2010.entity.GenreData
import milu.kiriu2010.milurssviewer.R

class GenreLstFragment: Fragment() {
    private lateinit var recyclerView: RecyclerView

    // ジャンルをタップしたときのコールバックインターフェース
    interface OnGenreSelectListener {
        fun onSelectedGenre( genreData: GenreData )
    }

    // ---------------------------------------------------------
    // このフラグメントがアクティビティに配置されたとき呼ばれる
    // ---------------------------------------------------------
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        Log.d( javaClass.simpleName, "" )
        Log.d( javaClass.simpleName, "onAttach" )
        Log.d( javaClass.simpleName, "========" )

        // アクティビティがコールバックを実装していなかったら例外を投げる
        if ( context !is OnGenreSelectListener ) {
            throw RuntimeException("$context must implement OnGenreSelectListener")
        }
    }

    // ---------------------------------------------------------
    // 表示するビューを生成する
    // ---------------------------------------------------------
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        Log.d( javaClass.simpleName, "" )
        Log.d( javaClass.simpleName, "onCreateView" )
        Log.d( javaClass.simpleName, "============" )

        val view = inflater.inflate( R.layout.fragment_rss_genrelst, container, false )
        recyclerView = view.findViewById(R.id.rvGenre)

        // ジャンルリストを縦方向に並べて表示
        val layoutManager = LinearLayoutManager( context, LinearLayoutManager.VERTICAL, false )
        recyclerView.layoutManager = layoutManager

        // コンテキストのnullチェック
        val ctx = context ?: return view

        // ジャンル一覧を表示するためのアダプタ
        val adapter = GenreLstAdapter( ctx, loadGenreData() ) { genreData ->
            // タップされたらコールバックを呼ぶ
            // コールバックにタップされたGenreDataオブジェクトを渡す
            ( ctx as OnGenreSelectListener).onSelectedGenre(genreData)
        }
        recyclerView.adapter = adapter

        // 区切り線を入れる
        // https://qiita.com/morimonn/items/035b1d85fec56e64f3e1
        val itemDecoration = DividerItemDecoration(ctx, DividerItemDecoration.VERTICAL )
        recyclerView.addItemDecoration(itemDecoration)

        return view
    }

    private fun loadGenreData(): MutableList<GenreData> {
        val genreLst: MutableList<GenreData> = mutableListOf<GenreData>()

        genreLst.add( GenreData( "2ch", 1 ))
        genreLst.add( GenreData( "豆知識", 2 ))
        genreLst.add( GenreData( "IT", 3 ) )

        return genreLst
    }

    class GenreLstAdapter(context: Context,
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
}