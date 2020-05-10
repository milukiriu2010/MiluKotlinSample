package milu.kiriu2010.excon2.a0x.a20

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_a20.*
import milu.kiriu2010.excon2.R

// 歩行センサ
class A20Activity : AppCompatActivity()
        , SensorEventListener
        , StepListener {

    // 歩行センサによるステップ数
    var stepInitCnt = -1

    // 加速度センサによるステップ数
    var stepAcclCnt = 0

    val simpleStepDetector = StepDetector()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a20)

        // 歩行検知
        simpleStepDetector.registerListener(this)

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // センサーの監視を開始する
        btnA20A_START.setOnClickListener {
            stepInitCnt = -1
            tvA20_STEPND.text = "0"
            stepAcclCnt = 0
            tvA20_ACCLD.text = "0"

            // 歩行センサ
            val sensorStep: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
            // 歩行センサあり
            if ( sensorStep != null ) {
                sensorManager.registerListener( this, sensorStep, SensorManager.SENSOR_DELAY_FASTEST)
            }
            // 歩行センサなし
            else {
                tvA20_STEPND.text = "×"
                tvA20_STEPTD.text = "×"
            }

            // 加速度センサ
            val sensorAccl: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            // 加速度センサあり
            if ( sensorAccl != null ) {
                sensorManager.registerListener(this, sensorAccl, SensorManager.SENSOR_DELAY_FASTEST)
            }
            // 加速度センサなし
            else {
                tvA20_ACCLD.text = "×"
            }
        }

        // センサーの監視を終了する
        btnA20B_STOP.setOnClickListener {
            sensorManager.unregisterListener(this)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        when (event?.sensor?.type) {
            // 歩行センサ
            Sensor.TYPE_STEP_COUNTER -> {
                // 今まで歩いた数がはいるらしい
                Log.d( javaClass.simpleName, "onSensorChanged:{${event.values[0]}}")

                val stepCnt = event.values[0].toInt()
                if ( stepInitCnt == -1 ) {
                    stepInitCnt = stepCnt
                }

                tvA20_STEPND.text = (stepCnt-stepInitCnt).toString()
                tvA20_STEPTD.text = stepCnt.toString()
            }
            // 加速度センサ
            Sensor.TYPE_ACCELEROMETER -> {
                simpleStepDetector.updateAccel(event.timestamp,event.values[0],event.values[1],event.values[2])
            }
        }
    }

    // StepListener
    override fun step(timeNs: Long) {
        stepAcclCnt++
        tvA20_ACCLD.text = stepAcclCnt.toString()
    }

}
