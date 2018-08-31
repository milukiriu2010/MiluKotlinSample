package milu.kiriu2010.exdb1.scheduler

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

// Realmのモデルクラスは継承できるようopen修飾子をつける
open class Schedule: RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var date: Date = Date()
    var title: String = ""
    var detail: String = ""
}