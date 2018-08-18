package com.example.milu.excon1

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.milu.abc.Team
import com.example.milu.abc.TeamBaseBall
import com.example.milu.abc.TeamSoccer
import com.example.milu.excon1.R
import kotlinx.android.synthetic.main.activity_team_list.*

class TeamListActivity : AppCompatActivity() {
    private val teamLst: MutableList<Team> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_list)

        this.createTeamLst()

        this.setListView()

        this.setAction()
    }

    private fun setAction()
    {
        lvTeamLst.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent( this, TeamActivity::class.java )
            val team : Team = this.teamLst[position]
            intent.putExtra( "team", team )

            startActivityForResult( intent, IntentID.ID_TEAM.value )
        }

        btnBack.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private fun createTeamLst() {
        var tigers = TeamBaseBall()
        tigers.type = "baseball"
        tigers.name = "tigers"
        tigers.league = TeamBaseBall.LEAGUE.CENTRAL
        tigers.addPlayer("nomi")
        tigers.addPlayer("fujinami")
        tigers.putYearPosMap(2016,4)
        tigers.putYearPosMap(2017,2)
        this.teamLst.add(tigers)

        var giants = TeamBaseBall()
        giants.type = "baseball"
        giants.name = "giants"
        giants.league = TeamBaseBall.LEAGUE.CENTRAL
        giants.addPlayer("sugano")
        giants.addPlayer("sawamura")
        giants.putYearPosMap(2016,2)
        giants.putYearPosMap(2017,4)
        this.teamLst.add(giants)

        var urawa = TeamSoccer()
        urawa.type = "soccer"
        urawa.name = "urawa"
        urawa.level = 1
        urawa.addPlayer("makino")
        urawa.addPlayer("kuroki")
        urawa.putYearPosMap(2016, 1)
        urawa.putYearPosMap(2017, 2)
        this.teamLst.add(urawa)

        var kashima = TeamSoccer()
        kashima.type = "soccer"
        kashima.name = "kashima"
        kashima.level = 2
        kashima.addPlayer("ogasawara")
        kashima.addPlayer("sogahata")
        kashima.putYearPosMap(2016, 11)
        kashima.putYearPosMap( 2017, 2)
        this.teamLst.add(kashima)
    }

    private fun setListView(){
        val adapter = ArrayAdapter<Team>(
                this,
                android.R.layout.simple_list_item_1,
                this.teamLst
        )

        lvTeamLst.adapter = adapter
    }


}
