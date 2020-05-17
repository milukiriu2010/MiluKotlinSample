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
import milu.kiriu2010.excon1.a09.A09Activity
import milu.kiriu2010.excon1.a04.A04Activity
import milu.kiriu2010.excon1.a07.A07Activity
import milu.kiriu2010.excon1.a06.A06Activity
import milu.kiriu2010.excon1.a16.A16Activity
import milu.kiriu2010.excon1.a11.A11Activity
import milu.kiriu2010.excon1.a05.A05Activity
import milu.kiriu2010.excon1.a14.A14Activity
import milu.kiriu2010.excon1.gaction.GactionActivity
import milu.kiriu2010.excon1.glabyrinth.GlabyrinthActivity
import milu.kiriu2010.excon1.gshooting.GshootingActivity
import milu.kiriu2010.excon1.a02.A02Activity
import milu.kiriu2010.excon1.a13.A13Activity
import milu.kiriu2010.excon1.a08.A08Activity
import milu.kiriu2010.excon1.a18.A18Activity
import milu.kiriu2010.excon1.a12.A12Activity
import milu.kiriu2010.excon1.a10.A10Activity
import milu.kiriu2010.excon1.a15.A15Activity
import milu.kiriu2010.excon1.a00.A00Activity
import milu.kiriu2010.excon1.a03.A03Activity
import milu.kiriu2010.excon1.a17.A17Activity
import milu.kiriu2010.excon1.id.IntentID

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 共有プリファレンスの値をロードし、テキストボックスに設定する
        this.setTVA00B()

        // ボタンをクリックしたときのアクションを定義
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
            startActivity( intent )
        }

        // タイムゾーンの一覧をリスト表示する
        btnA03.setOnClickListener {
            val intent = Intent( this, A03Activity::class.java )
            startActivity( intent )
        }

        // アラーム：時間がきたらダイアログが表示される
        btnA04.setOnClickListener {
            val intent = Intent( this, A04Activity::class.java )
            startActivity( intent )
        }

        // リソースのJSONをロードする
        btnA05.setOnClickListener {
            val intent = Intent( this, A05Activity::class.java )
            startActivity( intent )
        }

        // カウントダウン
        btnA06.setOnClickListener {
            val intent = Intent( this, A06Activity::class.java )
            startActivity( intent )
        }

        // 時刻表示
        btnA07.setOnClickListener {
            val intent = Intent( this, A07Activity::class.java )
            startActivity( intent )
        }

        // メモ
        btnA08.setOnClickListener {
            val intent = Intent( this, A08Activity::class.java )
            startActivity( intent )
        }

        // 加速度センサ
        btnA09.setOnClickListener {
            val intent = Intent( this, A09Activity::class.java )
            startActivity( intent )
        }

        // スライドショー
        btnA10.setOnClickListener {
            val intent = Intent( this, A10Activity::class.java )
            startActivity( intent )
        }

        // Android上のファイルシステムを一覧表示
        btnA11.setOnClickListener {
            val intent = Intent( this, A11Activity::class.java )
            startActivity( intent )
        }

        // スクロールビューの練習
        btnA12.setOnClickListener {
            val intent = Intent( this, A12Activity::class.java )
            startActivity( intent )
        }

        // ラジオボタンで選択した画像リソースを読み込む
        btnA13.setOnClickListener{
            val intent = Intent( this, A13Activity::class.java )
            startActivity( intent )
        }

        // リソースのXMLファイルを読み込む
        btnA14.setOnClickListener {
            val intent = Intent( this, A14Activity::class.java )
            startActivity( intent )
        }

        // アクティビティ間のSerializableデータ授受
        btnA15.setOnClickListener {
            val intent = Intent( this, A15Activity::class.java )
            startActivity( intent )
        }

        // フラグメントの練習
        btnA16.setOnClickListener {
            val intent = Intent( this, A16Activity::class.java )
            startActivity( intent )
        }

        // リサイクラービューの練習
        btnA17.setOnClickListener {
            val intent = Intent( this, A17Activity::class.java )
            startActivity( intent )
        }

        // 通知
        btnA18.setOnClickListener {
            val intent = Intent( this, A18Activity::class.java )
            startActivity( intent )
        }

        // ジャンプアクション
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
