package milu.kiriu2010.exdb1.realm01

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.kotlin.where
import milu.kiriu2010.exdb1.R

import kotlinx.android.synthetic.main.activity_realm01.*
import kotlinx.android.synthetic.main.layout_ream01.*
import org.jetbrains.anko.startActivity

// Realmを使ったスケジューラ
// スケジューラのリストを表示
class Realm01Activity : AppCompatActivity() {
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_realm01)
        setSupportActionBar(tbRealm01)

        // setDefaultConfigurationメソッドに設定したデータベースを取得しRealmインスタンスを返す
        realm = Realm.getDefaultInstance()
        // Realmインスタンスからデータを取得するクエリを発行
        val schedules = realm.where<Schedule>().findAll()
        lvRealm01.adapter = Realm01Adapter(schedules)
        
        // アイテムクリックでスケジュール詳細を表示する画面へ遷移
        lvRealm01.setOnItemClickListener { parent, _, position, _ ->
            val schedule = parent.getItemAtPosition(position) as Schedule
            // レコードのプライマリキーを渡す
            startActivity<ScheduleEditActivity>( "schedule_id" to schedule.id )
        }

        fabRealm01.setOnClickListener { _ ->
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
