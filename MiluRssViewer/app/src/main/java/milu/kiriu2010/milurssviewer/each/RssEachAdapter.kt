package milu.kiriu2010.milurssviewer.each

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import milu.kiriu2010.entity.Article
import milu.kiriu2010.milurssviewer.R

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

        // 記事をクリックすると、表示するためのコールバックを呼ぶ
        view.setOnClickListener {
            val position = viewHolder.adapterPosition
            val article = articleLst[position]
            onArticleClicked(article)
        }

        return viewHolder
    }

    override fun getItemCount(): Int = articleLst.size

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
