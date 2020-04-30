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
// --------------------------------------------------------------------------
// <?xml version="1.0" encoding="UTF-8"?>
// <rss xmlns:atom="http://www.w3.org/2005/Atom" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:georss="http://www.georss.org/georss" version="2.0">
//  <channel>
//    <title>BBC Weather - Forecast for  Manchester, GB</title>
//    <link>https://www.bbc.co.uk/weather/2643123</link>
//    <description>3-day forecast for Manchester from BBC Weather, including weather, temperature and wind information</description>
//    <language>en</language>
//    <copyright>Copyright: (C) British Broadcasting Corporation, see http://www.bbc.co.uk/terms/additional_rss.shtml for more details</copyright>
//    <pubDate>Thu, 30 Apr 2020 05:19:06 GMT</pubDate>
//    <dc:date>2020-04-30T05:19:06Z</dc:date>
//    <dc:language>en</dc:language>
//    <dc:rights>Copyright: (C) British Broadcasting Corporation, see http://www.bbc.co.uk/terms/additional_rss.shtml for more details</dc:rights>
//    <atom:link href="https://weather-broker-cdn.api.bbci.co.uk/%s/forecast/rss/3day/%s" type="application/rss+xml" rel="self" />
//    <image>
//      <title>BBC Weather - Forecast for  Manchester, GB</title>
//      <url>http://static.bbci.co.uk/weather/0.3.203/images/icons/individual_57_icons/en_on_light_bg/10.gif</url>
//      <link>https://www.bbc.co.uk/weather/2643123</link>
//    </image>
//    <item>
//      <title>Today: Light Rain Showers, Minimum Temperature: 7ﾂｰC (44ﾂｰF) Maximum Temperature: 11ﾂｰC (52ﾂｰF)</title>
//      <link>https://www.bbc.co.uk/weather/2643123?day=0</link>
//      <description>Maximum Temperature: 11ﾂｰC (52ﾂｰF), Minimum Temperature: 7ﾂｰC (44ﾂｰF), Wind Direction: Southerly, Wind Speed: 14mph, Visibility: Good, Pressure: 990mb, Humidity: 75%, UV Risk: 2, Pollution: Low, Sunrise: 05:35 BST, Sunset: 20:38 BST</description>
//      <pubDate>Thu, 30 Apr 2020 05:19:06 GMT</pubDate>
//      <guid isPermaLink="false">https://www.bbc.co.uk/weather/2643123-0-2020-04-29T21:57:00.000+0000</guid>
//      <dc:date>2020-04-30T05:19:06Z</dc:date>
//      <georss:point>53.4809 -2.2374</georss:point>
//    </item>
//    <item>
//      <title>Friday: Light Rain Showers, Minimum Temperature: 6ﾂｰC (43ﾂｰF) Maximum Temperature: 12ﾂｰC (54ﾂｰF)</title>
//      <link>https://www.bbc.co.uk/weather/2643123?day=1</link>
//      <description>Maximum Temperature: 12ﾂｰC (54ﾂｰF), Minimum Temperature: 6ﾂｰC (43ﾂｰF), Wind Direction: Westerly, Wind Speed: 14mph, Visibility: Good, Pressure: 997mb, Humidity: 75%, UV Risk: 3, Pollution: Low, Sunrise: 05:33 BST, Sunset: 20:40 BST</description>
//      <pubDate>Thu, 30 Apr 2020 05:19:06 GMT</pubDate>
//      <guid isPermaLink="false">https://www.bbc.co.uk/weather/2643123-1-2020-04-29T21:57:00.000+0000</guid>
//      <dc:date>2020-04-30T05:19:06Z</dc:date>
//      <georss:point>53.4809 -2.2374</georss:point>
//    </item>
//    <item>
//      <title>Saturday: Sunny Intervals, Minimum Temperature: 5ﾂｰC (42ﾂｰF) Maximum Temperature: 14ﾂｰC (57ﾂｰF)</title>
//      <link>https://www.bbc.co.uk/weather/2643123?day=2</link>
//      <description>Maximum Temperature: 14ﾂｰC (57ﾂｰF), Minimum Temperature: 5ﾂｰC (42ﾂｰF), Wind Direction: Westerly, Wind Speed: 12mph, Visibility: Good, Pressure: 1009mb, Humidity: 66%, UV Risk: 3, Pollution: Low, Sunrise: 05:31 BST, Sunset: 20:42 BST</description>
//      <pubDate>Thu, 30 Apr 2020 05:19:06 GMT</pubDate>
//      <guid isPermaLink="false">https://www.bbc.co.uk/weather/2643123-2-2020-04-29T21:57:00.000+0000</guid>
//      <dc:date>2020-04-30T05:19:06Z</dc:date>
//      <georss:point>53.4809 -2.2374</georss:point>
//    </item>
//  </channel>
//</rss>
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
