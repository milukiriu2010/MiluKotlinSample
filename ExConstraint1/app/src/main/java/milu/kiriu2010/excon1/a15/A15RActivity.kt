package milu.kiriu2010.excon1.a15

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_a15r.*
import milu.kiriu2010.abc.Team
import milu.kiriu2010.abc.TeamSoccer
import milu.kiriu2010.excon1.R

// アクティビティ間のSerializableデータ授受:受信側
class A15RActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a15r)

        val team : Team = intent.getSerializableExtra("team" ) as Team

        tvA15_Type.text = team.type
        tvA15_Name.text = team.name
        tvA15_Level.text = when (team) {
            is TeamSoccer -> team.level.toString()
            else -> "xxx"
        }
        // チームがサッカーでない場合、
        // レベルを非表示とする
        tvA15_Level.visibility = when (team){
            is TeamSoccer -> View.VISIBLE
            else -> View.INVISIBLE
        }
    }
}
