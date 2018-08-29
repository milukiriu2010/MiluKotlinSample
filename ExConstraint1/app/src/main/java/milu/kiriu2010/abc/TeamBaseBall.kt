package milu.kiriu2010.abc

class TeamBaseBall( ) : Team() {
    enum class LEAGUE {
        CENTRAL,
        PACIFIC,
        UNKOWN
    }

    var league: TeamBaseBall.LEAGUE = TeamBaseBall.LEAGUE.UNKOWN
}
