package com.example.milu.intent2

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_team.*
import com.example.milu.intent2.abc.Team
import com.example.milu.intent2.abc.TeamBaseBall
import com.example.milu.intent2.abc.TeamBaseBall.LEAGUE.*
import com.example.milu.intent2.abc.TeamSoccer

class TeamActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team)

        val team : Team = intent.getSerializableExtra("team" ) as Team

        textType.text = team.type
        textName.text = team.name
        textLevel.text = when (team) {
            is TeamSoccer -> team.level.toString()
            else -> "xxx"
        }
        textLevel.visibility = when (team){
            is TeamSoccer -> View.VISIBLE
            else -> View.INVISIBLE
        }

        btnBack.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}