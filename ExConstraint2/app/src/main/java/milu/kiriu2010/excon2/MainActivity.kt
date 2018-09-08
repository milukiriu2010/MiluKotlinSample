package milu.kiriu2010.excon2

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.RadioButton
import android.widget.Toast
import milu.kiriu2010.excon2.anime.AnimeActivity
import milu.kiriu2010.excon2.contextmenu.ContextMenuActivity
import milu.kiriu2010.excon2.customactionbar.CustomActionBarActivity
import milu.kiriu2010.excon2.dice.DiceRollerActivity
import milu.kiriu2010.excon2.fibonnaci.FibonnaciActivity
import milu.kiriu2010.excon2.largebmp.LargeBmpActivity
import milu.kiriu2010.excon2.pinch.PinchActivity
import milu.kiriu2010.excon2.rate.RateActivity
import milu.kiriu2010.excon2.recycler.RecycleActivity
import milu.kiriu2010.excon2.scan.ScanBarcodeActivity
import milu.kiriu2010.excon2.seek.SeekActivity
import milu.kiriu2010.excon2.stopwatch.StopWatchActivity
import milu.kiriu2010.excon2.temperature.TemperatureActivity
import milu.kiriu2010.excon2.traffic.TrafficLightActivity
import milu.kiriu2010.excon2.websearch.WebSearchActivity
import milu.kiriu2010.net.HttpGet
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // http://tekeye.uk/android/examples/android-debug-vs-release-build
        // check debug/release mode
        //   not 0 => debug
        //   0     => release
        Log.d( javaClass.simpleName, "Application Debug/Release:" + ( applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE ) )
        if ( ( applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE ) != 0 ) {
            tvAppInfo.setText("DEBUG")
        }
        else {
            tvAppInfo.setText("RELEASE")
        }

        if ( BuildConfig.DEBUG ) {
            tvBuildConf.setText("DEBUG")
        }
        else {
            tvBuildConf.setText("RELEASE")
        }

        gender.setOnCheckedChangeListener { radioGroup, checkedId ->
                val radio: RadioButton = findViewById<RadioButton>(checkedId)
                Toast.makeText(applicationContext, "On checked change : ${radio.text}", Toast.LENGTH_LONG).show()
        }

        btnRATE.transformationMethod = null
        btnRATE.setOnClickListener{
            val intent = Intent(this, RateActivity::class.java )

            this.startActivityForResult( intent, IntentID2.ID_RATE.value )
        }

        btnRecycle.transformationMethod = null
        btnRecycle.setOnClickListener{
            val intent = Intent(this, RecycleActivity::class.java )

            this.startActivityForResult( intent, IntentID2.ID_RECYCLE.value )
        }

        // https://stackoverflow.com/questions/26958909/why-is-my-button-text-forced-to-all-caps-on-lollipop
        // large => small caps
        btnANIME.transformationMethod = null
        btnANIME.setOnClickListener{
            val intent = Intent(this, AnimeActivity::class.java )
            this.startActivityForResult( intent, IntentID2.ID_ANIME.value )
        }

        btnSEEK.transformationMethod = null
        btnSEEK.setOnClickListener {
            val intent = Intent(this, SeekActivity::class.java )
            this.startActivityForResult( intent, IntentID2.ID_SEEK.value )
        }

        btnCM.transformationMethod = null
        btnCM.setOnClickListener {
            val intent = Intent(this, ContextMenuActivity::class.java )
            this.startActivityForResult( intent, IntentID2.ID_CONTEXT_MENU.value )
        }

        btnTemperature.transformationMethod = null
        btnTemperature.setOnClickListener {
            val intent = Intent(this, TemperatureActivity::class.java )
            this.startActivityForResult( intent, IntentID2.ID_TEMPERATURE.value )
        }

        btnSTOPWATCH.transformationMethod = null
        btnSTOPWATCH.setOnClickListener {
            val intent = Intent(this, StopWatchActivity::class.java )
            this.startActivityForResult( intent, IntentID2.ID_STOP_WATCH.value )
        }

        btnCustomAction.transformationMethod = null
        btnCustomAction.setOnClickListener {
            val intent = Intent(this, CustomActionBarActivity::class.java )
            this.startActivityForResult( intent, IntentID2.ID_CUSTOM_ACTION.value )
        }

        btnDICE.transformationMethod = null
        btnDICE.setOnClickListener {
            val intent = Intent(this, DiceRollerActivity::class.java )
            this.startActivityForResult( intent, IntentID2.ID_DICE.value )
        }

        btnLargeBmp.transformationMethod = null
        btnLargeBmp.setOnClickListener {
            val intent = Intent(this, LargeBmpActivity::class.java )
            this.startActivityForResult( intent, IntentID2.ID_LARGE_BMP.value )
        }

        btnTrafficLight.transformationMethod = null
        btnTrafficLight.setOnClickListener {
            val intent = Intent(this, TrafficLightActivity::class.java )
            this.startActivityForResult( intent, IntentID2.ID_TRAFFIC_LIGHT.value )
        }

        // http://tekeye.uk/android/examples/email-contact-form-in-app
        btnEMAIL.transformationMethod = null
        btnEMAIL.setOnClickListener {
            val strTo = "milu.kiriu2010@gmail.com"
            val strSub = "test"
            val strMsg = "ExConstraint2"
            val mail = Intent(Intent.ACTION_SEND)
            mail.putExtra(Intent.EXTRA_EMAIL,arrayOf<String>(strTo))
            mail.putExtra(Intent.EXTRA_SUBJECT,strSub)
            mail.putExtra(Intent.EXTRA_TEXT,strMsg)
            mail.setType("message/rfc822")
            startActivity(Intent.createChooser(mail,"Send email via:"))
        }

        btnFibonnaci.transformationMethod = null
        btnFibonnaci.setOnClickListener {
            val intent = Intent(this, FibonnaciActivity::class.java )
            this.startActivityForResult( intent, IntentID2.ID_FIBONNACI.value )
        }

        btnScanBarcode.transformationMethod = null
        btnScanBarcode.setOnClickListener {
            val intent = Intent(this, ScanBarcodeActivity::class.java )
            this.startActivityForResult( intent, IntentID2.ID_SCAN_BARCODE.value )
        }

        btnWebSearch.transformationMethod = null
        btnWebSearch.setOnClickListener {
            val intent = Intent(this, WebSearchActivity::class.java )
            this.startActivityForResult( intent, IntentID2.ID_WEB_SEARCH.value )
        }

        btnPinch.transformationMethod = null
        btnPinch.setOnClickListener {
            val intent = Intent( this, PinchActivity::class.java )
            this.startActivity( intent )
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