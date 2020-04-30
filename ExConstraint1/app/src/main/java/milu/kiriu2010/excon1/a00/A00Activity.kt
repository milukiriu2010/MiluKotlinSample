package milu.kiriu2010.excon1.a00

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
        Toast.makeText(this, str, Toast.LENGTH_LONG )
        Log.d("bXXXXXXXXXXXX:", str )

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
            if (android.os.Build.VERSION.SDK_INT > 9) {
                val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                StrictMode.setThreadPolicy(policy)
            }
            val httpGet = HttpGet()
            val strGet = httpGet.doGet(URL("https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643123"))
            Log.w("HttpGet:",strGet)
            tvA00C.text = strGet
        }

        // データをクリアする
        btnA00C.setOnClickListener {
            tvA00C.text = ""
        }
    }

}
