package milu.kiriu2010.excon2.sensorstep

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_sensor_step.*
import milu.kiriu2010.excon2.R

class SensorStepActivity : AppCompatActivity()
        , SensorEventListener {

    var stepInitCnt = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor_step)
    }

    // センサーの監視を開始する
    override fun onResume() {
        super.onResume()
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // 歩行センサ
        var sensorStep: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        // 歩行センサあり
        if ( sensorStep != null ) {
            sensorManager.registerListener( this, sensorStep, SensorManager.SENSOR_DELAY_GAME )
        }
        // 歩行センサなし
        else {
            dataStep.text = "×"
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
        if ( event?.sensor?.type != Sensor.TYPE_STEP_COUNTER ) return

        // 今まで歩いた数がはいるらしい
        Log.d( javaClass.simpleName, "onSensorChanged:{${event.values[0]}}")

        val stepCnt = event.values[0].toInt()
        if ( stepInitCnt == -1 ) {
            stepInitCnt = stepCnt
        }

        dataStep.text = (stepCnt-stepInitCnt).toString()
    }
}
