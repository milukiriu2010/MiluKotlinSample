package com.example.milu.radiogroup1

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.milu.net.HttpGet
import com.example.milu.xml.MyXMLParse
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class MainActivity : AppCompatActivity() {
    private val ID_RECYCLE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        gender.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { radioGroup, checkedId ->
                val radio: RadioButton = findViewById(checkedId) as RadioButton
                Toast.makeText(applicationContext, "On checked change : ${radio.text}", Toast.LENGTH_LONG).show()
            })

        btnGet.setOnClickListener{
            Log.d(this.javaClass.name,"XXXXXX")
            val httpGetTask = HttpGetTask()
            httpGetTask.execute(URL("https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643123"))
        }

        btnDOM.setOnClickListener{
            Log.d(this.javaClass.name, this.loadXMLbyDOM() )
        }

        btnRecycle.setOnClickListener{
            val intent = Intent(this,RecycleActivity::class.java )

            this.startActivityForResult( intent, this.ID_RECYCLE )
        }
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

    class HttpGetTask: AsyncTask<URL, Unit, String>() {

        override fun doInBackground(vararg params: URL?): String {
            Log.d("b1", "XXXXXXX")
            val httpGet = HttpGet()
            val strGet = httpGet.doGet(params[0]!!)
            Log.d("b1", strGet)
            return strGet
        }

        override fun onPostExecute(result: String?) {
            Log.d("b2",result)
            super.onPostExecute(result)
        }
    }

}
