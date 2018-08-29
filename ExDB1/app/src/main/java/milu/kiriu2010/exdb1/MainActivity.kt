package milu.kiriu2010.exdb1

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.exdb1.scheduler.SchedulerActivity
import milu.kiriu2010.exdb1.sqlite.SQLiteOpenHelperActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
