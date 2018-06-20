package com.example.milu.intent2

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import com.example.milu.intent2.abc.Team
import com.example.milu.intent2.abc.TeamBaseBall
import com.example.milu.intent2.abc.TeamBaseBall.LEAGUE.*
import com.example.milu.intent2.abc.TeamSoccer

class MainActivity : AppCompatActivity() {
    private val ID_USER_ADD = 1
    private val teamLst: MutableList<Team> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.createTeamLst()

        this.setListView()

        btnAddUser.setOnClickListener{
            val intent = Intent( this, UserAddActivity::class.java )
            val strFirstName = txtFirstName.text ?: "<arere>"
            Log.d("aXXXXXXXXXXXX:", strFirstName.toString() )
            intent.putExtra("firstName", strFirstName.toString() )

            startActivityForResult( intent, ID_USER_ADD )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if ( requestCode == ID_USER_ADD ){

        }
        //if ( resultCode == Activity.RESULT_OK )
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun createTeamLst() {
        var tigers = TeamBaseBall()
        tigers.type = "baseball"
        tigers.name = "tigers"
        tigers.league = CENTRAL
        tigers.addPlayer("nomi")
        tigers.addPlayer("fujinami")
        tigers.putYearPosMap(2016,4)
        tigers.putYearPosMap(2017,2)
        this.teamLst.add(tigers)

        var giants = TeamBaseBall()
        giants.type = "baseball"
        giants.name = "giants"
        giants.league = CENTRAL
        giants.addPlayer("sugano")
        giants.addPlayer("sawamura")
        giants.putYearPosMap(2016,2)
        giants.putYearPosMap(2017,4)
        this.teamLst.add(giants)

        var urawa = TeamSoccer()
        urawa.type = "soccer"
        urawa.name = "urawa"
        urawa.addPlayer("makino")
        urawa.addPlayer("kuroki")
        urawa.putYearPosMap(2016, 1)
        urawa.putYearPosMap(2017, 2)
        this.teamLst.add(urawa)

        var kashima = TeamSoccer()
        kashima.type = "soccer"
        kashima.name = "kashima"
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

        teamListView.adapter = adapter


    }
}
