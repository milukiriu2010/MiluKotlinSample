package milu.kiriu2010.excon1.a07

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import milu.kiriu2010.excon1.R
import kotlinx.android.synthetic.main.activity_a07a.*
import java.util.*

// タイムゾーンを一覧表示
class A07AActivity : AppCompatActivity() {

    val timeZones = TimeZone.getAvailableIDs().toMutableList()
    val timeZonesBackUp = TimeZone.getAvailableIDs().toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a07a)

        // タイムゾーンを表示するアダプタ
        // -------------------------------------------
        // BaseAdapterを継承したアダプタを更新する場合、
        // ArraysではだめでMutableListである必要がある
        // -------------------------------------------
        val adapter = A07AAdapter(this, timeZones)
        lvA07A.adapter = adapter

        // タイムゾーンをクリックすると、前のアクティビティに、そのタイムゾーンを追加する
        lvA07A.setOnItemClickListener { _, _, position, _ ->
            val timeZone = adapter.getItem(position) as String

            val result = Intent()
            result.putExtra("timeZone", timeZone )

            setResult( Activity.RESULT_OK, result )

            // close this activity
            finish()
        }

        // 絞り込み検索は英字とアンダーバーのみ許容
        etA07A.filters = arrayOf(A07InputFilter())
        // 絞り込み検索実行
        etA07A.addTextChangedListener( object : TextWatcher {
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
                        // --------------------------------------
                        // ArrayAdapterと違って、
                        // アダプタ自体に対するclearは使えないので、
                        // リストの中身を入れ替えている
                        // --------------------------------------
                        timeZones.clear()
                        filteredTimezones.forEach {
                            timeZones.add(it)
                        }
                        adapter.notifyDataSetChanged()
                    }
                    else {
                        // アダプタの更新
                        timeZonesBackUp.forEach {
                            timeZones.add(it)
                        }
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
    private class A07InputFilter: InputFilter {
        override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence {
            return if ( source.toString().matches(Regex("^[a-zA-Z_]+$") ) ) {
                source.toString()
            } else {
                ""
            }
        }
    }
}
