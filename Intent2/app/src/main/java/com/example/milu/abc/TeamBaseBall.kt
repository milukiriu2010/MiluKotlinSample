package com.example.milu.abc

class TeamBaseBall( ) : Team() {
    enum class LEAGUE {
        CENTRAL,
        PACIFIC,
        UNKOWN
    }

    var league: TeamBaseBall.LEAGUE = TeamBaseBall.LEAGUE.UNKOWN
}
