package milu.kiriu2010.rss

import milu.kiriu2010.entity.Rss
import milu.kiriu2010.xml.MyXMLParse
import org.w3c.dom.Document
import org.xml.sax.SAXException
import java.io.IOException
import javax.xml.parsers.ParserConfigurationException

abstract class MyRssParseRssAbs {
    // XMLパース
    lateinit var myXMLParse: MyXMLParse
    // XMLルートドキュメント
    lateinit var xmlRoot: Document

    @Throws(ParserConfigurationException::class, IOException::class, SAXException::class)
    abstract fun analyze(): Rss
}
