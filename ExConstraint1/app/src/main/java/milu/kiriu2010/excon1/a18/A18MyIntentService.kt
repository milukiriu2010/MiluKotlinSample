package milu.kiriu2010.excon1.a18

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.util.Log
import milu.kiriu2010.excon1.R

// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
private const val ACTION_FOO = "milu.kiriu2010.excon1.notify.action.FOO"
private const val ACTION_BAZ = "milu.kiriu2010.excon1.notify.action.BAZ"

// TODO: Rename parameters
private const val EXTRA_PARAM1 = "milu.kiriu2010.excon1.notify.extra.PARAM1"
private const val EXTRA_PARAM2 = "milu.kiriu2010.excon1.notify.extra.PARAM2"

class MyIntentService : IntentService("MyIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_FOO -> {
                val param1 = intent.getStringExtra(EXTRA_PARAM1)
                val param2 = intent.getStringExtra(EXTRA_PARAM2)
                handleActionFoo(param1!!, param2!!)
            }
            ACTION_BAZ -> {
                //val param1 = intent.getStringExtra(EXTRA_PARAM1)
                //val param2 = intent.getStringExtra(EXTRA_PARAM2)
                //handleActionBaz(param1, param2)
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionFoo(param1: String, param2: String) {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val name = "通知のタイトル的情報を設定"
        val notifyDescription = "この通知の詳細情報を設定します"

        // 通知チャネルを作成
        if (manager.getNotificationChannel(A18NotificationChannelID.ID_MY_INTENTSERVICE.id) == null) {
            val mChannel = NotificationChannel(A18NotificationChannelID.ID_MY_INTENTSERVICE.id,
                    name,
                    NotificationManager.IMPORTANCE_HIGH)
            mChannel.apply {
                description = notifyDescription
            }
            manager.createNotificationChannel(mChannel)
        }

        // 通知を作成
        val notification = NotificationCompat
                .Builder(this, A18NotificationChannelID.ID_MY_INTENTSERVICE.id).apply {
                    setContentTitle("通知のタイトル(MyIntentService:${param1})")
                    setContentText("MyIntentService(${param2})完了")
                    setSmallIcon(R.mipmap.ic_launcher)
                }.build()

        // 10秒後に通知
        Thread(
                Runnable {
                    (0..5).map {
                        Thread.sleep(2000)
                    }
                    Log.d(javaClass.simpleName, "MyIntentService is done.")
                    // 通知する
                    NotificationManagerCompat
                            .from(this)
                            .notify(A18NotificationID.ID_MY_INTENTSERVICE.id, notification)
                }
        ).start()
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    //private fun handleActionBaz(param1: String, param2: String) {
    //}

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        @JvmStatic
        fun startActionFoo(context: Context, param1: String, param2: String) {
            val intent = Intent(context, MyIntentService::class.java).apply {
                action = ACTION_FOO
                putExtra(EXTRA_PARAM1, param1)
                putExtra(EXTRA_PARAM2, param2)
            }
            context.startService(intent)
            //context.startForegroundService(intent)
        }

        /**
         * Starts this service to perform action Baz with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        @JvmStatic
        fun startActionBaz(context: Context, param1: String, param2: String) {
            val intent = Intent(context, MyIntentService::class.java).apply {
                action = ACTION_BAZ
                putExtra(EXTRA_PARAM1, param1)
                putExtra(EXTRA_PARAM2, param2)
            }
            context.startService(intent)
        }
    }
}
