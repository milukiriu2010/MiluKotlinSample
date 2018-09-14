package milu.kiriu2010.milurssviewer.each

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.util.Log
import android.view.MenuItem
import milu.kiriu2010.entity.Rss
import milu.kiriu2010.entity.URLData
import milu.kiriu2010.gui.common.ExceptionFragment
import milu.kiriu2010.id.IntentID
import milu.kiriu2010.id.LoaderID
import milu.kiriu2010.loader.AsyncResult
import milu.kiriu2010.loader.RssV2Loader
import milu.kiriu2010.milurssviewer.R
import java.io.IOException
import java.text.ParseException

class RssEachV2Activity : AppCompatActivity(), LoaderManager.LoaderCallbacks<AsyncResult<Rss>> {

    // ------------------------------------------------------
    // この画面が生成される際、呼び出される
    // ------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rss_each_v2)

        Log.d( javaClass.simpleName, "orCreate" )

        // 戻るボタンをアクションバーに表示
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 前画面より、この画面で表示するRSSのURL情報が送られてくる
        val urlData = intent.getParcelableExtra<URLData>(IntentID.KEY_RSS_EACH.id)

        Log.d( javaClass.simpleName, "====================================" )
        Log.d( javaClass.simpleName, "urlData[" + urlData.url.toString() + "]" )

        // ローダに渡すURL情報の箱を生成
        val bundle = Bundle()
        bundle.putParcelable( IntentID.KEY_URL_DATA.id, urlData )

        // RSSを取得するためのローダを初期化する
        supportLoaderManager.initLoader(LoaderID.ID_RSS_GET.id, bundle, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d( javaClass.simpleName, "onDestroy" )
    }

    override fun onStart() {
        super.onStart()
        Log.d( javaClass.simpleName, "onStart" )
    }

    override fun onStop() {
        super.onStop()
        Log.d( javaClass.simpleName, "onStop" )
    }

    override fun onResume() {
        super.onResume()
        Log.d( javaClass.simpleName, "onResume" )
    }

    override fun onPause() {
        super.onPause()
        Log.d( javaClass.simpleName, "onPause" )
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            // 前の画面へ戻る
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // "戻る"ボタンを押すと
    // デフォルトの動きは
    // フラグメントが削除されるだけなので
    // アクティビティを強制的に終了するようにする
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    // LoaderManager.LoaderCallbacks
    // ----------------------------------------------------
    // 非同期処理開始
    // ----------------------------------------------------
    // ローダが要求されたときによばれる
    // 非同期処理を行うLoaderを生成する
    // getLoaderManager().initLoaderで一回のみ呼び出される
    // ----------------------------------------------------
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<AsyncResult<Rss>> {
        Log.d( javaClass.simpleName, "" )
        Log.d( javaClass.simpleName, "orCreateLoader" )
        Log.d( javaClass.simpleName, "====================================" )

        return when ( id ) {
            LoaderID.ID_RSS_GET.id -> {
                val urlData: URLData? = args?.getParcelable(IntentID.KEY_URL_DATA.id)
                RssV2Loader(this,urlData)
            }
            else -> {
                throw RuntimeException("No Loader for id[" + id + "]")
            }
        }
    }

    // LoaderManager.LoaderCallbacks
    // ---------------------------------------------------
    // 非同期処理終了
    // ---------------------------------------------------
    override fun onLoadFinished(loader: Loader<AsyncResult<Rss>>, data: AsyncResult<Rss>?) {
        Log.d(javaClass.simpleName, "" )
        Log.d(javaClass.simpleName, "onLoadFinished" )
        Log.d(javaClass.simpleName, "==============" )
        if ( data == null ) return

        // RSS記事一覧取得でエラーあり
        if ( data.exception != null ) {
            val strMsg = when {
                data.exception is IOException -> "通信エラー"
                data.exception is ParseException -> "RSSフォーマットエラー"
                data.exception is Exception -> "その他エラー"
                else -> "不明"
            }
            Log.d(javaClass.simpleName, "strMsg[$strMsg]" )
            Log.d(javaClass.simpleName, "==============" )

            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameRssResult, ExceptionFragment.newInstance( strMsg, data.exception!! ), "fragmentRssResult")
                    .addToBackStack(null)
                    .commit()
        }
        // RSS記事一覧取得でエラーなしだが、RSS記事一覧なし
        else if ( data.data == null ){
            val strMsg = "データなし"
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameRssResult, ExceptionFragment.newInstance( strMsg ), "fragmentRssResult")
                    .addToBackStack(null)
                    .commit()
        }
        // RSS記事一覧取得でエラーなし、かつ、RSS記事一覧あり
        else {
            // RSS記事一覧を表示
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameRssResult, RssEachFragment.newInstance(data.data!!), "fragmentRssResult")
                    .addToBackStack(null)
                    .commit()
        }
    }

    // LoaderManager.LoaderCallbacks
    // ローダがリセットされたときに呼ばれる
    override fun onLoaderReset(loader: Loader<AsyncResult<Rss>>) {
        Log.d(javaClass.simpleName, "onLoadReset" )
    }
}
