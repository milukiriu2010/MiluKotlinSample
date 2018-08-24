package milu.kiriu2010.gui.each

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_rss_each.*
import milu.kiriu2010.entity.Article
import milu.kiriu2010.entity.Rss
import milu.kiriu2010.id.BundleID
import milu.kiriu2010.milurssviewer.R
import java.text.SimpleDateFormat

class RssEachFragment: Fragment() {
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

        Log.d(javaClass.simpleName, "")
        Log.d(javaClass.simpleName, "onAttach")
        Log.d(javaClass.simpleName, "========")
    }

    // ---------------------------------------------------------
    // 呼び出し時に渡される引数から指定されたジャンルを取り出す
    // ---------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = this.arguments ?: return
        this.rss = args.getParcelable(BundleID.ID_RSS.id)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        Log.d(javaClass.simpleName, "")
        Log.d(javaClass.simpleName, "onCreateView")
        Log.d(javaClass.simpleName, "============")

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
        val adapter = RssEachAdapter( ctx, this.rss.articles ) { article ->
            // RSS記事をタップすると"Chrome Custom Tabs"を開く
            val intent = CustomTabsIntent.Builder().build()
            intent.launchUrl(ctx, Uri.parse(article.link))
        }
        recyclerView.adapter = adapter

        // 区切り線を入れる
        // https://qiita.com/morimonn/items/035b1d85fec56e64f3e1
        val itemDecoration = DividerItemDecoration( ctx, DividerItemDecoration.VERTICAL  or DividerItemDecoration.HORIZONTAL )
        recyclerView.addItemDecoration(itemDecoration)

        return view
    }

    class RssEachAdapter(
            private val context: Context,
            private val articleLst: MutableList<Article> = mutableListOf<Article>(),
            private val onArticleClicked: (Article) -> Unit
        ): RecyclerView.Adapter<RssEachAdapter.RssEachViewHolder>() {

        private val inflater = LayoutInflater.from(context)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RssEachAdapter.RssEachViewHolder {
            // Viewを生成する
            val view = inflater.inflate(R.layout.list_row_rss_each,parent,false)
            // viewHolderを生成する
            val viewHolder = RssEachAdapter.RssEachViewHolder(view)

            // 記事をクリックすると、表示するためのコールバックを呼ぶ
            view.setOnClickListener {
                val position = viewHolder.adapterPosition
                val article = articleLst[position]
                onArticleClicked(article)
            }

            return viewHolder
        }

        override fun getItemCount(): Int = articleLst.size

        override fun onBindViewHolder(holder: RssEachAdapter.RssEachViewHolder, position: Int) {
            val article = articleLst[position]
            // 記事のタイトルを設定する
            holder.lblTitle.text = article.title
            // 記事の発行日付を設定する
            holder.lblPubDate.text = context.getString(R.string.LABEL_PUB_DATE,article.pubDate)
        }

        class RssEachViewHolder( view: View): RecyclerView.ViewHolder(view) {
            val lblTitle = view.findViewById<TextView>(R.id.lblTitle)
            val lblPubDate = view.findViewById<TextView>(R.id.lblPubDate)
        }
    }
}