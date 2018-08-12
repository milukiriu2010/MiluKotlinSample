package milu.kiriu2010.milurssviewer

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_rss_each.*
import milu.kiriu2010.entity.URLData
import milu.kiriu2010.entity.Article
import milu.kiriu2010.entity.Rss
import milu.kiriu2010.entity.RssLoader
import milu.kiriu2010.job.PollingJob
import milu.kiriu2010.job.createChannel
import java.util.concurrent.TimeUnit

class RssEachActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Rss> {

    lateinit var urlData: URLData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rss_each)

        this.urlData = intent.getParcelableExtra<URLData>(IntentKey.KEY_RSS_EACH.key)

        // deprecated
        // ローダーを呼び出す
        // loaderManager.initLoader(1,null,this)

        supportLoaderManager.initLoader(1,null, this )

        // 通知チャネルを作成する
        createChannel(this)

        // 定期的に新しい記事がないかをチェックするジョブ
        val fetchJob =
                JobInfo.Builder(1, ComponentName(this, PollingJob::class.java ) )
                        // 6時間ごとに実行
                        .setPeriodic(TimeUnit.HOURS.toMillis(6))
                        // 端末を再起動しても有効
                        .setPersisted(true)
                        // ネットワーク接続されていること
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .build()
        // ジョブを登録する
        getSystemService(JobScheduler::class.java).schedule(fetchJob)
    }

    // LoaderManager.LoaderCallbacks<Rss>
    // ローダが要求されたときによばれる
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Rss> {
        return RssLoader(this)
    }

    // LoaderManager.LoaderCallbacks<Rss>
    override fun onLoadFinished(loader: Loader<Rss>, data: Rss?) {
        if ( data != null ){
            // RecyclerViewをレイアウトから探す
            // RSSの記事一覧アダプタ
            val adapter = RssEachAdapter( this, data.articles ) { article->
                // 記事をタップしたときの処理
                // Chrome Custom Tabsを開く
                val intent = CustomTabsIntent.Builder().build()
                intent.launchUrl(this, Uri.parse(article.link))
            }
            recyclerView.adapter = adapter

            // グリッドを表示するレイアウトマネージャ
            val layoutManager = GridLayoutManager(this, 2)
            recyclerView.layoutManager = layoutManager
        }
    }

    // LoaderManager.LoaderCallbacks<Rss>
    // ローダがリセットされたときに呼ばれる
    override fun onLoaderReset(loader: Loader<Rss>) {
    }

    class RssEachAdapter(
            private val context: Context,
            private val articleLst: MutableList<Article> = mutableListOf<Article>(),
            private val onArticleClicked: (Article) -> Unit
    ): RecyclerView.Adapter<RssEachAdapter.RssEachViewHolder>() {

        private val inflater = LayoutInflater.from(context)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RssEachViewHolder {
            // Viewを生成する
            val view = inflater.inflate(R.layout.list_row_rss_each,parent,false)
            // viewHolderを生成する
            val viewHolder = RssEachViewHolder(view)

            view.setOnClickListener {
                val position = viewHolder.adapterPosition
                val article = articleLst[position]
                // コールバックをよぶ
                onArticleClicked(article)
            }

            return viewHolder
        }

        override fun getItemCount() = articleLst.size

        override fun onBindViewHolder(holder: RssEachViewHolder, position: Int) {
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
