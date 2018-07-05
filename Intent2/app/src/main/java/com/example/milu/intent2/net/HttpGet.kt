package com.example.milu.intent2.net

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HttpGet {
    fun doGet( url: URL) : String{
        val result = StringBuilder()
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

    }
}