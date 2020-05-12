package milu.kiriu2010.excon2.a0x.a24

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_a24.*
import milu.kiriu2010.excon2.R

// 傾きセンサ(結局、加速度センサ)
// TYPE_ORIENTATIONが非推奨となったので
// 加速度センサを用いるっぽい
class A24Activity : AppCompatActivity()
        , SensorEventListener {

    var mGravity: FloatArray = floatArrayOf(0f,0f,0f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a24)
    }

    // センサーの監視を開始する
    override fun onResume() {
        super.onResume()
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // 加速度センサ
        var sensorAccl: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        // 加速度センサあり
        if ( sensorAccl != null ) {
            sensorManager.registerListener( this, sensorAccl, SensorManager.SENSOR_DELAY_GAME )
        }
        // 加速度センサなし
        else {
            Log.d(javaClass.simpleName, "加速度センサなし")
        }

        // 地磁気センサ
        var sensorMag: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        // 地磁気センサあり
        if ( sensorMag != null ) {
            sensorManager.registerListener( this, sensorMag, SensorManager.SENSOR_DELAY_GAME )
        }
        // 地磁気センサなし
        else {
            Log.d(javaClass.simpleName, "地磁気センサなし")
        }

    }

    // センサーの監視を終了する
    override fun onPause() {
        super.onPause()
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if ( event?.sensor?.type == Sensor.TYPE_ACCELEROMETER ) {
            //mGravity[0] = event.values[0]
            mGravity = event.values.copyOf()

            tvA24.text = "{$mGravity[0]}\n$mGravity[1]\n$mGravity[2]"
        }
    }
}
