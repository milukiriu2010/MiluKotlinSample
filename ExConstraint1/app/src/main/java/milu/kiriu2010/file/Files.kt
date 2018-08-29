package milu.kiriu2010.file

import android.os.Environment
import android.text.format.DateFormat
import java.io.*
import java.util.*

// 出力先ディレクトリを決定する
private fun getFilesDir() : File {
    val publicDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOCUMENTS)

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

// ディレクトリ内のファイル一覧を取得
fun getFiles() = getFilesDir().listFiles().toList()

// ファイルを出力
fun outputFile(original: File?, content: String) : File {
    // ファイル名は「memo-タイムスタンプ」とする
    val timeStamp = DateFormat.format("yyyy-MM-dd-hh-mm-ss", Date())

    val file = original ?: File(getFilesDir(), "memo-$timeStamp")

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
