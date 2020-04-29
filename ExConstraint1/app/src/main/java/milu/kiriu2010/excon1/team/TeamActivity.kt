package milu.kiriu2010.excon1.team

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_team.*
import milu.kiriu2010.abc.Team
import milu.kiriu2010.abc.TeamSoccer
import milu.kiriu2010.excon1.R

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
