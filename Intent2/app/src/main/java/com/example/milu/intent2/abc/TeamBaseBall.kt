package com.example.milu.intent2.abc

class TeamBaseBall( ) : Team() {
    enum class LEAGUE {
        CENTRAL,
        PACIFIC,
        UNKOWN
    }

    var league: TeamBaseBall.LEAGUE = TeamBaseBall.LEAGUE.UNKOWN
}
