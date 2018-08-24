package milu.kiriu2010.tool

import java.io.PrintWriter
import java.io.StringWriter

class MyTool {
    companion object {
        fun exp2str( ex: Exception ): String {
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            ex.printStackTrace(pw)
            pw.flush()
            return sw.toString()
        }
    }
}