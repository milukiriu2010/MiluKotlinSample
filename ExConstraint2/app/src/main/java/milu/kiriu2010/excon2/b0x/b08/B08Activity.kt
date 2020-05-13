package milu.kiriu2010.excon2.b0x.b08

import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_b08.*
import milu.kiriu2010.excon2.R

// 着信音
class B08Activity : AppCompatActivity() {

    private var ringType: Int = RingtoneManager.TYPE_RINGTONE
    private lateinit var uri: Uri
    private lateinit var ringTone: Ringtone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b08)

        // 再生開始
        btnB08A.setOnClickListener {
            // 現在設定されている着信音を選択する
            //val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            //val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            //val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            uri = RingtoneManager.getDefaultUri(ringType)
            ringTone = RingtoneManager.getRingtone(this,uri)

            // 再生
            ringTone.play()
            // 着信音を表示
            tvB08.text = ringTone.getTitle(this)

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
        btnB08B.setOnClickListener {
            // 停止
            ringTone.stop()
        }

        // RingtoneManager.getDefaultUriで取得するタイプを設定
        rgB08.setOnCheckedChangeListener { _, checkedId ->
            ringType = when (checkedId) {
                R.id.rbB08Ringtone -> RingtoneManager.TYPE_RINGTONE
                R.id.rbB08Notification -> RingtoneManager.TYPE_NOTIFICATION
                R.id.rbB08Alarm -> RingtoneManager.TYPE_ALARM
                else -> RingtoneManager.TYPE_RINGTONE
            }
        }
        rgB08.check(R.id.rbB08Ringtone)
    }
}
