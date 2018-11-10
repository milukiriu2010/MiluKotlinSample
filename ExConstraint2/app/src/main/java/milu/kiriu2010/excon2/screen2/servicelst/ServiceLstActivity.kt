package milu.kiriu2010.excon2.screen2.servicelst

import android.app.ActivityManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_service_lst.*
import milu.kiriu2010.excon2.R

class ServiceLstActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_lst)

        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        // 起動中のアプリプロセスを取得
        val appProcesses = activityManager.runningAppProcesses

        // リサイクラービューのレイアウト
        val layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recyclerViewService.layoutManager = layoutManager

        // リサイクラービューのアダプタ
        val adapter = ServiceRecyclerViewAdapter(this, appProcesses)
        recyclerViewService.adapter = adapter

        // 枠線
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerViewService.addItemDecoration(itemDecoration)
    }
}
