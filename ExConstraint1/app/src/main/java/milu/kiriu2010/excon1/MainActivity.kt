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
import milu.kiriu2010.excon1.a00.A00Const
import milu.kiriu2010.excon1.accball.AccBallActivity
import milu.kiriu2010.excon1.a04.A04Activity
import milu.kiriu2010.excon1.a07.A07Activity
import milu.kiriu2010.excon1.a06.A06Activity
import milu.kiriu2010.excon1.counter.CounterActivity
import milu.kiriu2010.excon1.a05.FileRecycleActivity
import milu.kiriu2010.excon1.a05.A05Activity
import milu.kiriu2010.excon1.a05.XMLActivity
import milu.kiriu2010.excon1.gaction.GactionActivity
import milu.kiriu2010.excon1.glabyrinth.GlabyrinthActivity
import milu.kiriu2010.excon1.gshooting.GshootingActivity
import milu.kiriu2010.excon1.a02.A02Activity
import milu.kiriu2010.excon1.image.ImageActivity
import milu.kiriu2010.excon1.a08.A08Activity
import milu.kiriu2010.excon1.notify.NotifyActivity
import milu.kiriu2010.excon1.saintropez.SaintTropezActivity
import milu.kiriu2010.excon1.slide.SlideShowActivity
import milu.kiriu2010.excon1.a00.TeamListActivity
import milu.kiriu2010.excon1.a00.A00Activity
import milu.kiriu2010.excon1.a03.A03Activity
import milu.kiriu2010.excon1.a03.RecycleTimeZoneActivity
import milu.kiriu2010.excon1.id.IntentID

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.setTVA00B()

        this.setAction()
    }

    // 共有プリファレンスの値をロードし、テキストボックスに設定する
    private fun setTVA00B(){
        val pref: SharedPreferences = getApplicationContext().getSharedPreferences(A00Const.PREF_A00.toString(), Context.MODE_PRIVATE)
        val str = pref.getString( A00Const.KEY_A00.toString(), "" )
        tvA00B.setText(str)
    }


    private fun setAction() {
        // "ユーザ登録"ボタンを押下
        btnA00.setOnClickListener{
            // テキストボックスに入力した名前をA00Activityに渡す
            val intent = Intent( this, A00Activity::class.java )
            val str = tvA00B.text ?: "<arere>"
            Log.d(this.javaClass.name, str.toString() )
            intent.putExtra("a00", str.toString() )

            // テキストボックスに入力した内容を共有プリファレンスに格納する
            val pref: SharedPreferences = getApplicationContext().getSharedPreferences(A00Const.PREF_A00.toString(), Context.MODE_PRIVATE)
            val editor : Editor = pref.edit()
            editor.putString( A00Const.KEY_A00.toString(), str.toString() )
            editor.apply()

            startActivityForResult( intent, IntentID.ID_A00.value )
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

        // アラーム：時間がきたらダイアログが表示される
        btnA04.setOnClickListener {
            val intent = Intent( this, A04Activity::class.java )
            startActivityForResult( intent, IntentID.ID_A04.value )
        }

        // リソースのJSONをロードする
        btnA05.setOnClickListener {
            val intent = Intent( this, A05Activity::class.java )
            startActivityForResult( intent, IntentID.ID_A05.value )
        }

        // カウントダウン
        btnA06.setOnClickListener {
            val intent = Intent( this, A06Activity::class.java )
            startActivityForResult( intent, IntentID.ID_A06.value )
        }

        // 時刻表示
        btnA07.setOnClickListener {
            val intent = Intent( this, A07Activity::class.java )
            startActivityForResult( intent, IntentID.ID_A07.value )
        }

        // メモ
        btnA08.setOnClickListener {
            val intent = Intent( this, A08Activity::class.java )
            startActivityForResult( intent, IntentID.ID_A08.value )
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

        btnSaintTropez.transformationMethod = null
        btnSaintTropez.setOnClickListener {
            val intent = Intent( this, SaintTropezActivity::class.java )
            startActivityForResult( intent, IntentID.ID_SAINT_TROPEZ.value )
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
