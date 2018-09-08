package milu.kiriu2010.excon2.rate

import android.app.ActionBar
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.excon2.R
import kotlinx.android.synthetic.main.activity_rate.*

class RateActivity : AppCompatActivity() {

    lateinit var menuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate)

        this.setRatingBarValue( ratingBar.rating )

        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
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
        //supportActionBar?.setCustomView(R.layout.action_bar_layout_progressbar)
    }

    private fun setRatingBarValue( rating: Float ) {
        lblRate.text = String.format( "星の数は「%1.1f」です", rating )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_progress, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.menuLOAD -> {
                this.menuItem = item
                // http://www.vogella.com/tutorials/AndroidActionBar/article.html
                this.menuItem.setActionView(R.layout.action_bar_layout_progressbar)
                this.menuItem.expandActionView()
                val task = TestTask()
                task.execute("test")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

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
