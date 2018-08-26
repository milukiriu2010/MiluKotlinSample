package milu.kiriu2010.netv2

import android.util.Log
import java.net.URL
import java.net.URLConnection

class MyURLConFactory {
    companion object {
        fun createInstance( url: URL, myURLConAbsCmp: MyURLConAbs? ): MyURLConAbs? {
            Log.d( javaClass.simpleName, "================================" )
            Log.d( javaClass.simpleName, "URL protocol[" + url.protocol + "]" )
            Log.d( javaClass.simpleName, "URL host[" + url.host + "]" )
            Log.d( javaClass.simpleName, "URL port[" + url.port + "]" )
            Log.d( javaClass.simpleName, "URL path[" + url.path + "]" )
            Log.d( javaClass.simpleName, "================================" )

            // ------------------------------------------
            // 接続オブジェクトがある場合、
            // 接続先プロトコル/ホスト/ポートを比較し、
            // 同じ場合、
            // コネクションを再利用したいため、
            // オブジェクトをコピーしたものを返す
            // -----------------------------------------
            if ( myURLConAbsCmp != null ) {
                if (
                        ( url.protocol.equals(myURLConAbsCmp.url.protocol) ) and
                        ( url.host.equals(myURLConAbsCmp.url.host) ) and
                        (url.port.equals(myURLConAbsCmp.url.port))
                ) {
                    val myURLConAbsCopy = myURLConAbsCmp.clone() as MyURLConAbs
                    myURLConAbsCopy.url = url
                    return myURLConAbsCopy
                }
            }

            // ------------------------------------------
            // 接続オブジェクトがない場合、
            // 接続先プロトコルを元に生成
            // -----------------------------------------
            var myURLConAbs: MyURLConAbs? =
                if ( "http".equals(url.protocol) ) {
                    MyURLConHttp()
                }
                else if ( "https".equals(url.protocol) ) {
                    MyURLConHttps()
                }
                else{
                    null
                }

            if ( myURLConAbs != null ) {
                myURLConAbs.url = url
            }

            return myURLConAbs
        }
    }
}