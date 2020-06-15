package milu.kiriu2010.excon1.a18

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import android.util.Log
import kotlinx.android.synthetic.main.activity_a18.*
import milu.kiriu2010.excon1.R

// 通知
class A18Activity : AppCompatActivity() {
    private lateinit var notificationManager: NotificationManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a18)

        // 通知チャネルの作成
        createChannel()

        // "通知"ボタンをクリック
        btnNotify.setOnClickListener {
            val handler = Handler()
            // 10秒後に通知を実施
            handler.postDelayed(NotifyHandler(),1000 * dataDelayA18.text.toString().toLong() )
        }

        // "startForegroundService"ボタンをクリック
        btnNotifyStartService.setOnClickListener {
            val intent = Intent(this,A18ForegroundService::class.java)
            startForegroundService(intent)
        }

        // "Download"ボタンをクリック
        btnNotifyDownload.transformationMethod = null
        btnNotifyDownload.setOnClickListener {
            val intent = Intent(this,A18DownloadService::class.java)
            startForegroundService(intent)
        }

        // "IntentService開始"ボタンをクリック
        btnNotifyIntentStart.transformationMethod = null
        btnNotifyIntentStart.setOnClickListener {
            /*
            val intent = Intent(this,MyIntentService::class.java)
            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
                // minSdkVersion >= 26
                startForegroundService(intent)
            }
            else {
                // minSdkVersion < 26
                startService(intent)
            }
            */
            MyIntentService.startActionFoo(this,"a", "b")
        }

        // "IntentService終了"ボタンをクリック
        btnNotifyIntentStop.transformationMethod = null
        btnNotifyIntentStop.setOnClickListener {
            // うまく動いてない
            val intent = Intent(this,MyIntentService::class.java)
            stopService(intent)
            //MyIntentService.startActionFoo(this,"a", "b")
        }
    }

    // 通知チャネルの作成
    private fun createChannel() {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 通知チャネルはAndroid 8.0で導入された機能
        val channel = NotificationChannel(
                A18NotificationChannelID.ID_NEW_ARTICLE.id,
                "新着記事",
                NotificationManager.IMPORTANCE_DEFAULT)

        channel.description = "新着記事を通知するためのチャネルです"
        channel.enableVibration(true)
        channel.setShowBadge(true)
        notificationManager.createNotificationChannel(channel)
    }

    // 通知を実施
    private fun sendNotify() {
        Log.d( javaClass.simpleName, "sendNotify")

        // 通知タップ時に開かれる画面へのインテント
        val intent = Intent(this, A18Activity::class.java)
        // ペンディングインテントに詰める
        val pendingIntent = PendingIntent.getActivity(this,
                A18NotificationRequestCode.ID_NEW_ARTICLE.code,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT)

        // NotificationCompat.Builderオブジェクトを生成
        val builder = NotificationCompat
                .Builder(this, A18NotificationChannelID.ID_NEW_ARTICLE.id)

        when (radioGroupNotificationStyle.checkedRadioButtonId) {
            // BigPictureStyleによる通知
            R.id.radioButtonPictureA18 -> {
                NotificationCompat.BigPictureStyle(builder)
                        .bigPicture(BitmapFactory.decodeResource(resources,R.drawable.a18_cat))
                        .setBigContentTitle("BigPictureStyle")
                        .setSummaryText("BigPictureStyleの表示例")
            }
            // BigTextStyleによる通知
            R.id.radioButtonTextA18 -> {
                NotificationCompat.BigTextStyle(builder)
                        .bigText("abcde\nfghij\nklmno\n")
                        .setBigContentTitle("BigTextStyle")
                        .setSummaryText("BigTextStyleの表示例")
            }
            // InboxStyleによる通知
            R.id.radioButtonInboxA18 -> {
                NotificationCompat.InboxStyle(builder)
                        .setBigContentTitle("本日の予定")
                        .addLine("  7:00 起床")
                        .addLine(" 8:30 出勤")
                        .addLine("12:00 ランチ")
                        .setSummaryText("3件の予定があります")
            }
            // ProgressBarによる通知
            // https://qiita.com/naoi/items/eb9b72ef1668957a60be
        }

        // 通知の設定を行い、Notificationオブジェクトを生成する
        val notification = builder.setSmallIcon(android.R.drawable.ic_media_play)
                .setContentTitle(dataTitleA18.text)
                .setContentText(dataContentA18.text)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()
        // 通知を行う
        notificationManager.notify(A18NotificationID.ID_NEW_ARTICLE.id, notification)
    }

    // 〇秒後に通知を発行するときに用いる
    inner class NotifyHandler: Runnable {
        override fun run() {
            // 通知を実施
            sendNotify()
        }
    }
}
