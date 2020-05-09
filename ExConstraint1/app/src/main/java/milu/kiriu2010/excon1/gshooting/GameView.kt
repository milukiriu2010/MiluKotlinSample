package milu.kiriu2010.excon1.gshooting


import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.VibrationEffect
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import milu.kiriu2010.excon1.R

import java.util.ArrayList;
import java.util.Random;


class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private var droid: Droid? = null
    private var city: City? = null
    private val missileList = ArrayList<BaseObject>()
    private val bulletList = ArrayList<BaseObject>()

    private val paintScore = Paint()

    private var score: Long = 0

    private val rand = Random(System.currentTimeMillis())

    private var callback: Callback? = null

    private val handlerX: Handler

    private val vibrator: Vibrator

    private var drawThread: DrawThread? = null

    interface Callback {
        fun onGameOver(score: Long)
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    init {

        handlerX = Handler()

        vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        paintScore.color = Color.BLACK
        paintScore.textSize = SCORE_TEXT_SIZE
        paintScore.isAntiAlias = true

        holder.addCallback(this)
    }

    private fun drawObject(canvas: Canvas) {

        val width = canvas.width
        val height = canvas.height

        canvas.drawColor(Color.WHITE)

        if (droid == null) {
            val droidBitmap = BitmapFactory.decodeResource(resources, R.drawable.droid2)
            droid = Droid(droidBitmap, width, height)
        }

        if (city == null) {
            city = City(width, height)
        }

        drawObjectList(canvas, missileList, width, height)

        drawObjectList(canvas, bulletList, width, height)

        for (i in missileList.indices) {
            val missile = missileList[i]

            for (j in bulletList.indices) {
                val bullet = bulletList[j]

                if (bullet.isHit(missile)) {
                    missile.hit()
                    bullet.hit()

                    //vibrator.vibrate(VIBRATION_LENGTH_HIT_MISSILE)
                    vibrator.vibrate(VibrationEffect.createOneShot(VIBRATION_LENGTH_HIT_MISSILE, VibrationEffect.DEFAULT_AMPLITUDE))

                    score += 10
                }
            }
        }

        for (i in missileList.indices) {
            val missile = missileList[i]

            if (!missile.isAvailable(width, height)) {
                continue
            }

            var gameOver = false

            if (droid!!.isHit(missile)) {
                missile.hit()
                droid!!.hit()
                gameOver = true
            } else if (city!!.isHit(missile)) {
                missile.hit()
                city!!.hit()
                gameOver = true
            }

            if (gameOver) {
                //vibrator.vibrate(VIBRATION_LENGTH_HIT_DROID)
                vibrator.vibrate(VibrationEffect.createOneShot(VIBRATION_LENGTH_HIT_DROID, VibrationEffect.DEFAULT_AMPLITUDE))
                handlerX.post { callback!!.onGameOver(score) }

                break
            }
        }

        // ミサイルの発射
        if (rand.nextInt(MISSILE_LAUNCH_WEIGHT) == 0) {
            val count = score / SCORE_LEVEL + 1
            for (i in 0 until count) {
                launchMissile()
            }
        }

        city!!.draw(canvas)

        droid!!.draw(canvas)

        // スコアの表示
        canvas.drawText("Score: $score", 0f, SCORE_TEXT_SIZE, paintScore)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> fire(event.y, event.x)
        }

        return super.onTouchEvent(event)
    }

    private fun fire(y: Float, x: Float) {
        val alignX = (x - droid!!.rect.centerX()) / Math.abs(y - droid!!.rect.centerY())

        val bullet = Bullet(alignX, droid!!.rect)
        bulletList.add(0, bullet)
    }

    private fun launchMissile() {
        val fromX = rand.nextInt(width)
        val toX = rand.nextInt(width)

        val alignX = (toX - fromX) / height.toFloat()
        val missile = Missile(fromX, alignX)
        missileList.add(missile)
    }

    private inner class DrawThread : Thread() {
        var isFinished: Boolean = false

        override fun run() {
            super.run()

            val holder = holder
            while (!isFinished) {
                val canvas = holder.lockCanvas()
                if (canvas != null) {
                    drawObject(canvas)
                    holder.unlockCanvasAndPost(canvas)
                }

                try {
                    Thread.sleep(1000 / FPS)
                } catch (e: InterruptedException) {
                }

            }
        }
    }

    fun startDrawThread() {
        stopDrawThread()

        drawThread = DrawThread()
        drawThread!!.start()

    }

    fun stopDrawThread(): Boolean {
        if (drawThread == null) {
            return false
        }

        drawThread!!.isFinished = true
        drawThread = null
        return true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        startDrawThread()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        stopDrawThread()
    }

    companion object {

        private val MISSILE_LAUNCH_WEIGHT = 50
        private val FPS: Long = 60

        private val SCORE_TEXT_SIZE = 80.0f

        private val VIBRATION_LENGTH_HIT_MISSILE: Long = 100
        private val VIBRATION_LENGTH_HIT_DROID: Long = 1000

        private val SCORE_LEVEL: Long = 100

        private fun drawObjectList(canvas: Canvas, objectList: MutableList<BaseObject>, width: Int, height: Int) {
            var i = 0
            while (i < objectList.size) {
                val `object` = objectList[i]
                if (`object`.isAvailable(width, height)) {
                    `object`.move()
                    `object`.draw(canvas)
                } else {
                    objectList.remove(`object`)
                    i--
                }
                i++
            }
        }
    }
}