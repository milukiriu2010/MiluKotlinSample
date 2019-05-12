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
import milu.kiriu2010.exdb1.draw.DrawActivity
import milu.kiriu2010.exdb1.mgl00.Mgl00Activity
import milu.kiriu2010.exdb1.mgl01.Mgl01Activity
import milu.kiriu2010.exdb1.opengl01.OpenGL01Activity
import milu.kiriu2010.exdb1.opengl02.OpenGL02Activity
import milu.kiriu2010.exdb1.opengl03.OpenGL03Activity
import milu.kiriu2010.exdb1.opengl04.OpenGL04Activity
import milu.kiriu2010.exdb1.opengl05.OpenGL05Activity
import milu.kiriu2010.exdb1.opengl06.OpenGL06Activity
import milu.kiriu2010.exdb1.opengl07.OpenGL07Activity
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
        btnOpenGL01.transformationMethod = null
        btnOpenGL01.setOnClickListener {
            val intent = Intent(this, OpenGL01Activity::class.java)
            startActivity(intent)
        }

        // 描画OpenGL02ページへ遷移
        btnOpenGL02.transformationMethod = null
        btnOpenGL02.setOnClickListener {
            val intent = Intent(this, OpenGL02Activity::class.java)
            startActivity(intent)
        }

        // 描画OpenGL03ページへ遷移
        btnOpenGL03.transformationMethod = null
        btnOpenGL03.setOnClickListener {
            val intent = Intent(this, OpenGL03Activity::class.java)
            startActivity(intent)
        }

        // 描画OpenGL04ページへ遷移
        btnOpenGL04.transformationMethod = null
        btnOpenGL04.setOnClickListener {
            val intent = Intent(this, OpenGL04Activity::class.java)
            startActivity(intent)
        }

        // 描画OpenGL05ページへ遷移
        btnOpenGL05.transformationMethod = null
        btnOpenGL05.setOnClickListener {
            val intent = Intent(this, OpenGL05Activity::class.java)
            startActivity(intent)
        }

        // 描画OpenGL06ページへ遷移
        btnOpenGL06.transformationMethod = null
        btnOpenGL06.setOnClickListener {
            val intent = Intent(this, OpenGL06Activity::class.java)
            startActivity(intent)
        }

        // 描画OpenGL07ページへ遷移
        btnOpenGL07.transformationMethod = null
        btnOpenGL07.setOnClickListener {
            val intent = Intent(this, OpenGL07Activity::class.java)
            startActivity(intent)
        }

        // 描画MGL00ページへ遷移
        btnMGL00.transformationMethod = null
        btnMGL00.setOnClickListener {
            val intent = Intent(this, Mgl00Activity::class.java)
            startActivity(intent)
        }

        // 描画MGL01ページへ遷移
        btnMGL01.transformationMethod = null
        btnMGL01.setOnClickListener {
            val intent = Intent(this, Mgl01Activity::class.java)
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
