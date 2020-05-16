package milu.kiriu2010.exdb1.sqlite01

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

private const val DB_NAME = "SampleDatabase"
private const val DB_VERSION = 1

class SQLite01DB(context: Context):
        SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    // テーブルの作成
    // ・ID
    // ・テキスト
    // ・作成日時
    override fun onCreate(db: SQLiteDatabase?) {
        val sql = """
            |CREATE TABLE texts
            |(
            |_id         INTEGER PRIMARY KEY AUTOINCREMENT,
            |text        TEXT NOT NULL,
            |created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            |)
        """.trimMargin("|")
        Log.d(javaClass.simpleName, "SQL=[$sql]")
        db?.execSQL(sql)
    }

    // バージョン更新時のSQL発行
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

}

// textsテーブルから保存されているテキストを検索する
fun queryTexts(context: Context): List<String> {
    // 読み込み用のデータベースを開く
    val database = SQLite01DB(context).readableDatabase
    // データベースから全件検索する
    val cursor = database.query("texts",null,null,null,null,null,"created_at DESC")

    val texts = mutableListOf<String>()
    cursor.use {
        // カーソルで順次処理していく
        while(cursor.moveToNext()) {
            // 保存されているテキストを得る
            val text = cursor.getString(cursor.getColumnIndex("text"))
            texts.add(text)
        }
    }

    database.close()
    return texts
}

// textsテーブルにレコードを挿入する
fun insertText(context: Context, text: String) {
    // 書き込み可能なデータベースを開く
    val database = SQLite01DB(context).writableDatabase

    database.use { db ->
        // 挿入するレコード
        val record = ContentValues().apply {
            put("text",text)
        }
        // データベースに挿入する
        db.insert("texts",null,record)
    }
}