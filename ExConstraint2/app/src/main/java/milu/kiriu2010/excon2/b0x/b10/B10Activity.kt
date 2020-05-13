package milu.kiriu2010.excon2.b0x.b10

import android.hardware.Sensor
import android.hardware.SensorEventListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import milu.kiriu2010.excon2.R
import android.hardware.SensorManager
import android.widget.ImageView
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.hardware.SensorEvent

// コンパス
class B10Activity : AppCompatActivity()
    , SensorEventListener {

    private lateinit var mPointer: ImageView
    private lateinit var mSensorManager: SensorManager
    private var mAccelerometer: Sensor? = null
    private var mMagnetometer: Sensor? = null
    private val mLastAccelerometer = FloatArray(3)
    private val mLastMagnetometer = FloatArray(3)
    private var mLastAccelerometerSet = false
    private var mLastMagnetometerSet = false
    private val mR = FloatArray(9)
    private val mOrientation = FloatArray(3)
    private var mCurrentDegree = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b10)

        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mPointer = findViewById(R.id.ivB10);
    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME)
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(this, mAccelerometer)
        mSensorManager.unregisterListener(this, mMagnetometer)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.size)
            mLastAccelerometerSet = true
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.size)
            mLastMagnetometerSet = true
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer)
            SensorManager.getOrientation(mR, mOrientation)
            val azimuthInRadians = mOrientation[0]
            val azimuthInDegress = (Math.toDegrees(azimuthInRadians.toDouble()) + 360).toFloat() % 360
            val ra = RotateAnimation(
                    mCurrentDegree,
                    -azimuthInDegress,
                    // 自分のサイズの割合、0.5fが画像の中心
                    Animation.RELATIVE_TO_SELF,
                    // 回転軸のX座標
                    0.5f,
                    // 自分のサイズの割合、0.5fが画像の中心
                    Animation.RELATIVE_TO_SELF,
                    // 回転軸のY座標
                    0.5f)

            ra.duration = 250

            // アニメーション終了時にviewをそのまま残す
            ra.fillAfter = true

            mPointer.startAnimation(ra)
            mCurrentDegree = -azimuthInDegress
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
}
