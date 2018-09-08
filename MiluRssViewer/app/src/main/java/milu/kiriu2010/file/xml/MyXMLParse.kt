package milu.kiriu2010.file.xml

import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.SAXException
import java.io.ByteArrayInputStream
import java.io.IOException
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathExpressionException
import javax.xml.xpath.XPathFactory

class MyXMLParse {
    @Throws(ParserConfigurationException::class, IOException::class, SAXException::class)
    fun str2doc(strXML: String): Document {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        // http://www.baeldung.com/convert-string-to-input-stream
        val inStreamXML = ByteArrayInputStream(strXML.toByteArray())
        val document = builder.parse(inStreamXML)
        inStreamXML.close()
        return document
    }

    @Throws(XPathExpressionException::class)
    fun searchNode(node: Node, strPath: String): Node {
        // https://stackoverflow.com/questions/18576711/how-to-search-for-a-specific-element-in-an-xml-using-a-scanner-in-java?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
        val xPathFactory = XPathFactory.newInstance()
        val xPath = xPathFactory.newXPath()
        val xPathExpr = xPath.compile(strPath)

        return xPathExpr.evaluate(node, XPathConstants.NODE) as Node
    }

    @Throws(XPathExpressionException::class)
    fun searchNodeList(node: Node, strPath: String): NodeList {
        // https://stackoverflow.com/questions/18576711/how-to-search-for-a-specific-element-in-an-xml-using-a-scanner-in-java?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
        val xPathFactory = XPathFactory.newInstance()
        val xPath = xPathFactory.newXPath()
        val xPathExpr = xPath.compile(strPath)

        return xPathExpr.evaluate(node, XPathConstants.NODESET) as NodeList
    }

    @Throws(XPathExpressionException::class)
    fun searchNodeText(node: Node, strPath: String): String {
        // https://stackoverflow.com/questions/18576711/how-to-search-for-a-specific-element-in-an-xml-using-a-scanner-in-java?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
        val xPathFactory = XPathFactory.newInstance()
        val xPath = xPathFactory.newXPath()
        val xPathExpr = xPath.compile(strPath)

        val nodeHit = xPathExpr.evaluate(node, XPathConstants.NODE) as Node
        return nodeHit.nodeValue
    }

}