package milu.kiriu2010.loader

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import android.util.Log
import milu.kiriu2010.entity.Rss
import milu.kiriu2010.entity.URLData
import milu.kiriu2010.entity.parseRss
import milu.kiriu2010.net.httpGet


// RSSフィードをダウンロードしてRssオブジェクトを返すローダー
class RssLoader(context: Context, val urlData: URLData? ) : AsyncTaskLoader<Rss>(context) {
//class RssLoader(context: Context) : AsyncTaskLoader<Rss>(context) {

    private var cache : Rss? = null

    // このローダーがバックグラウンドで行う処理
    override fun loadInBackground(): Rss? {
        Log.d( javaClass.simpleName, "========================" )
        Log.d( javaClass.simpleName, urlData?.url?.toString() )
        Log.d( javaClass.simpleName, "========================" )
        val strURL = urlData?.url?.toString() ?: return null

        // HTTPでRSSのXMLを取得する
        //val response = httpGet("https://www.sbbit.jp/rss/HotTopics.rss")
        val response = httpGet(strURL)

        if (response != null) {
            // 取得に成功したら、パースして返す
            return parseRss(response)
        }

        return null
    }

    // コールバッククラスに返す前に通る処理
    override fun deliverResult(data: Rss?) {
        // 破棄されていたら結果を返さない
        if (isReset || data == null) return

        // 結果をキャッシュする
        cache = data
        super.deliverResult(data)
    }

    // バックグラウンド処理が開始される前に呼ばれる
    override fun onStartLoading() {
        // キャッシュがあるなら、キャッシュを返す
        if (cache != null) {
            deliverResult(cache)
        }

        // コンテンツが変化している場合やキャッシュがない場合には、バックグラウンド処理を行う
        if (takeContentChanged() || cache == null) {
            // 非同期処理を開始
            forceLoad()
        }
    }

    // ローダーが停止する前に呼ばれる処理
    override fun onStopLoading() {
        cancelLoad()
    }

    // ローダーが破棄される前に呼ばれる処理
    override fun onReset() {
        super.onReset()
        onStopLoading()
        cache = null
    }

}
