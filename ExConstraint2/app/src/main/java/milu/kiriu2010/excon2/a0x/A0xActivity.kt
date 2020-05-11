package milu.kiriu2010.excon2.a0x

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.RadioButton
import android.widget.Toast
import milu.kiriu2010.excon2.a0x.a01.A01Activity
import milu.kiriu2010.excon2.a0x.a02.A02Activity
import milu.kiriu2010.excon2.a0x.a03.A03Activity
import milu.kiriu2010.excon2.a0x.a04.A04Activity
import milu.kiriu2010.excon2.a0x.a05.A05Activity
import milu.kiriu2010.excon2.a0x.a06.A06Activity
import milu.kiriu2010.excon2.a0x.a07.A07Activity
import milu.kiriu2010.excon2.a0x.a08.A08Activity
import milu.kiriu2010.excon2.a0x.a09.A09Activity
import milu.kiriu2010.excon2.a0x.a10.A10Activity
import milu.kiriu2010.excon2.a0x.a11.A11Activity
import milu.kiriu2010.excon2.a0x.a12.A12Activity
import milu.kiriu2010.excon2.a0x.a13.A13Activity
import milu.kiriu2010.excon2.a0x.a14.A14Activity
import milu.kiriu2010.excon2.a0x.a15.A15Activity
import milu.kiriu2010.excon2.a0x.a16.A16Activity
import milu.kiriu2010.excon2.a0x.a17.A17Activity
import milu.kiriu2010.excon2.a0x.a18.A18Activity
import milu.kiriu2010.excon2.a0x.a19.A19Activity
import milu.kiriu2010.excon2.a0x.a20.A20Activity
import milu.kiriu2010.excon2.a0x.a21.A21Activity
import milu.kiriu2010.excon2.a0x.a22.A22Activity
import milu.kiriu2010.excon2.a0x.a23.A23Activity
import milu.kiriu2010.excon2.a0x.seek.SeekActivity
import milu.kiriu2010.excon2.a0x.temperature.TemperatureActivity
import kotlinx.android.synthetic.main.activity_a0x.*
import milu.kiriu2010.excon2.BuildConfig
import milu.kiriu2010.excon2.R
import milu.kiriu2010.excon2.a0x.navidrawer.NaviDrawerActivity
import milu.kiriu2010.excon2.a0x.sensorori.SensorOriActivity
import milu.kiriu2010.excon2.a0x.setting.SettingsActivity
import milu.kiriu2010.excon2.id.IntentID

class A0xActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a0x)

        // debugモード/releaseモード　どちらでコンパイルしているか表示
        // http://tekeye.uk/android/examples/android-debug-vs-release-build
        //   not 0 => debug
        //   0     => release
        Log.d( javaClass.simpleName, "Application Debug/Release:" + ( applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE ) )
        if ( ( applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE ) != 0 ) {
            tvA0XA.setText("DEBUG")
        }
        else {
            tvA0XA.setText("RELEASE")
        }

        // debugモード/releaseモード　どちらでコンパイルしているか表示
        // コンパイル時に自動的に生成されるBuildConfigクラスをもとに判断
        // "Build Clean"すると認識されず赤になるので、おすすめしない
        if (BuildConfig.DEBUG) {
            tvA0XB.setText("DEBUG")
        }
        else {
            tvA0XB.setText("RELEASE")
        }

        // 動作しているOSのバージョン
        tvA0XC.text = Build.VERSION.SDK_INT.toString()

        // 女or男のラジオボタンをクリックするとツールチップ表示する
        rgA0X.setOnCheckedChangeListener { _, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
                Toast.makeText(applicationContext, "On checked change : ${radio.text}", Toast.LENGTH_LONG).show()
        }

        // テキスト→音声変換
        btnA01.setOnClickListener {
            val intent = Intent(this, A01Activity::class.java )
            this.startActivity( intent )
        }

        // ストップウォッチ
        btnA02.setOnClickListener {
            val intent = Intent(this, A02Activity::class.java )
            this.startActivityForResult( intent, IntentID.ID_A02.value )
        }

        // アクションバーをカスタマイズ
        btnA03.setOnClickListener {
            val intent = Intent(this, A03Activity::class.java )
            this.startActivityForResult( intent, IntentID.ID_A03.value )
        }

        // サイコロを振る
        btnA04.setOnClickListener {
            val intent = Intent(this, A04Activity::class.java )
            this.startActivityForResult( intent, IntentID.ID_A04.value )
        }

        // Web検索
        btnA05.setOnClickListener {
            val intent = Intent(this, A05Activity::class.java )
            this.startActivityForResult( intent, IntentID.ID_A05.value )
        }

        // コンテキストメニュー
        btnA06.setOnClickListener {
            val intent = Intent(this, A06Activity::class.java )
            this.startActivityForResult( intent, IntentID.ID_A06.value )
        }

        // QRコード/バーコードスキャン
        btnA07.setOnClickListener {
            val intent = Intent(this, A07Activity::class.java )
            this.startActivityForResult( intent, IntentID.ID_A07.value )
        }

        // フィボナッチ数列
        btnA08.setOnClickListener {
            val intent = Intent(this, A08Activity::class.java )
            this.startActivityForResult( intent, IntentID.ID_A08.value )
        }

        // リサイクラービュー
        btnA09.setOnClickListener{
            val intent = Intent(this, A09Activity::class.java )
            this.startActivityForResult( intent, IntentID.ID_A09.value )
        }

        // 音声入力
        btnA10.setOnClickListener {
            val intent = Intent(this, A10Activity::class.java)
            this.startActivity(intent)
        }

        // 大きな画像をロードする
        btnA11.setOnClickListener {
            val intent = Intent(this, A11Activity::class.java )
            this.startActivityForResult( intent, IntentID.ID_A11.value )
        }

        // 信号のアニメーション
        btnA12.setOnClickListener {
            val intent = Intent(this, A12Activity::class.java )
            this.startActivityForResult( intent, IntentID.ID_A12.value )
        }

        // エクセル－アップロード
        btnA13.setOnClickListener {
            val intent = Intent(this, A13Activity::class.java )
            this.startActivity( intent )
        }

        // 温度センサ
        btnA14.setOnClickListener {
            val intent = Intent( this, A14Activity::class.java )
            this.startActivity( intent )
        }

        // レーティングバー
        btnA15.setOnClickListener{
            val intent = Intent(this, A15Activity::class.java )
            this.startActivityForResult( intent, IntentID.ID_A15.value )
        }

        // ピンチ　イン・アウト
        btnA16.transformationMethod = null
        btnA16.setOnClickListener {
            val intent = Intent( this, A16Activity::class.java )
            this.startActivity( intent )
        }

        // 照度センサ
        btnA17.setOnClickListener {
            val intent = Intent( this, A17Activity::class.java)
            this.startActivity(intent)
        }

        // 関数を使ったサイズ変更するアニメーション
        btnA18.setOnClickListener{
            val intent = Intent(this, A18Activity::class.java )
            this.startActivityForResult( intent, IntentID.ID_ANIME.value )
        }

        // キャンバス(SKEW)
        btnA19.setOnClickListener {
            val intent = Intent(this,A19Activity::class.java)
            this.startActivity(intent)
        }

        // 歩行センサ
        btnA20.setOnClickListener {
            val intent = Intent( this, A20Activity::class.java)
            this.startActivity(intent)
        }

        // アニメ(移動/回転/透明)
        btnA21.setOnClickListener {
            val intent = Intent( this, A21Activity::class.java )
            this.startActivity(intent)
        }

        // ページャ
        btnA22.setOnClickListener {
            val intent = Intent(this, A22Activity::class.java)
            this.startActivity(intent)
        }

        // 近接センサ
        btnA23.setOnClickListener {
            val intent = Intent( this, A23Activity::class.java)
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
