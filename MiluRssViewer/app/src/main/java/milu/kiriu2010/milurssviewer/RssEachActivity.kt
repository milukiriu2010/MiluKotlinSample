package milu.kiriu2010.milurssviewer

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import milu.kiriu2010.entity.URLData
import milu.kiriu2010.entity.Article

class RssEachActivity : AppCompatActivity() {

    lateinit var urlData: URLData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rss_each)

        this.urlData = intent.getParcelableExtra<URLData>(IntentKey.KEY_RSS_EACH.key)
    }

    class RssEachAdapter(
            private val context: Context,
            private val articleLst: MutableList<Article> = mutableListOf<Article>(),
            private val onArticleClicked: (Article) -> Unit
    ): RecyclerView.Adapter<RssEachAdapter.RssEachViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RssEachViewHolder {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getItemCount() = articleLst.size

        override fun onBindViewHolder(holder: RssEachViewHolder, position: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        class RssEachViewHolder( view: View): RecyclerView.ViewHolder(view) {
            val lblTitle = view.findViewById<TextView>(R.id.lblTitle)
            val lblPubDate = view.findViewById<TextView>(R.id.lblPubDate)
        }
    }
}
