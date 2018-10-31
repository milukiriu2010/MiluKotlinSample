package milu.kiriu2010.excon2.screen1.sensorprox

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_sensor_prox.*
import milu.kiriu2010.excon2.R

class SensorProxActivity : AppCompatActivity() , SensorEventListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor_prox)
    }

    // センサーの監視を開始する
    override fun onResume() {
        super.onResume()
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // 近接センサ
        var sensorProx: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        // 近接センサあり
        if ( sensorProx != null ) {
            sensorManager.registerListener( this, sensorProx, SensorManager.SENSOR_DELAY_GAME )
        }
        // 近接センサなし
        else {
            dataProx.text = "×"
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
        if ( event?.sensor?.type != Sensor.TYPE_PROXIMITY ) return

        Log.d( javaClass.simpleName, "onSensorChanged:{${event.values[0]}}")

        // どうやら
        // 0:近いとき
        // 5:遠いとき
        dataProx.text = event.values[0].toString()
    }
}
