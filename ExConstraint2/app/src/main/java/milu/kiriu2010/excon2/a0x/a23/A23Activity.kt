package milu.kiriu2010.excon2.a0x.a23

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_a23.*
import milu.kiriu2010.excon2.R

// 近接センサ
class A23Activity : AppCompatActivity() , SensorEventListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a23)
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
            dataA23.text = "×"
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
        dataA23.text = event.values[0].toString()
    }
}
