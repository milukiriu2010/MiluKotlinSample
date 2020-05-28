package milu.kiriu2010.excon1.a08

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.format.DateFormat
import android.util.Log
import java.io.*
import java.util.*

// 出力先ディレクトリを決定する
private fun getFilesDir() : File {
    // 内部ストレージのDocuments以下
    val publicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)

    if (publicDir != null) {
        // 存在しないディレクトリが返された場合は、作成する
        if (!publicDir.exists()) publicDir.mkdirs()
        return publicDir
    } else {
        val dir = File(Environment.getExternalStorageDirectory(), "MemoFiles")
        // まだ作成されていない場合は、作成する
        if (!dir.exists()) dir.mkdirs()
        return dir
    }
}

// 出力先ディレクトリを決定する
// API 29
// getExternalStoragePublicDirectory deprecated
// https://stackoverflow.com/questions/56468539/getexternalstoragepublicdirectory-deprecated-in-android-q
private fun getFilesDir(context: Context) : File {
    if ( Build.VERSION.SDK_INT < 29 ) {
        return getFilesDir()
    }
    else {
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "ExCon1Memo")
            put(MediaStore.MediaColumns.MIME_TYPE,"text/plain")
            put(MediaStore.MediaColumns.RELATIVE_PATH,"DCIM/ExCon1Memo")
        }

        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        Log.d("getFilesDir", "URI:{$uri}")

        return File(uri?.path!!)
    }
}

// ディレクトリ内のファイル一覧を取得
fun getFiles() = getFilesDir().listFiles()?.toList()!!

// ディレクトリ内のファイル一覧を取得
fun getFiles(context: Context) = getFilesDir(context).listFiles()?.toList()?.sortedDescending()!!

// ファイルを出力
fun outputFile(original: File?, content: String, context: Context) : File {
    // ファイル名は「memo-タイムスタンプ」とする
    val timeStamp = DateFormat.format("yyyy-MM-dd-hh-mm-ss", Date())

    val file = original ?: File(getFilesDir(context), "memo-$timeStamp")

    val writer = BufferedWriter(FileWriter(file))
    writer.use {
        it.write(content)
        it.flush()
    }

    return file
}

/// ファイルをロード
fun inputFile(file: File) : String {
    val reader = BufferedReader(FileReader(file))
    return reader.readLines().joinToString("\n")
}
