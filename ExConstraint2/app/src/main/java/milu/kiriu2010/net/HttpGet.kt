package milu.kiriu2010.net

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Reader
import java.net.HttpURLConnection
import java.net.URL

class HttpGet {
    fun doGet( url: URL) : String{
        Log.d(this.javaClass.name,"XXXXXX")
        val result = StringBuilder()
        var con: HttpURLConnection? = null

        try{
            Log.d(this.javaClass.name,"before connection")

            con = url?.openConnection() as HttpURLConnection
            con.connect()

            Log.d(this.javaClass.name,"connected")

            val status = con.getResponseCode()

            if ( status == HttpURLConnection.HTTP_OK ) {
                Log.d(this.javaClass.name,"get response OK")
                val ins = con.inputStream
                //val encoding = con.contentEncoding
                //val inReader = InputStreamReader( ins, encoding )
                val inReader: Reader = InputStreamReader( ins )
                val bufReader = BufferedReader(inReader)
                var line: String? = null
                while({ line = bufReader.readLine(); line } != null) {
                    result.append(line);
                }
                bufReader.close()
                inReader.close()
                ins.close()
            }
            else{
                Log.d(this.javaClass.name,"get response NG")
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
