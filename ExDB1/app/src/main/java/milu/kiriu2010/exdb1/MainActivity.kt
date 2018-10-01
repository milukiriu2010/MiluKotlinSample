package milu.kiriu2010.exdb1

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import milu.kiriu2010.exdb1.animeobj.AnimeObjActivity
import milu.kiriu2010.exdb1.draw.Draw1Fragment
import milu.kiriu2010.exdb1.scheduler.SchedulerActivity
import milu.kiriu2010.exdb1.sqlite.SQLiteOpenHelperActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // オブジェクトのアニメーションを表示するページへ遷移
        btnAnimeObj.transformationMethod = null
        btnAnimeObj.setOnClickListener {
            val intent = Intent(this,AnimeObjActivity::class.java)
            startActivity(intent)
        }
    }

    // ------------------------------------------------------
    // メニューを表示する(右上)
    // ------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_module,menu)
        return true
    }

    // -----------------------------------------------
    // メニューをクリックしたときのアクションを記述
    // -----------------------------------------------
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            // ----------------------------------------------------
            // "Draw1"をクリックすると、Draw1画面を表示
            // ----------------------------------------------------
            R.id.menuItemDraw1 -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.frameDraw, Draw1Fragment.newInstance())
                        .commit()
            }
            // ----------------------------------------------------
            // "Scheduler"をクリックすると、スケジューラ画面へ遷移
            // ----------------------------------------------------
            R.id.menuItemScheduler -> {
                val intent = Intent(this,SchedulerActivity::class.java)
                startActivity(intent)
            }
            // ----------------------------------------------------
            // "SQLite"をクリックすると、SQLite画面へ遷移
            // ----------------------------------------------------
            R.id.menuItemSQLite -> {
                val intent = Intent(this,SQLiteOpenHelperActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
