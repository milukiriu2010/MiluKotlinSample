package milu.kiriu2010.net

import java.io.BufferedInputStream
import java.io.InputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

fun httpGet( url: String ): InputStream? {
    val con = URL(url).openConnection() as HttpsURLConnection

    con.apply {
        requestMethod = "GET"
        // 接続のタイムアウト(ミリ秒)
        connectTimeout = 3000
        // 読み込みのタイムアウト(ミリ秒)
        readTimeout = 5000
        // リダイレクトの許可
        instanceFollowRedirects = true
    }

    con.connect()

    if ( con.responseCode in 200..299 ) {
        return BufferedInputStream(con.inputStream)
    }

    return null
}