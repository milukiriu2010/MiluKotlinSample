package milu.kiriu2010.excon1.a18

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import android.util.Log
import milu.kiriu2010.excon1.R
import java.io.File
import com.github.kittinunf.fuel.httpDownload

class A18DownloadService : Service() {

    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //return super.onStartCommand(intent, flags, startId)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val name = "通知のタイトル的情報を設定"
        val notifyDescription = "この通知の詳細情報を設定します"

        // 通知チャネルを作成
        if (manager.getNotificationChannel(A18NotificationChannelID.ID_DOWNLOAD.id) == null) {
            val mChannel = NotificationChannel(A18NotificationChannelID.ID_DOWNLOAD.id,
                    name,
                    NotificationManager.IMPORTANCE_HIGH)
            mChannel.apply {
                description = notifyDescription
            }
            manager.createNotificationChannel(mChannel)
        }

        // Not Found
        //val url = "http://ftp.jaist.ac.jp/pub/apache/tomcat/tomcat-9/v9.0.6/bin/apache-tomcat-9.0.6.zip"
        //val url = "http://ftp.riken.jp/net/apache/tomcat/tomcat-8/v8.5.34/bin/apache-tomcat-8.5.34.zip"
        val url = "https://sourceforge.net/projects/miludbviewer/files/MiluDBViewer0.3.0/MiluDBViewer_Setup0.3.0.exe"
        //val url = "https://sourceforge.net/projects/miludbviewer/files/MiluDBViewer0.3.0/MiluDBViewer_Setup0.3.0.exe/download"


        val builder = NotificationCompat
                .Builder(this, A18NotificationChannelID.ID_DOWNLOAD.id)
                .apply {
                    setContentTitle("MiluDBViewerダウンロード")
                    setContentText("0%")
                    setSmallIcon(R.drawable.ic_home_black_24dp)
                    setProgress(100, 0, false)
                }

        // Notification Managerを使用する場合の記述方法は下記の通り
        // android.app.RemoteServiceException: Context.startForegroundService() did not then call Service.startForeground():
        //manager.notify(NotificationID.ID_DOWNLOAD.id,builder.build())

        startForeground(A18NotificationID.ID_DOWNLOAD.id, builder.build())
        url.httpDownload()
                .destination { _, _ ->
                    // /data/user/0/milu.kiriu2010.excon1/cache/MiluDBViewer3192732266843548202.zip
                    val tempFile = File.createTempFile("MiluDBViewer", ".zip")
                    Log.d( javaClass.simpleName, "tempFile[${tempFile.absolutePath}]")

                    tempFile
                }
                .progress { readBytes, totalBytes ->
                    Log.d( javaClass.simpleName, "readBytes[$readBytes]totalBytes[$totalBytes]")
                    val total = (readBytes.toFloat() / totalBytes.toFloat()) * 100.0
                    builder.setProgress(100, total.toInt(), false)
                    builder.setContentText("%1$.1f%%".format(total))
                    // Notification Managerを使用する場合の記述方法は下記の通り
                    //manager.notify(NotificationID.ID_DOWNLOAD.id,builder.build())
                    startForeground(A18NotificationID.ID_DOWNLOAD.id, builder.build())
                }
                .response { request, response, result ->
                    Log.d( javaClass.simpleName, "request[$request]response[$response]result[$result]")
                    //stopForeground(true)
                    stopForeground(Service.STOP_FOREGROUND_DETACH)

                    //val (res, err) = result

                    val completeNotification = NotificationCompat.Builder(this, A18NotificationChannelID.ID_DOWNLOAD.id)
                            .apply {

                                setContentTitle( "ダウンロード完了")
                                setContentText("tomcatのダウンロードが完了しました。")
                                setSmallIcon(R.drawable.ic_home_black_24dp)
                            }.build()
                    manager.notify(A18NotificationID.ID_DOWNLOAD.id, completeNotification)
                }

        return START_STICKY
    }
}
