package milu.kiriu2010.excon1.a15

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SimpleAdapter
import kotlinx.android.synthetic.main.activity_a15r.*
import milu.kiriu2010.abc.Team
import milu.kiriu2010.abc.TeamBaseBall
import milu.kiriu2010.abc.TeamSoccer
import milu.kiriu2010.excon1.R

// ----------------------------------------------
// チーム情報を表示する
// ----------------------------------------------
// アクティビティ間のSerializableデータ授受:受信側
// ----------------------------------------------
class A15TeamActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a15r)

        val team : Team = intent.getSerializableExtra("team" ) as Team

        tvA15_Type.text = team.type
        tvA15_Name.text = team.name

        // チームが野球の場合、リーグを表示
        tvA15_League.visibility = when (team) {
            is TeamBaseBall -> View.VISIBLE
            // INVISIBLEだと、みえなくなるだけ
            // GONEは、みえなくなるだけでなく、隙間を詰めてくれる
            else -> View.GONE
        }
        tvA15_League.text = when (team) {
            is TeamBaseBall -> team.league.wamei
            else -> "xxx"
        }

        // チームがサッカーの場合、レベルを表示
        tvA15_Level.visibility = when (team){
            is TeamSoccer -> View.VISIBLE
            else -> View.GONE
        }
        tvA15_Level.text = when (team) {
            is TeamSoccer -> team.level.toString()
            else -> "xxx"
        }

        // プレイヤー一覧
        val adapterPlayers = ArrayAdapter(this, android.R.layout.simple_list_item_1, team.playerLst)
        lvA15_Players.adapter = adapterPlayers

        // 順位一覧
        val yearPosLst = mutableListOf<MutableMap<String,Int>>()
        // 年の降順にリストを並べる
        team.yearPosMap.toSortedMap(reverseOrder()).forEach {
            val data = mutableMapOf<String,Int>()
            data["year"] = it.key
            data["pos"] = it.value
            yearPosLst.add(data)
        }
        val adapterYearPos = SimpleAdapter(this,
                yearPosLst,
                android.R.layout.simple_list_item_2,
                arrayOf("year","pos"),
                intArrayOf(android.R.id.text1,android.R.id.text2)
        )
        lvA15_YearPos.adapter = adapterYearPos
    }
}
