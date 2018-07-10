package com.example.milu.intent2

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.milu.intent2.net.HttpGet
import kotlinx.android.synthetic.main.activity_user_add.*
import com.example.milu.intent2.net.HttpGetTask
import java.net.URL
import android.os.StrictMode
import com.example.milu.intent2.xml.MyXMLParse
import org.xmlpull.v1.XmlPullParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.stream.Collectors


class UserAddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_add)

        val strFirstName = intent.getStringExtra("firstName") ?: "<NULL>"
        lblFirstName.text = strFirstName
        Toast.makeText(this, strFirstName, Toast.LENGTH_LONG )
        Log.d("bXXXXXXXXXXXX:", strFirstName )

        btnBack.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }

        btnGetAsync.setOnClickListener{
            // https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643123
            val httpGetTask = HttpGetTask()
            httpGetTask.execute( URL("https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643123") )
            val strGet = httpGetTask.get()
            Log.w("HttpGetTask:",strGet)
        }

        btnGetNosync.setOnClickListener{
            if (android.os.Build.VERSION.SDK_INT > 9) {
                val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                StrictMode.setThreadPolicy(policy)
            }
            val httpGet = HttpGet()
            val strGet = httpGet.doGet(URL("https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643123"))
            Log.w("HttpGet:",strGet)
        }

        btnSAX.setOnClickListener{
            textXML.text = this.loadXMLbySAX()
        }

        btnDOM.setOnClickListener{
            textXML.text = this.loadXMLbyDOM()
        }
    }

    private fun loadXML(): String{
        val sb = StringBuffer()
        val xpp = this.resources.getXml(R.xml.myxml)
        xpp.next()
        var eventType = xpp.eventType
        while ( eventType != XmlPullParser.END_DOCUMENT ) {
            when (eventType){
                XmlPullParser.START_DOCUMENT -> sb.append("--- Start XML ---")
                XmlPullParser.START_TAG      -> sb.append("\nSTART_TAG: " + xpp.name )
                XmlPullParser.END_TAG        -> sb.append("\nEND_TAG  : " + xpp.name )
                XmlPullParser.TEXT           -> sb.append("\nTEXT     : " + xpp.text )
            }
            eventType = xpp.next()
        }
        sb.append("\n--- End   XML ---")
        return sb.toString()
    }

    private fun loadXMLbySAX() : String {
        val foodLst = mutableListOf<String>()
        val xpp = this.resources.getXml(R.xml.myxml)
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
        val myXmlParse = MyXMLParse()
        val istream = this.resources.openRawResource(R.raw.myxml)
        val br = BufferedReader(InputStreamReader(istream))

        var line: String? = null
        val sb = StringBuffer()
        while ( { line = br.readLine(); line } != null ){
            sb.append(line)
        }

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
