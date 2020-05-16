package milu.kiriu2010.exdb1.sqlite01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import milu.kiriu2010.exdb1.R

// SQLiteを使ったサンプル
// 入力した内容をSQLiteに保存し、
// 選択すると、DBへ保存
class SQLite01Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sqlite01)

        val btnSQLite01 = findViewById<Button>(R.id.btnSQLite01)
        // "追加"ボタンをクリックすると、SQLiteにメモを保存する
        btnSQLite01.setOnClickListener {
            val editText = findViewById<EditText>(R.id.etSQLite01)
            insertText(this,editText.text.toString())
            show()
        }

        show()
    }

    // 保存されている文字列をリスト表示する
    private fun show() {
        // データベースに登録されている文字列の一覧を得る
        val texts = queryTexts(this)
        val listView = findViewById<ListView>(R.id.lvSQLite01)
        listView.adapter = ArrayAdapter(this, R.layout.adapter_sqlite01, R.id.tvSQLite01, texts)
    }
}
