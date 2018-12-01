package milu.kiriu2010.excon2.screen2.bearing

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_bearing.*
import milu.kiriu2010.excon2.R

// https://google-developer-training.gitbooks.io/android-developer-advanced-course-concepts/content/unit-1-expand-the-user-experience/lesson-3-sensors/3-2-c-motion-and-position-sensors/3-2-c-motion-and-position-sensors.html
class BearingActivity : AppCompatActivity()
        , SensorEventListener {
    private lateinit var sensorManager: SensorManager

    // 回転行列
    private val MATRIX_SIZE = 16
    // R is the identity matrix when the device is aligned with the world's coordinate system,
    // that is, when the device's X axis points toward East,
    // the Y axis points to the North Pole and
    // the device is facing the sky.
    private val inR = FloatArray(MATRIX_SIZE)
    private val outR = FloatArray(MATRIX_SIZE)
    // I is a rotation matrix transforming the geomagnetic vector into the same coordinate space
    // as gravity (the world's coordinate space).
    // I is a simple rotation around the X axis.
    // The inclination angle in radians can be computed with getInclination(float[]).
    private val ix = FloatArray(MATRIX_SIZE)

    // センサー値
    private val AXIS_NUM = 3
    private var gravity = FloatArray(AXIS_NUM)
    private var geomagnetic = FloatArray(AXIS_NUM)
    private var orientation = FloatArray(AXIS_NUM)
    private val attitude = FloatArray(AXIS_NUM)
    private var light = FloatArray(1)
    private var gyro = FloatArray(AXIS_NUM)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bearing)
    }


    override fun onResume() {
        super.onResume()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // 加速度センサ
        var sensorAccel: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        // 加速度センサあり
        if (sensorAccel != null) {
            sensorManager.registerListener(this, sensorAccel, SensorManager.SENSOR_DELAY_UI)
        }

        // 磁気センサ
        var sensorMag: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        // 磁気センサあり
        if (sensorMag != null) {
            sensorManager.registerListener(this, sensorMag, SensorManager.SENSOR_DELAY_UI)
        }

        // 方位センサ
        var sensorOri: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
        // 方位センサあり
        if (sensorOri != null) {
            sensorManager.registerListener(this, sensorOri, SensorManager.SENSOR_DELAY_UI)
        }

        // 光センサ
        var sensorLight: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        // 光センサあり
        if (sensorLight != null) {
            sensorManager.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_UI)
        }

        // ジャイロセンサ
        var sensorGyro: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        // ジャイロセンサあり
        if (sensorGyro != null) {
            sensorManager.registerListener(this, sensorGyro, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> gravity = event.values.clone()
            Sensor.TYPE_MAGNETIC_FIELD -> geomagnetic = event.values.clone()
            Sensor.TYPE_ORIENTATION -> orientation = event.values.clone()
            Sensor.TYPE_LIGHT -> light = event.values.clone()
            Sensor.TYPE_GYROSCOPE -> gyro = event.values.clone()
            else -> return
        }

        if ( (gravity == null) or (geomagnetic == null) or (orientation == null)) return

        // 回転行列を計算
        SensorManager.getRotationMatrix(inR,ix,gravity,geomagnetic)
        // 端末の画面設定に合わせる(以下は, 縦表示で画面を上にした場合)
        SensorManager.remapCoordinateSystem(inR,SensorManager.AXIS_X,SensorManager.AXIS_Y,outR)
        // 方位角/傾きを取得
        SensorManager.getOrientation(outR,attitude)

        // 方位角:Z軸の回転角度(azimuth)
        // 傾斜角:X軸の回転角度(pitch:前後の傾斜)
        // 回転角:Y軸の回転角度(roll:左右の傾斜)
        val str = "1) 地磁気+加速度センサ" +
                "\n  方位角:" + "%3.1f".format(Math.toDegrees(attitude[0].toDouble())) +
                "\n  傾斜角:" + "%3.1f".format(Math.toDegrees(attitude[1].toDouble())) +
                "\n  回転角:" + "%3.1f".format(Math.toDegrees(attitude[2].toDouble())) +
                "\n\n 5) 光センサ" +
                "\n  　　Ⅹ:" + "%3.1f".format(light[0]) +
                "\n\n 2) 傾きセンサ" +
                "\n  方位角:" + "%3.1f".format( if (orientation[0] > 180.0) orientation[0]-360.0 else orientation[0] ) +
                "\n  傾斜角:" + "%3.1f".format(orientation[1]) +
                "\n  回転角:" + "%3.1f".format(orientation[2]) +
                "\n\n 3) 加速度センサ" +
                "\n  　　Ⅹ:" + "%3.1f".format(gravity[0]) +
                "\n  　　Ｙ:" + "%3.1f".format(gravity[1]) +
                "\n  　　Ｚ:" + "%3.1f".format(gravity[2]) +
                "\n\n 4) 地磁気センサ" +
                "\n  　　Ⅹ:" + "%3.1f".format(geomagnetic[0]) +
                "\n  　　Ｙ:" + "%3.1f".format(geomagnetic[1]) +
                "\n  　　Ｚ:" + "%3.1f".format(geomagnetic[2]) +
                "\n\n 6) ジャイロセンサ" +
                "\n  　　Ⅹ:" + "%f".format(gyro[0]) +
                "\n  　　Ｙ:" + "%f".format(gyro[1]) +
                "\n  　　Ｚ:" + "%f".format(gyro[2])

        dataBearing.text = str
    }
}
