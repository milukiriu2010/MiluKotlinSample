package milu.kiriu2010.milurssviewer.main

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
import milu.kiriu2010.entity.URLData
import milu.kiriu2010.id.BundleID
import milu.kiriu2010.milurssviewer.R
import java.net.URL

class URLLstFragment: Fragment() {
    private lateinit var recyclerView: RecyclerView

    private lateinit var genreData: GenreData

    // URLをタップしたときのコールバックインターフェース
    interface OnURLSelectListener {
        fun onSelectedURL( urlData: URLData )
    }

    // ---------------------------------------------------------
    // URL一覧フラグメントを生成
    // ---------------------------------------------------------
    companion object {
        fun newInstance( genreData: GenreData = GenreData( 1, "2ch" ) ): Fragment {
            val fragmentURLLst = URLLstFragment()

            // URL一覧フラグメントに渡すデータをセット
            val args = Bundle()
            args.putParcelable( BundleID.ID_GENRE.id, genreData )
            fragmentURLLst.arguments = args

            return fragmentURLLst
        }
    }

    // ---------------------------------------------------------
    // このフラグメントがアクティビティに配置されたとき呼ばれる
    // ---------------------------------------------------------
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.d( javaClass.simpleName, "onAttach" )

        // アクティビティがコールバックを実装していなかったら例外を投げる
        if ( context !is OnURLSelectListener ) {
            throw RuntimeException("$context must implement OnURLSelectListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(javaClass.simpleName, "onDetach")
    }

    // ---------------------------------------------------------
    // 呼び出し時に渡される引数から指定されたジャンルを取り出す
    // ---------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(javaClass.simpleName, "onCreate")

        val args = this.arguments ?: return
        this.genreData = args.getParcelable(BundleID.ID_GENRE.id) ?: GenreData()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(javaClass.simpleName, "onDestroy")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        Log.d( javaClass.simpleName, "onCreateView" )

        // XMLからURL一覧を表示するビューを生成
        val view = inflater.inflate( R.layout.fragment_rss_urllst, container, false )
        recyclerView = view.findViewById(R.id.rvURL)

        // URL一覧を縦方向に並べて表示
        val layoutManager = LinearLayoutManager( context, LinearLayoutManager.VERTICAL, false )
        recyclerView.layoutManager = layoutManager

        // コンテキストのnullチェック
        val ctx = context ?: return view

        // URL一覧を表示するためのアダプタ
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

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(javaClass.simpleName, "onDestroyView")
    }

    private fun loadURLData(): MutableList<URLData> {
        val urlLst: MutableList<URLData> = mutableListOf<URLData>()

        // RSS 1.0
        // 2ch
        urlLst.add( URLData( 1, 1, "痛いニュース(ﾉ∀`)", URL("http://blog.livedoor.jp/dqnplus/index.rdf")) )
        // RSS 1.0
        // 2ch
        urlLst.add( URLData( 2, 1, "【2ch】ニュー速クオリティ", URL("http://news4vip.livedoor.biz/index.rdf")) )
        // RSS 1.0
        // 2ch
        urlLst.add( URLData( 3, 1, "ハムスター速報", URL("http://hamusoku.com/index.rdf")) )
        // RSS 1.0
        // 2ch
        urlLst.add( URLData( 4, 1, "ニュー速VIPブログ(`･ω･´)", URL("http://blog.livedoor.jp/insidears/index.rdf")) )
        // RSS 2.0
        // 豆知識
        urlLst.add( URLData(5,2, "ライフハッカー", URL("http://feeds.lifehacker.jp/rss/lifehacker/index.xml")) )
        // 上のURLだと301が返される
        // こっちが現在のURL
        //urlLst.add( URLData(5,2, "ライフハッカー", URL("https://www.lifehacker.jp/feed/index.xml")) )
        // RSS 2.0
        // 豆知識
        urlLst.add( URLData( 6,2, "ロケットニュース", URL("http://feeds.rocketnews24.com/rocketnews24")) )
        // RSS 2.0
        // ニュース
        urlLst.add( URLData( 7,3, "Yahoo(主要)", URL("https://news.yahoo.co.jp/pickup/rss.xml")) )
        // RSS 2.0
        // 天気
        urlLst.add( URLData( 8,4, "Yahoo(東京)", URL("https://rss-weather.yahoo.co.jp/rss/days/4410.xml")) )
        // RSS 2.0
        // IT
        urlLst.add( URLData( 9,5, "ビジネスIT+IT HotTopics", URL("https://www.sbbit.jp/rss/HotTopics.rss")) )
        // RSS 2.0
        // IT
        urlLst.add( URLData( 10,5, "＠IT Smart & Socialフォーラム 最新記事一覧", URL("https://rss.itmedia.co.jp/rss/2.0/ait_smart.xml")) )
        // RSS 2.0
        // IT
        urlLst.add( URLData( 11,5, "＠IT HTML5 + UXフォーラム 最新記事一覧", URL("https://rss.itmedia.co.jp/rss/2.0/ait_ux.xml")) )
        // RSS 2.0
        // IT
        urlLst.add( URLData( 12, 5, "＠IT Coding Edgeフォーラム 最新記事一覧", URL("https://rss.itmedia.co.jp/rss/2.0/ait_coding.xml")) )
        // RSS 2.0
        // IT
        urlLst.add( URLData( 13, 5, "＠IT Java Agileフォーラム 最新記事一覧", URL("https://rss.itmedia.co.jp/rss/2.0/ait_java.xml")) )
        // RSS 2.0
        // IT
        urlLst.add( URLData( 14, 5, "＠IT Database Expertフォーラム 最新記事一覧", URL("https://rss.itmedia.co.jp/rss/2.0/ait_db.xml")) )
        // RSS 2.0
        // IT
        urlLst.add( URLData( 15, 5, "＠IT Linux＆OSSフォーラム 最新記事一覧", URL("https://rss.itmedia.co.jp/rss/2.0/ait_linux.xml")) )
        // RSS 2.0+1.1?
        // IT
        urlLst.add( URLData( 16, 5, "GIGAZINE", URL("https://gigazine.net/news/rss_2.0/")) )

        return urlLst.filter { urlData -> urlData.genreId.equals(genreData.id) }.toMutableList()

        //return urlLst
    }


}