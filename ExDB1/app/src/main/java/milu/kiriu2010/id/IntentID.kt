package milu.kiriu2010.id

enum class IntentID(val id: Int) {
    // SchedulerActivityへ遷移する際用いる
    ID_SCHEDULER(1),
    // SQLiteOpenHelperActivityへ遷移する際用いる
    ID_SQLITE(2)
}