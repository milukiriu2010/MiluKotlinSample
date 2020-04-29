package milu.kiriu2010.excon1.glabyrinth

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.WindowManager
import android.widget.Toast

class GlabyrinthActivity : AppCompatActivity()
    ,LabyrinthView.Callback {

    private lateinit var labyrinthView: LabyrinthView

    private var seed = 0

    var isFinished = false

    private val EXTRA_KEY_SEED = "key_seed"

    private fun newIntent(context: Context, seed: Int): Intent {
        val intent = Intent(context, GlabyrinthActivity::class.java)
        intent.putExtra(EXTRA_KEY_SEED, seed)
        return intent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        seed = getIntent().getIntExtra(EXTRA_KEY_SEED, 0);

        labyrinthView = LabyrinthView(this);
        labyrinthView.setSeed(seed);
        labyrinthView.setCallback(this);
        setContentView(labyrinthView);
    }

    override fun onResume() {
        super.onResume()

        labyrinthView.startSensor()
    }

    override fun onPause() {
        super.onPause()

        labyrinthView.stopSensor()
    }

    override fun onGoal() {
        if (isFinished) {
            return
        }
        isFinished = true

        Toast.makeText(this, "Goal!!", Toast.LENGTH_SHORT).show()

        labyrinthView.stopSensor()
        labyrinthView.stopDrawThread()

        nextStage()

        finish()
    }

    override fun onHole() {
        if (isFinished) {
            return
        }
        isFinished = true

        Toast.makeText(this, "Hole!!", Toast.LENGTH_SHORT).show()

        labyrinthView.stopSensor()

        retryStage()

        finish()
    }

    private fun nextStage() {
        val intent = newIntent(this, seed + 1)
        startActivity(intent)
    }

    private fun retryStage() {
        val intent = newIntent(this, seed)
        startActivity(intent)
    }

}
