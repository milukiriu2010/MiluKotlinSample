package milu.kiriu2010.excon2.a0x.a15

import android.annotation.SuppressLint
import android.app.ActionBar
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.excon2.R
import kotlinx.android.synthetic.main.activity_a15.*

// --------------------------------
// レーティングバー
// --------------------------------
// アクションバーにプログレスバーを表示
// "Load"メニューを選択すると、
// ５秒間プログレスバーが非同期で回る
// --------------------------------
class A15Activity : AppCompatActivity() {

    lateinit var menuItem: MenuItem

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a15)

        this.setRatingBarValue( rtA15.rating )

        rtA15.setOnRatingBarChangeListener { _, rating, _ ->
            this.setRatingBarValue( rating )
        }

        // http://www.vogella.com/tutorials/AndroidActionBar/article.html
        // java.lang.IllegalStateException: actionBar must not be null
        /*
        actionBar.displayOptions =
                ActionBar.DISPLAY_SHOW_HOME or
                ActionBar.DISPLAY_SHOW_TITLE or
                ActionBar.DISPLAY_SHOW_CUSTOM
                */
        supportActionBar?.displayOptions =
                ActionBar.DISPLAY_SHOW_HOME or
                ActionBar.DISPLAY_SHOW_TITLE or
                ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)
        //supportActionBar?.setCustomView(R.layout.layout_a15)
    }

    private fun setRatingBarValue( rating: Float ) {
        tvA15.text = String.format( "星の数は「%1.1f」です", rating )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_a15, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            // "Load"メニューを選択
            // ５秒間プログレスバーが非同期で回る
            R.id.itemA15B -> {
                this.menuItem = item
                // http://www.vogella.com/tutorials/AndroidActionBar/article.html
                this.menuItem.setActionView(R.layout.layout_a15)
                this.menuItem.expandActionView()
                val task = TestTask()
                task.execute("test")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // ５秒間プログレスバーが非同期で回る
    @SuppressLint("StaticFieldLeak")
    inner class TestTask: AsyncTask<String?, Unit, String?>() {
        override fun doInBackground(vararg p0: String?): String? {
            try {
                Thread.sleep(5000)
            } catch (ex: InterruptedException) {
                ex.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            //super.onPostExecute(result)
            menuItem.collapseActionView()
            menuItem.setActionView(null)
        }
    }
}
