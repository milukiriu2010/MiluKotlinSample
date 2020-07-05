package milu.kiriu2010.excon2.c0x.c04

import android.content.Context
import com.googlecode.tesseract.android.TessBaseAPI
import android.graphics.Bitmap
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class C04OCRUtil(context: Context) {

    private val TESS_DATA_DIR = "tessdata" + File.separator
    private val TESS_TRAINED_DATA = arrayListOf("eng.traineddata", "jpn.traineddata", "jpnnew.traineddata")

    init {
        checkTrainedData(context)
    }

    private fun checkTrainedData(context: Context) {
        val dataPath = context.filesDir.toString() + File.separator + TESS_DATA_DIR
        val dir = File(dataPath)
        if (!dir.exists() && dir.mkdirs()) {
            copyFiles(context)
        }
        if (dir.exists()) {
            TESS_TRAINED_DATA.forEach {
                val dataFilePath = dataPath + it
                val datafile = File(dataFilePath)
                if (!datafile.exists()) {
                    copyFiles(context)
                }
            }
        }
    }

    // baseApi.initの中では、言語データをFileでopenしているため
    // Fileで読み込める場所に言語データをコピーしてあげる必要があります。
    private fun copyFiles(context: Context) {
        try {
            TESS_TRAINED_DATA.forEach {
                val filePath = context.filesDir.toString() + File.separator + TESS_DATA_DIR + it

                // assets以下をinputStreamでopenしてbaseApi.initで読み込める領域にコピー
                context.assets.open(TESS_DATA_DIR + it).use { inputStream ->
                    FileOutputStream(filePath).use { outStream ->
                        val buffer = ByteArray(1024)
                        var read = inputStream.read(buffer)
                        while (read != -1) {
                            outStream.write(buffer, 0, read)
                            read = inputStream.read(buffer)
                        }
                        outStream.flush()
                    }
                }

                val file = File(filePath)
                if (!file.exists()) throw FileNotFoundException()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // 画像を文字に変換する
    fun getString(context: Context, bitmap: Bitmap, lang: String): String {
        val baseApi = TessBaseAPI()
        // initで言語データを読み込む
        baseApi.init(context.getFilesDir().toString(), lang)
        // ギャラリーから読み込んだ画像をFile or Bitmap or byte[] or Pix形式に変換して渡してあげる
        baseApi.setImage(bitmap)
        // これだけで読み取ったテキストを取得できる
        val recognizedText = baseApi.utF8Text
        baseApi.end()

        return recognizedText
    }

    companion object {
        enum class LangType constructor(val str: String) {
            jpn("jpn"),
            jpnnew("jpnnew"),
            eng("eng"),
            UNKNOWN("eng");

            companion object {

                fun getLangType(str: String): LangType {
                    val types = LangType.values()
                    for (type in types) {
                        if (type.str.equals(str)) {
                            return type
                        }
                    }
                    return UNKNOWN
                }
            }
        }
    }
}
