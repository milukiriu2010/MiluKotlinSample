package milu.kiriu2010.excon1.a03

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Toast
import milu.kiriu2010.excon1.R
import kotlinx.android.synthetic.main.activity_a03.*
import java.util.*

// タイムゾーンの一覧をリスト表示する
class A03Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a03)

        // タイムゾーンの一覧
        val timeZones = TimeZone.getAvailableIDs()

        // タイムゾーンの一覧をアダプタに設定
        //val adapter = ArrayAdapter(this, R.layout.adapter_a03, R.id.tvA03, timeZones)
        // 引数２つバージョンでないと、ArrayAdapterは動的にリストを変更できないらしい。
        // http://www.fineblue206.net/archives/172
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
        adapter.addAll(timeZones.toMutableList())
        lvA03.adapter = adapter

        // タイムゾーンのアイテムをクリックすると、クリックしたアイテムのツールチップ表示する
        lvA03.setOnItemClickListener { _, _, position, _ ->
            Toast.makeText( this, adapter.getItem(position), Toast.LENGTH_LONG ).show()
        }

        // 絞り込み検索は英字とアンダーバーのみ許容
        etA03.filters = arrayOf(A03InputFilter())
        // 絞り込み検索実行
        etA03.addTextChangedListener( object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                s?.let{
                    val str = it.toString()

                    if ( str.length > 0 ) {
                        val filteredTimezones = mutableListOf<String>()
                        timeZones.forEach { timeZone ->
                            if ( timeZone.toLowerCase(Locale.ROOT).contains(str) ) {
                                filteredTimezones.add(timeZone)
                            }
                        }

                        // アダプタの更新
                        adapter.clear()
                        adapter.addAll(filteredTimezones)
                        adapter.notifyDataSetChanged()
                    }
                    else {
                        // アダプタの更新
                        adapter.clear()
                        adapter.addAll(timeZones.toMutableList())
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    // 検索テキストのフィルタ
    private class A03InputFilter: InputFilter {
        override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence {
            return if ( source.toString().matches(Regex("^[a-zA-Z_]+$") ) ) {
                source.toString()
            } else {
                ""
            }
        }
    }
}
