package milu.kiriu2010.milux

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity

import android.support.v4.view.ViewPager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import milu.kiriu2010.milux.conf.AppConf
import milu.kiriu2010.milux.entity.LuxData
import milu.kiriu2010.util.LimitedArrayList
import java.util.Date
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity()
        , SensorEventListener {

    // アプリ設定
    val appConf = AppConf()

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    //private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var luxPagerAdapter: LuxPagerAdapter? = null

    // 照度センサの値
    private var luxData = LuxData()

    // 時刻ごとの照度値リスト
    // 1分間データを保持
    private val luxLst = LimitedArrayList<LuxData>(appConf.limit, appConf.limit)

    // タイマーで呼び出されるハンドラー
    //private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //setSupportActionBar(toolbar)

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        luxPagerAdapter = LuxPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = luxPagerAdapter

        // アクティブなフラグメントが切り替わったら呼び出される
        /*
        container.addOnPageChangeListener( object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(pos: Int) {
                val fragment = luxPagerAdapter?.getItem(pos) ?: return
                // 画面の方向を表示する内容によって変更する
                if ( fragment is OrientationListener ) {
                    requestedOrientation = fragment.onActivityOrientation()
                }
            }

        })
        */


        // 1秒ごとに照度値をバッファに蓄える
        /*
        timer( period = 1000 ) {
            handler.post {
                Log.d(javaClass.simpleName,"luxLst.size[${luxLst.size}]")
                luxLst.add(0,LuxData(Date(),lux))
            }
        }
        */


    }

    // センサーの監視を開始する
    override fun onResume() {
        super.onResume()
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // 照度センサ
        var sensorLight: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        // 照度センサあり
        if ( sensorLight != null ) {
            sensorManager.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_NORMAL)

        }
        // 照度センサなし
        else {
            // アラート画面
        }
    }

    // センサーの監視を終了する
    override fun onPause() {
        super.onPause()
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.unregisterListener(this)
    }

    // SensorEventListener
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    // SensorEventListener
    override fun onSensorChanged(event: SensorEvent?) {
        if ( event?.sensor?.type != Sensor.TYPE_LIGHT) return
        // 照度センサの値を取得
        val lux = event.values[0]
        // 計測時刻
        val now = Date()

        // １秒ごとに照度値をバッファに格納
        var tick = false
        if ( now.time/1000 != luxData.t.time/1000 ) {
            tick = true
            luxData = LuxData(now,lux)
            luxLst.add(0, luxData)
            Log.d(javaClass.simpleName,"luxLst.size[${luxLst.size}]")
        }

        // 登録されている表示ビュー全てに新しい値を伝える
        for ( i in 0 until luxPagerAdapter!!.count ) {
            val fragment = luxPagerAdapter?.getItem(i) as? NewVal01Listener ?: continue

            fragment.onUpdate(lux)
            if ( tick == true ) {
                fragment.onUpdate(luxLst)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
