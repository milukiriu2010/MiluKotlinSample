package milu.kiriu2010.net

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

// ----------------------------------------------
// 指定したURLにアクセスし情報を取得する
// ----------------------------------------------
class HttpGet {
    fun doGet( url: URL) : String{
        Log.v( this.javaClass.toString(), "=== HttpGet start ================" )
        Log.v( this.javaClass.toString(), url.path )
        val sb = StringBuilder()
        var con: HttpURLConnection? = null

        try{
            con = url.openConnection() as? HttpURLConnection ?: throw RuntimeException("cast error HttpURLConnection")
            con.connect()

            val status = con.getResponseCode()
            if ( status == HttpURLConnection.HTTP_OK ) {
                val istream = con.inputStream
                val reader = istream.bufferedReader()
                val iterator = reader.lineSequence().iterator()
                while ( iterator.hasNext() ) {
                    sb.append( iterator.next() )
                }

                Log.v( this.javaClass.toString(), sb.toString() )

                reader.close()
                istream.close()
            }

            return sb.toString()
        } catch( ex: Exception ){
            ex.printStackTrace()
            return sb.toString()
        } finally{
            Log.v( this.javaClass.toString(), "=== HttpGet end ================" )
            con?.disconnect()
        }

    }
}