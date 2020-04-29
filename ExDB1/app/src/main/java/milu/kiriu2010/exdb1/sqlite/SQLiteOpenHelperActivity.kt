package milu.kiriu2010.exdb1.sqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import milu.kiriu2010.exdb1.R

class SQLiteOpenHelperActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sqlite_open_helper)

        val btnAdd = findViewById<Button>(R.id.btnAdd)
        btnAdd.setOnClickListener {
            val editText = findViewById<EditText>(R.id.editText)
            insertText(this,editText.text.toString())
            show()
        }

        show()
    }

    // 保存されている文字列をリスト表示する
    private fun show() {
        // データベースに登録されている文字列の一覧を得る
        val texts = queryTexts(this)
        val listView = findViewById<ListView>(R.id.listView)
        listView.adapter = ArrayAdapter<String>(this, R.layout.list_row_text, R.id.textView, texts)
    }
}
