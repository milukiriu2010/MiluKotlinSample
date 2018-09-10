package milu.kiriu2010.excon1.notify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.annotation.RequiresApi
import milu.kiriu2010.id.NotificationChannelID

// https://qiita.com/naoi/items/03e76d10948fe0d45597
class ForegroundService : Service() {

    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val name = "通知のタイトル的情報を設定"
        val notifyDescription = "この通知の詳細情報を設定します"

        if (manager.getNotificationChannel(NotificationChannelID.ID_FOREGROUND.id) == null) {
            val mChannel = NotificationChannel(NotificationChannelID.ID_FOREGROUND.id, name, NotificationManager.IMPORTANCE_HIGH)
            mChannel.apply {
                description = notifyDescription
            }
            manager.createNotificationChannel(mChannel)
        }


        // サービスがシステムによって強制的に終了させられた場合のふるまいを指定
        // ------------------------------------------------------------------------
        // START_STICKY
        //   サービスを再起動する。
        //   ただし、まだ処理していないインテントがない場合、
        //   onStartCommand()に渡されるインテントはnullになる
        // ------------------------------------------------------------------------
        // START_NOT_STICKY
        //   サービスを再起動しない。
        //   ただし、まだ処理していないインテントがある場合、
        //   再起動される
        // ------------------------------------------------------------------------
        // START_REDELIVER_INTENT
        //   サービスを再起動する。
        //   onStartCommand()には、
        //   終了させられる前のインテントが渡される
        // ------------------------------------------------------------------------
        return START_STICKY
    }
}
