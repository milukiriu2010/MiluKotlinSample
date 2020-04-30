package milu.kiriu2010.excon1.a04

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import milu.kiriu2010.excon1.R
import kotlinx.android.synthetic.main.activity_a04.*
import org.jetbrains.anko.toast
import java.text.ParseException
import java.util.*

// -----------------------------------------
// アラーム
// といっても、時間がきたらダイアログをだすだけ
// -----------------------------------------
// 登録されたアラームは、
// デバイすがスリープしている間は保持されるが、
// オフになって再起動されるとクリアされる
// -----------------------------------------
class A04Activity : AppCompatActivity(),
        A04AlertDialog.OnClickListener,
        A04DatePickerFragment.OnDateSelectedListener,
        A04TimePickerFragment.OnTimeSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // アラームを受信した場合に、実行される
        //
        if ( intent?.getBooleanExtra("onReceive",false) == true ) {
            /*
            // スリープ中でもダイアログが表示されるようにする
            @Suppress("DEPRECATION")
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ->
                    window.addFlags(FLAG_TURN_SCREEN_ON or FLAG_SHOW_WHEN_LOCKED)
                else ->
                    window.addFlags(FLAG_TURN_SCREEN_ON or FLAG_SHOW_WHEN_LOCKED or FLAG_DISMISS_KEYGUARD )
            }
             */
            // スリープ中でもダイアログが表示されるようにする
            // https://stackoverflow.com/questions/48277302/android-o-flag-show-when-locked-is-deprecated
            setShowWhenLocked(true)
            setTurnScreenOn(true)

            val dialog = A04AlertDialog()
            dialog.show(supportFragmentManager, "alert_dialog")
        }

        setContentView(R.layout.activity_a04)

        // アラームを設定する
        btnA04A.setOnClickListener {
            val date = "${tvA04A.text} ${tvA04B.text}".toDate()
            when {
                date != null -> {
                    val calendar = Calendar.getInstance()
                    calendar.time = date
                    // アラームを設定する
                    setAlarmManager(calendar)
                    toast("アラームをセットしました")
                }
                else -> {
                    toast("日付の形式が正しくありません")
                }
            }
        }

        // アラームを取り消す
        btnA04B.setOnClickListener {
            cancelAlarmManager()
        }

        // アラームを設定するためカレンダを表示
        tvA04A.setOnClickListener {
            val dialog = A04DatePickerFragment()
            dialog.show(supportFragmentManager, "date_dialog")
        }

        // アラームを設定するため時計を表示
        tvA04B.setOnClickListener {
            val dialog = A04TimePickerFragment()
            dialog.show(supportFragmentManager, "time_dialog")
        }
    }

    // アラームが鳴る日付を設定する
    // A04DatePickerFragment.OnDateSelectedListener
    override fun onSelected(year: Int, month: Int, date: Int) {
        val c = Calendar.getInstance()
        c.set(year,month,date)
        tvA04A.text = android.text.format.DateFormat.format("yyyy/MM/dd", c )
    }

    // アラームが鳴る時分を設定する
    // A04TimePickerFragment.OnTimeSelectedListener
    override fun onSelected(hourOfDay: Int, minute: Int) {
        tvA04B.text = "%1$02d:%2$02d".format(hourOfDay,minute)
    }

    // "起きる"ボタンを押す
    // ⇒
    // アラームダイアログを閉じる
    // A04AlertDialog.OnClickListener
    override fun onPositiveClick() {
        finish()
    }

    // "あと5分"ボタンを押す
    // ⇒
    // 5分後にアラームが発生するよう設定
    // アラームダイアログを閉じる
    // A04AlertDialog.OnClickListener
    override fun onNegativeClick() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.MINUTE,5)
        // アラームを設定する
        setAlarmManager(calendar)
        finish()
    }

    // アラームを設定する
    private fun setAlarmManager(calendar: Calendar) {
        val intent = Intent(this, A04BroadcastReceiver::class.java)
        val pending = PendingIntent.getBroadcast(this,0,intent,0)
        // 時刻になったら、インテントを全体に通知
        // ブロードキャストするインテントと、ブロードキャストする時間をAlarmManagerに設定する
        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,pending)
        /*
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                val info = AlarmManager.AlarmClockInfo(calendar.timeInMillis,null)
                am.setAlarmClock(info,pending)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
                am.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pending)
            }
            else -> {
                am.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,pending)
            }
        }
         */
    }

    // アラームを取り消す
    private fun cancelAlarmManager() {
        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent( this, A04BroadcastReceiver::class.java )
        val pending = PendingIntent.getBroadcast(this,0,intent,0)
        am.cancel(pending)
    }

    // 文字列を日付型に変換
    fun String.toDate(pattern: String = "yyyy/MM/dd HH:mm"): Date? {
        val sdFormat = try {
            SimpleDateFormat(pattern)
        } catch( e: IllegalArgumentException) {
            null
        }
        val date = sdFormat?.let {
            try {
                it.parse(this)
            } catch (e: ParseException) {
                null
            }
        }
        return date
    }
}
