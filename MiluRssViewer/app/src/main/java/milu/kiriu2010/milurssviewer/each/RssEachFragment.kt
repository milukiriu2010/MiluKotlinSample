package milu.kiriu2010.milurssviewer.each

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.Fragment
import android.support.v7.widget.*
import android.util.Log
import android.view.*
import android.widget.TextView
import milu.kiriu2010.entity.Article
import milu.kiriu2010.entity.Rss
import milu.kiriu2010.id.BundleID
import milu.kiriu2010.milurssviewer.R
import java.text.SimpleDateFormat

class RssEachFragment: Fragment() {
    // RSSコンテンツを表示するリサイクラービュー
    private lateinit var recyclerView: RecyclerView

    // RSSコンテンツ
    private lateinit var rss: Rss

    // Rssの記事一覧を表示するフラグメントを生成
    companion object {
        fun newInstance(rss: Rss): Fragment {
            val fragmentRssEach = RssEachFragment()

            // Rss記事フラグメントに渡すデータをセット
            val args = Bundle()
            args.putParcelable(BundleID.ID_RSS.id, rss)
            fragmentRssEach.arguments = args

            return fragmentRssEach
        }
    }

    // ---------------------------------------------------------
    // このフラグメントがアクティビティに配置されたとき呼ばれる
    // ---------------------------------------------------------
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.d(javaClass.simpleName, "onAttach")
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
        this.rss = args.getParcelable(BundleID.ID_RSS.id) ?: Rss()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(javaClass.simpleName, "onDestroy")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        Log.d(javaClass.simpleName, "onCreateView")

        // XMLからURL一覧を表示するビューを生成
        val view = inflater.inflate(R.layout.fragment_rss_each, container, false)

        // RSSコンテンツのタイトル
        val labelTitle = view.findViewById<TextView>(R.id.labelTitle)
        labelTitle.setText(this.rss.title)

        // RSSコンテンツの公開日
        val labelPubDate = view.findViewById<TextView>(R.id.labelPubDate)
        //labelPubDate.text = "{this.rss.pubDate}月{this.rss.pubDate}日{this.rss.pubDate}時"
        val dateFormat = SimpleDateFormat("Y年M月d日H時")
        labelPubDate.text = dateFormat.format(this.rss.pubDate)

        // RSS記事一覧
        recyclerView = view.findViewById(R.id.rvRssEach)

        // RSS記事一覧をグリッドで並べる
        val layoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = layoutManager

        // コンテキストのnullチェック
        val ctx = context ?: return view

        // RSS記事一覧を表示するためのアダプタ
        val adapter = RssEachAdapter(ctx, this.rss.articles) { article ->
            // RSS記事をタップすると"Chrome Custom Tabs"を開く
            val intent = CustomTabsIntent.Builder().build()
            intent.launchUrl(ctx, Uri.parse(article.link))
        }
        recyclerView.adapter = adapter

        // StaggeredGridLayoutManagerの場合は、区切り線はじゃま
        /*
        // 区切り線を入れる
        // https://qiita.com/morimonn/items/035b1d85fec56e64f3e1
        val itemDecoration = DividerItemDecoration( ctx, DividerItemDecoration.VERTICAL  or DividerItemDecoration.HORIZONTAL )
        recyclerView.addItemDecoration(itemDecoration)
        */

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(javaClass.simpleName, "onDestroyView")
    }

    // onCreateViewの後,onStartの前に呼ばれる
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(javaClass.simpleName, "onActivityCreated")
        // オプションメニューを表示する
        setHasOptionsMenu(true)
    }

    // オプションメニューを表示
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_rss_each, menu)

        // デフォルトチェックを入れたいがうまく動かない
        //val menuItemGrid = menu?.findItem(R.id.menuItemGrid)
        //menuItemGrid?.isCheckable = true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return when (item?.itemId) {
            R.id.menuItemLinear -> {
                item.isChecked = true
                recyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false)
                true
            }
            R.id.menuItemGrid -> {
                item.isChecked = true
                recyclerView.layoutManager = GridLayoutManager(context,2)
                true
            }
            R.id.menuItemStaggered -> {
                item.isChecked = true
                recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
