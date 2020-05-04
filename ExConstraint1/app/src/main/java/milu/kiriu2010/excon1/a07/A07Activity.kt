package milu.kiriu2010.excon1.a07

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import milu.kiriu2010.excon1.id.IntentID
import milu.kiriu2010.excon1.R
import kotlinx.android.synthetic.main.activity_a07.*
import java.util.*

// 時刻表示
// 表示するタイムゾーンを追加しリスト表示する
class A07Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a07)

        // 現在のタイムゾーンを初期表示
        val timeZone = TimeZone.getDefault()
        tvA07.text = timeZone.displayName

        // 表示するタイムゾーンを追加
        btnA07A.setOnClickListener {
            val intent = Intent( this, A07AActivity::class.java)
            startActivityForResult(intent, IntentID.ID_A07A.value)
        }

        this.showWorldClocks()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 選択したタイムゾーンを共有プリファレンスに格納し、
        // タイムゾーン一覧に表示する
        if (
                (requestCode == IntentID.ID_A07A.value) &&
                ( resultCode == Activity.RESULT_OK ) &&
                ( data != null )
                ) {
            val timeZone = data.getStringExtra("timeZone" )

            val pref = getSharedPreferences( "prefs", Context.MODE_PRIVATE )
            val timeZones = pref.getStringSet("time_zone", mutableSetOf() )

            timeZones?.add( timeZone )

            pref.edit().putStringSet("time_zone", timeZones ).apply()

            this.showWorldClocks()
        }
    }

    // 共有プリファレンスに格納されたタイムゾーン一覧を表示
    private fun showWorldClocks(){
        val pref = getSharedPreferences("prefs", Context.MODE_PRIVATE )
        val timeZones = pref.getStringSet( "time_zone", setOf() )

        timeZones?.let {
            lvA07.adapter = A07AAdapter(this, it.toTypedArray())
        }
    }
}
