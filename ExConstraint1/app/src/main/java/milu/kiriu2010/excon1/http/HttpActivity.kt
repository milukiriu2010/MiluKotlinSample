package milu.kiriu2010.excon1.http

import android.app.Activity
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import milu.kiriu2010.excon1.R
import milu.kiriu2010.net.HttpGet
import milu.kiriu2010.xml.MyXMLParse
import kotlinx.android.synthetic.main.activity_http.*
import java.net.URL

class HttpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_http)

        btnBack.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }

        btnGetSync.setOnClickListener{
            // https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643123
            val httpGetTask = HttpGetTask()
            httpGetTask.execute( URL("https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643123") )
            val strGet = httpGetTask.get()
            Log.w("HttpGetTask:",strGet)

            if ( strGet == null ) {
                txtGet.text = "<NULL>"
            }
            else {
                val myXmlParse = MyXMLParse()
                val xmlDoc = myXmlParse.str2doc(strGet)
                val nodeListItem = myXmlParse.searchNodeList( xmlDoc, "//rss/channel/item")

                val titleLst = mutableListOf<String>()
                for ( i in 0 until nodeListItem.length ) {
                    val nodeItem = nodeListItem.item(i)
                    val title = myXmlParse.searchNodeText( nodeItem, "./title/text()")
                    titleLst.add(title)
                }

                txtGet.text = titleLst.joinToString( separator="\n", prefix = "DOM\n" )
            }

        }

        txtGet.text = ""

    }

    class HttpGetTask: AsyncTask<URL, Unit, String>() {

        override fun doInBackground(vararg params: URL?): String {
            Log.d(this.javaClass.toString(), "HttpGetTask::doInBackground start." )
            val httpGet = HttpGet()
            val strGet = httpGet.doGet(params[0]!!)
            Log.d(this.javaClass.toString(), "HttpGetTask::doInBackground strGet=[${strGet}]" )
            return strGet
        }

        override fun onPostExecute(result: String?) {
            Log.d(this.javaClass.toString(), "HttpGetTask::onPostExecute start." )
            Log.d(this.javaClass.toString(), "Result:${result}" )

            /*
            val text = findViewById(R.id.txtGet) as TextView

            if ( result == null )
            {
                HttpActivity.txtGet
                return
            }

            val myXmlParse = MyXMLParse()
            val xmlDoc = myXmlParse.str2doc(result)
            val nodeListItem = myXmlParse.searchNodeList( xmlDoc, "//rss/channel/item")
            */
            super.onPostExecute(result)
        }
    }

}