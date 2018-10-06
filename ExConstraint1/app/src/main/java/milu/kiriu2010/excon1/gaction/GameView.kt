package milu.kiriu2010.excon1.gaction

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import milu.kiriu2010.excon1.R

import java.util.ArrayList
import java.util.Random


class GameView(context: Context) : SurfaceView(context), Droid.Callback, SurfaceHolder.Callback {

    private var droid: Droid? = null
    private val groundList = ArrayList<Ground>()

    private var touchDownStartTime: Long = 0

    private var drawThread: DrawThread? = null

    private var lastGround: Ground? = null

    private val rand = Random()

    private var callback: Callback? = null

    private val handlerX: Handler

    private var isGameOver: Boolean = false

    interface Callback {
        fun onGameOver()
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    init {

        handlerX = Handler()

        holder.addCallback(this)
    }

    fun drawGame(canvas: Canvas) {
        val width = canvas.width
        val height = canvas.height

        canvas.drawColor(Color.WHITE)

        if (droid == null) {
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.droid)
            droid = Droid(bitmap, 0, 0, this)

            // 開始時に表示される地面
            lastGround = Ground(0, height - START_GROUND_HEIGHT, width, height)
            groundList.add(lastGround ?: return)
        }

        if (lastGround!!.isShown(width, height)) {
            // 地面を作成する
            // 0:地面
            // 1:なし
            // 2:地面
            // 3:なし
            // 4:地面
            for (i in 0 until ADD_GROUND_COUNT) {
                val left = lastGround!!.rect.right


                if (i % 2 == 0) {
                    val groundHeight = rand.nextInt(height / GROUND_BLOCK_HEIGHT) * GROUND_BLOCK_HEIGHT / 2 + START_GROUND_HEIGHT
                    lastGround = Ground(left, height - groundHeight, left + GROUND_WIDTH, height)
                } else {
                    lastGround = Blank(left, height - 1, left + GROUND_WIDTH, height)
                }
                groundList.add(lastGround ?: return)
            }
        }

        // 地面がキャンバス上の範囲に入ってるか判定し、
        // 入ってなければリストから削除する
        var i = 0
        while (i < groundList.size) {
            val ground = groundList[i]

            if (ground.isAvailable) {
                ground.move(GROUND_MOVE_TO_LEFT)

                if (ground.isShown(width, height)) {
                    ground.draw(canvas)
                }
            } else {
                groundList.remove(ground)
                i--
            }
            i++
        }

        // パワーゲージを表示する
        // タッチすると増えるっぽい
        if (touchDownStartTime > 0) {
            val elapsedTime = (System.currentTimeMillis() - touchDownStartTime).toFloat()
            canvas.drawRect(0f, 0f, width * (elapsedTime / MAX_TOUCH_TIME), POWER_GAUGE_HEIGHT,
                    PAINT_POWER_GAUGE)
        }

        // ドロイド君を移動
        droid!!.move()

        // ドロイド君を描画
        droid!!.draw(canvas)
    }

    override fun getDistanceFromGround(droid: Droid): Int {
        val width = width
        val height = height

        for (i in groundList.indices) {
            val ground = groundList[i]

            if (!ground.isShown(width, height)) {
                continue
            }

            val horizontal = !(droid.rect.left >= ground.rect.right || droid.rect.right <= ground.rect.left)
            if (horizontal) {
                if (!ground.isSolid) {
                    return Integer.MAX_VALUE
                }

                val distanceFromGround = ground.rect.top - droid.rect.bottom
                if (distanceFromGround < 0) {
                    gameOver()
                    return Integer.MAX_VALUE
                }
                return distanceFromGround
            }
        }

        return Integer.MAX_VALUE
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        // タッチするとゲージが増え
        // タッチを離すとジャンプする
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchDownStartTime = System.currentTimeMillis()
                return true
            }
            MotionEvent.ACTION_UP -> jumpDroid()
        }
        return super.onTouchEvent(event)
    }

    private fun jumpDroid() {
        var time = (System.currentTimeMillis() - touchDownStartTime).toFloat()
        touchDownStartTime = 0

        if (getDistanceFromGround(droid!!) != 0) {
            return
        }

        if (time > MAX_TOUCH_TIME) {
            time = MAX_TOUCH_TIME.toFloat()
        }

        droid!!.jump(time / MAX_TOUCH_TIME)
    }

    private fun gameOver() {
        if (isGameOver) {
            return
        }

        isGameOver = true

        droid!!.shutdown()

        handlerX.post { callback!!.onGameOver() }
    }

    private inner class DrawThread : Thread() {
        internal var isFinished: Boolean = false

        override fun run() {
            val holder = holder

            while (!isFinished) {
                val canvas = holder.lockCanvas()
                if (canvas != null) {
                    drawGame(canvas)
                    holder.unlockCanvasAndPost(canvas)
                }

                try {
                    Thread.sleep(1000 / FPS)
                } catch (e: InterruptedException) {
                }

            }
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        startDrawThread()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        stopDrawThread()
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

    companion object {

        private val START_GROUND_HEIGHT = 50
        private val GROUND_MOVE_TO_LEFT = 10

        private val MAX_TOUCH_TIME = 500 // msec
        private val FPS: Long = 60

        private val ADD_GROUND_COUNT = 5

        private val GROUND_WIDTH = 340
        private val GROUND_BLOCK_HEIGHT = 100

        private val POWER_GAUGE_HEIGHT = 30f
        private val PAINT_POWER_GAUGE = Paint()

        init {
            PAINT_POWER_GAUGE.color = Color.RED
        }
    }
}
