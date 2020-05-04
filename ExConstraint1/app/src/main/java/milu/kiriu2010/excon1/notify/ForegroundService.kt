package milu.kiriu2010.excon1.notify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import milu.kiriu2010.excon1.R
import milu.kiriu2010.excon1.id.NotificationChannelID
import milu.kiriu2010.excon1.id.NotificationID

// https://qiita.com/naoi/items/03e76d10948fe0d45597
class ForegroundService : Service() {

    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
            val name = "通知のタイトル的情報を設定"
            val notifyDescription = "この通知の詳細情報を設定します"

            if (manager.getNotificationChannel(NotificationChannelID.ID_FOREGROUND.id) == null) {
                val mChannel = NotificationChannel(NotificationChannelID.ID_FOREGROUND.id, name, NotificationManager.IMPORTANCE_HIGH)
                mChannel.apply {
                    description = notifyDescription
                }
                manager.createNotificationChannel(mChannel)
            }
        }
        else {

        }

        // 通知を作成
        val notification = NotificationCompat
                .Builder(this, NotificationChannelID.ID_FOREGROUND.id).apply {
                    setContentTitle("通知のタイトル(ForegroundService)")
                    setContentText("通知の内容(ForegroundService)")
                    setSmallIcon(R.mipmap.ic_launcher)
                }.build()

        Thread(
                Runnable {
                    (0..5).map {
                        Thread.sleep(1000)
                    }

                    if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
                        // サービスが終了する際に呼び出す
                        // STOP_FOREGROUND_REMOVE  => 通知を削除
                        // STOP_FOREGROUND_DETACH => 通知を残す
                        stopForeground(Service.STOP_FOREGROUND_REMOVE)
                    }
                    else {
                        // サービスが終了する際に呼び出す
                        // true  => 通知を削除
                        // false => 通知を残す
                        stopForeground(true)
                        // もしくは
                        // stopSelf()
                        // この場合は通知は削除される
                    }
                }).start()

        // 通知を表示する
        // ActivityでstartForegoundServiceを呼んでから
        // 5秒以内にstartForegroundを呼び出さないとANRになるらしい
        startForeground(NotificationID.ID_FOREGROUND.id, notification)
        // こっちでも呼べる
        //manager.notify(NotificationID.ID_FOREGROUND.id, notification)

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
