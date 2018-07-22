package com.example.milu.intent2

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.milu.net.HttpGet
import kotlinx.android.synthetic.main.activity_user_add.*
import com.example.milu.net.HttpGetTask
import java.net.URL
import android.os.StrictMode
import com.example.milu.xml.MyXMLParse
import org.xmlpull.v1.XmlPullParser
import java.io.BufferedReader
import java.io.InputStreamReader


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
    }

}
