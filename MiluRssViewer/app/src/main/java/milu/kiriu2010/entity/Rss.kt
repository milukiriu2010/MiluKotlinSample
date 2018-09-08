package milu.kiriu2010.entity

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.content.AsyncTaskLoader
import android.util.Log
import milu.kiriu2010.net.httpGet
import org.w3c.dom.NodeList
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

// RSSの各記事を表すデータクラス
data class Article( val title: String, val link: String, val pubDate: Date ): Parcelable {
    constructor(parcel: Parcel) : this(
            // title
            parcel.readString() ?: "",
            // link
            parcel.readString() ?: "",
            // pubDate
            Date(parcel.readLong())) {
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.let {
            it.writeString(title)
            it.writeString(link)
            it.writeLong(pubDate.time)
        }
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Article> {
        override fun createFromParcel(parcel: Parcel): Article {
            return Article(parcel)
        }

        override fun newArray(size: Int): Array<Article?> {
            return arrayOfNulls(size)
        }
    }

}

// RSSを表現するデータクラス
data class Rss( val title: String, val pubDate: Date, val articles: MutableList<Article> ): Parcelable {
    constructor(parcel: Parcel) : this(
            // title
            parcel.readString()  ?: "",
            // pubDate
            Date(parcel.readLong()),
            // https://haruue.moe/blog/2017/12/22/Kotlin-and-Android-Parcelable/
            parcel.readArrayList(Article::class.java.classLoader) as MutableList<Article>) {
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.let {
            it.writeString(title)
            it.writeLong(pubDate.time)
            it.writeList(articles)
        }
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Rss> {
        override fun createFromParcel(parcel: Parcel): Rss {
            return Rss(parcel)
        }

        override fun newArray(size: Int): Array<Rss?> {
            return arrayOfNulls(size)
        }
    }

}


// RSS2.0をパースしてRssオブジェクトに変換する
fun parseRss(stream: InputStream) : Rss {

    // XMLをDOMオブジェクトに変換する
    val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream)
    stream.close()

    // XPathを生成する
    val xPath = XPathFactory.newInstance().newXPath()

    // RSS2.0の日付書式である、RFC1123をDate型に変換するためのオブジェクト
    // Fri, 24 Aug 2018 07:10:00 +0900
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

