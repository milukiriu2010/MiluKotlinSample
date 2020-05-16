package milu.kiriu2010.exdb1.realm01

import android.app.Application
import io.realm.Realm

class Realm01Application: Application() {
    override fun onCreate() {
        super.onCreate()
        // Realmライブラリを初期化し、すぐに使用できるデフォルト構成を作成
        Realm.init(this)
    }
}