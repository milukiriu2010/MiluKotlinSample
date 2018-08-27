package milu.kiriu2010.rss

import android.util.Log
import milu.kiriu2010.entity.Article
import milu.kiriu2010.entity.Rss
import milu.kiriu2010.xml.MyXMLParse
import org.w3c.dom.Document
import org.xml.sax.SAXException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.parsers.ParserConfigurationException

class MyRssParse {
    val myXMLParse = MyXMLParse()

    @Throws(ParserConfigurationException::class,IOException::class,SAXException::class)
    fun str2rss( strXML: String ): Rss {
        // -------------------------------------------------------
        // 文字列XMLからDOMオブジェクトに変換する
        // -------------------------------------------------------
        val xmlRoot = this.str2doc(strXML)

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
        // RSSのpubDateを取得
        // -------------------------------------------------------
        val pubDateNode = myXMLParse.searchNode( xmlRoot, "/rss/channel/pubDate/text()")
        Log.d( javaClass.simpleName, "pubDate[$pubDateNode.nodeValue]")
        // RSS2.0の日付書式である、RFC1123をDate型に変換するためのオブジェクト
        // Fri, 24 Aug 2018 07:10:00 +0900
        val formatter = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
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
            // 記事のpubDateを取得
            // -------------------------------------------------------
            val itemPubDateNode = myXMLParse.searchNode( itemNode, "./pubDate/text()" )
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

    // -------------------------------------------------------
    // 文字列XMLからDOMオブジェクトに変換する
    // -------------------------------------------------------
    @Throws(ParserConfigurationException::class,IOException::class,SAXException::class)
    private fun str2doc( strXML: String ): Document {
        val xmlDoc = myXMLParse.str2doc(strXML)
        return xmlDoc
    }
}