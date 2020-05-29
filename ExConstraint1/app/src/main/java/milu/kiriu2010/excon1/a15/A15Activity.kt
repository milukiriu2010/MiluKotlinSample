package milu.kiriu2010.excon1.a15

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.widget.ArrayAdapter
import milu.kiriu2010.abc.Team
import milu.kiriu2010.abc.TeamBaseBall
import milu.kiriu2010.abc.TeamSoccer
import milu.kiriu2010.excon1.id.IntentID
import milu.kiriu2010.excon1.R
import kotlinx.android.synthetic.main.activity_a15.*
import java.util.*

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

    private fun setAction() {
        // 項目をタップすると、チーム情報の詳細を表示する
        lvA15.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent( this, A15TeamActivity::class.java )
            val team : Team = this.teamLst[position]
            intent.putExtra( "team", team )

            startActivity( intent )
        }

        // 絞り込み検索は英字とアンダーバーのみ許容
        etA15.filters = arrayOf(
                object: InputFilter {
                    override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence {
                        return if ( source.toString().matches(Regex("^[a-zA-Z_]+$") ) ) {
                            source.toString()
                        } else {
                            ""
                        }
                    }
                }
        )
        // 絞り込み検索実行
        etA15.addTextChangedListener( object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    // 入力されたフィルター文字
                    val str = it.toString()

                    if ( str.length > 0 ) {
                        val filteredLst = mutableListOf<Team>()
                        teamLst.forEach {
                            if (it.toString().toLowerCase(Locale.ROOT).contains(str)) {
                                filteredLst.add(it)
                                setListView(filteredLst)
                            }
                        }

                    }
                    else {
                        setListView()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun createTeamLst() {
        val tigers = TeamBaseBall()
        tigers.type = "baseball"
        tigers.name = "tigers"
        tigers.league = TeamBaseBall.LEAGUE.CENTRAL
        tigers.addPlayer("nomi")
        tigers.addPlayer("fujinami")
        tigers.putYearPosMap(2011,4)
        tigers.putYearPosMap(2012,5)
        tigers.putYearPosMap(2013,2)
        tigers.putYearPosMap(2014,2)
        tigers.putYearPosMap(2015,3)
        tigers.putYearPosMap(2016,4)
        tigers.putYearPosMap(2017,2)
        tigers.putYearPosMap(2018,6)
        tigers.putYearPosMap(2019,3)
        this.teamLst.add(tigers)

        val giants = TeamBaseBall()
        giants.type = "baseball"
        giants.name = "giants"
        giants.league = TeamBaseBall.LEAGUE.CENTRAL
        giants.addPlayer("sugano")
        giants.addPlayer("sawamura")
        giants.putYearPosMap(2016,2)
        giants.putYearPosMap(2017,4)
        this.teamLst.add(giants)

        val softbank = TeamBaseBall()
        softbank.type = "baseball"
        softbank.name = "softbank"
        softbank.league = TeamBaseBall.LEAGUE.PACIFIC
        softbank.addPlayer("senga")
        softbank.addPlayer("wada")
        softbank.putYearPosMap(2016,2)
        softbank.putYearPosMap(2017,1)
        this.teamLst.add(softbank)

        val urawa = TeamSoccer()
        urawa.type = "soccer"
        urawa.name = "urawa"
        urawa.level = 1
        urawa.addPlayer("makino")
        urawa.addPlayer("kuroki")
        urawa.putYearPosMap(2016, 1)
        urawa.putYearPosMap(2017, 2)
        this.teamLst.add(urawa)

        val kashima = TeamSoccer()
        kashima.type = "soccer"
        kashima.name = "kashima"
        kashima.level = 2
        kashima.addPlayer("ogasawara")
        kashima.addPlayer("sogahata")
        kashima.putYearPosMap(2016, 11)
        kashima.putYearPosMap( 2017, 2)
        this.teamLst.add(kashima)
    }

    private fun setListView(filteredLst: MutableList<Team> = mutableListOf()){
        val lst = when (filteredLst.size) {
            0 -> teamLst
            else -> filteredLst
        }

        val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                lst
        )

        lvA15.adapter = adapter
    }


}
