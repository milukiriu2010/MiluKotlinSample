package milu.kiriu2010.excon2.b0x.ringtone

import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_ringtone.*
import milu.kiriu2010.excon2.R

class RingtoneActivity : AppCompatActivity() {

    private var ringType: Int = RingtoneManager.TYPE_RINGTONE
    private lateinit var uri: Uri
    private lateinit var ringTone: Ringtone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ringtone)

        // 再生開始
        btnA02A.setOnClickListener {
            // 現在設定されている着信音を選択する
            //val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            //val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            //val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            uri = RingtoneManager.getDefaultUri(ringType)
            ringTone = RingtoneManager.getRingtone(this,uri)

            // 再生
            ringTone.play()
            // 着信音を表示
            dataRingtone.text = ringTone.getTitle(this)

            // 着信音一覧を表示しようと思ったが、でてこない。
            val ringtonManager = RingtoneManager(applicationContext)
            ringtonManager.setType(RingtoneManager.TYPE_RINGTONE)
            val cursor = ringtonManager.cursor
            //Log.d(javaClass.simpleName, "count:${cursor.columnCount}" )
            //cursor.moveToFirst()
            while (cursor.moveToNext()) {
                Log.d(javaClass.simpleName, "TITLE: " + cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX))
            }
        }

        // 再生停止
        btnStop.setOnClickListener {
            // 停止
            ringTone.stop()
        }

        // RingtoneManager.getDefaultUriで取得するタイプを設定
        radioGroupRingTone.setOnCheckedChangeListener { _, checkedId ->
            ringType = when (checkedId) {
                R.id.radioButtonRingtone -> RingtoneManager.TYPE_RINGTONE
                R.id.radioButtonNotification -> RingtoneManager.TYPE_NOTIFICATION
                R.id.radioButtonAlarm -> RingtoneManager.TYPE_ALARM
                else -> RingtoneManager.TYPE_RINGTONE
            }
        }
        radioGroupRingTone.check(R.id.radioButtonRingtone)
    }
}
