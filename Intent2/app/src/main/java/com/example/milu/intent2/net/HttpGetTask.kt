package com.example.milu.intent2.net

import android.os.AsyncTask
import android.util.Log

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.ProtocolException
import java.net.URL

// http://www.programing-style.com/android/android-api/android-httpurlconnection-get-text/
class HttpGetTask: AsyncTask<URL,Unit,String>() {

    override fun doInBackground(vararg params: URL?): String {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        val result = StringBuilder()
        val url = params[0]
        var con: HttpURLConnection? = null

        try{
            con = url?.openConnection() as HttpURLConnection

            return ""
        } catch( ex: Exception ){
            ex.printStackTrace()
            return ""
        } finally{
            con?.disconnect()
        }
    }
}