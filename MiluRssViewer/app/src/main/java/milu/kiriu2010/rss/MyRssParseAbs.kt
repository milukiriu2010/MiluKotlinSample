package milu.kiriu2010.rss

import milu.kiriu2010.entity.Rss
import milu.kiriu2010.xml.MyXMLParse
import org.w3c.dom.Document

abstract class MyRssParseAbs {
    abstract fun analyze(xmlRoot: Document, myXMLParse: MyXMLParse): Rss
}
