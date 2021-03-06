package milu.kiriu2010.excon2.a0x.a14

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_a14.*
import milu.kiriu2010.excon2.R

// 温度センサ
// は、センサがなかったので、
// 利用できるセンサ一覧を表示することにした
class A14Activity : AppCompatActivity()
    , SensorEventListener{

    private lateinit var sensorManager: SensorManager

    // 外気温
    var ambientTemperature: Float = 0.0f
    // 湿度
    var humidity: Float = 0.0f
    // 気圧
    var pressure: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a14)

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
        // 利用できるセンサを表示
        checkSensors()

        val notFoundLst = mutableListOf<String>()

        // 温度センサ
        val sensorAmbientTemperature: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        // 温度センサあり
        if ( sensorAmbientTemperature != null ) {
            sensorManager.registerListener(this, sensorAmbientTemperature, SensorManager.SENSOR_DELAY_GAME)
        }
        // 温度センサなし
        else {
            notFoundLst.add("温度センサ")
            tvA14_DataTemp.text = "×"
        }

        // 湿度センサ
        val sensorRelativeHumidity: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
        // 湿度センサあり
        if ( sensorRelativeHumidity != null ) {
            sensorManager.registerListener(this, sensorRelativeHumidity, SensorManager.SENSOR_DELAY_GAME)
        }
        // 湿度センサなし
        else {
            notFoundLst.add("湿度センサ")
            tvA14_DataHumi.text = "×"
        }

        // 圧力センサ
        val sensorPressure: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        // 圧力センサあり
        if ( sensorPressure != null ) {
            sensorManager.registerListener(this, sensorPressure, SensorManager.SENSOR_DELAY_GAME)
        }
        // 圧力センサなし
        else {
            notFoundLst.add("圧力センサ")
            tvA14_DataPres.text = "×"
        }

        // 温度センサ・湿度センサ・圧力センサのいづれかが見つからない場合
        // エラーダイアログを表示
        if ( notFoundLst.size > 0 ) {
            val sb = StringBuilder()
            notFoundLst.forEach { sb.append("$it\n") }
            AlertDialog.Builder(this)
                    .setTitle("センサなし")
                    .setMessage(sb.toString())
                    .setPositiveButton("OK",null)
                    .show()
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
        Log.d( javaClass.simpleName, "onSensorChanged[${event?.sensor?.type}]")
        when ( event?.sensor?.type ) {
            // 温度センサ
            Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                ambientTemperature = event.values[0]
                tvA14_DataTemp.text = ambientTemperature.toString()
            }
            // 湿度センサ
            Sensor.TYPE_RELATIVE_HUMIDITY -> {
                humidity = event.values[0]
                tvA14_DataHumi.text = humidity.toString()
            }
            // 圧力センサ
            Sensor.TYPE_PRESSURE -> {
                pressure = event.values[0]
                tvA14_DataPres.text = pressure.toString()
            }
            // その他のセンサ
        }
    }

    // アクションバーのアイコンがタップされると呼ばれる
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // 前画面に戻る
            android.R.id.home -> {
                finish()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    // 利用できるセンサを表示
    private fun checkSensors() {
        val sensors = sensorManager.getSensorList(Sensor.TYPE_ALL)

        // リサイクラ・ビュー上にセンサ一覧を縦に並べる
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvA14.layoutManager = layoutManager

        // 可変ではないので、java.lang.UnsupportedOperationException が発生する
        //sensors.sortBy{ it.stringType }
        //sensors.sortWith(Comparator({ a,b -> b.stringType.compareTo(a.stringType)}))

        // ソートするために可変リストに格納しなおす
        val sensorLst = mutableListOf<Sensor>()
        sensors.forEach { sensorLst.add(it) }
        sensorLst.sortBy { it.stringType }

        // センサ一覧を表示するためのアダプタを設定
        val adapter = A14Adapter(this, sensorLst)
        rvA14.adapter = adapter

        // 区切り線を入れる
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        rvA14.addItemDecoration(itemDecoration)
    }
}
