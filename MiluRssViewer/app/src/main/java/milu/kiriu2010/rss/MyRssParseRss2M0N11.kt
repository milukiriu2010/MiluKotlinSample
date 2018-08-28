package milu.kiriu2010.rss

import android.util.Log
import milu.kiriu2010.entity.Article
import milu.kiriu2010.entity.Rss
import java.text.SimpleDateFormat
import java.util.*

// https://gigazine.net/news/rss_2.0/
// rss2.0にrss1.0が混じってる気がする
//   rss2.0 pubDate
//   rss1.0 dc:date
class MyRssParseRss2M0N11: MyRssParseRssAbs() {
    override fun analyze(): Rss {
        // -------------------------------------------------------
        // DOMのルートタグ名を取得
        // -------------------------------------------------------
        val rootNode = xmlRoot.documentElement
        Log.d( javaClass.simpleName, "root[${rootNode.nodeName}][${rootNode.nodeValue}]")

        for ( i in 0 until rootNode.attributes.length ) {
            val attr = rootNode.attributes.item(i)
            Log.d( javaClass.simpleName, "rootAttr[$i][${attr.nodeName}][${attr.nodeValue}]")
        }


        // -------------------------------------------------------
        // RSSのtitleを取得
        // -------------------------------------------------------
        val titleNode = myXMLParse.searchNode( xmlRoot, "/rss/channel/title/text()")
        Log.d( javaClass.simpleName, "title[${titleNode.nodeValue}]")

        // -------------------------------------------------------
        // RSSのpubDate(dc:date)を取得
        // -------------------------------------------------------
        val pubDateNode = myXMLParse.searchNode( xmlRoot, "/rss/channel/*[name()='dc:date']/text()")
        Log.d( javaClass.simpleName, "pubDate[$pubDateNode.nodeValue]")
        // RSS1.0の日付書式である、ISO8601+RFC3339をDate型に変換するためのオブジェクト
        // 2018-08-28-28T19:00:00+09:00
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.US)
        // RSSのpubDateをDate型に変換
        val pubDate = formatter.parse(pubDateNode.nodeValue)

        // -------------------------------------------------------
        // RSSフィード内の記事の一覧
        // -------------------------------------------------------
        val articles = mutableListOf<Article>()

        // -------------------------------------------------------
        // RSSの記事(<item>要素)をすべて取得
        // -------------------------------------------------------
        val itemNodeList = myXMLParse.searchNodeList( xmlRoot, "/rss/channel//item" )

        // -------------------------------------------------------
        // RSSの記事(<item>要素)ごとにループ
        // -------------------------------------------------------
        for ( i in 0 until itemNodeList.length ) {
            val itemNode = itemNodeList.item(i)

            // -------------------------------------------------------
            // 記事のtitleを取得
            // -------------------------------------------------------
            val itemTitleNode = myXMLParse.searchNode( itemNode, "./title/text()" )
            // -------------------------------------------------------
            // 記事のlinkを取得
            // -------------------------------------------------------
            val itemLinkNode = myXMLParse.searchNode( itemNode, "./link/text()" )
            // -------------------------------------------------------
            // 記事のpubDate(dc:date)を取得
            // -------------------------------------------------------
            val itemPubDateNode = myXMLParse.searchNode( itemNode, "./*[name()='dc:date']/text()" )
            Log.d( javaClass.simpleName, "=============================================")
            Log.d( javaClass.simpleName, "itemTitle[${itemTitleNode.nodeValue}]")
            Log.d( javaClass.simpleName, "itemLink[${itemLinkNode.nodeValue}]")
            Log.d( javaClass.simpleName, "itemPubDate[${itemPubDateNode.nodeValue}]")
            Log.d( javaClass.simpleName, "=============================================")

            val article = Article(
                    itemTitleNode.nodeValue,
                    itemLinkNode.nodeValue,
                    formatter.parse(itemPubDateNode.nodeValue)
            )

            articles.add(article)
        }

        // RSSオブジェクトを生成
        val rss = Rss(
                titleNode.nodeValue,
                pubDate,
                articles
        )

        return rss
    }
}