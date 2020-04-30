package milu.kiriu2010.excon1.a03

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        val adapter = ArrayAdapter(this, R.layout.adapter_a03, R.id.tvA03, timeZones)
        lvA03.adapter = adapter

        // タイムゾーンのアイテムをクリックすると、クリックしたアイテムのツールチップ表示する
        lvA03.setOnItemClickListener { _, _, position, _ ->
            Toast.makeText( this, adapter.getItem(position), Toast.LENGTH_LONG ).show()
        }
    }
}
