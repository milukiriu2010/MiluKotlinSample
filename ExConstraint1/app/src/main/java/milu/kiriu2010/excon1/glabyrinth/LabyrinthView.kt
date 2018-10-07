package milu.kiriu2010.excon1.glabyrinth

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import milu.kiriu2010.excon1.R
import milu.kiriu2010.excon1.R.drawable.ball


class LabyrinthView(context: Context) : SurfaceView(context), SurfaceHolder.Callback, SensorEventListener {

    private val ballBitmap: Bitmap

    private var ball: Ball? = null
    private var map: Map? = null

    private var seed: Int = 0

    private var callback: Callback? = null


    private var drawThread: DrawThread? = null
    private var sensorValues: FloatArray? = null

    fun setSeed(seed: Int) {
        this.seed = seed
    }

    interface Callback {
        fun onGoal()

        fun onHole()
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    init {

        holder.addCallback(this)

        // ボールのBitmapをロード
        ballBitmap = BitmapFactory.decodeResource(resources, R.drawable.ball)
    }

    private inner class DrawThread : Thread() {
        var isFinished: Boolean = false

        override fun run() {

            while (!isFinished) {
                val canvas = holder.lockCanvas()
                if (canvas != null) {
                    drawLabyrinth(canvas)
                    holder.unlockCanvasAndPost(canvas)
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

    fun drawLabyrinth(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)

        val blockSize = ballBitmap.height
        if (map == null) {
            map = Map(canvas.width, canvas.height, blockSize, callback!!, seed)
        }

        if (ball == null) {
            ball = Ball(ballBitmap, map!!.startBlock!!, BALL_SCALE)
            ball!!.setOnMoveListener(map!!)
        }

        map!!.drawMap(canvas)

        ball!!.draw(canvas)

        if (sensorValues != null) {
            canvas.drawText("sensor[0] = " + sensorValues!![0], 10f, 150f, TEXT_PAINT)
            canvas.drawText("sensor[1] = " + sensorValues!![1], 10f, 200f, TEXT_PAINT)
            canvas.drawText("sensor[2] = " + sensorValues!![2], 10f, 250f, TEXT_PAINT)
        }
    }

    fun startSensor() {
        sensorValues = null

        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
    }

    fun stopSensor() {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.unregisterListener(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        startDrawThread()

        startSensor()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        stopDrawThread()

        stopSensor()
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (sensorValues == null) {
            sensorValues = FloatArray(3)
            sensorValues!![0] = event.values[0]
            sensorValues!![1] = event.values[1]
            sensorValues!![2] = event.values[2]
            return
        }

        sensorValues!![0] = sensorValues!![0] * ALPHA + event.values[0] * (1f - ALPHA)
        sensorValues!![1] = sensorValues!![1] * ALPHA + event.values[1] * (1f - ALPHA)
        sensorValues!![2] = sensorValues!![2] * ALPHA + event.values[2] * (1f - ALPHA)

        if (ball != null) {
            ball!!.move(-sensorValues!![0] * ACCEL_WEIGHT, sensorValues!![1] * ACCEL_WEIGHT)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    companion object {

        private val BALL_SCALE = 0.8f

        private val ACCEL_WEIGHT = 3f

        private val TEXT_PAINT = Paint()

        init {
            TEXT_PAINT.color = Color.WHITE
            TEXT_PAINT.textSize = 40f
        }

        private val ALPHA = 0.8f
    }
}
