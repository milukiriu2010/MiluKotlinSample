package milu.kiriu2010.excon1.accball

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import milu.kiriu2010.excon1.R
import kotlinx.android.synthetic.main.activity_acc_ball.*

class AccBallActivity : AppCompatActivity(), SensorEventListener, SurfaceHolder.Callback {

    // サーフェースビューの幅
    private var surfaceWidth: Int = 0
    // サーフェースビューの高さ
    private var surfaceHeight: Int = 0

    // ボールの半径を表す定数
    private val radius = 50.0f
    // ボールの移動量を調整するための計数
    private val coef = 1000.0f

    // ボールの現在のx座標
    private var ballX: Float = 0f
    // ボールの現在のy座標
    private var ballY: Float = 0f
    // ボールのx方向への加速度
    private var vx: Float = 0f
    // ボールのy方向への加速度
    private var vy: Float = 0f
    // 前回時間の保持
    private var time: Long = 0L

    // SurfaceHolder.Callback
    // ボール位置の初期設定
    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        surfaceWidth = width
        surfaceHeight = height

        ballX = (width/2).toFloat()
        ballY = (height/2).toFloat()
    }

    // SurfaceHolder.Callback
    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.unregisterListener(this)
    }

    // SurfaceHolder.Callback
    override fun surfaceCreated(holder: SurfaceHolder?) {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this,accSensor,SensorManager.SENSOR_DELAY_GAME)
    }

    // SensorEventListener
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    // SensorEventListener
    // ボールの位置を計算する
    override fun onSensorChanged(event: SensorEvent?) {
        if ( event == null ) return

        if ( time == 0L ) time = System.currentTimeMillis()
        if ( event.sensor.type == Sensor.TYPE_ACCELEROMETER ) {
            val x = -event.values[0]
            val y = event.values[1]

            var t = (System.currentTimeMillis() - time).toFloat()
            time = System.currentTimeMillis()
            t /= 1000.0f

            val dx = vx*t + x*t*t/2.0f
            val dy = vy*t + y*t*t/2.0f
            ballX += dx*coef
            ballY += dy*coef
            vx += x*t
            vy += y*t

            if ( ballX - radius < 0 && vx < 0 ) {
                vx = -vx/1.5f
                ballX = radius
            }
            else if ( ballX + radius > surfaceWidth && vx > 0 ) {
                vx = -vx/1.5f
                ballX = surfaceWidth - radius
            }

            if ( ballY - radius < 0 && vy < 0 ) {
                vy = -vy/1.5f
                ballY = radius
            }
            else if ( ballY + radius > surfaceHeight && vy > 0 ) {
                vy = -vy/1.5f
                ballY = surfaceHeight  - radius
            }

            drawCanvas()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 画面が回転しないようにする
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_acc_ball)

        val holder = surfaceView.holder
        holder.addCallback(this)
    }

    // ボールの位置をサーフェスビューに描画する
    private fun drawCanvas() {
        val canvas = surfaceView.holder.lockCanvas()
        canvas.drawColor(Color.YELLOW)
        canvas.drawCircle(ballX, ballY, radius, Paint().apply{ color = Color.MAGENTA })
        surfaceView.holder.unlockCanvasAndPost(canvas)
    }
}


/*
class AccBallActivity : AppCompatActivity(), SensorEventListener {
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if ( event == null ) return
        if ( event.sensor.type == Sensor.TYPE_ACCELEROMETER ) {
            Log.d( "AccBallActivity",
                    "x=${event.values[0].toString()}," +
                            "y=${event.values[1].toString()}," +
                            "z=${event.values[2].toString()}" )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acc_ball)
    }

    // センサーの監視を開始する
    override fun onResume() {
        super.onResume()
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this,accSensor,SensorManager.SENSOR_DELAY_GAME)
    }

    // センサーの監視を終了する
    override fun onPause() {
        super.onPause()
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.unregisterListener(this)
    }
}
*/
