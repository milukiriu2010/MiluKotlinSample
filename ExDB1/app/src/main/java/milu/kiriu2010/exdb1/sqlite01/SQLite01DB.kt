package milu.kiriu2010.exdb1.sqlite01

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

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

    companion object {
        private const val DB_NAME = "SampleDatabase"
        private const val DB_VERSION = 1

        // textsテーブルから保存されているテキストを検索する
        fun queryTexts(context: Context): MutableList<SQLite01Data> {
            // 読み込み用のデータベースを開く
            val database = SQLite01DB(context).readableDatabase
            // データベースから全件検索する
            val cursor = database.query("texts",
                    null,
                    null,
                    null,
                    null,
                    null,
                    "created_at DESC")

            val datas = mutableListOf<SQLite01Data>()
            cursor.use {
                // カーソルで順次処理していく
                while(cursor.moveToNext()) {
                    // ID
                    val id = cursor.getInt(cursor.getColumnIndex("_id"))
                    // テキスト
                    val text = cursor.getString(cursor.getColumnIndex("text"))
                    // 作成日
                    val created_at = cursor.getString(cursor.getColumnIndex("created_at"))

                    datas.add(SQLite01Data(id,text,created_at))
                }
            }

            database.close()
            return datas
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

        // textsテーブルからレコードを削除
        fun deleteText(context: Context, data: SQLite01Data) {
            // 書き込み可能なデータベースを開く
            val database = SQLite01DB(context).writableDatabase

            database.use { db ->
                db.delete("texts", "_id = ?", arrayOf(data.id.toString()) )
            }
        }
    }
}

