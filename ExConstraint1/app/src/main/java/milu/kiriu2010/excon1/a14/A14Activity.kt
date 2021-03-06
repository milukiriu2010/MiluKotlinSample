package milu.kiriu2010.excon1.a14

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import milu.kiriu2010.excon1.R
import milu.kiriu2010.xml.MyXMLParse
import kotlinx.android.synthetic.main.activity_a14.*
import org.xmlpull.v1.XmlPullParser

// リソースのXMLファイルを読み込む
class A14Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a14)

        // SAX方式でXMLを読み込む
        btnA14SAX.setOnClickListener{
            tvA14.text = this.loadXMLbySAX()
        }

        // DOM方式でXMLを読み込む
        btnA14DOM.setOnClickListener{
            tvA14.text = this.loadXMLbyDOM()
        }

    }

    private fun loadXMLbySAX() : String {
        val foodLst = mutableListOf<String>()
        val xpp = this.resources.getXml(R.xml.xml_a14)
        xpp.next()
        var eventType = xpp.eventType
        var foodSection = false
        while ( eventType != XmlPullParser.END_DOCUMENT ) {
            when {
                ( eventType == XmlPullParser.START_TAG ) && ( xpp.name.equals("name") ) -> foodSection = true
                ( eventType == XmlPullParser.END_TAG   ) && ( foodSection == true )     -> foodSection = false
                ( eventType == XmlPullParser.TEXT      ) && ( foodSection == true )     -> foodLst.add(xpp.text)
            }
            eventType = xpp.next()
        }

        return foodLst.joinToString(separator = "\n", prefix = "SAX\n")

        //return foodLst.joinToString("\n")

        //return foodLst.stream().collect(Collectors.joining("\n") )
    }

    private fun loadXMLbyDOM(): String{
        Log.v( this.javaClass.toString(), "loadXMLbyDOM start." )
        val myXmlParse = MyXMLParse()
        val istream = this.resources.openRawResource(R.raw.myxml)
        /*
        val br = BufferedReader(InputStreamReader(istream))

        var line: String? = null
        val sb = StringBuffer()
        while ( { line = br.readLine(); line } != null ){
            Log.e( this.javaClass.toString(), line )
            sb.append(line)
        }
        */

        val sb = StringBuffer()
        // https://stackoverflow.com/questions/41000584/best-way-to-use-bufferedreader-in-kotlin
        /*
        istream.bufferedReader().useLines { it.map { line -> sb.append(line) } }
        Log.v( this.javaClass.toString(), "=== XML content ================" )
        Log.v( this.javaClass.toString(), sb.toString() )
        Log.v( this.javaClass.toString(), "================================" )
        */
        Log.v( this.javaClass.toString(), "=== XML content ================" )
        val reader = istream.bufferedReader()
        val iterator = reader.lineSequence().iterator()
        while ( iterator.hasNext() ) {
            sb.append( iterator.next() )
        }
        Log.v( this.javaClass.toString(), sb.toString() )
        Log.v( this.javaClass.toString(), "================================" )

        val xmlDoc = myXmlParse.str2doc(sb.toString())
        val nodeListTitle = myXmlParse.searchNodeList( xmlDoc, "//PreferenceScreen/food/name")

        val foodLst = mutableListOf<String>()
        for ( i in 0 until nodeListTitle.length){
            val nodeTitle = nodeListTitle.item(i)
            val title = myXmlParse.searchNodeText( nodeTitle, "./text()")
            foodLst.add(title)
        }

        return foodLst.joinToString(separator = "\n", prefix = "DOM\n")
    }
}
