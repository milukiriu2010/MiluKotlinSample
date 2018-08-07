package milu.kiriu2010.entity

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import milu.kiriu2010.net.httpGet
import org.w3c.dom.NodeList
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

// RSSの各記事を表すデータクラス
data class Article( val title: String, val link: String, val pubDate: Date )

// RSSを表現するデータクラス
data class Rss( val title: String, val pubDate: Date, val articles: MutableList<Article> )


// RSS2.0をパースしてRssオブジェクトに変換する
fun parseRss(stream: InputStream) : Rss {

    // XMLをDOMオブジェクトに変換する
    val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream)
    stream.close()

    // XPathを生成する
    val xPath = XPathFactory.newInstance().newXPath()

    // RSS2.0の日付書式である、RFC1123をDate型に変換するためのオブジェクト
    val formatter = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)

    // チャンネル内の<item>要素を全て取り出す
    val items = xPath.evaluate("/rss/channel//item", doc, XPathConstants.NODESET) as NodeList

    // RSSフィード内の記事の一覧
    val articles = arrayListOf<Article>()

    // <item>の要素ごとに繰り返す
    for (i in 0 until items.length) {
        val item = items.item(i)

        // Articleオブジェクトにまとめる
        val article = Article(
                title = xPath.evaluate("./title/text()", item),
                link  = xPath.evaluate("./link/text()", item),
                pubDate = formatter.parse(xPath.evaluate("./pubDate/text()", item)))

        articles.add(article)
    }

    // RSSオブジェクトにまとめて返す
    return Rss(title = xPath.evaluate("/rss/channel/title/text()", doc),
            pubDate = formatter.parse(xPath.evaluate("/rss/channel/pubDate/text()", doc)),
            articles = articles)
}

// RSSフィードをダウンロードしてRssオブジェクトを返すローダー
class RssLoader(context: Context) : AsyncTaskLoader<Rss>(context) {

    private var cache : Rss? = null

    // このローダーがバックグラウンドで行う処理
    override fun loadInBackground(): Rss? {
        // HTTPでRSSのXMLを取得する
        val response = httpGet("https://www.sbbit.jp/rss/HotTopics.rss")

        if (response != null) {
            // 取得に成功したら、パースして返す
            return parseRss(response)
        }

        return null
    }

    // コールバッククラスに返す前に通る処理
    override fun deliverResult(data: Rss?) {
        // 破棄されていたら結果を返さない
        if (isReset || data == null) return

        // 結果をキャッシュする
        cache = data
        super.deliverResult(data)
    }

    // バックグラウンド処理が開始される前に呼ばれる
    override fun onStartLoading() {
        // キャッシュがあるなら、キャッシュを返す
        if (cache != null) {
            deliverResult(cache)
        }

        // コンテンツが変化している場合やキャッシュがない場合には、バックグラウンド処理を行う
        if (takeContentChanged() || cache == null) {
            forceLoad()
        }
    }

    // ローダーが停止する前に呼ばれる処理
    override fun onStopLoading() {
        cancelLoad()
    }

    // ローダーが破棄される前に呼ばれる処理
    override fun onReset() {
        super.onReset()
        onStopLoading()
        cache = null
    }

}

