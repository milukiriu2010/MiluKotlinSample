package milu.kiriu2010.exdb1.scheduler

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_schedule_edit.*
import milu.kiriu2010.exdb1.R
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ScheduleEditActivity : AppCompatActivity() {
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_edit)

        // Realmインスタンスを取得
        realm = Realm.getDefaultInstance()

        // 前画面から渡されるレコードのプライマリキーを取得
        // "追加"ボタンの場合、渡されないため"-1"とする
        val scheduleId = intent?.getLongExtra("schedule_id", -1L)
        if (scheduleId != -1L) {
            // プライマリキーに対応するレコードをDBから取得
            val schedule = realm.where<Schedule>()
                    .equalTo("id", scheduleId).findFirst()
            dateEdit.setText(DateFormat.format("yyyy/MM.dd",schedule?.date))
            titleEdit.setText(schedule?.title)
            detailEdit.setText(schedule?.detail)
            // アイテムをクリックして遷移してきた場合は削除を可能とするため、
            // 削除ボタンを表示する
            delete.visibility = View.VISIBLE
        }
        else {
            // "追加"ボタンから遷移してきた場合は登録処理を行うので、
            // 削除ボタンは表示しない
            delete.visibility = View.INVISIBLE
        }

        // 保存ボタンをクリック
        save.setOnClickListener {
            when (scheduleId) {
                // "追加"ボタンをクリックしたら、レコードを登録する
                -1L -> {
                    realm.executeTransaction {
                        // Scheduleのidフィールドの最大値を取得
                        val maxId = realm.where<Schedule>().max("id")
                        val nextId = (maxId?.toLong() ?: 0L) + 1
                        // データを1行追加
                        val schedule = realm.createObject<Schedule>(nextId)
                        // toDateという拡張関数を定義してテキストエディタの日付をDate型にする
                        dateEdit.text.toString().toDate("yyyy/MM/dd")?.let {
                            schedule.date = it
                        }
                        schedule.title = titleEdit.text.toString()
                        schedule.detail = detailEdit.text.toString()
                    }

                    // アラートダイアログを表示
                    alert("追加しました") {
                        // ScheduleEditActivityを終了してSchedulerActivityに戻る
                        yesButton { finish() }
                    }.show()
                }
                // アイテムクリック後に編集したら、レコードを更新する
                else -> {
                    realm.executeTransaction {
                        // アイテムのプライマリキーに相当するレコードを取得
                        val schedule = realm.where<Schedule>()
                                .equalTo("id",scheduleId)
                                .findFirst()
                        // toDateという拡張関数を定義してテキストエディタの日付をDate型にする
                        dateEdit.text.toString().toDate("yyyy/MM/dd")?.let {
                            schedule?.date = it
                        }
                        schedule?.title = titleEdit.text.toString()
                        schedule?.detail = detailEdit.text.toString()
                    }

                    // アラートダイアログを表示
                    alert("修正しました") {
                        // ScheduleEditActivityを終了してSchedulerActivityに戻る
                        yesButton { finish() }
                    }.show()

                }
            }

        }

        // 削除ボタンをクリック
        delete.setOnClickListener {
            realm.executeTransaction {
                // アイテムのプライマリキーに相当するレコードを取得&削除を実施
                realm.where<Schedule>()
                        .equalTo("id",scheduleId)
                        ?.findFirst()
                        ?.deleteFromRealm()
            }
            // アラートダイアログを表示
            alert("削除しました") {
                // ScheduleEditActivityを終了してSchedulerActivityに戻る
                yesButton { finish() }
            }.show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}

// Stringクラスの拡張関数
private fun String.toDate(pattern: String = "yyyy/MM/dd HH:mm"): Date? {
    val sdFormat = try {
        SimpleDateFormat(pattern)
    } catch( e: IllegalArgumentException) {
        null
    }

    val date = sdFormat?.let {
        try{
            it.parse(this)
        }
        catch (e: ParseException) {
            null
        }
    }
    return date
}
