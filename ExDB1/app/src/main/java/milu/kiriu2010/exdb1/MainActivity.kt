package milu.kiriu2010.exdb1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import milu.kiriu2010.exdb1.a0x.A0XActivity
import milu.kiriu2010.exdb1.b0x.B0XActivity
import milu.kiriu2010.exdb1.c0x.C0XActivity
import milu.kiriu2010.exdb1.d0x.D0XActivity
import milu.kiriu2010.exdb1.es30x01.ES30x01Activity
import milu.kiriu2010.exdb1.es30x02.ES30x02Activity
import milu.kiriu2010.exdb1.es32x01.ES32x01Activity
import milu.kiriu2010.exdb1.glsl01.GLSL01Activity
import milu.kiriu2010.exdb1.glsl02.GLSL02Activity
import milu.kiriu2010.exdb1.mgl00.Mgl00Activity
import milu.kiriu2010.exdb1.mgl01.Mgl01Activity
import milu.kiriu2010.exdb1.opengl01.OpenGL01Activity
import milu.kiriu2010.exdb1.opengl02.OpenGL02Activity
import milu.kiriu2010.exdb1.opengl03.OpenGL03Activity
import milu.kiriu2010.exdb1.opengl04.OpenGL04Activity
import milu.kiriu2010.exdb1.opengl05.OpenGL05Activity
import milu.kiriu2010.exdb1.opengl06.OpenGL06Activity
import milu.kiriu2010.exdb1.opengl07.OpenGL07Activity
import milu.kiriu2010.exdb1.opengl08.OpenGL08Activity
import milu.kiriu2010.exdb1.realm01.Realm01Activity
import milu.kiriu2010.exdb1.sqlite01.SQLite01Activity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // オブジェクトのアニメーションを表示するページへ遷移
        btnA0X.setOnClickListener {
            val intent = Intent(this,A0XActivity::class.java)
            startActivity(intent)
        }

        // shapeを用いたサンプルを表示するページへ遷移
        btnB0X.setOnClickListener {
            val intent = Intent(this, B0XActivity::class.java)
            startActivity(intent)
        }

        // Canvas上のオブジェクトのアニメーションを表示するページへ遷移
        btnC0X.setOnClickListener {
            val intent = Intent(this, C0XActivity::class.java)
            startActivity(intent)
        }

        // 描画基本ページへ遷移
        btnD0X.setOnClickListener {
            val intent = Intent(this, D0XActivity::class.java)
            startActivity(intent)
        }

        // 描画OpenGL01ページへ遷移
        btnOpenGL01.setOnClickListener {
            val intent = Intent(this, OpenGL01Activity::class.java)
            startActivity(intent)
        }

        // 描画OpenGL02ページへ遷移
        btnOpenGL02.setOnClickListener {
            val intent = Intent(this, OpenGL02Activity::class.java)
            startActivity(intent)
        }

        // 描画OpenGL03ページへ遷移
        btnOpenGL03.setOnClickListener {
            val intent = Intent(this, OpenGL03Activity::class.java)
            startActivity(intent)
        }

        // 描画OpenGL04ページへ遷移
        btnOpenGL04.setOnClickListener {
            val intent = Intent(this, OpenGL04Activity::class.java)
            startActivity(intent)
        }

        // 描画OpenGL05ページへ遷移
        btnOpenGL05.setOnClickListener {
            val intent = Intent(this, OpenGL05Activity::class.java)
            startActivity(intent)
        }

        // 描画OpenGL06ページへ遷移
        btnOpenGL06.setOnClickListener {
            val intent = Intent(this, OpenGL06Activity::class.java)
            startActivity(intent)
        }

        // 描画OpenGL07ページへ遷移
        btnOpenGL07.setOnClickListener {
            val intent = Intent(this, OpenGL07Activity::class.java)
            startActivity(intent)
        }

        // 描画OpenGL08ページへ遷移
        btnOpenGL08.setOnClickListener {
            val intent = Intent(this, OpenGL08Activity::class.java)
            startActivity(intent)
        }

        // 描画GLSL ES30x01ページへ遷移
        btnES30x01.setOnClickListener {
            val intent = Intent(this, ES30x01Activity::class.java)
            startActivity(intent)
        }

        // 描画GLSL ES30x02ページへ遷移
        btnES30x02.setOnClickListener {
            val intent = Intent(this, ES30x02Activity::class.java)
            startActivity(intent)
        }

        // 描画MGL00ページへ遷移
        btnMGL00.setOnClickListener {
            val intent = Intent(this, Mgl00Activity::class.java)
            startActivity(intent)
        }

        // 描画MGL01ページへ遷移
        btnMGL01.setOnClickListener {
            val intent = Intent(this, Mgl01Activity::class.java)
            startActivity(intent)
        }

        // 描画GLSL01ページへ遷移
        btnOpenGLSL01.setOnClickListener {
            val intent = Intent(this, GLSL01Activity::class.java)
            startActivity(intent)
        }

        // 描画GLSL02ページへ遷移
        btnOpenGLSL02.setOnClickListener {
            val intent = Intent(this, GLSL02Activity::class.java)
            startActivity(intent)
        }

        // 描画GLSL ES32x01ページへ遷移
        btnES32x01.setOnClickListener {
            val intent = Intent(this, ES32x01Activity::class.java)
            startActivity(intent)
        }

    }

    // ------------------------------------------------------
    // メニューを表示する(右上)
    // ------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    // -----------------------------------------------
    // メニューをクリックしたときのアクションを記述
    // -----------------------------------------------
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            // ----------------------------------------------------
            // Realmの練習
            // ----------------------------------------------------
            R.id.item_top_realm01 -> {
                val intent = Intent(this,Realm01Activity::class.java)
                startActivity(intent)
            }
            // ----------------------------------------------------
            // SQLiteの練習
            // ----------------------------------------------------
            R.id.item_top_sqlite01 -> {
                val intent = Intent(this,SQLite01Activity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item!!)
    }
}
