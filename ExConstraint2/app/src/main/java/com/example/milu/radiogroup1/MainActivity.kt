package com.example.milu.radiogroup1

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.widget.RadioButton
import android.widget.Toast
import com.example.milu.net.HttpGet
import com.example.milu.xml.MyXMLParse
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gender.setOnCheckedChangeListener { radioGroup, checkedId ->
                val radio: RadioButton = findViewById<RadioButton>(checkedId)
                Toast.makeText(applicationContext, "On checked change : ${radio.text}", Toast.LENGTH_LONG).show()
        }

        btnGet.setOnClickListener{
            Log.d(this.javaClass.name,"XXXXXX")
            val httpGetTask = HttpGetTask()
            httpGetTask.execute(URL("https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643123"))
        }

        btnRATE.setOnClickListener{
            val intent = Intent(this,RateActivity::class.java )

            this.startActivityForResult( intent, IntentID2.ID_RATE.value )
        }

        btnRecycle.setOnClickListener{
            val intent = Intent(this,RecycleActivity::class.java )

            this.startActivityForResult( intent, IntentID2.ID_RECYCLE.value )
        }

        // https://stackoverflow.com/questions/26958909/why-is-my-button-text-forced-to-all-caps-on-lollipop
        // large => small caps
        btnANIME.transformationMethod = null
        btnANIME.setOnClickListener{
            val intent = Intent(this,AnimeActivity::class.java )
            this.startActivityForResult( intent, IntentID2.ID_ANIME.value )
        }

        btnSEEK.setOnClickListener {
            val intent = Intent(this,SeekActivity::class.java )
            this.startActivityForResult( intent, IntentID2.ID_SEEK.value )
        }

        btnCM.setOnClickListener {
            val intent = Intent(this,ContextMenuActivity::class.java )
            this.startActivityForResult( intent, IntentID2.ID_CONTEXT_MENU.value )
        }

        btnTemperature.setOnClickListener {
            val intent = Intent(this,TemperatureActivity::class.java )
            this.startActivityForResult( intent, IntentID2.ID_TEMPERATURE.value )
        }

        btnSTOPWATCH.setOnClickListener {
            val intent = Intent(this,StopWatchActivity::class.java )
            this.startActivityForResult( intent, IntentID2.ID_STOP_WATCH.value )
        }

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
