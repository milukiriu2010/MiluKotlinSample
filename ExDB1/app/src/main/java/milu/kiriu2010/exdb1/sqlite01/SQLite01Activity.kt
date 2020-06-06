package milu.kiriu2010.exdb1.sqlite01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import milu.kiriu2010.exdb1.R

// SQLiteを使ったサンプル
// 入力した内容をSQLiteに保存し、
// 選択すると、DBへ保存
class SQLite01Activity : AppCompatActivity() {

    lateinit var datas: MutableList<SQLite01Data>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sqlite01)

        val btnSQLite01 = findViewById<Button>(R.id.btnSQLite01)
        // "追加"ボタンをクリックすると、SQLiteにメモを保存する
        btnSQLite01.setOnClickListener {
            val editText = findViewById<EditText>(R.id.etSQLite01)
            SQLite01DB.insertText(this,editText.text.toString())
            show()
        }

        show()
    }

    // 保存されている文字列をリスト表示する
    private fun show() {
        // データベースに登録されているオブジェクトの一覧を得る
        datas = SQLite01DB.queryTexts(this)
        val texts = datas.map { it.text }
        val listView = findViewById<ListView>(R.id.lvSQLite01)
        listView.adapter = ArrayAdapter(this, R.layout.adapter_sqlite01, R.id.tvSQLite01, texts)

        listView.setOnItemLongClickListener { parent, view, pos, id ->
            //Toast.makeText(this, texts[pos], Toast.LENGTH_LONG).show()

            AlertDialog.Builder(this)
                    .setTitle("データ削除しますか？")
                    .setPositiveButton("OK", { _, _ ->
                        val data = datas[pos]
                        SQLite01DB.deleteText(this,data)
                        show()
                    })
                    .setNegativeButton("キャンセル", { _, _ ->
                    })
                    .show()


            true
        }
    }
}
