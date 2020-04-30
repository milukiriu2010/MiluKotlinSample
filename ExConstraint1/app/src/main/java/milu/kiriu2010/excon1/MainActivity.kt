package milu.kiriu2010.excon1

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import kotlinx.android.synthetic.main.activity_main.*
import milu.kiriu2010.abc.AppConst
import milu.kiriu2010.excon1.accball.AccBallActivity
import milu.kiriu2010.excon1.alarm.AlarmClockActivity
import milu.kiriu2010.excon1.clock.WorldClockActivity
import milu.kiriu2010.excon1.countdown.CountDownActivity
import milu.kiriu2010.excon1.counter.CounterActivity
import milu.kiriu2010.excon1.file.FileRecycleActivity
import milu.kiriu2010.excon1.file.JsonActivity
import milu.kiriu2010.excon1.file.XMLActivity
import milu.kiriu2010.excon1.gaction.GactionActivity
import milu.kiriu2010.excon1.glabyrinth.GlabyrinthActivity
import milu.kiriu2010.excon1.gshooting.GshootingActivity
import milu.kiriu2010.excon1.a02.A02Activity
import milu.kiriu2010.excon1.image.ImageActivity
import milu.kiriu2010.excon1.memo.MemoActivity
import milu.kiriu2010.excon1.notify.NotifyActivity
import milu.kiriu2010.excon1.saintropez.SaintTropezActivity
import milu.kiriu2010.excon1.slide.SlideShowActivity
import milu.kiriu2010.excon1.team.TeamListActivity
import milu.kiriu2010.excon1.team.UserAddActivity
import milu.kiriu2010.excon1.a03.A03Activity
import milu.kiriu2010.excon1.a03.RecycleTimeZoneActivity
import milu.kiriu2010.id.IntentID

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
        // ブラウザを起動する
        btnA01.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://sourceforge.net/projects/miludbviewer/files/?source=navbar")
            startActivity(intent)
        }

        // マンチェスターの天気予報を取得する
        btnA02.setOnClickListener {
            val intent = Intent( this, A02Activity::class.java )
            startActivityForResult( intent, IntentID.ID_A02.value )
        }

        // タイムゾーンの一覧をリスト表示する
        btnA03.setOnClickListener {
            val intent = Intent( this, A03Activity::class.java )
            startActivityForResult( intent, IntentID.ID_A03.value )
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

        btnJSON.setOnClickListener {
            val intent = Intent( this, JsonActivity::class.java )
            startActivityForResult( intent, IntentID.ID_JSON.value )
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

        // 通知
        btnNotify.setOnClickListener {
            val intent = Intent( this, NotifyActivity::class.java )
            startActivityForResult( intent, IntentID.ID_NOTIFY.value )
        }

        // アクション
        btnGaction.setOnClickListener {
            val intent = Intent( this, GactionActivity::class.java )
            startActivity(intent)
        }

        // シューティング
        btnGshooting.setOnClickListener {
            val intent = Intent( this, GshootingActivity::class.java )
            startActivity(intent)
        }

        // 迷路
        btnGlabyrinth.setOnClickListener {
            val intent = Intent( this, GlabyrinthActivity::class.java )
            startActivity(intent)
        }
    }
}
