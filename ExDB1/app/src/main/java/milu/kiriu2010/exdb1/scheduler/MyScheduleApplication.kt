package milu.kiriu2010.exdb1.scheduler

import android.app.Application
import io.realm.Realm

class MyScheduleApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        // Realmライブラリを初期化し、すぐに使用できるデフォルト構成を作成
        Realm.init(this)
    }
}