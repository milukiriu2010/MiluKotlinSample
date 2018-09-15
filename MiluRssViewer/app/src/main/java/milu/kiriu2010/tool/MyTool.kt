package milu.kiriu2010.tool

import java.io.PrintWriter
import java.io.StringWriter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

// 2018.09.15  String(ISO8601+RFC3339)をDateへ変換
class MyTool {
    companion object {
        // ----------------------------------------
        // Exceptionを文字列に変換
        // ----------------------------------------
        fun exp2str( ex: Exception ): String {
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            ex.printStackTrace(pw)
            pw.flush()
            return sw.toString()
        }

        // ----------------------------------------
        // String(ISO8601+RFC3339)をDateへ変換
        // ----------------------------------------
        // RFC3339
        //   2018-08-28T19:00:00+09:00
        //   2018-09-14T21:46:00Z
        // ----------------------------------------
        fun rfc3339date(str: String): Date {
            try {
                val formatterRFC3339_1 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.US)
                return formatterRFC3339_1.parse(str)
            } catch ( parseEx: ParseException ) {
                val formatterRFC3339_2 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
                return formatterRFC3339_2.parse(str)
            }
        }

    }
}