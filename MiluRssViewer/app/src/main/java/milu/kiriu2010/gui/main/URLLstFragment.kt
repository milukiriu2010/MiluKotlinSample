package milu.kiriu2010.gui.main

import android.content.Context
import android.content.Intent
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
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.list_row_url.view.*
import milu.kiriu2010.entity.URLData
import milu.kiriu2010.milurssviewer.R
import java.net.URL

class URLLstFragment: Fragment() {
    private lateinit var recyclerView: RecyclerView

    // URLをタップしたときのコールバックインターフェース
    interface OnURLSelectListener {
        fun onSelectedURL( urlData: URLData )
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
        if ( context !is OnURLSelectListener ) {
            throw RuntimeException("$context must implement OnURLSelectListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        Log.d( javaClass.simpleName, "" )
        Log.d( javaClass.simpleName, "onCreateView" )
        Log.d( javaClass.simpleName, "============" )

        val view = inflater.inflate( R.layout.fragment_rss_urllst, container, false )
        recyclerView = view.findViewById(R.id.rvURL)

        // ジャンルリストを縦方向に並べて表示
        val layoutManager = LinearLayoutManager( context, LinearLayoutManager.VERTICAL, false )
        recyclerView.layoutManager = layoutManager

        // コンテキストのnullチェック
        val ctx = context ?: return view

        // URL一覧を表示するためのアダプター
        val adapter = URLLstAdapter( ctx, loadURLData() ) { urlData ->
            // タップされたらコールバックを呼ぶ
            // コールバックにタップされたURLDataオブジェクトを渡す
            ( ctx as OnURLSelectListener).onSelectedURL(urlData)
        }
        recyclerView.adapter = adapter

        // 区切り線を入れる
        // https://qiita.com/morimonn/items/035b1d85fec56e64f3e1
        val itemDecoration = DividerItemDecoration(ctx, DividerItemDecoration.VERTICAL )
        recyclerView.addItemDecoration(itemDecoration)

        return view
    }

    private fun loadURLData(): MutableList<URLData> {
        val urlLst: MutableList<URLData> = mutableListOf<URLData>()

        // RSS 2.0
        urlLst.add( URLData( "IT", "ビジネスIT+IT HotTopics", URL("https://www.sbbit.jp/rss/HotTopics.rss")) )
        // RSS 2.0
        urlLst.add( URLData( "IT", "＠IT Smart & Socialフォーラム 最新記事一覧", URL("https://rss.itmedia.co.jp/rss/2.0/ait_smart.xml")) )
        // RSS 2.0
        urlLst.add( URLData( "IT", "＠IT HTML5 + UXフォーラム 最新記事一覧", URL("https://rss.itmedia.co.jp/rss/2.0/ait_ux.xml")) )
        // RSS 2.0
        urlLst.add( URLData( "IT", "＠IT Coding Edgeフォーラム 最新記事一覧", URL("https://rss.itmedia.co.jp/rss/2.0/ait_coding.xml")) )
        // RSS 2.0
        urlLst.add( URLData( "IT", "＠IT Java Agileフォーラム 最新記事一覧", URL("https://rss.itmedia.co.jp/rss/2.0/ait_java.xml")) )
        // RSS 2.0
        urlLst.add( URLData( "IT", "＠IT Database Expertフォーラム 最新記事一覧", URL("https://rss.itmedia.co.jp/rss/2.0/ait_db.xml")) )
        // RSS 2.0
        urlLst.add( URLData( "IT", "＠IT Linux＆OSSフォーラム 最新記事一覧", URL("https://rss.itmedia.co.jp/rss/2.0/ait_linux.xml")) )
        // RSS 2.0
        urlLst.add( URLData( "IT", "GIGAZINE", URL("https://gigazine.net/news/rss_2.0/")) )

        return urlLst
    }

    class URLLstAdapter(context: Context,
                        // URL一覧
            private val urlDataLst: MutableList<URLData> = mutableListOf<URLData>(),
                        // アイテムをクリックすると呼ばれる
            private val onItemClick: (URLData) -> Unit )
        : RecyclerView.Adapter<URLLstAdapter.URLViewHolder>() {
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

        private val inflater = LayoutInflater.from(context)

        class URLViewHolder(view: View): RecyclerView.ViewHolder(view) {
            val labelTitle = view.findViewById<TextView>(R.id.labelTitle)
            val labelURL  = view.findViewById<TextView>(R.id.labelURL)
        }
    }
}