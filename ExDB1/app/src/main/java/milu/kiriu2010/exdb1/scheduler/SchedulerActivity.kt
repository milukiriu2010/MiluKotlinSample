package milu.kiriu2010.exdb1.scheduler

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.kotlin.where
import milu.kiriu2010.exdb1.R

import kotlinx.android.synthetic.main.activity_scheduler.*
import kotlinx.android.synthetic.main.content_scheduler.*
import org.jetbrains.anko.startActivity

class SchedulerActivity : AppCompatActivity() {
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scheduler)
        setSupportActionBar(toolbar)

        // setDefaultConfigurationメソッドに設定したデータベースを取得しRealmインスタンスを返す
        realm = Realm.getDefaultInstance()
        // Realmインスタンスからデータを取得するクエリを発行
        val schedules = realm.where<Schedule>().findAll()
        listView.adapter = ScheduleAdapter(schedules)
        
        // アイテムクリックでスケジュール詳細を表示する画面へ遷移
        listView.setOnItemClickListener { parent, _, position, _ ->
            val schedule = parent.getItemAtPosition(position) as Schedule
            // レコードのプライマリキーを渡す
            startActivity<ScheduleEditActivity>( "schedule_id" to schedule.id )
        }

        fab.setOnClickListener { _ ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //        .setAction("Action", null).show()
            startActivity<ScheduleEditActivity>()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }


}
