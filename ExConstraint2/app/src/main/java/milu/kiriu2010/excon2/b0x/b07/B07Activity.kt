package milu.kiriu2010.excon2.b0x.b07

import android.app.ActivityManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_b07.*
import milu.kiriu2010.excon2.R

// 起動中アプリ一覧を表示
class B07Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b07)

        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        // 起動中のアプリプロセスを取得
        val appProcesses = activityManager.runningAppProcesses

        // リサイクラービューのレイアウト
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvB07.layoutManager = layoutManager

        // リサイクラービューのアダプタ
        val adapter = B07Adapter(this, appProcesses)
        rvB07.adapter = adapter

        // 枠線
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        rvB07.addItemDecoration(itemDecoration)
    }
}
