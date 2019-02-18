package milu.kiriu2010.exdb1

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import milu.kiriu2010.exdb1.animeobj.AnimeObjActivity
import milu.kiriu2010.exdb1.basic.BasicActivity
import milu.kiriu2010.exdb1.canvas.CanvasActivity
import milu.kiriu2010.exdb1.draw.Draw01Fragment
import milu.kiriu2010.exdb1.draw.DrawActivity
import milu.kiriu2010.exdb1.opengl.OpenGLActivity
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

        // shapeを用いたサンプルを表示するページへ遷移
        btnDraw.setOnClickListener {
            val intent = Intent(this, DrawActivity::class.java)
            startActivity(intent)
        }

        // Canvas上のオブジェクトのアニメーションを表示するページへ遷移
        btnCanvas.transformationMethod = null
        btnCanvas.setOnClickListener {
            val intent = Intent(this, CanvasActivity::class.java)
            startActivity(intent)
        }

        // 描画基本ページへ遷移
        btnBasic.transformationMethod = null
        btnBasic.setOnClickListener {
            val intent = Intent(this, BasicActivity::class.java)
            startActivity(intent)
        }

        // 描画OpenGLページへ遷移
        btnOpenGL.transformationMethod = null
        btnOpenGL.setOnClickListener {
            val intent = Intent(this, OpenGLActivity::class.java)
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
