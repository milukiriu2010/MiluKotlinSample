package milu.kiriu2010.excon1.a00

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import milu.kiriu2010.net.HttpGet
import kotlinx.android.synthetic.main.activity_a00.*
import milu.kiriu2010.net.HttpGetTask
import java.net.URL
import android.os.StrictMode
import milu.kiriu2010.excon1.R


class A00Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a00)

        // 前のアクティビティから渡されたデータを表示する
        val str = intent.getStringExtra("a00") ?: "<NULL>"
        tvA00A.text = str

        // データをバックグラウンドで取得する
        btnA00A.setOnClickListener{
            // https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643123
            val httpGetTask = HttpGetTask()
            httpGetTask.execute( URL("https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643123") )
            val strGet = httpGetTask.get()
            Log.w("HttpGetTask:",strGet)
            tvA00C.text = strGet
        }

        // データを取得する
        btnA00B.setOnClickListener{
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)

            val httpGet = HttpGet()
            val strGet = httpGet.doGet(URL("https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643123"))
            Log.w("HttpGet:",strGet)
            tvA00C.text = strGet
        }

        // XMLデータをクリアする
        btnA00C.setOnClickListener {
            tvA00C.text = ""
        }

        // 共有プリファレンスをクリアする
        btnA00D.setOnClickListener {
            val pref: SharedPreferences = getApplicationContext().getSharedPreferences(A00Const.PREF_A00.toString(), Context.MODE_PRIVATE)
            pref.edit()
                    .remove(A00Const.KEY_A00.toString())
                    .apply()

            tvA00A.text = ""
        }
    }

}
