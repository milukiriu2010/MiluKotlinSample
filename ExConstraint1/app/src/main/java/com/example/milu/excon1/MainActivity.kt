package com.example.milu.excon1

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import kotlinx.android.synthetic.main.activity_main.*
import com.example.milu.abc.AppConst

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.setTextFirstName()

        this.setAction()
    }

    private fun setTextFirstName(){
        // https://techacademy.jp/magazine/4773
        //lblFirstName.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
        val pref: SharedPreferences = getApplicationContext().getSharedPreferences(AppConst.PREF_USER_FIRST_NAME.toString(), Context.MODE_PRIVATE)
        val firstName = pref.getString( AppConst.KEY_USER_FIRST_NAME.toString(), "" )
        txtFirstName.setText(firstName)
    }


    private fun setAction() {
        btnAddUser.setOnClickListener{
            val intent = Intent( this, UserAddActivity::class.java )
            val strFirstName = txtFirstName.text ?: "<arere>"
            Log.d(this.javaClass.name, strFirstName.toString() )
            intent.putExtra("firstName", strFirstName.toString() )

            val pref: SharedPreferences = getApplicationContext().getSharedPreferences(AppConst.PREF_USER_FIRST_NAME.toString(), Context.MODE_PRIVATE)
            val editor : Editor = pref.edit()
            editor.putString( AppConst.KEY_USER_FIRST_NAME.toString(), strFirstName.toString() )
            editor.apply()

            startActivityForResult( intent, IntentID.ID_USER_ADD.value )
        }

        // http://www.vogella.com/tutorials/AndroidIntent/article.html
        // start browser
        btnBrowse.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://sourceforge.net/projects/miludbviewer/files/?source=navbar")
            startActivity(intent)
        }

        btnImage.setOnClickListener{
            val intent = Intent( this, ImageActivity::class.java )
            startActivityForResult( intent, IntentID.ID_IMAGE.value )
        }

        btnTeamLst.setOnClickListener {
            val intent = Intent( this, TeamListActivity::class.java )
            startActivityForResult( intent, IntentID.ID_TEAM_LIST.value )
        }

        btnXML.setOnClickListener {
            val intent = Intent( this, XMLActivity::class.java )
            startActivityForResult( intent, IntentID.ID_XML.value )
        }

        btnHTTP.setOnClickListener {
            val intent = Intent( this, HttpActivity::class.java )
            startActivityForResult( intent, IntentID.ID_HTTP.value )
        }

        btnJSON.setOnClickListener {
            val intent = Intent( this, JsonActivity::class.java )
            startActivityForResult( intent, IntentID.ID_JSON.value )
        }

        btnTimeZone.setOnClickListener {
            val intent = Intent( this, ListTimeZoneActivity::class.java )
            startActivityForResult( intent, IntentID.ID_TIMEZONE.value )
        }

        btnClock.setOnClickListener {
            val intent = Intent( this, WorldClockActivity::class.java )
            startActivityForResult( intent, IntentID.ID_CLOCK.value )
        }

        btnTimeZoneRV.setOnClickListener {
            val intent = Intent( this, RecycleTimeZoneActivity::class.java )
            startActivityForResult( intent, IntentID.ID_TIMEZONE_RV.value )
        }

        btnFileRV.setOnClickListener {
            val intent = Intent( this, FileRecycleActivity::class.java )
            startActivityForResult( intent, IntentID.ID_FILE_RV.value )
        }

        btnCounter.transformationMethod = null
        btnCounter.setOnClickListener {
            val intent = Intent( this, CounterActivity::class.java )
            startActivityForResult( intent, IntentID.ID_COUNTER.value )
        }

        btnMemo.transformationMethod = null
        btnMemo.setOnClickListener {
            val intent = Intent( this, MemoActivity::class.java )
            startActivityForResult( intent, IntentID.ID_MEMO.value )
        }

        btnAccBall.transformationMethod = null
        btnAccBall.setOnClickListener {
            val intent = Intent( this, AccBallActivity::class.java )
            startActivityForResult( intent, IntentID.ID_ACC_BALL.value )
        }

        btnSlide.transformationMethod = null
        btnSlide.setOnClickListener {
            val intent = Intent( this, SlideShowActivity::class.java )
            startActivityForResult( intent, IntentID.ID_SLIDE.value )
        }

        btnCountDown.transformationMethod = null
        btnCountDown.setOnClickListener {
            val intent = Intent( this, CountDownActivity::class.java )
            startActivityForResult( intent, IntentID.ID_COUNT_DOWN.value )
        }

        btnSaintTropez.transformationMethod = null
        btnSaintTropez.setOnClickListener {
            val intent = Intent( this, SaintTropezActivity::class.java )
            startActivityForResult( intent, IntentID.ID_SAINT_TROPEZ.value )
        }

        btnAlarmClock.transformationMethod = null
        btnAlarmClock.setOnClickListener {
            val intent = Intent( this, AlarmClockActivity::class.java )
            startActivityForResult( intent, IntentID.ID_ALARM_CLOCK.value )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when ( requestCode ){
            IntentID.ID_USER_ADD.value  -> ""
            IntentID.ID_IMAGE.value      -> ""
            IntentID.ID_TEAM_LIST.value -> ""
            IntentID.ID_XML.value        -> ""
            IntentID.ID_HTTP.value       -> ""
            IntentID.ID_JSON.value       -> ""
            IntentID.ID_TIMEZONE.value  -> ""
            IntentID.ID_TIMEZONE_RV.value  -> ""
        }
        //if ( resultCode == Activity.RESULT_OK )
        super.onActivityResult(requestCode, resultCode, data)
    }
}
