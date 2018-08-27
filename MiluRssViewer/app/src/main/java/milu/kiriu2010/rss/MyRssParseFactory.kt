package milu.kiriu2010.rss

import android.util.Log
import milu.kiriu2010.xml.MyXMLParse
import org.xml.sax.SAXException
import java.io.IOException
import javax.xml.parsers.ParserConfigurationException

object MyRssParseFactory {
    @Throws(ParserConfigurationException::class, IOException::class, SAXException::class)
    fun createInstance( strXML: String ): MyRssParseAbs? {
        val myXMLParse = MyXMLParse()
        val xmlDoc = myXMLParse.str2doc(strXML)
        // ルート要素
        val rootNode = xmlDoc.documentElement
        // ルート要素の属性
        val attrMap = mutableMapOf<String,String>()
        for ( i in 0 until rootNode.attributes.length ) {
            val attr = rootNode.attributes.item(i)
            Log.d( javaClass.simpleName, "rootAttr[$i][${attr.nodeName}][${attr.nodeValue}]")
            attrMap.put( attr.nodeName, attr.nodeValue )
        }

        var myRssParseAbs: MyRssParseAbs? = null
        /// ルート要素のタグ名によって、処理を振り分ける
        when ( rootNode.nodeName ) {
            // RSS2.0
            // RSS0.91
            "rss" -> {
                myRssParseAbs = analyzeRss( attrMap )
            }
            // RSS1.0
            "rdf:RDF" ->{

            }
            else ->{
                return myRssParseAbs
            }
        }

        return myRssParseAbs
    }

    fun analyzeRss( attrMap: MutableMap<String,String> ): MyRssParseAbs? {
        val version: String? = attrMap["version"]
        val xmlnsDC: String? = attrMap["xmlns:dc"]

        var myRssParseAbs: MyRssParseAbs? = null

        myRssParseAbs =
            when ( version ) {
                "2.0" -> {
                    when ( xmlnsDC ) {
                        "http://purl.org/dc/elements/1.1/" -> MyRssParseRss2M0N11()
                        else -> MyRssParseRss2M0()
                    }
                }
                "0.91" -> MyRssParseRss2M0()
                else -> null
            }

        return myRssParseAbs
    }
}