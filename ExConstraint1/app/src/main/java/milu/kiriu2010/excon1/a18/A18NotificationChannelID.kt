package milu.kiriu2010.excon1.a18

enum class A18NotificationChannelID(val id: String) {
    // A18Actitivityで作成している通知チャネル
    ID_NEW_ARTICLE("new articles"),
    // ForegoundServiceで作成している通知チャネル
    ID_FOREGROUND("casareal_foreground"),
    // ダウンロードで作成している通知チャネル
    ID_DOWNLOAD("download"),
    // MyIntentServiceで作成している通知チャネル
    ID_MY_INTENTSERVICE("my_intent")
}