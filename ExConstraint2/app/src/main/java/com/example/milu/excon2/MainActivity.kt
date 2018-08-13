package com.example.milu.excon2

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.RadioButton
import android.widget.Toast
import com.example.milu.net.HttpGet
import kotlinx.android.synthetic.main.activity_main.*
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

        btnCustomAction.transformationMethod = null
        btnCustomAction.setOnClickListener {
            val intent = Intent(this,CustomActionBarActivity::class.java )
            this.startActivityForResult( intent, IntentID2.ID_CUSTOM_ACTION.value )
        }

        btnDICE.transformationMethod = null
        btnDICE.setOnClickListener {
            val intent = Intent(this,DiceRollerActivity::class.java )
            this.startActivityForResult( intent, IntentID2.ID_DICE.value )
        }

    }

    // -------------------------------------------------------------------
    // Inflating the Menu Into the Android ActionBar
    // -------------------------------------------------------------------
    // https://www.journaldev.com/9357/android-actionbar-example-tutorial
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //return super.onCreateOptionsMenu(menu)
        menuInflater.inflate( R.menu.menu_journaldev, menu )
        return true
    }

    // -------------------------------------------------------------------
    // Responding to Android Action Bar Events
    // -------------------------------------------------------------------
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.menuADD    -> {
                Toast.makeText(this,"Add is clicked",Toast.LENGTH_LONG).show()
                true
            }
            R.id.menuRESET  -> {
                Toast.makeText(this,"Reset is clicked",Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menuABOUT  -> {
                Toast.makeText(this,"About is clicked",Toast.LENGTH_LONG).show()
                true
            }
            R.id.menuEXIT   -> {
                finish()
                true
            }
            else ->  super.onOptionsItemSelected(item)
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
