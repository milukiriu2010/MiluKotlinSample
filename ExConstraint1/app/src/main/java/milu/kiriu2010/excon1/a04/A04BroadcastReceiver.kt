package milu.kiriu2010.excon1.a04

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

// ブロードキャストされたインテントを受信する
class A04BroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // アプリが軌道していない場合は、自動的にアプリを起動して処理を実行する
        val intent2 = Intent(context, A04Activity::class.java)
                .putExtra("onReceive", true)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(intent2)
    }
}
