package milu.kiriu2010.excon1.notify

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
import kotlinx.android.synthetic.main.activity_notify.*
import milu.kiriu2010.excon1.R
import milu.kiriu2010.excon1.id.NotificationChannelID
import milu.kiriu2010.excon1.id.NotificationRequestCode
import milu.kiriu2010.excon1.id.NotificationID

class NotifyActivity : AppCompatActivity() {
    private lateinit var notificationManager: NotificationManager

    @RequiresApi(Build.VERSION_CODES.O)
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

        // "startServiceによる開始"ボタンをクリック
        btnNotifyStartService.setOnClickListener {
            val intent = Intent(this,ForegroundService::class.java)
            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
                // minSdkVersion >= 26
                startForegroundService(intent)
            }
            else {
                // minSdkVersion < 26
                startService(intent)
            }
        }

        // "Download"ボタンをクリック
        btnNotifyDownload.transformationMethod = null
        btnNotifyDownload.setOnClickListener {
            val intent = Intent(this,DownloadService::class.java)
            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
                // minSdkVersion >= 26
                startForegroundService(intent)
            }
            else {
                // minSdkVersion < 26
                startService(intent)
            }
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
        Log.d( javaClass.simpleName, "sendNotify")

        // 通知タップ時に開かれる画面へのインテント
        val intent = Intent(this, NotifyActivity::class.java)
        // ペンディングインテントに詰める
        val pendingIntent = PendingIntent.getActivity(this,NotificationRequestCode.ID_NEW_ARTICLE.code, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // NotificationCompat.Builderオブジェクトを生成
        val builder = NotificationCompat.Builder(this,NotificationChannelID.ID_NEW_ARTICLE.id)

        when (radioGroupNotificationStyle.checkedRadioButtonId) {
            // BigPictureStyleによる通知
            R.id.radioButtonPicture -> {
                NotificationCompat.BigPictureStyle(builder)
                        .bigPicture(BitmapFactory.decodeResource(resources,R.drawable.cat))
                        .setBigContentTitle("BigPictureStyle")
                        .setSummaryText("BigPictureStyleの表示例")
            }
            // BigTextStyleによる通知
            R.id.radioButtonText -> {
                NotificationCompat.BigTextStyle(builder)
                        .bigText("abcde\nfghij\nklmno\n")
                        .setBigContentTitle("BigTextStyle")
                        .setSummaryText("BigTextStyleの表示例")
            }
            // InboxStyleによる通知
            R.id.radioButtonInbox -> {
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
                .setContentTitle(dataTitle.text)
                .setContentText(dataContent.text)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()
        // 通知を行う
        notificationManager.notify(NotificationID.ID_NEW_ARTICLE.id, notification)
    }

    // 〇秒後に通知を発行するときに用いる
    inner class NotifyHandler: Runnable {
        override fun run() {
            // 通知を実施
            sendNotify()
        }
    }
}
