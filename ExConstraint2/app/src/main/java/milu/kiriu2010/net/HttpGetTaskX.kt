package milu.kiriu2010.net

import android.os.AsyncTask

import java.net.URL

// http://www.programing-style.com/android/android-api/android-httpurlconnection-get-text/
class HttpGetTaskX: AsyncTask<URL,Unit,String>() {

    override fun doInBackground(vararg params: URL?): String {
        val httpGet = HttpGet()
        return httpGet.doGet(params[0]!!)
        /*
        val result = StringBuilder()
        val url = params[0]
        var con: HttpURLConnection? = null

        try{
            con = url?.openConnection() as HttpURLConnection
            con.connect()

            val status = con.getResponseCode()
            if ( status == HttpURLConnection.HTTP_OK ) {
                val ins = con.inputStream
                //val encoding = con.contentEncoding
                //val inReader = InputStreamReader( ins, encoding )
                val inReader = InputStreamReader( ins )
                val bufReader = BufferedReader(inReader)
                var line: String? = null
                while({ line = bufReader.readLine(); line } != null) {
                    result.append(line);
                }
                bufReader.close()
                inReader.close()
                ins.close()
            }

            return result.toString()
        } catch( ex: Exception ){
            ex.printStackTrace()
            return result.toString()
        } finally{
            con?.disconnect()
        }
        */
    }
}
