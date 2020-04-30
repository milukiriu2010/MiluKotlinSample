package milu.kiriu2010.net

import android.os.AsyncTask
import android.util.Log

import java.net.URL

// --------------------------------------------
// 指定したURLにアクセスし情報を取得する
// --------------------------------------------
// http://www.programing-style.com/android/android-api/android-httpurlconnection-get-text/
// --------------------------------------------
class HttpGetTask: AsyncTask<URL,Unit,String>() {

    override fun doInBackground(vararg params: URL?): String {
        Log.d(this.javaClass.toString(), "HttpGetTask::doInBackground start." )
        val httpGet = HttpGet()
        val strGet = httpGet.doGet(params[0]!!)
        Log.d(this.javaClass.toString(), "HttpGetTask::doInBackground strGet=[${strGet}]" )
        return strGet
    }

    override fun onPostExecute(result: String?) {
        Log.d(this.javaClass.toString(), "HttpGetTask::onPostExecute start." )
        Log.d(this.javaClass.toString(), "Result:${result}" )

        super.onPostExecute(result)
    }
}
