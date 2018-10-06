package milu.kiriu2010.excon1.gaction

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import milu.kiriu2010.excon1.R

class GactionActivity : AppCompatActivity()
        ,GameView.Callback {

    private lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_gaction)

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        gameView = GameView(this)
        gameView.setCallback(this)

        setContentView(gameView)
    }


    override fun onResume() {
        super.onResume()

        gameView.startDrawThread()
    }

    override fun onPause() {
        super.onPause()

        gameView.stopDrawThread()
    }

    /*
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.getItemId()
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }
    */

    override fun onGameOver() {
        Toast.makeText(this, "Game Over", Toast.LENGTH_LONG).show()
    }
}
