package milu.kiriu2010.excon2.sensorlight

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_sensor_light.*
import milu.kiriu2010.excon2.R

class SensorLightActivity : AppCompatActivity()
    , SensorEventListener {

    private lateinit var sensorManager: SensorManager

    // 照度
    var light: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor_light)

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }

    // センサーの監視を開始する
    override fun onResume() {
        super.onResume()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // 照度センサ
        var sensorLight: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        // 照度センサあり
        if ( sensorLight != null ) {
            sensorManager.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_NORMAL)
        }
        // 照度センサなし
        else {
            dataLight.text = "×"
        }
    }

    // センサーの監視を終了する
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    // SensorEventListener
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    // SensorEventListener
    override fun onSensorChanged(event: SensorEvent?) {
        if ( event?.sensor?.type != Sensor.TYPE_LIGHT) return

        // 照度センサの値を取得
        light = event.values[0]
        dataLight.text = light.toString()

        // http://seesaawiki.jp/w/moonlight_aska/d/%be%c8%c5%d9%a5%bb%a5%f3%a5%b5%a1%bc%a4%ce%c3%cd%a4%f2%bc%e8%c6%c0%a4%b9%a4%eb
        // 明るさの目安
        //   LIGHT_SUNLIGHT_MAX 120000.0       5.079
        //   LIGHT_SUNLLIGHT    110000.0       5.041
        //   LIGHT_SHADE         20000.0       4.301
        //   LIGHT_OVERCAST      10000.0       4
        //   LIGHT_SUNRISE         400.0       2.602
        //   LIGHT_CLOUDY          100.0       2
        //   LIGHT_FULLMOON          0.25     -0.602
        //   LIGHT_NO_MOON           0.0010   -3
        viewLight.lux = light
        viewLight.movePos()
        // 再描画
        viewLight.invalidate()
    }

    // アクションバーのアイコンがタップされると呼ばれる
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            // 前画面に戻る
            android.R.id.home -> {
                finish()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
