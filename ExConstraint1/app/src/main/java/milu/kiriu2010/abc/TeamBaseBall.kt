package milu.kiriu2010.abc

class TeamBaseBall : Team() {
    enum class LEAGUE(val wamei: String) {
        CENTRAL("セリーグ"),
        PACIFIC("パリーグ"),
        UNKOWN("不明")
    }

    var league: LEAGUE = LEAGUE.UNKOWN
}
