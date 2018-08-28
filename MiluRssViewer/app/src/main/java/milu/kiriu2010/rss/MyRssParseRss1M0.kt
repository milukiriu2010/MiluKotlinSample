package milu.kiriu2010.rss

import android.util.Log
import milu.kiriu2010.entity.Article
import milu.kiriu2010.entity.Rss
import java.text.SimpleDateFormat
import java.util.*

// view-source:http://blog.livedoor.jp/dqnplus/index.rdf
// rss1.0
class MyRssParseRss1M0: MyRssParseRssAbs() {
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
        val titleNode = myXMLParse.searchNode( xmlRoot, "/*[name()='rdf:RDF']/channel/title/text()")
        Log.d( javaClass.simpleName, "title[${titleNode.nodeValue}]")

        // -------------------------------------------------------
        // RSSのpubDateを取得
        // RSS1.0では、ない項目かも
        // -------------------------------------------------------
        //val pubDateNode = myXMLParse.searchNode( xmlRoot, "/*[name()='rdf:RDF']/channel/pubDate/text()")
        //Log.d( javaClass.simpleName, "pubDate[$pubDateNode.nodeValue]")
        // RSS1.0の日付書式である、ISO8601+RFC3339をDate型に変換するためのオブジェクト
        // 2018-08-28-28T19:00:00+09:00
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.US)
        //// RSSのpubDateをDate型に変換
        //val pubDate = formatter.parse(pubDateNode.nodeValue)
        val pubDate = Date()

        // -------------------------------------------------------
        // RSSフィード内の記事の一覧
        // -------------------------------------------------------
        val articles = mutableListOf<Article>()

        // -------------------------------------------------------
        // RSSの記事(<item>要素)をすべて取得
        // -------------------------------------------------------
        val itemNodeList = myXMLParse.searchNodeList( xmlRoot, "/*[name()='rdf:RDF']//item" )

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