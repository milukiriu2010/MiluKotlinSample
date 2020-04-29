package milu.kiriu2010.excon1.gshooting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast



class GshootingActivity : AppCompatActivity()
    ,GameView.Callback {

    private lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    override fun onGameOver(score: Long) {

        gameView.stopDrawThread()

        Toast.makeText(this, "Game Over スコア $score", Toast.LENGTH_LONG).show()
    }
}
