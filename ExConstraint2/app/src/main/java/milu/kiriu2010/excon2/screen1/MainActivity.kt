package milu.kiriu2010.excon2.screen1

import android.content.Intent
import android.content.pm.ApplicationInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.RadioButton
import android.widget.Toast
import milu.kiriu2010.excon2.screen1.animescale.AnimeScaleActivity
import milu.kiriu2010.excon2.screen1.contextmenu.ContextMenuActivity
import milu.kiriu2010.excon2.screen1.customactionbar.CustomActionBarActivity
import milu.kiriu2010.excon2.screen1.dice.DiceRollerActivity
import milu.kiriu2010.excon2.screen1.fibonnaci.FibonnaciActivity
import milu.kiriu2010.excon2.screen1.largebmp.LargeBmpActivity
import milu.kiriu2010.excon2.screen1.pinch.PinchActivity
import milu.kiriu2010.excon2.screen1.rate.RateActivity
import milu.kiriu2010.excon2.screen1.recycler.RecycleActivity
import milu.kiriu2010.excon2.screen1.scan.ScanBarcodeActivity
import milu.kiriu2010.excon2.screen1.seek.SeekActivity
import milu.kiriu2010.excon2.screen1.stopwatch.StopWatchActivity
import milu.kiriu2010.excon2.screen1.temperature.TemperatureActivity
import milu.kiriu2010.excon2.screen1.traffic.TrafficLightActivity
import milu.kiriu2010.excon2.screen1.websearch.WebSearchActivity
import kotlinx.android.synthetic.main.activity_main.*
import milu.kiriu2010.excon2.BuildConfig
import milu.kiriu2010.excon2.R
import milu.kiriu2010.excon2.screen1.animemove.AnimeMoveActivity
import milu.kiriu2010.excon2.screen1.canvas.CanvasActivity
import milu.kiriu2010.excon2.screen1.excel.ExcelActivity
import milu.kiriu2010.excon2.screen1.navibottom.BottomNaviActivity
import milu.kiriu2010.excon2.screen1.navidrawer.NaviDrawerActivity
import milu.kiriu2010.excon2.screen1.sensorlight.SensorLightActivity
import milu.kiriu2010.excon2.screen1.sensorori.SensorOriActivity
import milu.kiriu2010.excon2.screen1.sensorprox.SensorProxActivity
import milu.kiriu2010.excon2.screen1.sensorstep.SensorStepActivity
import milu.kiriu2010.excon2.screen1.sensortemp.SensorTemperatureActivity
import milu.kiriu2010.excon2.screen1.setting.SettingsActivity
import milu.kiriu2010.excon2.screen1.tabbed.TabbedActivity
import milu.kiriu2010.excon2.screen1.text2speech.Text2SpeechActivity
import milu.kiriu2010.excon2.screen1.voiceinput.VoiceInputActivity
import milu.kiriu2010.id.IntentID

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

        if (BuildConfig.DEBUG) {
            tvBuildConf.setText("DEBUG")
        }
        else {
            tvBuildConf.setText("RELEASE")
        }

        gender.setOnCheckedChangeListener { _, checkedId ->
                val radio: RadioButton = findViewById<RadioButton>(checkedId)
                Toast.makeText(applicationContext, "On checked change : ${radio.text}", Toast.LENGTH_LONG).show()
        }

        btnRATE.transformationMethod = null
        btnRATE.setOnClickListener{
            val intent = Intent(this, RateActivity::class.java )

            this.startActivityForResult( intent, IntentID.ID_RATE.value )
        }

        btnRecycle.transformationMethod = null
        btnRecycle.setOnClickListener{
            val intent = Intent(this, RecycleActivity::class.java )

            this.startActivityForResult( intent, IntentID.ID_RECYCLE.value )
        }

        // https://stackoverflow.com/questions/26958909/why-is-my-button-text-forced-to-all-caps-on-lollipop
        // large => small caps
        btnANIME.transformationMethod = null
        btnANIME.setOnClickListener{
            val intent = Intent(this, AnimeScaleActivity::class.java )
            this.startActivityForResult( intent, IntentID.ID_ANIME.value )
        }

        btnAnimeMove.transformationMethod = null
        btnAnimeMove.setOnClickListener {
            val intent = Intent( this, AnimeMoveActivity::class.java )
            this.startActivity(intent)
        }

        // エクセル－アップロード
        btnExcel.transformationMethod = null
        btnExcel.setOnClickListener {
            val intent = Intent(this, ExcelActivity::class.java )
            this.startActivity( intent )
        }

        // コンテキストメニュー
        btnCM.transformationMethod = null
        btnCM.setOnClickListener {
            val intent = Intent(this, ContextMenuActivity::class.java )
            this.startActivityForResult( intent, IntentID.ID_CONTEXT_MENU.value )
        }

        // テキスト→音声変換
        btnText2Speech.transformationMethod = null
        btnText2Speech.setOnClickListener {
            val intent = Intent(this, Text2SpeechActivity::class.java )
            this.startActivity( intent )
        }

        btnSTOPWATCH.transformationMethod = null
        btnSTOPWATCH.setOnClickListener {
            val intent = Intent(this, StopWatchActivity::class.java )
            this.startActivityForResult( intent, IntentID.ID_STOP_WATCH.value )
        }

        btnCustomAction.transformationMethod = null
        btnCustomAction.setOnClickListener {
            val intent = Intent(this, CustomActionBarActivity::class.java )
            this.startActivityForResult( intent, IntentID.ID_CUSTOM_ACTION.value )
        }

        btnDICE.transformationMethod = null
        btnDICE.setOnClickListener {
            val intent = Intent(this, DiceRollerActivity::class.java )
            this.startActivityForResult( intent, IntentID.ID_DICE.value )
        }

        btnLargeBmp.transformationMethod = null
        btnLargeBmp.setOnClickListener {
            val intent = Intent(this, LargeBmpActivity::class.java )
            this.startActivityForResult( intent, IntentID.ID_LARGE_BMP.value )
        }

        btnTrafficLight.transformationMethod = null
        btnTrafficLight.setOnClickListener {
            val intent = Intent(this, TrafficLightActivity::class.java )
            this.startActivityForResult( intent, IntentID.ID_TRAFFIC_LIGHT.value )
        }

        // 音声入力
        btnVoiceInput.transformationMethod = null
        btnVoiceInput.setOnClickListener {
            val intent = Intent(this, VoiceInputActivity::class.java)
            this.startActivity(intent)
        }

        btnFibonnaci.transformationMethod = null
        btnFibonnaci.setOnClickListener {
            val intent = Intent(this, FibonnaciActivity::class.java )
            this.startActivityForResult( intent, IntentID.ID_FIBONNACI.value )
        }

        btnScanBarcode.transformationMethod = null
        btnScanBarcode.setOnClickListener {
            val intent = Intent(this, ScanBarcodeActivity::class.java )
            this.startActivityForResult( intent, IntentID.ID_SCAN_BARCODE.value )
        }

        btnWebSearch.transformationMethod = null
        btnWebSearch.setOnClickListener {
            val intent = Intent(this, WebSearchActivity::class.java )
            this.startActivityForResult( intent, IntentID.ID_WEB_SEARCH.value )
        }

        // ピンチ　イン・アウト
        btnPinch.transformationMethod = null
        btnPinch.setOnClickListener {
            val intent = Intent( this, PinchActivity::class.java )
            this.startActivity( intent )
        }

        // キャンバス
        btnCanvas.transformationMethod = null
        btnCanvas.setOnClickListener {
            val intent = Intent(this,CanvasActivity::class.java)
            this.startActivity(intent)
        }

        // Tabbed
        btnTabbed.transformationMethod = null
        btnTabbed.setOnClickListener {
            val intent = Intent(this, TabbedActivity::class.java)
            this.startActivity(intent)
        }

        // Bottom Navigation
        btnBottomNavi.transformationMethod = null
        btnBottomNavi.setOnClickListener {
            val intent = Intent( this, BottomNaviActivity::class.java)
            this.startActivity(intent)
        }

        // Navigation Drawer
        btnNaviDrawer.transformationMethod = null
        btnNaviDrawer.setOnClickListener {
            val intent = Intent( this, NaviDrawerActivity::class.java)
            this.startActivity(intent)
        }

        // 設定
        btnSetting.transformationMethod = null
        btnSetting.setOnClickListener {
            val intent = Intent( this, SettingsActivity::class.java)
            this.startActivity(intent)
        }

        // 温度センサ
        btnSensorTemperature.setOnClickListener {
            val intent = Intent( this, SensorTemperatureActivity::class.java )
            this.startActivity( intent )
        }

        // 照度センサ
        btnSensorLight.setOnClickListener {
            val intent = Intent( this, SensorLightActivity::class.java)
            this.startActivity(intent)
        }

        // 歩行センサ
        btnSensorStep.setOnClickListener {
            val intent = Intent( this, SensorStepActivity::class.java)
            this.startActivity(intent)
        }

        // 近接センサ
        btnSensorProx.setOnClickListener {
            val intent = Intent( this, SensorProxActivity::class.java)
            this.startActivity(intent)
        }

        // 傾きセンサ
        btnSensorOri.setOnClickListener {
            val intent = Intent( this, SensorOriActivity::class.java)
            this.startActivity(intent)
        }
    }

    // -------------------------------------------------------------------
    // Inflating the Menu Into the Android ActionBar
    // -------------------------------------------------------------------
    // https://www.journaldev.com/9357/android-actionbar-example-tutorial
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_journaldev, menu )
        return true
    }

    // -------------------------------------------------------------------
    // Responding to Android Action Bar Events
    // -------------------------------------------------------------------
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            // 温度(C<=>F)
            R.id.menuCF -> {
                val intent = Intent(this, TemperatureActivity::class.java )
                this.startActivityForResult( intent, IntentID.ID_TEMPERATURE.value )
                true
            }
            // メール送信
            // http://tekeye.uk/android/examples/email-contact-form-in-app
            R.id.menuEMAIL -> {
                val strTo = "milu.kiriu2010@gmail.com"
                val strSub = "test"
                val strMsg = "ExConstraint2"
                val mail = Intent(Intent.ACTION_SEND)
                mail.putExtra(Intent.EXTRA_EMAIL,arrayOf<String>(strTo))
                mail.putExtra(Intent.EXTRA_SUBJECT,strSub)
                mail.putExtra(Intent.EXTRA_TEXT,strMsg)
                mail.setType("message/rfc822")
                startActivity(Intent.createChooser(mail,"Send email via:"))
                true
            }
            R.id.menuRESET -> {
                Toast.makeText(this,"Reset is clicked",Toast.LENGTH_SHORT).show()
                true
            }
            // Seekバー
            R.id.menuSEEK -> {
                val intent = Intent(this, SeekActivity::class.java )
                this.startActivityForResult( intent, IntentID.ID_SEEK.value )
                true
            }
            R.id.menuEXIT -> {
                finish()
                true
            }
            else ->  super.onOptionsItemSelected(item!!)
        }
    }

}
