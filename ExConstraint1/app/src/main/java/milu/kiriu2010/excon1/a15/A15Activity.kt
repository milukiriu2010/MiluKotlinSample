package milu.kiriu2010.excon1.a15

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import milu.kiriu2010.abc.Team
import milu.kiriu2010.abc.TeamBaseBall
import milu.kiriu2010.abc.TeamSoccer
import milu.kiriu2010.excon1.id.IntentID
import milu.kiriu2010.excon1.R
import kotlinx.android.synthetic.main.activity_a15.*

// アクティビティ間のSerializableデータ授受:送信側
class A15Activity : AppCompatActivity() {
    private val teamLst: MutableList<Team> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a15)

        this.createTeamLst()

        this.setListView()

        this.setAction()
    }

    private fun setAction()
    {
        lvA15.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent( this, A15RActivity::class.java )
            val team : Team = this.teamLst[position]
            intent.putExtra( "team", team )

            startActivityForResult( intent, IntentID.ID_A15A.value )
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

        lvA15.adapter = adapter
    }


}
