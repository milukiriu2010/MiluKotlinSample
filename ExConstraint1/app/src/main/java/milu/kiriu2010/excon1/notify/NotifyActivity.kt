package milu.kiriu2010.excon1.notify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_notify.*
import milu.kiriu2010.excon1.R
import milu.kiriu2010.id.NotificationChannelID
import milu.kiriu2010.id.NotificationRequestCode
import milu.kiriu2010.id.NotifyID

class NotifyActivity : AppCompatActivity() {
    private lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notify)

        // 通知チャネルの作成
        createChannel()


        // "通知"ボタンをクリック
        btnNotify.setOnClickListener {
            val handler = Handler()
            // 10秒後に通知を実施
            handler.postDelayed(NotifyHandler(),1000 * dataDelay.text.toString().toLong() )
        }
    }

    // 通知チャネルの作成
    private fun createChannel() {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 通知チャネルはAndroid 8.0で導入された機能
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
            val channel = NotificationChannel(NotificationChannelID.ID_NEW_ARTICLE.id, "新着記事", NotificationManager.IMPORTANCE_DEFAULT)

            channel.description = "新着記事を通知するためのチャネルです"
            channel.enableVibration(true)
            channel.setShowBadge(true)
            notificationManager.createNotificationChannel(channel)
        }
    }

    // 通知を実施
    private fun sendNotify() {
        // 通知タップ時に開かれる画面へのインテント
        val intent = Intent(this, NotifyActivity::class.java)
        // ペンディングインテントに詰める
        val pendingIntent = PendingIntent.getActivity(this,NotificationRequestCode.ID_NEW_ARTICLE.code, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // NotificationCompat.Builderオブジェクトを生成
        val builder = NotificationCompat.Builder(this,NotificationChannelID.ID_NEW_ARTICLE.id)

        // 通知の設定を行い、Notificationオブジェクトを生成する
        val notification = builder.setSmallIcon(android.R.drawable.ic_media_play)
                .setContentTitle(dataTitle.text)
                .setContentText(dataContent.text)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()
        // 通知を行う
        notificationManager.notify(NotifyID.ID_NEW_ARTICLE.id, notification)
    }

    // 〇秒後に通知を発行するときに用いる
    inner class NotifyHandler: Runnable {
        override fun run() {
            // 通知を実施
            sendNotify()
        }
    }
}
