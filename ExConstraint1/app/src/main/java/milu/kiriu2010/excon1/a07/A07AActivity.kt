package milu.kiriu2010.excon1.a07

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import milu.kiriu2010.excon1.R
import kotlinx.android.synthetic.main.activity_a07a.*

// タイムゾーンを一覧表示
class A07AActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a07a)

        // tap "Cancel" button
        setResult(Activity.RESULT_CANCELED)

        // タイムゾーンを表示するアダプタ
        val adapter = A07AAdapter(this)
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
    }
}
