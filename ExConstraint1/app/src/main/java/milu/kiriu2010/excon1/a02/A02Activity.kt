package milu.kiriu2010.excon1.a02

import android.app.Activity
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import milu.kiriu2010.excon1.R
import milu.kiriu2010.net.HttpGet
import milu.kiriu2010.xml.MyXMLParse
import kotlinx.android.synthetic.main.activity_a02.*
import milu.kiriu2010.net.HttpGetTask
import java.net.URL

// --------------------------------------------------------------------------
// https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643123
// にアクセスしてマンチェスターの天気予報を取得する
// 天気予報は、RSSv2形式
// アクションバーがない⇒AndroidManifest.xmlで設定している
// --------------------------------------------------------------------------
// 取得しているXMLのサンプルは、以下を参照
// doc/excon1/bbc_weather20200518.xml
// --------------------------------------------------------------------------
class A02Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a02)

        // 戻るボタン押下で、前アクティビティに戻る
        btnA02A.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }

        // 取得ボタン押下で、天気予報を取得する
        btnA02B.setOnClickListener{
            // https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643123
            val httpGetTask = HttpGetTask()
            httpGetTask.execute( URL("https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643123") )
            val strGet = httpGetTask.get()
            Log.w("HttpGetTask:",strGet)

            // 天気予報を取得できない場合、ERRORと表示
            if ( strGet == null ) {
                tvA02.text = "<ERROR>"
            }
            // 天気予報を取得できた場合、その内容を表示
            else {
                val myXmlParse = MyXMLParse()
                val xmlDoc = myXmlParse.str2doc(strGet)
                // 当日を含む3日分の予報
                val nodeListItem = myXmlParse.searchNodeList( xmlDoc, "//rss/channel/item")

                val titleLst = mutableListOf<String>()
                for ( i in 0 until nodeListItem.length ) {
                    val nodeItem = nodeListItem.item(i)
                    // 各曜日ごとの予報
                    val title = myXmlParse.searchNodeText( nodeItem, "./title/text()")
                    titleLst.add(title)
                }

                // 各曜日ごと予報をテキストとして張り付ける
                tvA02.text = titleLst.joinToString( separator="\n\n", prefix = "Manchester\n\n" )
            }

        }

        tvA02.text = ""

    }
}
